package com.fjd.creditosmovil.activities.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View view;
    private String currentPhotoPath;
    private static final String DATABASES_FOLDER_NAME = "databases";
    private static final String PHOTOS_SUBFOLDER_NAME = "fotos";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void onCaptureButtonClicked() {
        if (ContextCompat.checkSelfPermission((HomeActivity)view, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            view.showCameraPermissionRequest();
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // Permiso denegado
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            view.showImageSavedSuccess();
        }
    }

    private void dispatchTakePictureIntent() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile((HomeActivity)view, "com.fjd.CreditosMovil.fileprovider", photoFile);
            view.startCameraIntent(photoUri);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(((HomeActivity)view).getFilesDir(), DATABASES_FOLDER_NAME + File.separator + PHOTOS_SUBFOLDER_NAME);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = new File(storageDir, imageFileName + ".jpg");
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        ((HomeActivity)view).sendBroadcast(mediaScanIntent);
        view.openGallery(contentUri);
    }
}
