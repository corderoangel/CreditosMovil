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
import com.fjd.creditosmovil.data.local.DAO;
import com.fjd.creditosmovil.data.local.ManagerDataBase;
import com.fjd.creditosmovil.data.local.entities.FotosEntity;
import com.fjd.creditosmovil.databinding.ActivityProcessBinding;
import com.fjd.creditosmovil.util.contracts.ShowMessages;
import com.fjd.creditosmovil.util.photos.InfoPhoto;
import com.fjd.creditosmovil.util.photos.SavePhoto;
import com.fjd.creditosmovil.util.singletons.Alerts;
import com.fjd.creditosmovil.util.singletons.Permissions;

public class ProcessActivity extends AppCompatActivity implements ProcessContract.View, ShowMessages {
    private static final String TAG = "ProcessActivity";
    SavePhoto savePhoto;
    InfoPhoto infoPhoto;
    ActivityProcessBinding binding;
    Intent intent;
    DAO dao;
    String ID_CREDIT, ID_BIOMETRIC;
    ResponseData CLIENT_DATA;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
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
        eventsManager();
    }

    void eventsManager() {
        //evento para capturar la foto
        binding.capturePhotoButton.setOnClickListener(v -> {
            setValues();
            if (dao.getFotoFindId(ID_CREDIT, "FOTO", "S") != null) {
                showMessages().showWarning("Ya tienes una foto en proceso");
                return;
            }
            infoPhoto.setDataPhoto(InfoPhoto.TYPE, "FOTO");
            Permissions.setPerms(this);
            savePhoto.CapturarFoto();
        });
        //evento para capturar la foto de dni 1
        binding.captureDni1Button.setOnClickListener(v -> {
            setValues();
            if (dao.getFotoFindId(ID_CREDIT, "dni 1", "S") != null) {
                showMessages().showWarning("Ya tienes una foto en proceso");
                return;
            }
            infoPhoto.setDataPhoto(InfoPhoto.TYPE, "dni 1");
            Permissions.setPerms(this);
            savePhoto.CapturarFoto();
        });
        //evento para capturar la foto de dni 2
        binding.captureDni2Button.setOnClickListener(v -> {
            setValues();
            if (dao.getFotoFindId(ID_CREDIT, "dni 2", "S") != null) {
                showMessages().showWarning("Ya tienes una foto en proceso");
                return;
            }
            infoPhoto.setDataPhoto(InfoPhoto.TYPE, "dni 2");
            Permissions.setPerms(this);
            savePhoto.CapturarFoto();
        });
        //Evento para agregar la firma
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
    public boolean onOptionsItemSelected(
            @NonNull
            MenuItem item) {
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
        //dao.deleteFotoFindID(ID_CREDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SavePhoto.REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                //Una vez se toma la foto se procesa y pasa a su envio por el presenter
                savePhoto.GuardarFoto(() -> {
                    //Validamos que exista una foto con este temporal biometrico
                    if (dao.getFotoFindId(infoPhoto.getDataPhoto(InfoPhoto.ID_FOTO), infoPhoto.getDataPhoto(InfoPhoto.TYPE),
                                          "N") != null) {
                        //Ejecutamos el envio de la foto encontrada
                        new ProcessPresenter(this).sendBiometric(infoPhoto.getDataPhoto(InfoPhoto.TYPE), ID_CREDIT, CLIENT_DATA);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "onActivityResult: ", e);
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
        } else {
            showMessages().showWarning("Debes Agregar todos los datos");
        }
    }


    /**
     * Devuelve una implementación de ShowMessages para mostrar mensajes en la interfaz de usuario.
     * Este método proporciona una implementación de la interfaz ShowMessages que permite mostrar mensajes
     * de carga, éxito, advertencia y error en la interfaz de usuario de la actividad actual.
     * La implementación utiliza Alerts para mostrar los mensajes y gestionar la visibilidad de los elementos de la interfaz.
     */
    @Override
    public ShowMessages showMessages() {
        return this;
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
            viewPhoto();
            viewFirma();
        }

    }

    void viewPhoto() {
        try {
            FotosEntity photo = dao.getFotoFindId(ID_CREDIT, infoPhoto.getDataPhoto(InfoPhoto.TYPE), "S");
            if (photo == null) {
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(photo.FOTO);
            if (bitmap == null) {
                return;
            }
            switch (infoPhoto.getDataPhoto(InfoPhoto.TYPE)) {
                case "FOTO":
                    binding.capturePhotoButton.setImageBitmap(bitmap);
                    break;
                case "dni 1":
                    binding.captureDni1Button.setImageBitmap(bitmap);
                    break;
                case "dni 2":
                    binding.captureDni2Button.setImageBitmap(bitmap);
                    break;
            }

        } catch (Exception e) {
            Log.e(TAG, "viewPhoto: ", e);
        }
    }

    void viewFirma() {
        try {
            FotosEntity firma = dao.getFotoFindId(ID_CREDIT, "FIRMA", "%%");
            if (firma == null) {
                return;
            }
            byte[] bitmap = Base64.decode(firma.FOTO, Base64.DEFAULT);
            binding.captureSignatureButton.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
        } catch (Exception e) {
            Log.e(TAG, "viewFirma: ", e);
        }
    }

    @Override
    public Context getContextClass() {
        return this;
    }

    @Override
    protected void onResume() {
        setValues();
        viewPhoto();
        viewFirma();
        verifySignature();
        super.onResume();
    }

    public void showLoader(String str) {
        Alerts.success(binding.getRoot(), str, this);
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
        Alerts.errorDialog(this, err);
        binding.capturePhotoButton.setEnabled(true);
        binding.captureSignatureButton.setEnabled(true);
    }

    @Override
    public void showSuccess(String success) {
        binding.progressBar.getRoot().setVisibility(View.GONE);
        Alerts.success(binding.getRoot(), success, this);
        binding.capturePhotoButton.setEnabled(true);
        binding.captureSignatureButton.setEnabled(true);
    }

    @Override
    public void showWarning(String warn) {
        binding.progressBar.getRoot().setVisibility(View.GONE);
        Alerts.warring(binding.getRoot(), warn, this);
        binding.capturePhotoButton.setEnabled(true);
        binding.captureSignatureButton.setEnabled(true);
    }
}