package com.fjd.creditosmovil.activities.login.MVP;

import android.util.Log;

import com.fjd.creditosmovil.activities.login.Models.FieldsLogin;
import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;
import com.fjd.creditosmovil.data.remote.EndPoints;
import com.fjd.creditosmovil.data.remote.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractor {

    String TAG = "Errors";
    public void retriveResponseLogin(MainContract.retrieveResponseCallback callback, FormLogin formLogin) {
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
                        assert response.body() != null;
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
