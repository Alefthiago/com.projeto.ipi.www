package model.bankAccounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

import connMySQL.ConnBD;
import dao.AccountDAO;

public class BankAccounts {
    private Integer number;
    private String type;
    private String cpfOwner;
    private BigDecimal balance;
    private LocalDate openingDate;
    private int status;

    public BankAccounts(String cpfOwner, String type) {
        this.number = new Random().nextInt(999999999);
        this.cpfOwner = cpfOwner;
        this.balance = BigDecimal.ZERO;
        this.openingDate = LocalDate.now();
        this.status = 1;
        this.type = type;
    }

    public Integer getNumber() {
        return this.number;
    }

    public String getType() {
        return this.type;
    }

    public String getOwner() {
        return this.cpfOwner;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOwner(String cpf) {
        this.cpfOwner = cpf;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setOpeningDate(LocalDate date) {
        this.openingDate = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAccountDetails(int number, String type, String cpfOwner, BigDecimal balance, LocalDate openingDate,
            int status) {
        setNumber(number);
        setType(type);
        setOwner(cpfOwner);
        setBalance(balance);
        setOpeningDate(openingDate);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "{" +
                " number='" + getNumber() + "'" +
                ", type='" + getType() + "'" +
                ", cpfOwner='" + getOwner() + "'" +
                ", balance='" + getBalance() + "'" +
                ", openingDate='" + getOpeningDate() + "'" +
                ", status='" + getStatus() + "'" +
                "}";
    }

    public Boolean toDeposit(BigDecimal value) {
        if (this.status != 1) {
            return false;
        }
        AccountDAO DAO = new AccountDAO(new ConnBD());
        if (!DAO.performDeposit(this.number, value)) {
            return false;
        }
        return true;
    }

    public Boolean toWithdraw(BigDecimal value) {
        if (this.status != 1) {
            return false;
        }
        AccountDAO withdraw = new AccountDAO(new ConnBD());
        if (!withdraw.performWithdraw(this.number, value)) {
            return false;
        }
        return true;
    }

    public Boolean toTransfer(BigDecimal value, int destinyNumber) {
        AccountDAO DAO = new AccountDAO(new ConnBD());
        DAO.performTransfer(value, destinyNumber, this.getNumber());
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.number);
    }

    public boolean compareClass(Object obj) {
        if (getClass() == obj.getClass()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        try {
            BankAccounts other = (BankAccounts) obj;
            return Objects.equals(this.number, other.number) &&
                    Objects.equals(this.openingDate, other.openingDate);
        } catch (ClassCastException exception) {
            throw new RuntimeException("Houve um erro tente novamente!", exception);
        }
    }
}
