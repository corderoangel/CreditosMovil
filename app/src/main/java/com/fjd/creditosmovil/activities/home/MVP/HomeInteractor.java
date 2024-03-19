package com.fjd.creditosmovil.activities.home.MVP;

import com.fjd.creditosmovil.activities.home.models.FieldsData;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.login.Models.FieldsLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;
import com.fjd.creditosmovil.data.remote.ApiClient;
import com.fjd.creditosmovil.data.remote.EndPoints;
import com.fjd.creditosmovil.util.contracts.SessionUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractor {
    /**
     * Recupera los datos de respuesta del servidor utilizando la API proporcionada.
     * Este método realiza una solicitud a la API especificada para recuperar los datos de respuesta del servidor.
     * Los datos de la solicitud incluyen un token de acceso y la acción requerida, que generalmente es "search".
     * Una vez que se recibe la respuesta del servidor, se procesa para verificar si es exitosa y se maneja en consecuencia.
     * Si la solicitud es exitosa y se obtienen datos de respuesta, se llama al método onResponse del CallbackParams.
     *
     * @param callbackParams Los parámetros de callback que contienen la lógica para manejar la respuesta del servidor.
     *                       Proporciona métodos para mostrar mensajes y devolver la respuesta procesada al llamador.
     */
    public void retriveResponseData(HomeContract.CallbackParams callbackParams) {
        try {
            EndPoints api = ApiClient.getApiService(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.URL_CONNECTION));
            FieldsData data = new FieldsData();
            data.setToken_access(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.TOKEN));
            data.setAction("search");
            api.service(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()) {
                        callbackParams.showMessages().showErrors(response.message());
                        return;
                    }
                    if (response.body().size() > 0) {
                        ResponseData responseData = response.body().get(0);
                        if (responseData.getS_1().equalsIgnoreCase("0")) {
                            callbackParams.showMessages().showErrors(responseData.getS_2());
                            return;
                        }
                    }

                    callbackParams.onResponse(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseData>> call, Throwable t) {
                    callbackParams.showMessages().showErrors(t.getMessage());
                }
            });
        } catch (Exception e) {
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recupera y valida un token utilizando la API proporcionada.
     * Este método realiza una solicitud a la API especificada para validar un token utilizando un token de acceso y un token hash.
     * La acción de la solicitud es "validar_codigo".
     * Una vez que se recibe la respuesta del servidor, se procesa para verificar si es exitosa y se maneja en consecuencia.
     * Si la solicitud es exitosa y se obtienen datos de respuesta, se llama al método onResponse del CallbackParams.
     *
     * @param hastToken      El token hash que se va a validar.
     * @param callbackParams Los parámetros de callback que contienen la lógica para manejar la respuesta del servidor.
     *                       Proporciona métodos para mostrar mensajes y devolver la respuesta procesada al llamador.
     */
    public void retriveValidateToken(String hastToken,HomeContract.CallbackParams callbackParams) {
        try {
            EndPoints api = ApiClient.getApiService(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.URL_CONNECTION));
            FieldsData data = new FieldsData();
            data.setToken_access(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.TOKEN));
            data.setToken_hash(hastToken);
            data.setAction("validar_codigo");
            api.service(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()) {
                        callbackParams.showMessages().showErrors(response.message());
                        return;
                    }
                    if (response.body().size() > 0) {
                        ResponseData responseData = response.body().get(0);
                        if (responseData.getS_1().equalsIgnoreCase("0")) {
                            callbackParams.showMessages().showErrors(responseData.getS_2());
                            return;
                        }
                    }

                    callbackParams.onResponse(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseData>> call, Throwable t) {
                    callbackParams.showMessages().showErrors(t.getMessage());
                }
            });
        } catch (Exception e) {
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Realiza un cierre de sesión en el servidor utilizando la API proporcionada.
     *
     * Este método realiza una solicitud al servidor para cerrar la sesión del usuario actual.
     * La solicitud incluye el nombre de usuario y el token de acceso del usuario actual.
     * Una vez que se recibe la respuesta del servidor, se procesa para verificar si es exitosa y se maneja en consecuencia.
     * Si la solicitud es exitosa y se obtienen datos de respuesta, se llama al método onResponse del CallbackParams.
     *
     * @param callbackParams Los parámetros de callback que contienen la lógica para manejar la respuesta del servidor.
     *                       Proporciona métodos para mostrar mensajes y devolver la respuesta procesada al llamador.
     */
    public void retriveLogout(HomeContract.CallbackParams callbackParams) {
        try {
            EndPoints api = ApiClient.getApiService(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.URL_CONNECTION));
            FieldsData data = new FieldsData();
            data.setUser(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.USER));
            data.setToken_access(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.TOKEN));
            api.logout(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()) {
                        callbackParams.showMessages().showErrors(response.message());
                        return;
                    }
                    if (response.body().size() > 0) {
                        ResponseData responseData = response.body().get(0);
                        if (responseData.getS_1().equalsIgnoreCase("0")) {
                            callbackParams.showMessages().showErrors(responseData.getS_2());
                            return;
                        }
                    }

                    callbackParams.onResponse(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseData>> call, Throwable t) {
                    callbackParams.showMessages().showErrors(t.getMessage());
                }
            });
        } catch (Exception e) {
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }
}
