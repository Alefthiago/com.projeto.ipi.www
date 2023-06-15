package model.customException;

import javax.swing.JOptionPane;

public class InvalidCPFOrPasswordE extends Exception {
    private String message = "CPF ou senha inválidos!";

    public InvalidCPFOrPasswordE() {
        super();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void alert() {
        JOptionPane.showMessageDialog(null, getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
