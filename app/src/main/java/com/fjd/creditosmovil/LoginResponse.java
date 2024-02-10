package com.fjd.creditosmovil;

import com.google.gson.annotations.SerializedName;

/**
 * Clase que representa la respuesta de inicio de sesión recibida
 * desde el servidor.
 */
public class LoginResponse {
    @SerializedName("token")
    private String token;

    /**
     * Método para obtener el token de sesión.
     *
     * @return El token de sesión asociado a la respuesta de inicio de sesión.
     */
    public String getToken() {
        return token;
    }
}
