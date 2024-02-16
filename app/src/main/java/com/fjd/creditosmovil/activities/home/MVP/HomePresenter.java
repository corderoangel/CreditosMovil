package com.fjd.creditosmovil.activities.home.MVP;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.fjd.creditosmovil.activities.home.HomeActivity;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class HomePresenter  implements HomeContract.Presenter{

    private HomeContract.View view;
    HomeInteractor homeInteractor;
    String TAG = "ErrorHomePresenter";


    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }


    @Override
    public void executeSend() {
        view.showMessages().showSuccess("Enviando información por favor espere...");
        homeInteractor = new HomeInteractor();
        homeInteractor.retriveResponseData(new HomeContract.CallbackParams() {
            @Override
            public void onResponse(ArrayList<ResponseData> response) {
                view.onResponse(response);
            }

            @Override
            public Context getContextClass() {
                return view.getContextClass();
            }

            @Override
            public ShowMessages showMessages() {
                return view.showMessages();
            }
        });

    }




}
