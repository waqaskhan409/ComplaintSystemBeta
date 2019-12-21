package com.example.complaintsystembeta.model;

import com.google.gson.annotations.SerializedName;

public class AllComplains {
    @SerializedName("complain_id")
    private String complain_id;

    @SerializedName("account_number")
    private String account_number;

    @SerializedName("complain_body")
    private String complain_body;

    @SerializedName("complain_status")
    private String complain_status;

    @SerializedName("created_us")
    private String created_us;

    @SerializedName("attachment_id")
    private String attachment_id;

    @SerializedName("attachment_name")
    private String attachment_name;

    @SerializedName("attachment_file_type")
    private String attachment_file_type;

    @SerializedName("created_at")
    private String created_at;

    private String days;


    public AllComplains(String complain_id, String account_number, String complain_body, String complain_status, String created_us, String attachment_id, String attachment_name, String attachment_file_type, String created_at, String days) {
        this.complain_id = complain_id;
        this.account_number = account_number;
        this.complain_body = complain_body;
        this.complain_status = complain_status;
        this.created_us = created_us;
        this.attachment_id = attachment_id;
        this.attachment_name = attachment_name;
        this.attachment_file_type = attachment_file_type;
        this.created_at = created_at;
        this.days = days;
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getAttachment_file_type() {
        return attachment_file_type;
    }

    public void setAttachment_file_type(String attachment_file_type) {
        this.attachment_file_type = attachment_file_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComplain_id() {
        return complain_id;
    }

    public void setComplain_id(String complain_id) {
        this.complain_id = complain_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getComplain_body() {
        return complain_body;
    }

    public void setComplain_body(String complain_body) {
        this.complain_body = complain_body;
    }

    public String getComplain_status() {
        return complain_status;
    }

    public void setComplain_status(String complain_status) {
        this.complain_status = complain_status;
    }

    public String getCreated_us() {
        return created_us;
    }

    public void setCreated_us(String created_us) {
        this.created_us = created_us;
    }


    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
