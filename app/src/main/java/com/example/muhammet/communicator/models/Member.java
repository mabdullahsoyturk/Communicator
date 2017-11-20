package com.example.muhammet.communicator.models;

/**
 * Created by Muhammet on 1.11.2017.
 */

public class Member {

    private int member_icon_id;
    private String member_name;
    private String member_debt;

    public Member(int member_icon_id, String member_name, String member_debt) {
        this.member_icon_id = member_icon_id;
        this.member_name = member_name;
        this.member_debt = member_debt;
    }

    public String getMember_debt() {
        return member_debt;
    }

    public void setMember_debt(String member_debt) {
        this.member_debt = member_debt;
    }

    public int getMember_icon_id() {
        return member_icon_id;
    }

    public void setMember_icon_id(int member_icon_id) {
        this.member_icon_id = member_icon_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }
}
