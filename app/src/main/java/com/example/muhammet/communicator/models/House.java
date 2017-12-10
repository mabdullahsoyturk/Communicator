package com.example.muhammet.communicator.models;

/**
 * Created by Muhammet on 9.12.2017.
 */

public class House {

    private int id;
    private String name;
    private String created_time;

    public House(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }
}
