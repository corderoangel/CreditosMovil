package com.fjd.creditosmovil.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("S_1")
    private int s1;

    @SerializedName("S_2")
    private String s2;

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
}
