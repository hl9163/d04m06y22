package com.example.d04m06y22.classes;

public class Worker {
    private String personal_id;
    private String card_id;
    private String first_name;
    private String last_name;
    private String company;
    private String phone_number;
    private boolean is_working;

    public Worker (){}
    public Worker(String personal_id, String card_id, String first_name, String last_name, String phone_number, String company, boolean is_working){
        this.personal_id = personal_id;
        this.card_id = card_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company = company;
        this.phone_number = phone_number;
        this.is_working = is_working;

    }

    public String getPersonal_id() {
        return personal_id;
    }

    public void setPersonal_id(String personal_id) {
        this.personal_id = personal_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isIs_working() {
        return is_working;
    }

    public void setIs_working(boolean is_working) {
        this.is_working = is_working;
    }






}
