package com.fjd.creditosmovil.util.photos;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.fjd.creditosmovil.data.local.ManagerDataBase;
import com.fjd.creditosmovil.data.local.entities.FotosEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class InfoPhoto {
    public static final String ID_FOTO = "idfoto", INFRA = "infra", BARRIO = "barrio", UBICATION = "ubication",
            PREFIJO="prefijo" , TYPE ="TYPE", AP="APOYO", TR="TRAFO",  MAN="MANTENIMEINTO";
    private Context context;

    public InfoPhoto(@NonNull Context context) {
        this.context = context;
    }

    public String getDataPhoto(@NonNull String key){
        SharedPreferences preferences =context.getSharedPreferences("PHOTOS", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }
    public  void setDataPhoto(@NonNull String key, String tag){
        SharedPreferences preferences = context.getSharedPreferences("PHOTOS", MODE_PRIVATE);
        SharedPreferences.Editor edit =  preferences.edit();
        edit.putString(key, tag);
        edit.apply();
        edit.commit();
    }
    public int getMaxPhoto(){
        ArrayList<FotosEntity> countFotos = new ArrayList<>(ManagerDataBase.getInstance(context).getDAO().getPhotosById(getDataPhoto(ID_FOTO), "%%"));
        return  countFotos.size()+1;
    }
    public String SavePhotoDirFiles(@NonNull byte[] foto){
        String path =context.getDataDir()+"/databases/Photos";
        File folder =  new File(path);
        if (!folder.exists()){
            folder.mkdir();
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(foto,0,foto.length);
        String filename = getDataPhoto(ID_FOTO)+"_"+ getMaxPhoto()+".jpg";
        FileOutputStream out = null;
        File file = new File(path, filename);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();

    }

}
