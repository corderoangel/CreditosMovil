package com.fjd.creditosmovil.activities.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.util.SnackbarUtil;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    // Declaraciones de variables de vistas y presentador
    private EditText servidorEditText;
    private EditText dominioEditText;
    private EditText ccAuthEditText;
    private EditText codAuthEditText;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de las vistas y el presentador
        servidorEditText = findViewById(R.id.servidor);
        dominioEditText = findViewById(R.id.dominio);
        ccAuthEditText = findViewById(R.id.cc_auth);
        codAuthEditText = findViewById(R.id.cod_auth);
        presenter = new MainPresenter(this);

        // Configuración del botón de autenticación
        Button btnAuth = findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(v -> {
            // Obtener los valores de los campos de texto
            String servidor = servidorEditText.getText().toString();
            String doiminio = dominioEditText.getText().toString();
            String ccAuth = ccAuthEditText.getText().toString();
            String codAuth = codAuthEditText.getText().toString();

            // Llamar al método de inicio de sesión del presentador
            presenter.login(servidor, doiminio, ccAuth, codAuth);
        });
    }

    // Implementación de métodos de la interfaz MainContract.View

    @Override
    public void showLoginOk(String message) {
        SnackbarUtil.showCustomSnackbar(this, message, R.color.green);
    }

    @Override
    public void showLoginError(String message) {
        SnackbarUtil.showCustomSnackbar(this, message, R.color.red);
    }

    @Override
    public void showNetworkError(String message) {
        SnackbarUtil.showCustomSnackbar(this, message, R.color.red);
    }

    @Override
    public void showApiServiceError(String message) {
        SnackbarUtil.showCustomSnackbar(this, message, R.color.red);
    }


    @Override
    public Context getContext() {
        return this;
    }
}
