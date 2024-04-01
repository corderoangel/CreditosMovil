package com.fjd.creditosmovil.activities.process;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.process.mvp.ProcessContract;
import com.fjd.creditosmovil.activities.process.mvp.ProcessPresenter;
import com.fjd.creditosmovil.activities.siganture.SignatureActivity;
import com.fjd.creditosmovil.database.DAO;
import com.fjd.creditosmovil.database.ManagerDataBase;
import com.fjd.creditosmovil.database.entities.FotosEntity;
import com.fjd.creditosmovil.databinding.ActivityProcessBinding;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.InfoPhoto;
import com.fjd.creditosmovil.util.photos.SavePhoto;
import com.fjd.creditosmovil.util.singletons.Permissions;
import com.fjd.creditosmovil.util.singletons.SnackbarUtil;

public class ProcessActivity extends AppCompatActivity implements ProcessContract.View {
    SavePhoto savePhoto;
    InfoPhoto infoPhoto;
    ActivityProcessBinding binding;
    Intent intent;
    DAO dao;
    String ID_CREDIT, ID_BIOMETRIC;
    ResponseData CLIENT_DATA;

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
        resetDataClient();
        binding.capturePhotoButton.setOnClickListener(v -> {
            Log.e("TAG", "onCreate: " + dao.getFotoFindId(ID_CREDIT, "FOTO", "N"));
            if (dao.getFotoFindId(ID_CREDIT, "FOTO", "N") != null) {
                showMessages().showWarning("Ya tienes una foto en proceso");
                return;
            }
            Permissions.setPerms(this);
            savePhoto.CapturarFoto();
        });
        binding.captureSignatureButton.setOnClickListener(v -> {
            if (dao.getFotoFindId(ID_CREDIT, "FIRMA", "S") != null) {
                showMessages().showWarning("Ya tienes una firma en proceso");
                return;
            }
            Intent intentSignature = new Intent(this, SignatureActivity.class);
            intentSignature.putExtra("credit", ID_CREDIT);
            intentSignature.putExtra("biometric", ID_BIOMETRIC);
            startActivity(intentSignature);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_process, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            verifySuccessBiometrics();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Establece los valores necesarios para la actividad actual.
     * Este método recupera los datos enviados desde la actividad anterior a través de un Intent.
     * Si se encuentran los datos esperados, se utilizan para inicializar variables relevantes y configurar
     * la información de la foto en el objeto InfoPhoto.
     * Si los datos esperados no están presentes en el Intent, la actividad se finaliza.
     */

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
            CLIENT_DATA = objetClient;
        } else {
            finish();
        }
    }

    /**
     * Restablece los datos del cliente eliminando la información de la foto asociada al crédito actual.
     * Este método elimina la información de la foto correspondiente al ID de crédito actual
     * de la base de datos, dejando los datos del cliente en un estado inicial o vacío.
     */
    void resetDataClient() {
        dao.deleteFotoFindID(ID_CREDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SavePhoto.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                savePhoto.GuardarFoto(() -> {
                    if (dao.getFotoFindId(infoPhoto.getDataPhoto(InfoPhoto.ID_FOTO), "FOTO", "N") != null) {
                        new ProcessPresenter(this).sendBiometric("foto", ID_CREDIT, CLIENT_DATA);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica la existencia de una firma asociada al crédito actual y la envía para su procesamiento.
     * Este método verifica si hay una firma asociada al ID de crédito actual que aún no se haya procesado.
     * Si se encuentra una firma no procesada en la base de datos, se envía al servidor para su procesamiento.
     */
    void verifySignature() {
        FotosEntity entity = dao.getFotoFindId(ID_CREDIT, "FIRMA", "N");
        if (entity != null) {
            new ProcessPresenter(this).sendBiometric("FIRMA", ID_CREDIT, CLIENT_DATA);
        }
    }
    /**
     * Verifica si se han agregado correctamente todas las biometrías asociadas al crédito actual.
     * Este método comprueba si se han agregado con éxito todas las biometrías requeridas asociadas al ID de crédito actual.
     * Si todas las biometrías requeridas han sido agregadas con éxito, la actividad se finaliza.
     * Si faltan biometrías por agregar, se muestra un mensaje de advertencia indicando que se deben agregar todos los datos.
     */
    void verifySuccessBiometrics() {
        if (dao.getSuccessPhoto(ID_CREDIT) > 0) {
            finish();
        }else {
            showMessages().showWarning("Debes Agregar todos los datos");
        }
    }

    @Override
    protected void onResume() {
        verifySignature();
        super.onResume();
    }

    /**
     * Devuelve una implementación de ShowMessages para mostrar mensajes en la interfaz de usuario.
     * Este método proporciona una implementación de la interfaz ShowMessages que permite mostrar mensajes
     * de carga, éxito, advertencia y error en la interfaz de usuario de la actividad actual.
     * La implementación utiliza SnackbarUtil para mostrar los mensajes y gestionar la visibilidad de los elementos de la interfaz.
     */
    @Override
    public ShowMessages showMessages() {
        return new ShowMessages() {
            public void showLoader(String str) {
                SnackbarUtil.success(binding.getRoot(), str, ProcessActivity.this);
                binding.progressBar.getRoot().setVisibility(View.VISIBLE);
                binding.capturePhotoButton.setEnabled(false);
                binding.captureSignatureButton.setEnabled(false);
            }

            @Override
            public void hideLoader() {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                binding.capturePhotoButton.setEnabled(true);
                binding.captureSignatureButton.setEnabled(true);
            }


            @Override
            public void showErrors(String err) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.danger(binding.getRoot(), err, ProcessActivity.this);
                binding.capturePhotoButton.setEnabled(true);
                binding.captureSignatureButton.setEnabled(true);
            }

            @Override
            public void showSuccess(String success) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.success(binding.getRoot(), success, ProcessActivity.this);
                binding.capturePhotoButton.setEnabled(true);
                binding.captureSignatureButton.setEnabled(true);
            }

            @Override
            public void showWarning(String warn) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.warring(binding.getRoot(), warn, ProcessActivity.this);
                binding.capturePhotoButton.setEnabled(true);
                binding.captureSignatureButton.setEnabled(true);
            }
        };
    }

    /**
     * Maneja la respuesta recibida del servidor después de procesar una solicitud de biometría.
     * Este método se llama cuando se recibe una respuesta del servidor después de procesar una solicitud de biometría.
     * Si la respuesta es positiva (true), se obtienen y muestran las fotos asociadas al crédito actual,
     * así como la firma, si está disponible.
     *
     * @param response El resultado de la solicitud de biometría procesada por el servidor.
     *                 Es true si la solicitud se procesó con éxito, false en caso contrario.
     */
    @Override
    public void onResponse(boolean response) {
        if (response) {
            dao.getFotosFindId(infoPhoto.getDataPhoto(InfoPhoto.ID_FOTO), "S").forEach(fotosEntity -> {
                Bitmap bitmap = BitmapFactory.decodeFile(fotosEntity.FOTO);
                if (bitmap == null) {
                    return;
                }
                binding.capturePhotoButton.setImageBitmap(bitmap);
            });
            FotosEntity entity = dao.getFotoFindId(ID_CREDIT, "FIRMA", "%%");
            if (entity == null) {
                return;
            }
            byte[] bitmap = Base64.decode(entity.FOTO, Base64.DEFAULT);
            binding.captureSignatureButton.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
        }

    }

    @Override
    public Context getContextClass() {
        return this;
    }

}