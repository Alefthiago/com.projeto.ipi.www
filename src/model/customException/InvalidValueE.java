package model.customException;

import javax.swing.JOptionPane;

public class InvalidValueE extends Exception{
    private String message = "Valor inválido!";

    public InvalidValueE() {
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
