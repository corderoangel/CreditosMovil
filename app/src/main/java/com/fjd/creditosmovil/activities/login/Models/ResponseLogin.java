package com.fjd.creditosmovil.activities.login.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

    @SerializedName("S_1")
    private int s1;

    @SerializedName("S_2")
    private String s2;

    @SerializedName("S_EST_SESSION")
    private String state_session;

    @SerializedName("JWT")
    private String jwt;

    public int getS_1() {
        return s1;
    }

    public void set_S1(int s1) {
        this.s1 = s1;
    }

    public String getS_2() {
        return s2;
    }

    public void setS_2(String s2) {
        this.s2 = s2;
    }

    public String getJWT() {
        return jwt;
    }

    public void setJWT(String jwt) {
        this.jwt = jwt;
    }
    public String getState_session() {
        return state_session;
    }

    public void setState_session(String state_session) {
        this.state_session = state_session;
    }

}
