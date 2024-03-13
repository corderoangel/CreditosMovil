package com.fjd.creditosmovil.util.singletons;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Tools {
    @NonNull
    public static String getTextsTV(@NonNull AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getTag() == null ? "" : String.valueOf(autoCompleteTextView.getTag());
    }
    @NonNull
    public static String getTextsET(@NonNull EditText edittex) {
        return edittex.getText().toString().trim().isEmpty() ? "" : edittex.getText().toString().trim();
    }
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            }
        }
        return false;
    }

}
