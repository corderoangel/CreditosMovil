package com.fjd.creditosmovil.util.photos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.util.contracts.CallbackPhoto;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SavePhoto {

    private Context context;
    private Uri photoURI;
    private DAO dao;
    private ShowMessages messages;
    InfoPhoto infoPhoto;
    String ID_FOTO, UBICATION, INFRA, BARRIO, PREFIJO;
    int N_MAX_PHOTO;
    public static final int REQUEST_CODE = 111;
    private String TAG = "ErrorSaveFotoClass";

    /**
     * Constructor de la clase
     *
     * @param context  Contexto donde se implementará las diferentes operaciones
     * @param messages CallBack donde se mostrarán las notificaciones
     */
    public SavePhoto(Context context, ShowMessages messages) {
        this.context = context;
        this.messages = messages;
        infoPhoto = new InfoPhoto(context);
        dao = ManagerDataBase.getInstance(context).getDAO();
    }

    /**
     * Guardamos la imagen en la carpeta asignada y la base de datos
     *
     * @param callbackPhoto callback donde agregamos toda la funcionalidad que se realizará una vez que se haya guardado la foto en la base de datos
     */
    @SuppressLint("SimpleDateFormat")
    public void GuardarFoto(CallbackPhoto callbackPhoto) {
        // AGREGAMOS LAS FIRMAS
        try {
            //////////////////////////////////////////////////
            MarkerPhoto markerPhoto = new MarkerPhoto(context, photoURI, UBICATION, INFRA, BARRIO);
            FotosEntity fotosEntity = new FotosEntity();
            /////////////////////////////////////////////////////////
            fotosEntity.ID_FOTO = ID_FOTO;
            fotosEntity.N_FOTOS = String.valueOf(N_MAX_PHOTO);
            fotosEntity.ANO = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            fotosEntity.MES = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            fotosEntity.FOTO = infoPhoto.SavePhotoDirFiles(markerPhoto.WaterMaker());
            fotosEntity.ESTADO = "N";
            fotosEntity.TYPE = "FOTO";
            /////////////////////////////////////////////////////////
            long res1 = dao.insert(fotosEntity);
            if (res1 > 0) {
                File file = obtenerFoto(ID_FOTO + "_" + N_MAX_PHOTO);
                file.delete();
                messages.showSuccess("Foto Guardada correctamente");
                callbackPhoto.OnPostTakePhoto();
            }
        } catch (Exception e) {
            e.printStackTrace();
            messages.showErrors(e.getMessage());
        }
    }

    /**
     * Buscamos la imagen en la galeria del dispositivo y la retornamos
     *
     * @param nombreFoto nombre de la imagen que se quiere buscar
     * @return { File } retornamos el archivo File
     */
    private File obtenerFoto(String nombreFoto) {
        File carpetaPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File foto = new File(carpetaPictures, nombreFoto + ".jpg");
        if (foto.exists()) {
            return foto;
        } else {
            // La foto no existe o no se encontró en la carpeta Pictures
            return null;
        }
    }

    /**
     * Creams una Imagen temporal y la guardamos en la galeria del dispositivo
     *
     * @return retornamos la imagen temporal que se creó
     * @throws IOException implementamos la excepcion de error en cas de no obtener un error
     */
    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = ID_FOTO + new SimpleDateFormat("ddMMyy").format(new Date());
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        return image;
    }

    void getTexts() {
        PREFIJO = infoPhoto.getDataPhoto(InfoPhoto.PREFIJO);
        UBICATION = infoPhoto.getDataPhoto(InfoPhoto.UBICATION);
        INFRA = infoPhoto.getDataPhoto(InfoPhoto.INFRA);
        BARRIO = infoPhoto.getDataPhoto(InfoPhoto.BARRIO);
        ID_FOTO = infoPhoto.getDataPhoto(InfoPhoto.ID_FOTO);
        N_MAX_PHOTO = infoPhoto.getMaxPhoto();
    }

    /**
     * Abrims la camara y capturamos la foto
     */
    public void CapturarFoto() {
        try {
            getTexts();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, ID_FOTO + "_" + N_MAX_PHOTO);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // Reemplaza "image/jpeg" con el tipo MIME deseado
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messages.showErrors(e.getMessage());
        }
    }


}
