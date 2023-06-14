package dao;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import connMySQL.ConnBD;
import model.bankAccounts.BankAccounts;
import model.bankAccounts.BankCheckingAccount;
import model.bankAccounts.BankSavingsAccount;
import model.clients.BankUsers;

public class AccountDAO {
    private ConnBD conn;

    public AccountDAO(ConnBD conn) {
        this.conn = conn;
    }

    public void addAccount(BankAccounts account, String cpf) {
        // Verificar se o usuário já possui uma conta do mesmo tipo
        List<BankAccounts> existingAccounts = recoverAccounts(cpf);

        for (BankAccounts existingAccount : existingAccounts) {
            if (existingAccount.getType().equals(account.getType())) {
                JOptionPane.showMessageDialog(null, "O usuário já possui uma conta desse tipo.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Adicionar a nova conta
        String sql = "INSERT INTO BK_ACCOUNT (ACT_NUMBER, ACT_TYPE, ACT_BALANCE, ACT_OPENING_DATE, ACT_STATUS, CLI_CPF) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setLong(1, account.getNumber());
            statement.setString(2, account.getType());
            statement.setBigDecimal(3, account.getBalance());
            statement.setDate(4, java.sql.Date.valueOf(account.getOpeningDate()));
            statement.setInt(5, account.getStatus());
            statement.setString(6, cpf);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Conta adicionada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
                }
            }
            this.conn.closeConnection();
        }
    }

    public List<BankAccounts> recoverAccounts(String cpf) {
        String sql = "SELECT * FROM BK_ACCOUNT WHERE CLI_CPF = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<BankAccounts> accounts = new ArrayList<>();

        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            statement.setString(1, cpf);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int number = resultSet.getInt("ACT_NUMBER");
                String type = resultSet.getString("ACT_TYPE");
                BigDecimal balance = resultSet.getBigDecimal("ACT_BALANCE");
                java.sql.Date sqlDate = resultSet.getDate("ACT_OPENING_DATE");
                LocalDate localDate = sqlDate.toLocalDate();
                int status = resultSet.getInt("ACT_STATUS");
                String cpfOwner = resultSet.getString("CLI_CPF");

                if (resultSet.getString("ACT_TYPE").equals("poupança")) {
                    BankSavingsAccount newAccountSavings = new BankSavingsAccount(cpf, "poupança");
                    newAccountSavings.setAccountDetails(number, type, cpfOwner, balance, localDate, status);
                    accounts.add(newAccountSavings);
                }
                if (resultSet.getString("ACT_TYPE").equals("corrente")) {
                    BankCheckingAccount newAccountChecking = new BankCheckingAccount(cpf, "corrente");
                    newAccountChecking.setAccountDetails(number, type, cpfOwner, balance, localDate, status);
                    accounts.add(newAccountChecking);
                }
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o ResultSet: " + exception.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
                }
            }
        }
        return accounts;
    }

    public List<BankAccounts> removeAccount(BankAccounts account, BankUsers user) {
        String sql = "DELETE FROM BK_ACCOUNT WHERE ACT_NUMBER = ?";
        PreparedStatement statement = null;
        List<BankAccounts> accounts = new ArrayList<>();
        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setLong(1, account.getNumber());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Conta removida com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "A conta não foi encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
                }
            }
        }
        return accounts;
    }

    public Boolean performDeposit(int numberAccount, BigDecimal amount) {
        String sql = "UPDATE BK_ACCOUNT SET ACT_BALANCE = ACT_BALANCE + ? WHERE ACT_NUMBER = ?";
        PreparedStatement statement = null;

        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setBigDecimal(1, amount);
            statement.setLong(2, numberAccount);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
                }
            }
            this.conn.closeConnection();
        }
    }

    public Boolean performWithdraw(int numberAccount, BigDecimal amount) {
        String sql = "UPDATE BK_ACCOUNT SET ACT_BALANCE = ACT_BALANCE - ? WHERE ACT_NUMBER = ?";
        PreparedStatement statement = null;

        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setBigDecimal(1, amount);
            statement.setLong(2, numberAccount);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
                }
            }
            this.conn.closeConnection();
        }
    }
}
