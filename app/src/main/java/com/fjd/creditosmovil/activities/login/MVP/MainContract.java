package com.fjd.creditosmovil.activities.login.MVP;

import android.content.Context;

import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;

/**
 * Contrato que define los métodos que deben ser implementados
 * por la vista para interactuar con el presentador.
 */
public interface MainContract {
    /**
     * Interfaz que define los métodos que la vista debe implementar
     * para recibir notificaciones del presentador.
     */
    interface View{
        void showLoader();
        void hideLoader();
        void showErrors(String err);
        void onPostResponse(boolean response);
        Context getContext();
    }

    interface Presenter{
        void execute(FormLogin formLogin);
    }
    interface retrieveResponseCallback{
        void onResponse(ResponseLogin response);
        void setOnError(String err);
        Context getContext();
    }
}
