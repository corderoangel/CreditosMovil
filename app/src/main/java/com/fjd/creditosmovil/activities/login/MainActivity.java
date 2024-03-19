package com.fjd.creditosmovil.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fjd.creditosmovil.activities.home.view.HomeActivity;
import com.fjd.creditosmovil.activities.login.MVP.MainContract;
import com.fjd.creditosmovil.activities.login.MVP.MainPresenter;
import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.databinding.ActivityMainBinding;
import com.fjd.creditosmovil.util.singletons.Permissions;
import com.fjd.creditosmovil.util.singletons.SnackbarUtil;
import com.fjd.creditosmovil.util.singletons.Tools;
import com.fjd.creditosmovil.util.contracts.SessionUser;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    // Declaraciones de variables de vistas y presentador
    ActivityMainBinding binding;
    private MainPresenter presenter;
    FormLogin formLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Permissions.setPerms(this);
        presenter = new MainPresenter(this);
        formLogin = new FormLogin();
        // Configuración del botón de autenticación
        binding.btnLogin.setOnClickListener(v -> {
            if (!Tools.isNetworkAvailable(this)){
                showErrors("No tienes Acceso a internet");
                return;
            }
            setFormLogin();
            // Llamar al método de inicio de sesión del presentador
            presenter.login(formLogin);
        });
        eventsManager();
        activeSession();
    }

    /**
     * Activa la sesión del usuario si existe un token de acceso guardado en las preferencias compartidas.
     * Este método verifica si existe un token de acceso guardado en las preferencias compartidas.
     * Si existe un token de acceso, inicia la actividad de la pantalla principal (HomeActivity) y finaliza la actividad actual.
     * Si no hay un token de acceso, no realiza ninguna acción.
     */
    void activeSession(){
        if (!SessionUser.getAccess(this, SessionUser.TOKEN).isEmpty()){
            startActivity( new Intent(this, HomeActivity.class));
            finish();
        }
    }

    /**
     * Establece los datos del formulario de inicio de sesión.
     * Este método recupera los datos ingresados en los campos de la interfaz de usuario relacionados con el inicio de sesión,
     * como el nombre de usuario, la contraseña, el servidor y el dominio.
     * Luego, utiliza estos datos para configurar los campos correspondientes del objeto FormLogin.
     */
    void setFormLogin(){
        formLogin.PASS = Tools.getTextsET(binding.codAuth);
        formLogin.USER = Tools.getTextsET(binding.ccAuth);
        formLogin.SERVER = Tools.getTextsET(binding.servidor);
        formLogin.DOMAIN = Tools.getTextsET(binding.dominio);
        formLogin.URL_CONNECTION = "https://" + Tools.getTextsET(binding.servidor)+"/"+Tools.getTextsET(binding.dominio)+"/api/";
    }

    /**
     * Gestiona los eventos de cambio en los campos del formulario de inicio de sesión.
     * Este método establece un TextWatcher en cada campo del formulario de inicio de sesión.
     * El TextWatcher detecta cambios en los campos y llama al método setFormLogin() para actualizar los datos del formulario.
     * Además, habilita o deshabilita el botón de inicio de sesión en función de la validez del formulario.
     */
    void eventsManager(){
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setFormLogin();
                binding.btnLogin.setEnabled(validFormLogin(formLogin));
            }
        };
        binding.dominio.addTextChangedListener(watcher);
        binding.servidor.addTextChangedListener(watcher);
        binding.ccAuth.addTextChangedListener(watcher);
        binding.codAuth.addTextChangedListener(watcher);
    }

    /**
     * Valida si los campos del formulario de inicio de sesión contienen datos válidos.
     * Este método comprueba si los campos del formulario de inicio de sesión, incluyendo el nombre de usuario,
     * la contraseña, el servidor y el dominio, no están en blanco.
     * Devuelve true si todos los campos contienen datos válidos, de lo contrario, devuelve false.
     */
    public boolean validFormLogin(FormLogin formLogin){
        return !formLogin.USER.isBlank() && !formLogin.PASS.isBlank() && !formLogin.DOMAIN.isBlank() && !formLogin.SERVER.isBlank();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // Validamos si el usuario acepta el permiso para que la aplicación acceda a los datos internos del equipo, si no denegamos el acceso
            if (grantResults.length <= 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Permissions.setPerms(this);
            }
        }
    }

    // Implementación de métodos de la interfaz MainContract.View
    @Override
    public void showLoader() {
      binding.progressBar.getRoot().setVisibility(View.VISIBLE);
      binding.btnLogin.setEnabled(false);
    }

    @Override
    public void hideLoader() {
        binding.progressBar.getRoot().setVisibility(View.GONE);
        binding.btnLogin.setEnabled(true);
    }

    @Override
    public void showErrors(String err) {
        binding.btnLogin.setEnabled(true);
        SnackbarUtil.danger(binding.getRoot(), err,this);
        binding.progressBar.getRoot().setVisibility(View.GONE);
    }

    /**
     * Maneja la respuesta posterior a la solicitud de inicio de sesión.
     *
     * Este método se llama después de recibir una respuesta del servidor al intentar iniciar sesión.
     * Habilita el botón de inicio de sesión y oculta el indicador de progreso.
     * Si la respuesta indica un inicio de sesión exitoso (true), activa la sesión del usuario.
     *
     * @param response  Indica si la solicitud de inicio de sesión fue exitosa.
     *                  Es true si el inicio de sesión fue exitoso, false en caso contrario.
     */
    @Override
    public void onPostResponse(boolean response) {
        binding.btnLogin.setEnabled(true);
        binding.progressBar.getRoot().setVisibility(View.GONE);
        if (response){
           activeSession();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

}
