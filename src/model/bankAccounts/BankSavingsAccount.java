package model.bankAccounts;

public class BankSavingsAccount extends BankAccounts {
    private String type;

    public BankSavingsAccount(String cpfOwner) {
        super(cpfOwner, "savings");
    }

    public String getType() {
        return this.type;
    }
}