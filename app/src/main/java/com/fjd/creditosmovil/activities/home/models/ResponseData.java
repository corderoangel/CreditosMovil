package com.fjd.creditosmovil.activities.home.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ResponseData  implements Serializable {
    String S_1;
    String S_2;
    String S_3;
    @SerializedName("TCR_ID")
    String creditId;
    @SerializedName("ABI_ID")
    String tempBiometricsId;
    @SerializedName("PER_NOMBRE")
    String clientName;
    @SerializedName("fecha")
    String expirationDate;
    @SerializedName("PER_IDENTIF")
    String clientDni;
    @SerializedName("PER_ID")
    String clientId;

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getTempBiometricsId() {
        return tempBiometricsId;
    }

    public void setTempBiometricsId(String tempBiometricsId) {
        this.tempBiometricsId = tempBiometricsId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getClientDni() {
        return clientDni;
    }

    public void setClientDni(String clientDni) {
        this.clientDni = clientDni;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getS_1() {
        return S_1;
    }

    public String getS_2() {
        return S_2;
    }

    public String getS_3() {
        return S_3;
    }


}
