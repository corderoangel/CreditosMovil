package com.fjd.creditosmovil.activities.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.home.MVP.HomeContract;
import com.fjd.creditosmovil.activities.home.MVP.HomePresenter;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.homeGallery.homeGalleryActivity;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomeContract.Presenter presenter;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        presenter = new HomePresenter(this);

    }


    @Override
    public ShowMessages showMessages() {
        return null;
    }

    @Override
    public void onResponse(ArrayList<ResponseData> response) {

    }

    @Override
    public Context getContextClass() {
        return null;
    }
}