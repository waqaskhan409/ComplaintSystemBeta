package com.example.complaintsystembeta.model;

public class Department {
    private String department_id;
    private String department_name;
    private String department_description;
    private String last_update_ts;
    private String department_city_name;

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_description() {
        return department_description;
    }

    public void setDepartment_description(String department_description) {
        this.department_description = department_description;
    }

    public String getLast_update_ts() {
        return last_update_ts;
    }

    public void setLast_update_ts(String last_update_ts) {
        this.last_update_ts = last_update_ts;
    }

    public String getDepartment_city_name() {
        return department_city_name;
    }

    public void setDepartment_city_name(String department_city_name) {
        this.department_city_name = department_city_name;
    }

    public Department(String department_id, String department_name, String department_description, String last_update_ts, String department_city_name) {
        this.department_id = department_id;
        this.department_name = department_name;
        this.department_description = department_description;
        this.last_update_ts = last_update_ts;
        this.department_city_name = department_city_name;
    }
}
