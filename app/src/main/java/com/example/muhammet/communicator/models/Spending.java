package com.example.muhammet.communicator.models;

/**
 * Created by Muhammet on 1.11.2017.
 */

public class Spending {

    private String name;
    private String date;
    private String amount;
    private String share;
    private int icon_id;

    public Spending(String name, String date, String share, int icon_id, String amount) {
        this.name = name;
        this.date = date;
        this.share = share;
        this.icon_id = icon_id;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }
}
