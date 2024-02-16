package com.fjd.creditosmovil.activities.home.MVP;

import com.fjd.creditosmovil.activities.home.models.FieldsData;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.login.Models.FieldsLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;
import com.fjd.creditosmovil.data.remote.ApiClient;
import com.fjd.creditosmovil.data.remote.EndPoints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractor {

    public void retriveResponseData(HomeContract.CallbackParams callbackParams){
        try {
            EndPoints api = ApiClient.getApiService("URL_CONN");
            FieldsData data = new FieldsData();
            data.setToken_access("");
            api.service(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()){
                        callbackParams.showMessages().showErrors(response.message());
                        return;
                    }
                    callbackParams.onResponse(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<ResponseData>> call, Throwable t) {
                    callbackParams.showMessages().showErrors(t.getMessage());
                }
            });
        }catch (Exception e){
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }
}
