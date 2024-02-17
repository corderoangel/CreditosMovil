package com.fjd.creditosmovil.activities.home.models;

import com.google.gson.annotations.SerializedName;

public class FieldsData {
    @SerializedName("token")
    String token_access;
    @SerializedName("hash_token")
    String token_hash;
    @SerializedName("action")
    String action;
    @SerializedName("foto_b64")
    String photoB64;
    @SerializedName("firma_b64")
    String firmaB64;

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

    public void setFirmaB64(String firmaB64) {
        this.firmaB64 = firmaB64;
    }
}
