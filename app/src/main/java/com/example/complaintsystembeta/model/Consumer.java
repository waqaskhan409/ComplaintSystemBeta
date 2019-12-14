package com.example.complaintsystembeta.model;

public class Consumer {
    private String account_number;
    private String user_cnic;
    private String user_name;
    private String user_email;
    private String user_password;
    private String user_address;
    private String user_cnic_front_image;
    private String user_cnic_back_image;
    private String user_wasa_bill_image;
    private String created_at;
    private String user_contact;
    private String is_verified;

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
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

    public String getUser_wasa_bill_image() {
        return user_wasa_bill_image;
    }

    public void setUser_wasa_bill_image(String user_wasa_bill_image) {
        this.user_wasa_bill_image = user_wasa_bill_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Consumer(String account_number, String user_cnic, String user_name, String user_email, String user_password, String user_address, String user_cnic_front_image, String user_cnic_back_image, String user_wasa_bill_image, String created_at, String user_contact, String is_verified) {
        this.account_number = account_number;
        this.user_cnic = user_cnic;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_address = user_address;
        this.user_cnic_front_image = user_cnic_front_image;
        this.user_cnic_back_image = user_cnic_back_image;
        this.user_wasa_bill_image = user_wasa_bill_image;
        this.created_at = created_at;
        this.user_contact = user_contact;
        this.is_verified = is_verified;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }
}
