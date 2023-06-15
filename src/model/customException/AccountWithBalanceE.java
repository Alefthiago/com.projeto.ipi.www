package model.customException;

import javax.swing.JOptionPane;

public class AccountWithBalanceE extends Exception {
    private String message = "Não é possível remover uma conta com saldo.";

    public AccountWithBalanceE() {
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
