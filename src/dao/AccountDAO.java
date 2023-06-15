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
import model.bankAccounts.BankTransactionRecord;
import model.clients.BankUsers;

public class AccountDAO {
    private ConnBD conn;

    public AccountDAO(ConnBD conn) {
        this.conn = conn;
    }

    public void addAccount(BankAccounts account, String cpf) {
        List<BankAccounts> existingAccounts = recoverAccounts(cpf);

        for (BankAccounts existingAccount : existingAccounts) {
            if (existingAccount.getType().equals(account.getType())) {
                JOptionPane.showMessageDialog(null, "O usuário já possui uma conta desse tipo.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

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
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + exception.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
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
            statement.executeUpdate();
            if (user != null) {
                JOptionPane.showMessageDialog(null, "Conta removida com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            this.conn.closeConnection();
        }
        return accounts;
    }

    public boolean findAccountByNumber(int accountNumber) {
        String sql = "SELECT COUNT(*) FROM BK_ACCOUNT WHERE ACT_NUMBER = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setInt(1, accountNumber);
            resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + exception.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            conn.closeConnection();
        }
        return false;
    }

    public void removeUser(String cpf) {
        List<BankAccounts> userAccounts = recoverAccounts(cpf);

        for (BankAccounts account : userAccounts) {
            removeAccount(account, null);
        }

        String sqlDeleteAccounts = "DELETE FROM BK_ACCOUNT WHERE CLI_CPF = ?";
        String sqlDeleteClient = "DELETE FROM BK_CLIENTS WHERE CLI_CPF = ?";
        PreparedStatement deleteAccountsStatement = null;
        PreparedStatement deleteClientStatement = null;

        try {
            deleteAccountsStatement = conn.getConnection().prepareStatement(sqlDeleteAccounts);
            deleteAccountsStatement.setString(1, cpf);
            deleteAccountsStatement.executeUpdate();

            deleteClientStatement = conn.getConnection().prepareStatement(sqlDeleteClient);
            deleteClientStatement.setString(1, cpf);
            int rowsAffected = deleteClientStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "O usuário não foi encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (deleteAccountsStatement != null) {
                try {
                    deleteAccountsStatement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (deleteClientStatement != null) {
                try {
                    deleteClientStatement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            this.conn.closeConnection();
        }
    }

    public Boolean performDeposit(int numberAccount, BigDecimal amount) {
        String sql = "UPDATE BK_ACCOUNT SET ACT_BALANCE = ACT_BALANCE + ? WHERE ACT_NUMBER = ?";
        PreparedStatement statement = null;

        try {
            statement = conn.getConnection().prepareStatement(sql);
            statement.setBigDecimal(1, amount);
            statement.setLong(2, numberAccount);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Depósito realizado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
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
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            this.conn.closeConnection();
        }
    }

    public void performTransfer(BigDecimal amount, int destinyNumber, int originNumber) {
        String decreaseSql = "UPDATE BK_ACCOUNT SET ACT_BALANCE = ACT_BALANCE - ? WHERE ACT_NUMBER = ?";
        String increaseSql = "UPDATE BK_ACCOUNT SET ACT_BALANCE = ACT_BALANCE + ? WHERE ACT_NUMBER = ?";
        String transactionRecordSql = "INSERT INTO BK_TRANSACTION_RECORD (TR_NUMBER, TR_ORIGIN_NUMBER_ACCOUNT, TR_DESTINY_NUMBER_ACCOUNT, TR_DATE_TRANSACTION, TR_VALUE_TRANSACTION) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement decreaseStatement = null;
        PreparedStatement increaseStatement = null;
        PreparedStatement transactionStatement = null;

        try {
            decreaseStatement = this.conn.getConnection().prepareStatement(decreaseSql);
            decreaseStatement.setBigDecimal(1, amount);
            decreaseStatement.setLong(2, originNumber);
            decreaseStatement.executeUpdate();

            increaseStatement = this.conn.getConnection().prepareStatement(increaseSql);
            increaseStatement.setBigDecimal(1, amount);
            increaseStatement.setLong(2, destinyNumber);
            increaseStatement.executeUpdate();

            BankTransactionRecord tr = new BankTransactionRecord(originNumber, destinyNumber, amount);
            transactionStatement = this.conn.getConnection().prepareStatement(transactionRecordSql);
            transactionStatement.setInt(1, tr.getNumber());
            transactionStatement.setLong(2, originNumber);
            transactionStatement.setLong(3, destinyNumber);
            transactionStatement.setDate(4, java.sql.Date.valueOf(tr.getDateTransaction()));
            transactionStatement.setBigDecimal(5, amount);
            transactionStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            try {
                this.conn.getConnection().rollback();
            } catch (SQLException rollbackException) {
                JOptionPane.showMessageDialog(null, "Erro ao executar rollback: " + rollbackException.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            if (decreaseStatement != null) {
                try {
                    decreaseStatement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (increaseStatement != null) {
                try {
                    increaseStatement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (transactionStatement != null) {
                try {
                    transactionStatement.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            this.conn.closeConnection();
        }
    }

    public boolean checkAccountBalances(String cpf) {
        List<BankAccounts> accounts = recoverAccounts(cpf);

        for (BankAccounts account : accounts) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                return true; 
            }
        }
        return false;
    }
}
