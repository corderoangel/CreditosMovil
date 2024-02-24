package com.fjd.creditosmovil.activities.siganture;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.databinding.ActivitySignatureBinding;
import com.fjd.creditosmovil.util.photos.MarkerPhoto;
import com.fjd.creditosmovil.util.singletons.CaptureBitmapView;

import java.io.File;
import java.util.Calendar;

public class SignatureActivity extends AppCompatActivity {

    ActivitySignatureBinding binding;
    CaptureBitmapView captureBitmapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignatureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        captureBitmapView = new CaptureBitmapView(this, null);
        binding.contentSignatureView.addView(captureBitmapView);
        binding.clearSignatureButton.setOnClickListener(v -> captureBitmapView.ClearCanvas());
        binding.saveSignatureButton.setOnClickListener(v -> saveSignarute());
    }

    private void saveSignarute() {
        try {

            Intent intent = getIntent();
            if (intent == null) {
                return;
            }
            if (intent.getStringExtra("credit") == null || intent.getStringExtra("biometric") == null) {
                return;
            }
            DAO dao = ManagerDataBase.getInstance(this).getDAO();
            FotosEntity fotosEntity = new FotosEntity();
            fotosEntity.ID_FOTO = intent.getStringExtra("credit");
            fotosEntity.N_FOTOS = String.valueOf(1);
            fotosEntity.ANO = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            fotosEntity.MES = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            fotosEntity.FOTO = Base64.encodeToString(captureBitmapView.getBytes(), Base64.DEFAULT);
            fotosEntity.ESTADO = "N";
            fotosEntity.TYPE = "FIRMA";
            /////////////////////////////////////////////////////////
            long res1 = dao.insert(fotosEntity);
            if (res1 > 0) {
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}