package com.fjd.creditosmovil.data.remote;

import com.fjd.creditosmovil.data.remote.models.ErrorResponse;
import com.fjd.creditosmovil.data.remote.models.LoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaz que define los métodos para realizar solicitudes a la API.
 */
public interface ApiService {
    @POST("/creditos_fjd/api/login")
    Call<List<ErrorResponse>> login(@Body LoginRequest loginRequest);

}

