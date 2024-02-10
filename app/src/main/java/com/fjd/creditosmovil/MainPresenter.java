package com.fjd.creditosmovil;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase que actúa como presentador para manejar la lógica de negocio relacionada
 * con el inicio de sesión.
 */
public class MainPresenter {

    // Vista asociada al presentador
    private final MainContract.View view;

    /**
     * Constructor para inicializar el presentador con la vista proporcionada.
     *
     * @param view La vista que implementa la interfaz MainContract.View
     */
    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    /**
     * Método para realizar el inicio de sesión utilizando los datos proporcionados.
     *
     * @param servidor El servidor al que se realiza la solicitud de inicio de sesión.
     * @param dominio  El dominio asociado al usuario que inicia sesión.
     * @param ccAuth   El nombre de usuario o identificación para autenticar.
     * @param codAuth  El código de autenticación asociado al usuario.
     */
    public void login(String servidor, String dominio, String ccAuth, String codAuth) {
        // Obtener el servicio de la API correspondiente al servidor y dominio proporcionados
        try {
            ApiService apiService = ApiClient.getApiService(servidor, dominio);

            // Verificar si el servicio de la API se obtuvo correctamente
            if (apiService != null) {
                // Realizar una llamada asíncrona para iniciar sesión
                Call<LoginResponse> call = apiService.login(ccAuth, codAuth);
                // Manejar la respuesta exitosa
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            Log.e("login", String.valueOf(response.body()));
                            if (loginResponse != null) {
                                String token = loginResponse.getToken();
                                view.showLoginSuccess(token);
                            }
                        } else {
                            // Manejar la respuesta de error
                            Log.e("login", response.toString());
                            view.showLoginError();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Manejar el fallo en la solicitud
                        view.showNetworkError();
                    }
                });
            } else {
                // Manejar el caso en el que apiService sea null
                view.showApiServiceError();
            }
        } catch (IllegalArgumentException e) {
            // Manejar el error de IllegalArgumentException
            Log.e("MainPresenter", "Error: " + e.getMessage());
            view.showApiServiceError();
        } catch (Exception e) {
            // Manejar otros errores inesperados
            Log.e("MainPresenter", "Error inesperado: " + e.getMessage());
            view.showApiServiceError();
        }
    }
}
