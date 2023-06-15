package model.customException;

import javax.swing.JOptionPane;

public class SameAccountTransferE extends Exception {
    private String message = "Não é possível transferir para a mesma conta!";

    public SameAccountTransferE() {
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
