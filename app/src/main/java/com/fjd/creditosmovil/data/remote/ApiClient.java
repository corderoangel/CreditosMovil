package com.fjd.creditosmovil.data.remote;

import com.fjd.creditosmovil.data.remote.EndPoints;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para obtener una instancia del servicio de API utilizando Retrofit.
 * Esta clase maneja la configuración de la URL base del servicio de API.
 */

public class ApiClient {

    private static EndPoints endpoints;

    public static EndPoints getApiService(String baseUrl){
        Retrofit retrofit = null;
        final HttpLoggingInterceptor logging
                = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit != null ? retrofit.create(EndPoints.class) : null;
    }
}