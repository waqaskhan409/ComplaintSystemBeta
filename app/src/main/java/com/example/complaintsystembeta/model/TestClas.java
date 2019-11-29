package com.example.complaintsystembeta.model;

import com.google.gson.annotations.SerializedName;

public class TestClas {
   @SerializedName("sqlMessage")
   private String error;

   @SerializedName("success")
   private String success;

    public TestClas(String error, String success) {
        this.error = error;
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}



