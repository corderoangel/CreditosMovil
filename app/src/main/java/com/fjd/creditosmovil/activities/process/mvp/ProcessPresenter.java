package com.fjd.creditosmovil.activities.process.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.process.models.DataProcess;
import com.fjd.creditosmovil.data.local.DAO;
import com.fjd.creditosmovil.data.local.ManagerDataBase;
import com.fjd.creditosmovil.data.local.entities.FotosEntity;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.MarkerPhoto;

import java.io.File;
import java.util.ArrayList;


public class ProcessPresenter implements ProcessContract.Presenter {

    private static final String TAG = "ProcessPresenter";
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
     * @param type         El tipo de biometría a enviar.
     * @param idBiometric  El ID único de la biometría a enviar.
     * @param responseData Los datos de respuesta asociados con la operación de envío de la biometría.
     *                     Se utiliza para manejar la respuesta recibida del servidor.
     */

    @Override
    public void sendBiometric(String type, String idBiometric, ResponseData responseData) {
        try {
            // Mostramos el loader
            view.showMessages().showLoader("Enviando...");
            //Obtenemos el registro delm dato a enviar
            FotosEntity fotosEntity = getBiometric(type, idBiometric);
            if (fotosEntity == null) {
                view.showMessages().showWarning("Evidencia no encontrada");
                return;
            }

            String biometric = fotosEntity.FOTO;
            if (!type.equalsIgnoreCase(DataProcess.SIGNATURE)) {
                Bitmap bitmap = BitmapFactory.decodeFile(biometric);
                MarkerPhoto markerPhoto = new MarkerPhoto(view.getContextClass(), null, null, null, null);
                biometric = Base64.encodeToString(markerPhoto.getBytes(bitmap), Base64.DEFAULT);
            }
            //Realizamos el proceso de envio y resolvemos la respuesta
            processInteractor.retrieveBiometricResponse(responseData, biometric, type, new ProcessContract.CallbackParams() {
                @Override
                public void onResponse(ArrayList<ResponseData> response) {
                    try {
                        //Validamos que la respuesta no este vacia
                        if (response.isEmpty()) {
                            view.showMessages().hideLoader();
                            dao.updatePhotoStateFailedById(String.valueOf(getBiometric(type, idBiometric).ID));
                            return;
                        }
                        // Validamos si la respuesta fue correcta
                        boolean status = response.get(0).getS_1().equalsIgnoreCase("1");
                        if (status) {
                            //Mostramos mensaje de exito y actualizamos en la base de datos y eliminamos el file de la foto
                            view.showMessages().showSuccess(response.get(0).getS_2());
                            dao.updatePhotoStateById(String.valueOf(getBiometric(type, idBiometric).ID));
                            deleteFilePhoto(fotosEntity.FOTO, fotosEntity.ID);

                        } else {
                            // Mostramos el mensaje de error
                            view.showMessages().showErrors(response.get(0).getS_2());
                            dao.updatePhotoStateFailedById(String.valueOf(getBiometric(type, idBiometric).ID));
                            return;
                        }
                        // Ejecutamos el proceso de muestra de la foto en la vista
                        view.onResponse(true, type);
                        view.showMessages().hideLoader();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
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
            Log.e(TAG, "sendBiometric: ", e);
            view.showMessages().showErrors(e.getMessage());
        }
    }

    /**
     * Finalizamos el proce de datos biometricos
     *
     * @param creditId ide del temporal del credito
     */
    @Override
    public void finalizeBiometrics(String creditId) {
        try {
            //Mostramos el loader
            view.showMessages().showLoader("Verificando...");
            //Ejecutamos la peticion
            processInteractor.finalizeBiometricResponse(creditId, new ProcessContract.CallbackParams() {
                @Override
                public void onResponse(ArrayList<ResponseData> response) {
                    try {
                        //Validamos que la respuesta no este vacia
                        if (response.isEmpty()) {
                            view.showMessages().hideLoader();
                            return;
                        }
                        // Validamos si la respuesta fue correcta
                        boolean status = response.get(0).getS_1().equalsIgnoreCase("1");
                        // Mostramos los mensajes de exito o error
                        if (status) {
                            view.showMessages().showSuccess(response.get(0).getS_2());
                            //Procedemos a realizar la operacion en la vista
                            view.onFinalizeBiometric(true);
                        } else {
                            view.showMessages().showErrors(response.get(0).getS_2());
                        }

                        view.showMessages().hideLoader();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
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
            Log.e(TAG, "finalizeBiometrics: ", e);
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
     * Devuelve null si no se encuentra ninguna entidad biométrica que coincida con el tipo e ID proporcionados.
     */
    FotosEntity getBiometric(String type, String id) {
        return dao.findPhotoById(id, type);
    }

    /**
     * Eliminamos el archivo de la foto que se guardo en local
     *
     * @param path ruta de la foto
     * @param id   id del registro en la base de datos
     */
    void deleteFilePhoto(String path, int id) {
        try {
            //Eliminamos el registro de la base de datos
            if (dao.deletePhotoFindId(id) > 0) {
                //Obtenemos la file de la foto
                File photo = new File(path);
                // Validamos si existe y si es una archivo
                if (photo.exists() && photo.isFile()) {
                    //eliminamos el archivo
                    boolean delete = photo.delete();
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "deleteFilePhoto: ", ex);
        }

    }

}
