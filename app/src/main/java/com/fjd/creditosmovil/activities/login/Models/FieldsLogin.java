package com.fjd.creditosmovil.activities.login.Models;

import com.google.gson.annotations.SerializedName;

public class FieldsLogin {
    @SerializedName("EIDENTIF_USUARIO")
    private String user;
    @SerializedName("ECLAVE")
    private String password;
    @SerializedName("ETOKEN")
    private String tokenRefresh;

    private String action;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenRefresh() {
        return tokenRefresh;
    }

    public void setTokenRefresh(String tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
