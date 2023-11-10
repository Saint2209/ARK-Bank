package com.arkbank.arkbankbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;

//Entity is a database object
@Entity 
public class BankAccount {
    //Name specifiers by using @column
    @Id
    @Column(name = "ACCOUNT_NUMBER") 
    private Integer accountNumber;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "BALANCE", nullable = false)
    private Long balance;

    @Column(name = "NEXT_PAYMENT_DATE", nullable = false)
    private Date nextPaymentDate;

    @Column(name = "SESSION")
    private String session;

    // Method to authenticate a user based on account number and password
    public Boolean authenticate(Integer accountNumber,
            String password) {
        return this.accountNumber.equals(accountNumber)
                && this.password.equals(password);
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public Date getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(Date nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public String getPassword() {
        return password;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
