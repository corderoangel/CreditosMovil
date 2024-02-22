package com.fjd.creditosmovil.util.contracts;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUser {
    public static final String USER = "user", D_USER = "d_user", URL_CONNECTION = "url",
            IMEI = "imei", CEDULA = "cedula", TOKEN = "TOKEN";

    public static String getAccess(Context context, String key){
        SharedPreferences preferences =context.getSharedPreferences("LOGIN", MODE_PRIVATE);
        return preferences.getString(key, "");
    }
}
