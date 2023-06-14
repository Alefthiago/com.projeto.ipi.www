package model.clients;

import java.util.ArrayList;
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
        this.accounts = new ArrayList<>();
        loadAccounts();
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

    @Override
    public String toString() {
        return "{" +
                " cpf='" + getCpf() + "'" +
                ", fullName='" + getFullName() + "'" +
                ", pass='" + getPass() + "'" +
                ", accounts='" + getAccounts() + "'" +
                "}";
    }

    public void loadAccounts(){
        AccountDAO accountList = new AccountDAO(new ConnBD());
        this.accounts = accountList.recoverAccounts(this.cpf);
    }

    public void addAccount(BankAccounts ac) {
        AccountDAO account = new AccountDAO(new ConnBD());
        account.addAccount(ac, this.cpf);
    }

    public void removeAccount(BankAccounts ac, BankUsers user){
        AccountDAO account = new AccountDAO(new ConnBD());
        account.removeAccount(ac, user);
    }
}