package com.fjd.creditosmovil.util.singletons;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.fjd.creditosmovil.R;
import com.google.android.material.snackbar.Snackbar;

public class Alerts {
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void danger(View layout, String srt, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(context.getDrawable(R.drawable.baseline_cancel_24));
        builder.setTitle("Error!!").setCancelable(false).setMessage(srt)
               .setPositiveButton("Ok", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void success(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG).setBackgroundTint(context.getColor(R.color.success))
                                 .setTextColor(context.getColor(R.color.white));
        snack.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void warring(View layout, String srt, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(context.getDrawable(R.drawable.baseline_warning_24));
        builder.setTitle("Error!!").setCancelable(false).setMessage(srt)
               .setPositiveButton("Ok", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void info(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG).setBackgroundTint(context.getColor(R.color.info))
                                 .setTextColor(context.getColor(R.color.white));
        snack.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void errorDialog(Context context, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(context.getDrawable(R.drawable.baseline_cancel_24));
        builder.setTitle("Error!!").setCancelable(false).setMessage(str)
               .setPositiveButton("Ok", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
