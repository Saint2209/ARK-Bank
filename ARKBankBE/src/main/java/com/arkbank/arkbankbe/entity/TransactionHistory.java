package com.arkbank.arkbankbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger transactionId;

    @ManyToOne
    @JoinColumn(name = "bankAccount", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "debit", nullable = false)
    private Long debit;

    @Column(name = "credit", nullable = false)
    private Long credit;

    @Column(name = "date", nullable = false)
    private Timestamp transactionDate;

    public void setDebit(Long debit) {
        this.debit = debit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }


    public Long getDebit() {
        return debit;
    }

    public Long getCredit() {
        return credit;
    }


    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
