package model.customException;

import javax.swing.JOptionPane;

public class InvalidCpfTypeE extends Exception {
    private String message = "CPF deve conter apenas números!";

    public InvalidCpfTypeE() {
        super();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void alert() {
        JOptionPane.showMessageDialog(null, getMessage(), "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}
