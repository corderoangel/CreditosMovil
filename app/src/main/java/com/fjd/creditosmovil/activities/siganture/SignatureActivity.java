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

    /**
     * Llamado cuando la actividad se está iniciando.
     * Este método se llama cuando la actividad se está iniciando por primera vez.
     * Configura la interfaz de usuario inflando el diseño de la actividad y estableciéndolo como contenido de la ventana.
     * Crea una instancia de CaptureBitmapView para capturar la firma y la agrega al diseño de la actividad.
     * Configura el botón de limpiar firma para borrar la firma capturada y el botón de guardar firma para guardar la firma capturada.
     *
     * @param savedInstanceState El estado de la actividad guardado anteriormente, si está disponible.
     */
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

    /**
     * Guarda la firma capturada en la base de datos local.
     * Este método guarda la firma capturada en la base de datos local si se proporcionan los datos necesarios.
     * Se espera que los datos de crédito y biometría estén presentes en la intención que inició esta actividad.
     * La firma se guarda como una entidad de fotos en la base de datos local, con el tipo "FIRMA".
     * Si la operación de inserción es exitosa, se finaliza la actividad.
     */
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