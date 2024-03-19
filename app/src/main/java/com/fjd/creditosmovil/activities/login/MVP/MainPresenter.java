package com.fjd.creditosmovil.activities.login.MVP;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;

/**
 * Clase que actúa como presentador para manejar la lógica de negocio relacionada
 * con el inicio de sesión.
 */
public class MainPresenter {

    // Vista asociada al presentador
    private final MainContract.View view;
    MainInteractor mainInteractor = new MainInteractor();

    /**
     * Constructor para inicializar el presentador con la vista proporcionada.
     *
     * @param view La vista que implementa la interfaz MainContract.View
     */
    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    /**
     * Método para realizar el inicio de sesión utilizando los datos proporcionados.
     *
     * @param formLogin El servidor al que se realiza la solicitud de inicio de sesión.
     */
    public void login(FormLogin formLogin) {
        view.showLoader();
        try {
            mainInteractor.retriveResponseLogin(new MainContract.retrieveResponseCallback() {
                @Override
                public void onResponse(ResponseLogin response) {
                    boolean res = response.getS_1() == 1;
                    if (res){
                        saveAccessLogin(view.getContext(), response.getJWT(),formLogin);
                    }
                    view.onPostResponse(res);
                }

                @Override
                public void setOnError(String err) {
                    view.showErrors(err);
                }

                @Override
                public Context getContext() {
                    return view.getContext();
                }
            }, formLogin);

            view.showLoader();

        }catch (Exception e){
            e.printStackTrace();
            view.showErrors(e.getMessage());
        }
    }

    /**
     * Guarda los datos de acceso del usuario en las preferencias compartidas.
     * Este método guarda el token de acceso, el nombre de usuario y la URL de conexión en las preferencias compartidas
     * para que estén disponibles para futuras sesiones de inicio de sesión.
     *
     * @param context    El contexto de la aplicación.
     * @param token      El token de acceso generado durante el inicio de sesión.
     * @param formLogin  Los datos del formulario de inicio de sesión, incluyendo el nombre de usuario y la URL de conexión.
     */
    public static void saveAccessLogin(Context context, String token, FormLogin formLogin) {
        SharedPreferences preferences = context.getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("TOKEN", token);
        edit.putString("user", formLogin.USER);
        edit.putString("url", formLogin.URL_CONNECTION);
        edit.apply();
    }

}
