package com.fjd.creditosmovil.activities.process.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.MarkerPhoto;

import java.util.ArrayList;


public class ProcessPresenter implements ProcessContract.Presenter {

    private final ProcessContract.View view;
    ProcessInteractor processInteractor;
    DAO dao;


    /**
     * Constructor de la clase ProcessPresenter.
     * Este constructor inicializa un nuevo objeto ProcessPresenter con la vista proporcionada,
     * obteniendo el DAO de la base de datos y creando un nuevo ProcessInteractor.
     *
     * @param view La vista asociada al presentador.
     *             Proporciona la interfaz de usuario con la que interactúa el presentador.
     */

    public ProcessPresenter(ProcessContract.View view) {
        this.view = view;
        dao = ManagerDataBase.getInstance(view.getContextClass()).getDAO();
        processInteractor = new ProcessInteractor();
    }

    /**
     * Envía una biometría al servidor y maneja la respuesta.
     * Este método envía una biometría al servidor y procesa la respuesta recibida. Dependiendo del tipo de biometría,
     * puede ser necesario realizar alguna conversión antes de enviarla, como codificar una imagen en Base64.
     *
     * @param type          El tipo de biometría a enviar.
     * @param idBiometric   El ID único de la biometría a enviar.
     * @param responseData  Los datos de respuesta asociados con la operación de envío de la biometría.
     *                      Se utiliza para manejar la respuesta recibida del servidor.
     */

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
           // view.showMessages().hideLoader();

        } catch (Exception e) {
            e.printStackTrace();
            view.showMessages().showErrors(e.getMessage());
        }
    }

    /**
     * Recupera una entidad biométrica de la base de datos.
     * Este método recupera una entidad biométrica de la base de datos basándose en el tipo y el ID proporcionados.
     *
     * @param type El tipo de biométrica a recuperar.
     *             Ejemplos de tipos válidos incluyen "huella dactilar", "facial", "iris", etc.
     * @param id   El identificador único de la entidad biométrica a recuperar de la base de datos.
     *             Este ID se utiliza para localizar la entidad biométrica en la base de datos.
     * @return Un objeto FotosEntity que representa la entidad biométrica recuperada.
     *         Devuelve null si no se encuentra ninguna entidad biométrica que coincida con el tipo e ID proporcionados.
     */

    FotosEntity getBiometric(String type, String id) {
        return dao.getFotoFindById(id, type);
    }

}
