package com.fjd.creditosmovil.activities.process.mvp;

import com.fjd.creditosmovil.activities.home.models.FieldsData;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.data.remote.ApiClient;
import com.fjd.creditosmovil.data.remote.EndPoints;
import com.fjd.creditosmovil.util.contracts.SessionUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessInteractor {

    public void retrieveBiometricResponse(ResponseData responseData, String biometric, String action,
            ProcessContract.CallbackParams callbackParams) {
        try {
            EndPoints api =
                    ApiClient.getApiService(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.URL_CONNECTION));
            FieldsData data = new FieldsData();
            data.setToken_access(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.TOKEN));
            data.setPhotoB64(biometric);
            data.setAction(action);
            data.setType(action);
            data.setUser(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.USER));
            data.setClientDni(responseData.getClientDni());
            data.setClientId(responseData.getClientId());
            data.setCreditId(responseData.getCreditId());
            data.setTempBiometricsId(responseData.getTempBiometricsId());
            data.setClientName(responseData.getClientName());
            api.biometrics(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()) {
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
        } catch (Exception e) {
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }

    public void finalizeBiometricResponse(String creditId, ProcessContract.CallbackParams callbackParams) {
        try {
            EndPoints api =
                    ApiClient.getApiService(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.URL_CONNECTION));
            FieldsData data = new FieldsData();
            data.setToken_access(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.TOKEN));
            data.setAction("finalize_biometrics");
            data.setUser(SessionUser.getAccess(callbackParams.getContextClass(), SessionUser.USER));
            data.setCreditId(creditId);

            api.service(data).enqueue(new Callback<ArrayList<ResponseData>>() {
                @Override
                public void onResponse(Call<ArrayList<ResponseData>> call, Response<ArrayList<ResponseData>> response) {
                    if (!response.isSuccessful()) {
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
        } catch (Exception e) {
            callbackParams.showMessages().showErrors(e.getMessage());
            e.printStackTrace();
        }
    }

}
