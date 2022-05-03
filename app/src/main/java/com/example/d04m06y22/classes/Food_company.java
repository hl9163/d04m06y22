package com.example.d04m06y22.classes;

public class Food_company {
    private String company_id;
    private String company_name;
    private String phone_number;
    private String second_phone_number;
    private boolean provide_service;

    public Food_company (){}
    public Food_company(String company_id, String company_name, String phone_number, String second_phone_number, boolean provide_service) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.phone_number = phone_number;
        this.second_phone_number = second_phone_number;
        this.provide_service = provide_service;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSecond_phone_number() {
        return second_phone_number;
    }

    public void setSecond_phone_number(String second_phone_number) {
        this.second_phone_number = second_phone_number;
    }

    public boolean isProvide_service() {
        return provide_service;
    }

    public void setProvide_service(boolean provide_service) {
        this.provide_service = provide_service;
    }


}
