package dao;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connMySQL.ConnBD;
import model.bankAccounts.BankAccounts;
import model.bankAccounts.BankCheckingAccount;
import model.bankAccounts.BankSavingsAccount;

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
            statement.setFloat(3, account.getBalance());
            statement.setDate(4, java.sql.Date.valueOf(account.getOpeningDate().toLocalDate()));
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
                if (resultSet.getString("ACT_TYPE").equals("savings")) {
                    BankSavingsAccount newAccountSavings = new BankSavingsAccount(cpf);
                    accounts.add(newAccountSavings);
                }
                if (resultSet.getString("ACT_TYPE").equals("checking")) {
                    BankCheckingAccount newAccountChecking = new BankCheckingAccount(cpf);
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
}
