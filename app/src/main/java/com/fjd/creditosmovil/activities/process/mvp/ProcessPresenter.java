package com.fjd.creditosmovil.activities.process.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.MarkerPhoto;

import java.util.ArrayList;


public class ProcessPresenter implements ProcessContract.Presenter {

    private ProcessContract.View view;
    ProcessInteractor processInteractor;
    String TAG = "ErrorHomePresenter";
    DAO dao;


    public ProcessPresenter(ProcessContract.View view) {
        this.view = view;
        dao = ManagerDataBase.getInstance(view.getContextClass()).getDAO();
        processInteractor = new ProcessInteractor();
    }

    @Override
    public void sendBiometric(String type, String idBiometric, ResponseData responseData) {
        try {
            view.showMessages().showLoader("Enviando "+type);
            String biometric = getBiometric(type, idBiometric).FOTO;
            if (type.equalsIgnoreCase("foto")) {
                Bitmap bitmap = BitmapFactory.decodeFile(biometric);
                MarkerPhoto markerPhoto = new MarkerPhoto(view.getContextClass(), null, null, null, null);
                biometric = Base64.encodeToString(markerPhoto.getBytes(bitmap), Base64.DEFAULT);
            }
            processInteractor.retriveBiometricResponse(responseData, biometric, type.toLowerCase(), new ProcessContract.CallbackParams() {
                @Override
                public void onResponse(ArrayList<ResponseData> response) {
                   try {
                       if (response.size()==0){
                           view.showMessages().hideLoader();
                           dao.updatePhotosStateFailed(String.valueOf(getBiometric(type, idBiometric).ID));
                           return;
                       }
                       boolean status =response.get(0).getS_1().equalsIgnoreCase("1");
                       if (status){
                           view.showMessages().showSuccess(response.get(0).getS_2());
                           dao.updatePhotosState(String.valueOf(getBiometric(type, idBiometric).ID));
                       }else {
                           view.showMessages().showErrors(response.get(0).getS_2());
                           dao.updatePhotosStateFailed(String.valueOf(getBiometric(type, idBiometric).ID));
                           return;
                       }
                       view.onResponse(status);
                       view.showMessages().hideLoader();
                   }catch (Exception e){
                       e.printStackTrace();
                   }

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

        } catch (Exception e) {
            e.printStackTrace();
            view.showMessages().showErrors(e.getMessage());
        }
    }

    FotosEntity getBiometric(String type, String id) {
        return dao.getFotoFindById(id, type);
    }


}
