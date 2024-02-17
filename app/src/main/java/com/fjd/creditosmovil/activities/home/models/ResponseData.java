package com.fjd.creditosmovil.activities.home.models;

import com.google.gson.annotations.SerializedName;


public class ResponseData {
    String S_1;
    String S_2;
    String S_3;
    @SerializedName("TCR_ID")
    int creditId;
    @SerializedName("ABI_ID")
    int tempBiometricsId;
    @SerializedName("PER_NOMBRE")
    String clientName;
    @SerializedName("fecha")
    String expirationDate;
    @SerializedName("PER_IDENTIF")
    int clientDni;
    @SerializedName("PER_ID")
    int clientId;

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public int getTempBiometricsId() {
        return tempBiometricsId;
    }

    public void setTempBiometricsId(int tempBiometricsId) {
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

    public int getClientDni() {
        return clientDni;
    }

    public void setClientDni(int clientDni) {
        this.clientDni = clientDni;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
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
