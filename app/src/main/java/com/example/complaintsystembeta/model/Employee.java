package com.example.complaintsystembeta.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Employee implements Serializable {


    public Employee() {
    }
    private String emp_des_id;
    private String employee_id;

    private String des_id;
    private String cnic;
    private String full_name;
    private String father_name;
    private String order_date;
    private String order_letter_photo;
    private String is_active;
    private String last_update_ts;
    private String appointment_date;
    private String gender;
    private String email;
    private String local;
    private String employee_photo;
    private String des_title;
    private String des_scale;
    private String department_id;
    private String birth_date;
    private String department_name;
    private String department_description;
    private String department_city_name;

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

    public String getDepartment_city_name() {
        return department_city_name;
    }

    public void setDepartment_city_name(String department_city_name) {
        this.department_city_name = department_city_name;
    }

    public Employee(String emp_des_id, String employee_id, String des_id, String cnic, String full_name, String father_name, String order_date, String order_letter_photo, String is_active, String last_update_ts, String appointment_date, String gender, String email, String local, String employee_photo, String des_title, String des_scale, String department_id, String birth_date, String department_name, String department_description, String department_city_name) {
        this.emp_des_id = emp_des_id;
        this.employee_id = employee_id;
        this.des_id = des_id;
        this.cnic = cnic;
        this.full_name = full_name;
        this.father_name = father_name;
        this.order_date = order_date;
        this.order_letter_photo = order_letter_photo;
        this.is_active = is_active;
        this.last_update_ts = last_update_ts;
        this.appointment_date = appointment_date;
        this.gender = gender;
        this.email = email;
        this.local = local;
        this.employee_photo = employee_photo;
        this.des_title = des_title;
        this.des_scale = des_scale;
        this.department_id = department_id;
        this.birth_date = birth_date;
        this.department_name = department_name;
        this.department_description = department_description;
        this.department_city_name = department_city_name;
    }

    public String getEmp_des_id() {
        return emp_des_id;
    }

    public void setEmp_des_id(String emp_des_id) {
        this.emp_des_id = emp_des_id;
    }

    public String getDes_id() {
        return des_id;
    }

    public void setDes_id(String des_id) {
        this.des_id = des_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_letter_photo() {
        return order_letter_photo;
    }

    public void setOrder_letter_photo(String order_letter_photo) {
        this.order_letter_photo = order_letter_photo;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getLast_update_ts() {
        return last_update_ts;
    }

    public void setLast_update_ts(String last_update_ts) {
        this.last_update_ts = last_update_ts;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getEmployee_photo() {
        return employee_photo;
    }

    public void setEmployee_photo(String employee_photo) {
        this.employee_photo = employee_photo;
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

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
