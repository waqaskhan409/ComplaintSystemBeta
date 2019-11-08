package com.example.complaintsystembeta.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PermanentLogin")
public class PermanentLogin {

    public int getId() {
        return id;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String CNIC;

    private Boolean isLoggedIn;
    private String userName;


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

    public PermanentLogin(String CNIC, Boolean isLoggedIn, String userName) {
        this.CNIC = CNIC;
        this.isLoggedIn = isLoggedIn;
        this.userName = userName;
    }
}



