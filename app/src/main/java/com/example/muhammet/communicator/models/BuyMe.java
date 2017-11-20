package com.example.muhammet.communicator.models;

/**
 * Created by Muhammet on 12.11.2017.
 */

public class BuyMe {

    private String name;
    private String desc;

    public BuyMe(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
