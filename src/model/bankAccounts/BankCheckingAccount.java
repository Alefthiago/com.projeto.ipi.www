package model.bankAccounts;

public class BankCheckingAccount extends BankAccounts {
    
    public BankCheckingAccount(String cpfOwner) {
        super(cpfOwner, "checking");
    }
}
