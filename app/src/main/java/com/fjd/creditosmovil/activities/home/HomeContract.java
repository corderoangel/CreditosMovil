package com.fjd.creditosmovil.activities.home;

import android.content.Intent;
import android.net.Uri;

public interface HomeContract {
    interface View {
        void showCameraPermissionRequest();
        void startCameraIntent(Uri photoUri);
        void showImageSavedSuccess();
        void openGallery(Uri photoUri);
    }

    interface Presenter {
        void onCaptureButtonClicked();
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
