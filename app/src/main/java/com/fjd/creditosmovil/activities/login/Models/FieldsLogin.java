package com.fjd.creditosmovil.activities.login.Models;

import com.google.gson.annotations.SerializedName;

public class FieldsLogin {
    @SerializedName("EIDENTIF_USUARIO")
    private String user;
    @SerializedName("ECLAVE")
    private String password;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
