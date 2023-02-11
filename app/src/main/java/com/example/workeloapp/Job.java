package com.example.workeloapp;

public class Job {
    String Title, Desc, Wage, Address, Assigned;

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getWage() {
        return Wage;
    }

    public String getAddress() {
        return Address;
    }

    public String getAssigned() {
        return Assigned;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setWage(String wage) {
        Wage = wage;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setAssigned(String assigned) {
        Assigned = assigned;
    }
}
