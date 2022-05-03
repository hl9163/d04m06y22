package com.example.d04m06y22.classes;

public class Order {
    private String date;
    private String time;
    private String food_company_name;
    private String worker_id;
    private String appetizer;
    private String main_course;
    private String extra;

    public Order(){}
    public Order(String date, String time, String food_company_name, String worker_id, String appetizer, String main_course, String extra, String dessert, String drink) {
        this.date = date;
        this.time = time;
        this.food_company_name = food_company_name;
        this.worker_id = worker_id;
        this.appetizer = appetizer;
        this.main_course = main_course;
        this.extra = extra;
        this.dessert = dessert;
        this.drink = drink;
    }

    private String dessert;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFood_company_name() {
        return food_company_name;
    }

    public void setFood_company_name(String food_company_name) {
        this.food_company_name = food_company_name;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getAppetizer() {
        return appetizer;
    }

    public void setAppetizer(String appetizer) {
        this.appetizer = appetizer;
    }

    public String getMain_course() {
        return main_course;
    }

    public void setMain_course(String main_course) {
        this.main_course = main_course;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDessert() {
        return dessert;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    private String drink;
}
