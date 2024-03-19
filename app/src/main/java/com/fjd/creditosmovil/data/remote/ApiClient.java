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


    /**
     * Obtiene una instancia de la interfaz de punto final de la API para una URL base dada.
     * Este método crea y configura un cliente HTTP con un interceptor de registro de nivel de cuerpo.
     * Luego, construye una instancia de Retrofit con la URL base proporcionada y el cliente HTTP configurado.
     * Finalmente, crea y devuelve una instancia de la interfaz de punto final de la API utilizando Retrofit.
     *
     * @param baseUrl La URL base de la API a la que se realizarán las solicitudes.
     * @return Una instancia de la interfaz de punto final de la API para la URL base dada, o null si ocurrió un error.
     */
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