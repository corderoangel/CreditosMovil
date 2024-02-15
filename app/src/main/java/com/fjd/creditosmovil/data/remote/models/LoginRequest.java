package com.fjd.creditosmovil.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("EIDENTIF_USUARIO")
    private String ccAuth;

    @SerializedName("ECLAVE")
    private String codAuth;

    public LoginRequest(String ccAuth, String codAuth) {
        this.ccAuth = ccAuth;
        this.codAuth = codAuth;
    }
}

