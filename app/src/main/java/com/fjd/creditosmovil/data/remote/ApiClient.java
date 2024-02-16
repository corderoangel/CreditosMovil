package com.fjd.creditosmovil.data.remote;

import com.fjd.creditosmovil.data.remote.EndPoints;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para obtener una instancia del servicio de API utilizando Retrofit.
 * Esta clase maneja la configuración de la URL base del servicio de API.
 */

public class ApiClient {

    private static EndPoints endpoints;

    public static EndPoints getApiService(String baseUrl){
        if(endpoints == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            endpoints = retrofit.create(EndPoints.class);
        }
        return endpoints;
    }
}