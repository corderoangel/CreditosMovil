package com.fjd.creditosmovil.activities.login.MVP;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;

/**
 * Clase que actúa como presentador para manejar la lógica de negocio relacionada
 * con el inicio de sesión.
 */
public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";
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
     * Guarda los datos de acceso del usuario en las preferencias compartidas.
     * Este método guarda el token de acceso, el nombre de usuario y la URL de conexión en las preferencias compartidas
     * para que estén disponibles para futuras sesiones de inicio de sesión.
     *
     * @param context   El contexto de la aplicación.
     * @param token     El token de acceso generado durante el inicio de sesión.
     * @param formLogin Los datos del formulario de inicio de sesión, incluyendo el nombre de usuario y la URL de conexión.
     */
    public static void saveAccessLogin(Context context, String token, FormLogin formLogin) {
        SharedPreferences preferences = context.getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("TOKEN", token);
        edit.putString("user", formLogin.USER);
        edit.putString("url", formLogin.URL_CONNECTION);
        edit.apply();
    }

    /**
     * Método para realizar el inicio de sesión utilizando los datos proporcionados.
     *
     * @param formLogin El servidor al que se realiza la solicitud de inicio de sesión.
     */
    public void login(FormLogin formLogin) {
        view.showLoader();
        try {
            mainInteractor.retrieveResponseLogin(new MainContract.retrieveResponseCallback() {
                @Override
                public void onResponse(ResponseLogin response) {
                    try {
                        boolean state = response.getS_1() == 1;
                        boolean refreshToken = response.getState_session().equalsIgnoreCase("0");
                        if (state) {
                            saveAccessLogin(view.getContext(), response.getJWT(), formLogin);
                        }
                        if (refreshToken) {
                            refreshTokenDialog(formLogin, response.getJWT(), response.getS_2());
                            return;
                        }
                        if (!state) {
                            view.showErrors(response.getS_2());
                            return;
                        }

                        view.onPostResponse(state);
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
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

        } catch (Exception e) {
            Log.e(TAG, "login: ", e);
            view.showErrors(e.getMessage());
        }
    }

    @Override
    public void refreshToken(FormLogin formLogin, String token) {
        try {
            mainInteractor.retrieveResponseRefreshToken(new MainContract.retrieveResponseCallback() {
                @Override
                public void onResponse(ResponseLogin response) {
                    boolean res = response.getS_1() == 1;
                    if (res) {
                        saveAccessLogin(view.getContext(), response.getJWT(), formLogin);
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
            }, formLogin, token);

            view.showLoader();
        } catch (Exception e) {
            Log.e(TAG, "refreshToken: ", e);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void refreshTokenDialog(FormLogin formLogin, String token, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setIcon(view.getContext().getDrawable(R.drawable.baseline_cancel_24));
        builder.setTitle(msg);
        builder.setCancelable(false);
        builder.setMessage("Deseas finalizar todas las sesiones abiertas en otros dispositivos...");
        builder.setPositiveButton("Finalizar", (dialog, id) -> refreshToken(formLogin, token));
        builder.setNegativeButton("Cancelar", (dialog, id) -> {
            view.hideLoader();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
