package com.fjd.creditosmovil.activities.login.MVP;

import com.fjd.creditosmovil.activities.login.Models.FieldsLogin;
import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;
import com.fjd.creditosmovil.data.remote.EndPoints;
import com.fjd.creditosmovil.data.remote.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractor {

    /**
     * Recupera la respuesta de inicio de sesión del servidor utilizando la API proporcionada.
     * Este método realiza una solicitud de inicio de sesión al servidor utilizando los datos proporcionados en el formulario de inicio de sesión.
     * Una vez que se recibe la respuesta del servidor, se procesa para determinar si el inicio de sesión fue exitoso o no.
     * Si el inicio de sesión fue exitoso, se llama al método onResponse del callback con los datos de inicio de sesión recibidos.
     * Si hubo un error durante la solicitud, se llama al método onError del callback con el mensaje de error correspondiente.
     *
     * @param callback   El objeto de callback que maneja la respuesta del servidor y los posibles errores.
     * @param formLogin  Los datos del formulario de inicio de sesión, incluyendo el nombre de usuario, la contraseña y la URL de conexión.
     */
    public void retrieveResponseLogin(MainContract.retrieveResponseCallback callback, FormLogin formLogin) {
        try {
            EndPoints api;
            FieldsLogin login = new FieldsLogin();
            login.setUser(formLogin.USER);
            login.setPassword(formLogin.PASS);
            api = ApiClient.getApiService(formLogin.URL_CONNECTION);
            api.login(login).enqueue(new Callback<ArrayList<ResponseLogin>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseLogin>> call, Response<ArrayList<ResponseLogin>> response) {
                    try {

                        ResponseLogin result = response.body().get(0);
                        if (response.isSuccessful()) {
                            callback.onResponse(result);
                        } else {
                            String error = response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                            callback.setOnError(error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.setOnError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseLogin>> call, Throwable t) {
                    callback.setOnError(t.getMessage());
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callback.setOnError(e.getMessage());
        }
    }
    public void retrieveResponseRefreshToken(MainContract.retrieveResponseCallback callback, FormLogin formLogin,
                                             String tokenRefresh) {
        try {
            EndPoints api;
            FieldsLogin login = new FieldsLogin();
            login.setUser(formLogin.USER);
            login.setPassword(formLogin.PASS);
            login.setTokenRefresh(tokenRefresh);
            login.setAction("session_reset");
            api = ApiClient.getApiService(formLogin.URL_CONNECTION);
            api.session_reset(login).enqueue(new Callback<ArrayList<ResponseLogin>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseLogin>> call, Response<ArrayList<ResponseLogin>> response) {
                    try {
                        if (response.body() == null){
                            return;
                        }
                        boolean state = response.body().get(0).getS_1() == 1;
                        ResponseLogin result = response.body().get(0);
                        if (response.isSuccessful()) {
                            if (!state) {
                                callback.setOnError(result.getS_2());
                                return;
                            }
                            callback.onResponse(result);
                        } else {
                            String error = response.errorBody().string().replace("{\"jwt\":\"\",\"msg\":\"", "").replace("\"}", "");
                            callback.setOnError(error);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.setOnError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseLogin>> call, Throwable t) {
                    callback.setOnError(t.getMessage());
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            callback.setOnError(e.getMessage());
        }
    }
}
