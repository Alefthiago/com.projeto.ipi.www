package model.customException;

import javax.swing.JOptionPane;

public class AccountHasBalanceE extends Exception {
    private String message = "Não é possível remover a conta devido ao saldo em uma das contas.";

    public AccountHasBalanceE() {
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
