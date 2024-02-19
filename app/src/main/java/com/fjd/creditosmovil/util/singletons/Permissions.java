package com.fjd.creditosmovil.util.singletons;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.annotation.NonNull;

public class Permissions {
    static final Integer PHONESTATS = 0x1;

    static String[] perms = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void setPerms(@NonNull Context context) {
        int cameraPermission = context.checkSelfPermission(CAMERA);
        int internet = context.checkSelfPermission(INTERNET);
        int write = context.checkSelfPermission(WRITE_EXTERNAL_STORAGE);
        int read = context.checkSelfPermission(READ_EXTERNAL_STORAGE);
        if ( cameraPermission != PackageManager.PERMISSION_GRANTED || internet != PackageManager.PERMISSION_GRANTED || write != PackageManager.PERMISSION_GRANTED || read != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(((Activity) context), perms, PHONESTATS);
        }
    }




}
