package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import connMySQL.ConnBD;
import model.clients.BankUsers;
import model.customException.ExistingCpfE;
import model.customException.InvalidCPFOrPasswordE;

public class UsersDAO {

    private ConnBD conn;

    public UsersDAO(ConnBD conn) {
        this.conn = conn;
    }

    public Boolean checkCpf(String cpf) throws ExistingCpfE {
        String sql = "SELECT CLI_CPF FROM BK_CLIENTS WHERE CLI_CPF = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            statement.setString(1, cpf);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String existingCpf = resultSet.getString("CLI_CPF");
                if (existingCpf.equals(cpf)) {
                    throw new ExistingCpfE();
                }
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro no servidor: " + e.getMessage(), e);
        } catch (ExistingCpfE e) {
            e.alert();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                   JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                   JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                }
            }
            this.conn.closeConnection();
        }
        return false;
    }

    public Boolean addUser(BankUsers client) {
        String sql = "INSERT INTO BK_CLIENTS (CLI_CPF, CLI_FULLNAME, CLI_PASSWORD) VALUES (?, ?, ?)";
        PreparedStatement statement = null;

        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            statement.setString(1, client.getCpf());
            statement.setString(2, client.getFullName());
            statement.setString(3, client.getPass());
            statement.executeUpdate();
            return true;
        } catch (SQLException exception) {
            throw new RuntimeException("Erro no servidor: " + exception.getMessage(), exception);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                   JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            this.conn.closeConnection();
        }
    }

    public BankUsers loginUser(String cpf, String pass) {
        String sql = "SELECT * FROM BK_CLIENTS WHERE CLI_CPF = ? AND CLI_PASSWORD = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            statement.setString(1, cpf);
            statement.setString(2, pass);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String resultCpf = resultSet.getString("CLI_CPF");
                String resultFullName = resultSet.getString("CLI_FULLNAME");
                String resultPass = resultSet.getString("CLI_PASSWORD");

                if (!resultCpf.equals(cpf) || !resultPass.equals(pass)) {
                    throw new InvalidCPFOrPasswordE();
                }
                BankUsers user = new BankUsers(resultCpf, resultFullName, resultPass);
                return user;
            }
            throw new InvalidCPFOrPasswordE();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (InvalidCPFOrPasswordE exeption) {
            exeption.alert();
            return null;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                   JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                }
            }
            this.conn.closeConnection();
        }
    }

    public BankUsers getUserByCPF(String cpf) {
        String getUserSql = "SELECT * FROM BK_CLIENTS WHERE CLI_CPF = ?";
        PreparedStatement statementUser = null;
        ResultSet resultSetUser = null;
        BankUsers data = null;
        try {
            statementUser = this.conn.getConnection().prepareStatement(getUserSql);
            statementUser.setString(1, cpf);
            resultSetUser = statementUser.executeQuery();

            if (resultSetUser.next()) {
                String cpfUser = resultSetUser.getString("CLI_CPF");
                String fullname = resultSetUser.getString("CLI_FULLNAME");
                String pass = resultSetUser.getString("CLI_PASSWORD");

                data = new BankUsers(cpfUser, fullname, pass);
                data.loadAccounts();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (resultSetUser != null) {
                try {
                    resultSetUser.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statementUser != null) {
                try {
                    statementUser.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.conn.closeConnection();
        return data;
    }
}
