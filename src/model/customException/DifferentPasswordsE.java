package model.customException;

import javax.swing.JOptionPane;

public class DifferentPasswordsE extends Exception {
    private String message = "As senhas precisam ser iguais!";

    public DifferentPasswordsE() {
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
