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
        homeInteractor = new HomeInteractor();
    }


    @Override
    public void getDataList() {
      try {
          view.showMessages().showLoader("");
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
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    @Override
    public void validateToken(String tokenHash, ResponseData responseData){
       try {
           view.showMessages().showLoader("");
           homeInteractor.retriveValidateToken(tokenHash, new HomeContract.CallbackParams() {
               @Override
               public void onResponse(ArrayList<ResponseData> response) {
                   view.validateToken(response.get(0).getS_1().equalsIgnoreCase("1"), responseData);
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
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    @Override
    public void logout() {
        try {
            view.showMessages().showLoader("");
            homeInteractor.retriveLogout(new HomeContract.CallbackParams() {
                @Override
                public void onResponse(ArrayList<ResponseData> response) {
                    view.logout(response.get(0).getS_1().equalsIgnoreCase("1"));
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
