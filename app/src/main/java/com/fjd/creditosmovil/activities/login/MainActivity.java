package com.fjd.creditosmovil.activities.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.fjd.creditosmovil.activities.login.MVP.MainContract;
import com.fjd.creditosmovil.activities.login.MVP.MainPresenter;
import com.fjd.creditosmovil.activities.login.Models.FormLogin;
import com.fjd.creditosmovil.activities.login.Models.ResponseLogin;
import com.fjd.creditosmovil.databinding.ActivityMainBinding;
import com.fjd.creditosmovil.util.SnackbarUtil;
import com.fjd.creditosmovil.util.Tools;

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
    }
    void setFormLogin(){
        formLogin.PASS = Tools.getTextsET(binding.codAuth);
        formLogin.USER = Tools.getTextsET(binding.ccAuth);
        formLogin.SERVER = Tools.getTextsET(binding.servidor);
        formLogin.DOMAIN = Tools.getTextsET(binding.dominio);
        formLogin.URL_CONNECTION = "https://" + Tools.getTextsET(binding.servidor)+"/"+ Tools.getTextsET(binding.dominio)+"/api/login";
    }
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

    public boolean validFormLogin(FormLogin formLogin){
        return !formLogin.USER.isBlank() && !formLogin.PASS.isBlank() && !formLogin.DOMAIN.isBlank() && !formLogin.SERVER.isBlank();
    }

    // Implementación de métodos de la interfaz MainContract.View
    @Override
    public void showLoader() {
       // SnackbarUtil.success(binding.getRoot(), "Iniciando sesión...",this);
    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showErrors(String err) {
        SnackbarUtil.danger(binding.getRoot(), err,this);
    }

    @Override
    public void onPostResponse(boolean response) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
