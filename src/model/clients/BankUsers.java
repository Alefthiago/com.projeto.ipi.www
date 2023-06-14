package model.clients;

import java.util.List;

import connMySQL.ConnBD;
import dao.AccountDAO;
import model.bankAccounts.BankAccounts;

public class BankUsers {


    private String cpf;
    private String fullName;
    private String pass;
    private List<BankAccounts> accounts;

    public BankUsers(String cpf, String fullName, String pass) {
        
        this.cpf = cpf;
        this.fullName = fullName;
        this.pass = pass;
        this.accounts = loadAccounts();
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<BankAccounts> getAccounts() {
        return this.accounts;
    }

    public Boolean verifyAccounts()
    {
      return null;       
    }

    @Override
    public String toString() {
        return "{" +
                " cpf='" + getCpf() + "'" +
                ", fullName='" + getFullName() + "'" +
                ", pass='" + getPass() + "'" +
                ", accounts='" + getAccounts() + "'" +
                "}";
    }

    public List<BankAccounts> loadAccounts(){
        AccountDAO accountList = new AccountDAO(new ConnBD());
        return accountList.recoverAccounts(this.cpf);
    }

    public void addAccount(BankAccounts ac) {
        AccountDAO account = new AccountDAO(new ConnBD());
        account.addAccount(ac, this.cpf);
    }
}