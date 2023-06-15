package model.customException;

import javax.swing.JOptionPane;

public class EmptyFieldsE extends Exception {
    private String message = "Todos os campos devem ser preenchidos!";

    public EmptyFieldsE() {
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
