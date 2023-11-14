package com.example.botusers.Classes;

public class Bot_users {
    private String expiry_date;
    private String installed_date;
    private String mac_address;
    private String payment_status;

    public Bot_users() {
        // Default constructor required for Firebase
    }

    public Bot_users(String expiry_date, String installed_date, String mac_address, String payment_status) {
        this.expiry_date = expiry_date;
        this.installed_date = installed_date;
        this.mac_address = mac_address;
        this.payment_status = payment_status;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getInstalled_date() {
        return installed_date;
    }

    public void setInstalled_date(String installed_date) {
        this.installed_date = installed_date;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
