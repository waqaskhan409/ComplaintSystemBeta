package com.example.complaintsystembeta.model;

import com.google.gson.annotations.SerializedName;

public class Designation {
    @SerializedName("des_id")
    private String des_id;

    public String getDes_id() {
        return des_id;
    }

    public void setDes_id(String des_id) {
        this.des_id = des_id;
    }

    public String getDes_title() {
        return des_title;
    }

    public void setDes_title(String des_title) {
        this.des_title = des_title;
    }

    public String getDes_scale() {
        return des_scale;
    }

    public void setDes_scale(String des_scale) {
        this.des_scale = des_scale;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getLast_update_ts() {
        return last_update_ts;
    }

    public void setLast_update_ts(String last_update_ts) {
        this.last_update_ts = last_update_ts;
    }

    public Designation(String des_id, String des_title, String des_scale, String department_id, String last_update_ts) {
        this.des_id = des_id;
        this.des_title = des_title;
        this.des_scale = des_scale;
        this.department_id = department_id;
        this.last_update_ts = last_update_ts;
    }

    @SerializedName("des_title")
    private String des_title;
    @SerializedName("des_scale")
    private String des_scale;
    @SerializedName("department_id")
    private String department_id;
    @SerializedName("last_update_ts")
    private String last_update_ts;

}
