package com.sebastialonso.transfer.models;

import java.util.UUID;

/**
 * Created by seba on 30-07-16.
 */
public class Account {
    private String accountId;
    private String bankName;
    private String accountNumber;
    private String ownerName;
    private String ownerRut;
    private String accountType;

    public Account() {
    }

    public Account(String bankName, String accountNumber, String ownerName, String ownerRut, String accountType) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.ownerRut = ownerRut;
        this.accountType = accountType;
    }

    public Account(String bankName, String accountNumber, String ownerName, String ownerRut, String accountType, String accountId) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.ownerRut = ownerRut;
        this.accountType = accountType;
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId() {
        this.accountId = UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.accountId = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerRut() {
        return ownerRut;
    }

    public void setOwnerRut(String ownerRut) {
        this.ownerRut = ownerRut;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
