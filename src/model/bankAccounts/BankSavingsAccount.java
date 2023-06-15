package model.bankAccounts;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import connMySQL.ConnBD;
import dao.AccountDAO;

public class BankSavingsAccount extends BankAccounts {

    public BankSavingsAccount(String cpfOwner, String type) {
        super(cpfOwner, type);
    }

    @Override
    public Boolean toTransfer(BigDecimal value, int destinyNumber) {
        BigDecimal depositFee = value.multiply(new BigDecimal("0.05"));
        value = value.subtract(depositFee);
        AccountDAO DAO = new AccountDAO(new ConnBD());
        DAO.performTransfer(value, destinyNumber, this.getNumber());
        JOptionPane.showMessageDialog(null, "Uma taxa de 5% foi aplicada sobre o valor da transferência para conta poupança.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        return null;
    }
}
