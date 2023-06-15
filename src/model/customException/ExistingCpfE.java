package model.customException;

import javax.swing.JOptionPane;

public class ExistingCpfE extends Exception {
    private String message = "Cpf já utilizado";

    public ExistingCpfE() {
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
