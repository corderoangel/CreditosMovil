package com.fjd.creditosmovil;

import android.text.TextUtils;
import android.util.Log;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase para obtener una instancia del servicio de API utilizando Retrofit.
 * Esta clase maneja la configuración de la URL base del servicio de API.
 */
public class ApiClient {
    private static ApiService apiService;

    /**
     * Método para obtener una instancia del servicio de API con la URL base proporcionada.
     *
     * @param servidor El servidor donde se aloja el servicio de API.
     * @param dominio  El dominio asociado al servidor.
     * @return Una instancia del servicio de API con la URL base configurada.
     */
    public static ApiService getApiService(String servidor, String dominio){
        if (TextUtils.isEmpty(servidor) || TextUtils.isEmpty(dominio)) {
            // Mostrar un mensaje de error al usuario
            Log.e("ApiClient", "Servidor y dominio no pueden estar vacíos");
            // Puedes retornar null o una instancia de ApiService con un comportamiento predeterminado
            return null;
        }

        // Construir la URL base utilizando el servidor y el dominio proporcionados
        String baseUrl = "https://" + servidor + "/" + dominio + "/";

        // Verificar si ya se ha creado una instancia de ApiService
        if(apiService == null){
            // Si no existe una instancia, crear una nueva con la URL base proporcionada
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        } else {
            // Si ya existe una instancia, verificar si la URL base es diferente
            if (!apiService.baseUrl().equals(baseUrl)) {
                // Si la URL base es diferente, crear una nueva instancia con la URL actualizada
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiService = retrofit.create(ApiService.class);
            }
        }

        return apiService;
    }
}

