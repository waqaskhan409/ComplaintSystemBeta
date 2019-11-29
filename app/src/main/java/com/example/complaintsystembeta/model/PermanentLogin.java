package com.example.complaintsystembeta.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PermanentLogin")
public class PermanentLogin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String CNIC;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Boolean getEmployee() {
        return isEmployee;
    }

    public void setEmployee(Boolean employee) {
        isEmployee = employee;
    }

    private String accountNumber;

    private Boolean isLoggedIn;
    private String userName;
    private Boolean isEmployee;



    public String getCNIC() {
        return CNIC;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }


    public PermanentLogin( String CNIC, String accountNumber, Boolean isLoggedIn, String userName, Boolean isEmployee) {
        this.CNIC = CNIC;
        this.accountNumber = accountNumber;
        this.isLoggedIn = isLoggedIn;
        this.userName = userName;
        this.isEmployee = isEmployee;
    }
}



