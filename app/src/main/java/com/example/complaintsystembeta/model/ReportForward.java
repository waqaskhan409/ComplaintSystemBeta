package com.example.complaintsystembeta.model;

public class ReportForward {
    private String complains_reporting_id;
    private String complain_id;
    private String forwards_to;
    private String forwards_by;
    private String forwards_date;
    private String forwards_message;
    private String suggested_date_reply;
    private String reporting_attachments_id;
    private String reporting_attachment_file_type;
    private String reporting_attachment_name;
    private String reporting_created_at;
    private String des_id;
    private String des_title;
    private String des_scale;
    private String department_id;
    private String last_update_ts;
    private String full_name;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    private String employee_name;
    private String is_reply;
    private String is_delay;
    private String status;
    private String is_current;
    private String is_seen;
    private String is_acknowledged;
    private String is_public;



    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getComplains_reporting_id() {
        return complains_reporting_id;
    }

    public void setComplains_reporting_id(String complains_reporting_id) {
        this.complains_reporting_id = complains_reporting_id;
    }

    public String getComplain_id() {
        return complain_id;
    }

    public void setComplain_id(String complain_id) {
        this.complain_id = complain_id;
    }

    public String getForwards_to() {
        return forwards_to;
    }

    public void setForwards_to(String forwards_to) {
        this.forwards_to = forwards_to;
    }

    public String getForwards_by() {
        return forwards_by;
    }

    public void setForwards_by(String forwards_by) {
        this.forwards_by = forwards_by;
    }

    public String getForwards_date() {
        return forwards_date;
    }

    public void setForwards_date(String forwards_date) {
        this.forwards_date = forwards_date;
    }

    public String getForwards_message() {
        return forwards_message;
    }

    public void setForwards_message(String forwards_message) {
        this.forwards_message = forwards_message;
    }

    public String getSuggested_date_reply() {
        return suggested_date_reply;
    }

    public void setSuggested_date_reply(String suggested_date_reply) {
        this.suggested_date_reply = suggested_date_reply;
    }

    public String getReporting_attachments_id() {
        return reporting_attachments_id;
    }

    public void setReporting_attachments_id(String reporting_attachments_id) {
        this.reporting_attachments_id = reporting_attachments_id;
    }

    public String getReporting_attachment_file_type() {
        return reporting_attachment_file_type;
    }

    public void setReporting_attachment_file_type(String reporting_attachment_file_type) {
        this.reporting_attachment_file_type = reporting_attachment_file_type;
    }

    public String getReporting_attachment_name() {
        return reporting_attachment_name;
    }

    public void setReporting_attachment_name(String reporting_attachment_name) {
        this.reporting_attachment_name = reporting_attachment_name;
    }

    public String getReporting_created_at() {
        return reporting_created_at;
    }

    public void setReporting_created_at(String reporting_created_at) {
        this.reporting_created_at = reporting_created_at;
    }

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

    public String  isIs_reply() {
        return is_reply;
    }

    public void setIs_reply(String  is_reply) {
        this.is_reply = is_reply;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String  isIs_current() {
        return is_current;
    }

    public void setIs_current(String is_current) {
        this.is_current = is_current;
    }

    public String getIs_reply() {
        return is_reply;
    }

    public String getIs_current() {
        return is_current;
    }

    public String getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(String is_seen) {
        this.is_seen = is_seen;
    }

    public String getIs_acknowledged() {
        return is_acknowledged;
    }

    public void setIs_acknowledged(String is_acknowledged) {
        this.is_acknowledged = is_acknowledged;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public ReportForward(String complains_reporting_id, String complain_id, String forwards_to, String forwards_by, String forwards_date, String forwards_message, String suggested_date_reply, String reporting_attachments_id, String reporting_attachment_file_type, String reporting_attachment_name, String reporting_created_at, String des_id, String des_title, String des_scale, String department_id, String last_update_ts, String full_name, String employee_name, String is_reply, String is_delay, String status, String is_current, String is_seen, String is_acknowledged, String is_public) {
        this.complains_reporting_id = complains_reporting_id;
        this.complain_id = complain_id;
        this.forwards_to = forwards_to;
        this.forwards_by = forwards_by;
        this.forwards_date = forwards_date;
        this.forwards_message = forwards_message;
        this.suggested_date_reply = suggested_date_reply;
        this.reporting_attachments_id = reporting_attachments_id;
        this.reporting_attachment_file_type = reporting_attachment_file_type;
        this.reporting_attachment_name = reporting_attachment_name;
        this.reporting_created_at = reporting_created_at;
        this.des_id = des_id;
        this.des_title = des_title;
        this.des_scale = des_scale;
        this.department_id = department_id;
        this.last_update_ts = last_update_ts;
        this.full_name = full_name;
        this.employee_name = employee_name;
        this.is_reply = is_reply;
        this.is_delay = is_delay;
        this.status = status;
        this.is_current = is_current;
        this.is_seen = is_seen;
        this.is_acknowledged = is_acknowledged;
        this.is_public = is_public;
    }

    public String getIs_delay() {
        return is_delay;
    }

    public void setIs_delay(String is_delay) {
        this.is_delay = is_delay;
    }
}
