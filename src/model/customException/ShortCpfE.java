package model.customException;

import javax.swing.JOptionPane;

public class ShortCpfE extends Exception {
    private String message = "CPF deve conter 11 digitos!";

    public ShortCpfE() {
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
