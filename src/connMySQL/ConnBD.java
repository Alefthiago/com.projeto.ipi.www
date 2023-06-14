package connMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnBD {
    private final static String BD_USER = "root";
    private final static String BD_PASS = "root";
    private final static String BD_ADDRESS = "localhost";
    private final static String BD_PORT = "3306";
    private final static String BD_DATABASE = "bank_app";

    private Connection conn; // Declaração do campo conn

    public Connection getConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + BD_ADDRESS + ":" + BD_PORT + "/" + BD_DATABASE, BD_USER, BD_PASS);
            return conn;
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(null, "Erro no servidor: " + exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException exception) {
                System.out.println("Erro ao fechar a conexão: " + exception.getMessage());
            }
        }
    }
}
