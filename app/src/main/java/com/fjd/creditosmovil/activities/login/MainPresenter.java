package com.fjd.creditosmovil.activities.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fjd.creditosmovil.activities.home.HomeActivity;
import com.fjd.creditosmovil.data.remote.ApiService;
import com.fjd.creditosmovil.data.remote.models.ApiClient;
import com.fjd.creditosmovil.data.remote.models.ErrorResponse;
import com.fjd.creditosmovil.data.remote.models.LoginRequest;

import java.util.List;

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

            //construir url
            String baseUrl = "https://"+servidor+"/"+dominio+"/";

            //traer servicio de la url
            ApiService apiService = ApiClient.getApiService(baseUrl);

            // Verificar si el servicio de la API se obtuvo correctamente
            if (apiService != null) {
                LoginRequest loginRequest = new LoginRequest(ccAuth, codAuth);
                // Realizar una llamada asíncrona para iniciar sesión
                Call<List<ErrorResponse>> call = apiService.login(loginRequest);
                // Manejar la respuesta exitosa
                call.enqueue(new Callback<List<ErrorResponse>>() {
                    @Override
                    public void onResponse(Call<List<ErrorResponse>> call, Response<List<ErrorResponse>> response) {
                        if (response.isSuccessful()) {
                            List<ErrorResponse> errorResponses = response.body();
                            if (errorResponses != null && !errorResponses.isEmpty()) {
                                int s1 = 0;
                                String s2 = null;
                                // Procesar cada objeto de la lista de errorResponses
                                for (ErrorResponse errorResponse : errorResponses) {
                                    s1 = errorResponse.getS_1();
                                    s2 = errorResponse.getS_2();
                                    String jwt = errorResponse.getJWT();
                                    // Aquí puedes hacer lo que necesites con los valores s1 y s2
                                    Log.e("login", "S_1: " + s1 + ", S_2: " + s2 + ", JWT: " + jwt);
                                }
                                // En este punto, ya has procesado todos los objetos de la lista
                                // Puedes mostrar un mensaje de error genérico o realizar alguna otra acción si es necesario
                                // Dentro del método onResponse si la respuesta es exitosa

                                if(s1 == 1){
                                    Context context = view.getContext();
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    context.startActivity(intent);
                                    view.showLoginOk(s2);
                                }else{
                                    view.showLoginError(s2);
                                }

                            }
                        } else {
                            // Manejar la respuesta de error
                            Log.e("login", response.toString());
                            view.showLoginError("error");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ErrorResponse>> call, Throwable t) {
                        // Manejar el fallo en la solicitud
                        view.showNetworkError(t.getMessage());
                    }
                });
            } else {
                // Manejar el caso en el que apiService sea null
                view.showApiServiceError("error en la vain a de la api");
            }
        } catch (IllegalArgumentException e) {
            // Manejar el error de IllegalArgumentException
            Log.e("MainPresenter", "Error: " + e.getMessage());
            view.showApiServiceError(e.getMessage());
        } catch (Exception e) {
            // Manejar otros errores inesperados
            Log.e("MainPresenter", "Error inesperado: " + e.getMessage());
            view.showApiServiceError(e.getMessage());
        }
    }

}
