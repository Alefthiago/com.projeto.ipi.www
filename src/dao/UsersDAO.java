package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import connMySQL.ConnBD;
import model.clients.BankUsers;
import model.customException.ExistingCPFException;

public class UsersDAO {

    private ConnBD conn;

    public UsersDAO(ConnBD conn) {
        this.conn = conn;
    }

    public Boolean checkCpf(String cpf) throws ExistingCPFException {
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
                    throw new ExistingCPFException("CPF indisponível!");
                }
            } else {
                return true;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Erro no servidor: " + exception.getMessage(), exception);
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
                    System.out.println("Erro ao fechar o PreparedStatement: " + exception.getMessage());
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
                    JOptionPane.showMessageDialog(null, "CPF ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                BankUsers user = new BankUsers(resultCpf, resultFullName, resultPass);
                return user;
            }

            JOptionPane.showMessageDialog(null, "CPF ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
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
            this.conn.closeConnection();
        }
    }



    public Integer getUserIdByCPF(String name) {
        String sql = "SELECT USR_ID FROM java_user WHERE USR_NAME = ?";

        try {
            PreparedStatement statement = conn.getConnection().prepareStatement(sql);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("USR_ID");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
