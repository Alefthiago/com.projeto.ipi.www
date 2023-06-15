package model.bankAccounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public class BankTransactionRecord {
    private Integer number;
    private long originNumberAccount;
    private long destinyNumberAccount;
    private LocalDate dateTransaction;
    private BigDecimal valueTransaction;

    public BankTransactionRecord(long originNumberAccount, long destinyNumberAccount, BigDecimal valueTransaction) {
        this.number = new Random().nextInt(999999999);
        this.originNumberAccount = originNumberAccount;
        this.destinyNumberAccount = destinyNumberAccount;
        this.dateTransaction = LocalDate.now();
        this.valueTransaction = valueTransaction;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getOriginNumberAccount() {
        return originNumberAccount;
    }

    public void setOriginNumberAccount(long originNumberAccount) {
        this.originNumberAccount = originNumberAccount;
    }

    public long getDestinyNumberAccount() {
        return destinyNumberAccount;
    }

    public void setDestinyNumberAccount(long destinyNumberAccount) {
        this.destinyNumberAccount = destinyNumberAccount;
    }

    public LocalDate getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDate dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public BigDecimal getValueTransaction() {
        return valueTransaction;
    }

    public void setValueTransaction(BigDecimal valueTransaction) {
        this.valueTransaction = valueTransaction;
    }
}
