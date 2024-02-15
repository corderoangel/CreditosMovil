package com.fjd.creditosmovil.activities.login;

import android.content.Context;

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

        void showLoginOk(String message);
        /**
         * Método llamado cuando ocurre un error durante el
         * inicio de sesión.
         */
        void showLoginError(String message);
        /**
         * Método llamado cuando hay un error de red durante
         * el inicio de sesión.
         */
        void showNetworkError(String message);
        /**
         * Método llamado cuando hay un error en el servicio de
         * la API durante el inicio de sesión.
         */
        void showApiServiceError(String message);

        /**
         * Método para obtener el contexto de la vista.
         * @return El contexto de la vista.
         */
        Context getContext();
    }
}
