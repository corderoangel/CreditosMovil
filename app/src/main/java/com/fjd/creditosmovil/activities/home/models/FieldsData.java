package com.fjd.creditosmovil.activities.home.models;

import com.google.gson.annotations.SerializedName;

public class FieldsData {
    @SerializedName("token")
    String token_access;
    @SerializedName("ECODIGO")
    String token_hash;
    @SerializedName("action")
    String action;
    @SerializedName("FOTO64")
    String photoB64;
    @SerializedName("TCR_ID")
    String creditId;
    @SerializedName("ABI_ID")
    String tempBiometricsId;
    @SerializedName("PER_NOMBRE")
    String clientName;
    @SerializedName("PER_IDENTIF")
    String clientDni;
    @SerializedName("PER_ID")
    String clientId;
    @SerializedName("ETIPO")
    String type;
    @SerializedName("EIDENTIF_USUARIO")
    private String user;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public void setTempBiometricsId(String tempBiometricsId) {
        this.tempBiometricsId = tempBiometricsId;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public void setToken_access(String token_access) {
        this.token_access = token_access;
    }

    public void setToken_hash(String token_hash) {
        this.token_hash = token_hash;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPhotoB64(String photoB64) {
        this.photoB64 = photoB64;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
