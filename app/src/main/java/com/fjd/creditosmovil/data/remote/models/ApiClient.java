package com.fjd.creditosmovil.data.remote.models;

import com.fjd.creditosmovil.data.remote.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para obtener una instancia del servicio de API utilizando Retrofit.
 * Esta clase maneja la configuración de la URL base del servicio de API.
 */

public class ApiClient {

    private static ApiService apiService;

    public static ApiService getApiService(String baseUrl){
        if(apiService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}