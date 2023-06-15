package dao;

import connMySQL.ConnBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ClientsListDAO {
    private ConnBD conn;

    public ClientsListDAO(ConnBD conn) {
        this.conn = conn;
    }

    public List<String> getAllClients() {
        List<String> clientList = new ArrayList<>();
        String sql = "SELECT * FROM BK_CLIENTS";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String clientCpf = resultSet.getString("CLI_CPF");
                String dataClient = "CPF: " + clientCpf + ".  ";
                clientList.add(dataClient);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                   JOptionPane.showMessageDialog(null, "Erro ao fechar o PreparedStatement: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar o ResultSet: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        this.conn.closeConnection();

        return clientList;
    }

    public List<String> getDataClient(String cpf) {
        List<String> dataClient = new ArrayList<>();
        String sql = "SELECT * FROM BK_CLIENTS WHERE CLI_CPF = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = this.conn.getConnection().prepareStatement(sql);
            statement.setString(1, cpf);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String clientCpf = resultSet.getString("CLI_CPF");
                String clientFullName = resultSet.getString("CLI_NAME");
                String data = "cpf: " + clientCpf + "name: " + clientFullName;
                dataClient.add(data);
            }
        } catch (SQLException exception) {
            exception.getStackTrace();
        }
        return null;
    }
}
