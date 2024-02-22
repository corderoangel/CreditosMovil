package com.fjd.creditosmovil.activities.home.MVP;

import android.content.Context;


import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;


public class HomePresenter  implements HomeContract.Presenter{

    private HomeContract.View view;
    HomeInteractor homeInteractor;
    String TAG = "ErrorHomePresenter";


    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }


    @Override
    public void getDataList() {
        view.showMessages().showLoader("");
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
        view.showMessages().hideLoader();
    }




}
