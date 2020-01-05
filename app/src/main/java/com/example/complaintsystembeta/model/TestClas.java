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

//    const success = {
//            success: "Success"
//    }
//    const fail= {
//            error: "Query failed with sql message:+ " +e;
//    }

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



