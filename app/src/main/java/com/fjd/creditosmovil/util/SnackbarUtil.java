package com.fjd.creditosmovil.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.fjd.creditosmovil.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtil {
    public static void showCustomSnackbar(Context context, String message, int backgroundColor) {
        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE); // Cambia el color del texto si es necesario
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, backgroundColor)); // Cambia el color de fondo si es necesario
        snackbar.show();
    }
}
