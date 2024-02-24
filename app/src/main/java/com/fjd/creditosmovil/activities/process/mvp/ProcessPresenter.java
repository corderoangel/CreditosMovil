package com.fjd.creditosmovil.activities.process.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.MarkerPhoto;

import java.util.ArrayList;


public class ProcessPresenter implements ProcessContract.Presenter{

    private ProcessContract.View view;
    ProcessInteractor processInteractor;
    String TAG = "ErrorHomePresenter";


    public ProcessPresenter(ProcessContract.View view) {
        this.view = view;
        processInteractor = new ProcessInteractor();
    }

    @Override
    public void sendBiometric(String type, String idBiometric){
       try {
           view.showMessages().showLoader("");
           String biometric = getBiometric(type, idBiometric);
           if (type.equalsIgnoreCase("foto")){
               Bitmap bitmap = BitmapFactory.decodeFile(biometric);
               MarkerPhoto markerPhoto = new MarkerPhoto(view.getContextClass(), null, null, null, null);
               biometric = Base64.encodeToString(markerPhoto.getBytes(bitmap), Base64.DEFAULT);
           }
           processInteractor.retriveBiometricResponse(biometric, type, new ProcessContract.CallbackParams() {
               @Override
               public void onResponse(ArrayList<ResponseData> response) {
                   view.onResponse(response.get(0).getS_1().equalsIgnoreCase("1"));
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
           view.showMessages().showErrors(e.getMessage());
       }
    }

    String getBiometric(String type, String id){
        DAO dao = ManagerDataBase.getInstance(view.getContextClass()).getDAO();
        if(type.equalsIgnoreCase("foto")){
            return dao.getFotoFindById(id, type).FOTO;
        }else{
            return dao.getFotoFindById(id, type).FIRMA;
        }
    }

}
