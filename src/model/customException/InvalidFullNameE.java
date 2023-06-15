package model.customException;

import javax.swing.JOptionPane;

public class InvalidFullNameE extends Exception {
    private String message = "Nome e sobrenome devem conter apenas letras!";

    public InvalidFullNameE() {
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
