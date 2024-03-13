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
            Log.e("TAG", "onCreate: " + ID_CREDIT);
            if (dao.getFotoFindById(ID_CREDIT, "FOTO") != null) {
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

    void verifySignature() {
        FotosEntity entity = dao.getFotoFindId(ID_CREDIT, "FIRMA", "N");
        if (entity != null) {
            new ProcessPresenter(this).sendBiometric("FIRMA", ID_CREDIT, CLIENT_DATA);
        }
    }

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

    @Override
    public ShowMessages showMessages() {
        return new ShowMessages() {
            public void showLoader(String str) {
                SnackbarUtil.success(binding.getRoot(), str, ProcessActivity.this);
                binding.progressBar.getRoot().setVisibility(View.VISIBLE);
            }

            @Override
            public void hideLoader() {
                binding.progressBar.getRoot().setVisibility(View.GONE);
            }


            @Override
            public void showErrors(String err) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.danger(binding.getRoot(), err, ProcessActivity.this);
            }

            @Override
            public void showSuccess(String success) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.success(binding.getRoot(), success, ProcessActivity.this);
            }

            @Override
            public void showWarning(String warn) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.warring(binding.getRoot(), warn, ProcessActivity.this);
            }
        };
    }

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