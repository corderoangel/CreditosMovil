package com.fjd.creditosmovil.data.remote;

import com.fjd.creditosmovil.activities.home.models.FieldsData;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.login.Models.FieldsLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaz que define los métodos para realizar solicitudes a la API.
 */
public interface EndPoints {
    @POST("login")
    Call<ArrayList<ResponseLogin>> login(@Body FieldsLogin fieldsLogin);

    @POST("logout")
    Call<ArrayList<ResponseData>> logout(@Body FieldsData fieldsLogout);

    @POST("service")
    Call<ArrayList<ResponseData>> service(@Body FieldsData fieldsData);

    @POST("foto")
    Call<ArrayList<ResponseData>> biometrics(@Body FieldsData fieldsData);

}

