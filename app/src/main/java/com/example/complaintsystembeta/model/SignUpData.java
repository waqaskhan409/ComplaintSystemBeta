package com.example.complaintsystembeta.model;

import com.google.gson.annotations.SerializedName;

public class SignUpData {
    @SerializedName("sign_up_id")
    String sign_up_id;
    @SerializedName("user_cnic")
    String user_cnic;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("user_email")
    String user_email;
    @SerializedName("user_password")
    String user_password;
    @SerializedName("user_cnic_front_image")
    String user_cnic_front_image;
    @SerializedName("user_cnic_back_image")
    String user_cnic_back_image;
    @SerializedName("user_address")
    String user_address;
    @SerializedName("user_contact")
    String user_contact;

    public SignUpData(String sign_up_id, String user_cnic, String user_name, String user_email, String user_password, String user_cnic_front_image, String user_cnic_back_image, String user_address, String user_contact) {
        this.sign_up_id = sign_up_id;
        this.user_cnic = user_cnic;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_cnic_front_image = user_cnic_front_image;
        this.user_cnic_back_image = user_cnic_back_image;
        this.user_address = user_address;
        this.user_contact = user_contact;
    }

    public String  getSign_up_id() {
        return sign_up_id;
    }

    public void setSign_up_id(String sign_up_id) {
        this.sign_up_id = sign_up_id;
    }

    public String getUser_cnic() {
        return user_cnic;
    }

    public void setUser_cnic(String user_cnic) {
        this.user_cnic = user_cnic;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_cnic_front_image() {
        return user_cnic_front_image;
    }

    public void setUser_cnic_front_image(String user_cnic_front_image) {
        this.user_cnic_front_image = user_cnic_front_image;
    }

    public String getUser_cnic_back_image() {
        return user_cnic_back_image;
    }

    public void setUser_cnic_back_image(String user_cnic_back_image) {
        this.user_cnic_back_image = user_cnic_back_image;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }
}
