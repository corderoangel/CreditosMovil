package com.fjd.creditosmovil.activities.process;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.process.mvp.ProcessContract;
import com.fjd.creditosmovil.activities.siganture.SignatureActivity;
import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.databinding.ActivityProcessBinding;
import com.fjd.creditosmovil.util.singletons.CaptureBitmapView;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.InfoPhoto;
import com.fjd.creditosmovil.util.photos.SavePhoto;
import com.fjd.creditosmovil.util.singletons.Permissions;

import java.util.ArrayList;

public class ProcessActivity extends AppCompatActivity implements ProcessContract.View {
    SavePhoto savePhoto;
    InfoPhoto infoPhoto;
    ActivityProcessBinding binding;
    Intent intent;
    DAO dao;
    String ID_CREDIT, ID_BIOMETRIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        dao = ManagerDataBase.getInstance(this).getDAO();
        infoPhoto = new InfoPhoto(this);
        savePhoto = new SavePhoto(this, showMessages());
        setValues();
        binding.capturePhotoButton.setOnClickListener(v -> {
            Permissions.setPerms(this);
            savePhoto.CapturarFoto();
        });
        binding.captureSignatureButton.setOnClickListener(v -> {
            Intent intentSignature = new Intent(this, SignatureActivity.class);
            intentSignature.putExtra("credit", ID_CREDIT);
            intentSignature.putExtra("biometric", ID_BIOMETRIC);
            startActivity(intentSignature);
        });
    }

    void setValues() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("objetCredit")) {
            // Obtener el objeto MiClase enviado desde ActivityA
            ResponseData objetClient = (ResponseData) intent.getSerializableExtra("objetCredit");
            // Utilizar los métodos de la clase MiClase
            ID_CREDIT = String.valueOf(objetClient.getCreditId());
            ID_BIOMETRIC = String.valueOf(objetClient.getTempBiometricsId());
            infoPhoto.setDataPhoto(InfoPhoto.ID_FOTO, String.valueOf(objetClient.getCreditId()));
            infoPhoto.setDataPhoto(InfoPhoto.PREFIJO, String.valueOf(objetClient.getTempBiometricsId()));
        } else {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SavePhoto.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                savePhoto.GuardarFoto(() -> {
                    dao.getFotosFindId(infoPhoto.getDataPhoto(InfoPhoto.ID_FOTO), "%%")
                            .forEach(fotosEntity -> {
                                Bitmap bitmap = BitmapFactory.decodeFile(fotosEntity.FOTO);
                                if (bitmap == null) {
                                    showMessages().showWarning("Error bitmap null");
                                    return;
                                }
                                binding.capturePhotoButton.setImageBitmap(bitmap);
                            });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public ShowMessages showMessages() {
        return new ShowMessages() {
            @Override
            public void showLoader(String str) {

            }

            @Override
            public void hideLoader() {

            }

            @Override
            public void showErrors(String err) {

            }

            @Override
            public void showSuccess(String success) {

            }

            @Override
            public void showWarning(String warn) {

            }
        };
    }

    @Override
    public void onResponse(boolean response) {

    }

    @Override
    public Context getContextClass() {
        return this;
    }

}