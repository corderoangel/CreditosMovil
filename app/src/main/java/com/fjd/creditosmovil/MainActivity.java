package com.fjd.creditosmovil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainContract.View{

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
    public void showLoginSuccess(String token) {
        Log.e("bien", "success");
    }

    @Override
    public void showLoginError() {
        Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showApiServiceError() {
        Toast.makeText(this, "Error en el servicio de la API", Toast.LENGTH_SHORT).show();
    }
}
