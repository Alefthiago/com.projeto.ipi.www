package model.bankAccounts;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

public class BankAccounts {
    // atributos
    private Integer number;
    private String type;
    private String cpfOwner;
    private float balance;
    private LocalDateTime openingDate;
    private int status;

    public BankAccounts(String cpfOwner, String type) {
        this.number = new Random().nextInt(999999999);
        this.cpfOwner = cpfOwner;
        this.balance = 0f;
        this.openingDate = LocalDateTime.now();
        this.status = 1;
        this.type = type;
    }

    // getters
    public Integer getNumber() {
        return this.number;
    }

    public String getType() {
        return this.type;
    }

    public String cpfOwner() {
        return this.cpfOwner;
    }

    public Float getBalance() {
        return this.balance;
    }

    public LocalDateTime getOpeningDate() {
        return this.openingDate;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getCpfOwner() {
    return this.cpfOwner;
    }

    // métodos

    @Override
    public String toString() {
        return "{" +
            " number='" + getNumber() + "'" +
            ", type='" + getType() + "'" +
            ", cpfOwner='" + getCpfOwner() + "'" +
            ", balance='" + getBalance() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
    
    public Boolean toDeposit(Float value) {

        if (this.status != 1) {
            return false;
        }
        if (value <= 0) {
            return false;
        }

        this.balance += value;
        return true;
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
