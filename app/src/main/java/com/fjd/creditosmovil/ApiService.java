package com.fjd.creditosmovil;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Interfaz que define los métodos para realizar solicitudes a la API.
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("token")
    Call<LoginResponse> login(
            //@Field("servidor") String servidor,
            //@Field("dominio") String dominio,
            @Field("codigo") String cc_auth,
            @Field("cedula") String cod_auth
    );

    /**
     * Método para obtener la URL base de la API.
     *
     * @return La URL base de la API.
     */
    String baseUrl();
}

