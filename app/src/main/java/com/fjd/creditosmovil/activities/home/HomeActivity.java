package com.fjd.creditosmovil.activities.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.homeGallery.homeGalleryActivity;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomeContract.Presenter presenter;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomePresenter(this);

        ImageButton captureButton = findViewById(R.id.captureButton);
        captureButton.setOnClickListener(view -> presenter.onCaptureButtonClicked());
    }

    @Override
    public void showCameraPermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void startCameraIntent(Uri photoUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void showImageSavedSuccess() {
        Toast.makeText(this, "Imagen guardada con éxito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openGallery(Uri photoUri) {
        Intent galleryIntent = new Intent(this, homeGalleryActivity.class);
        galleryIntent.putExtra("PHOTO_URI", photoUri);
        startActivity(galleryIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}