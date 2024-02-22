package com.fjd.creditosmovil.util.singletons;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.fjd.creditosmovil.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtil {
    public static void danger(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.danger))
                .setTextColor(context.getColor(R.color.white));
        snack.show();
    }

    public static void success(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.success))
                .setTextColor(context.getColor(R.color.white));
        snack.show();
    }

    public static void warring(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.warning))
                .setTextColor(context.getColor(R.color.white));
        snack.show();
    }

    public static void info(View layout, String srt, Context context) {
        Snackbar snack = Snackbar.make(layout, srt, Snackbar.LENGTH_LONG)
                .setBackgroundTint(context.getColor(R.color.info))
                .setTextColor(context.getColor(R.color.white));
        snack.show();
    }
}
