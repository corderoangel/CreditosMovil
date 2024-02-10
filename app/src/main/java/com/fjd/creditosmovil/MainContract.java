package com.fjd.creditosmovil;

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
        /**
         * Método llamado cuando el inicio de sesión es exitoso.
         *
         * @param token El token de sesión obtenido después del
         * inicio de sesión exitoso.
         */
        void showLoginSuccess(String token);
        /**
         * Método llamado cuando ocurre un error durante el
         * inicio de sesión.
         */
        void showLoginError();
        /**
         * Método llamado cuando hay un error de red durante
         * el inicio de sesión.
         */
        void showNetworkError();
        /**
         * Método llamado cuando hay un error en el servicio de
         * la API durante el inicio de sesión.
         */
        void showApiServiceError();
    }
}
