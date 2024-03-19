package com.fjd.creditosmovil.activities.home.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.home.MVP.HomeContract;
import com.fjd.creditosmovil.activities.home.MVP.HomePresenter;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.login.MainActivity;
import com.fjd.creditosmovil.activities.process.ProcessActivity;
import com.fjd.creditosmovil.databinding.ActivityHomeBinding;
import com.fjd.creditosmovil.databinding.VerifyAccessViewBinding;
import com.fjd.creditosmovil.util.singletons.Permissions;
import com.fjd.creditosmovil.util.singletons.SnackbarUtil;
import com.fjd.creditosmovil.util.singletons.Tools;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomeContract.Presenter presenter;
    ActivityHomeBinding binding;
    HomeViewModel homeViewModel;
    HomeListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Permissions.setPerms(this);
        presenter = new HomePresenter(this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.listData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        homeViewModel.getListData().observe(this, dataListModels -> {
            listAdapter = new HomeListAdapter(this, dataListModels, this::startAddBiometric, showMessages());
            binding.listData.setAdapter(listAdapter);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.getDataList();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Inicia el proceso de agregar una biometría.
     * Este método comprueba la disponibilidad de la red. Si no hay conexión a Internet,
     * muestra un mensaje de advertencia y no continúa con el proceso de agregar la biometría.
     * Si hay conexión a Internet, llama al método para verificar el acceso necesario para agregar la biometría.
     *
     * @param dataListModel El modelo de datos que contiene la información necesaria para agregar la biometría.
     *                      Se utiliza para determinar si se requiere acceso especial para agregar la biometría.
     */
    void startAddBiometric(ResponseData dataListModel) {
        if (!Tools.isNetworkAvailable(this)) {
            showMessages().showWarning("No tienes conexión a internet");
            return;
        }
        verifyAccess(dataListModel);
    }

    /**
     * Verifica el acceso necesario para agregar una biometría.
     * Este método muestra un cuadro de diálogo que solicita al usuario que ingrese un token de acceso
     * enviado a su correo registrado en el sistema. El token de acceso es necesario para agregar la biometría.
     * Una vez que el usuario ingresa el token de acceso, se llama al método para validar el token.
     *
     * @param responseData El modelo de datos que contiene la información necesaria para agregar la biometría.
     *                      Se utiliza para procesar la respuesta después de verificar el token de acceso.
     */
    void verifyAccess(ResponseData responseData) {
        VerifyAccessViewBinding accessViewBinding = VerifyAccessViewBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.ic_app));
        builder.setTitle("Verificar Acceso!")
                .setCancelable(false)
                .setMessage("Ingresa el token de acceso que fue enviado a tu correo registrado en el sistema.")
                .setView(accessViewBinding.getRoot())
                .setPositiveButton("Ok", (dialog, id) -> {
                    String tokenHash = Tools.getTextsET(accessViewBinding.etTokenAccess);
                    presenter.validateToken(tokenHash, responseData);
                })
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Muestra un diálogo para confirmar el cierre de sesión.
     *
     * Este método muestra un diálogo de alerta que solicita al usuario confirmar si desea cerrar sesión.
     * Si el usuario confirma el cierre de sesión, se eliminan las preferencias compartidas relacionadas con el inicio de sesión.
     * Si el usuario cancela, el diálogo se cierra sin realizar ninguna acción adicional.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.ic_app));
        builder.setTitle("¡Cerrar Sesión!")
                .setCancelable(false)
                .setMessage("¿Estas seguro de que deseas salir?")
                .setPositiveButton("Ok", (dialog, id) -> presenter.logout())
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_app:
                presenter.getDataList();
                break;
            case R.id.log_out:
                logoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Devuelve una implementación de ShowMessages para mostrar mensajes en la interfaz de usuario.
     * Este método proporciona una implementación de la interfaz ShowMessages que permite mostrar mensajes
     * de carga, éxito, advertencia y error en la interfaz de usuario de la actividad actual.
     * La implementación utiliza SnackbarUtil para mostrar los mensajes y gestionar la visibilidad de los elementos de la interfaz.
     */
    @Override
    public ShowMessages showMessages() {
        return new ShowMessages() {
            @Override
            public void showLoader(String str) {
                binding.progressBar.getRoot().setVisibility(View.VISIBLE);
            }

            @Override
            public void hideLoader() {
                binding.progressBar.getRoot().setVisibility(View.GONE);
            }

            @Override
            public void showErrors(String err) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.danger(binding.getRoot(), err, HomeActivity.this);
            }

            @Override
            public void showSuccess(String success) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.success(binding.getRoot(), success, HomeActivity.this);
            }

            @Override
            public void showWarning(String warn) {
                binding.progressBar.getRoot().setVisibility(View.GONE);
                SnackbarUtil.warring(binding.getRoot(), warn, HomeActivity.this);
            }
        };
    }

    /**
     * Maneja la respuesta recibida del servidor después de realizar una solicitud de datos.
     *
     * Este método se llama cuando se recibe una respuesta del servidor después de realizar una solicitud de datos.
     * Si la respuesta está vacía o no contiene información de cliente, se establece una lista vacía en el modelo de vista.
     * De lo contrario, se establece la lista de datos recibidos en el modelo de vista para su visualización en la interfaz de usuario.
     *
     * @param response La lista de datos recibidos del servidor.
     *                 Puede contener información sobre los clientes o estar vacía si no se encontraron datos.
     */
    @Override
    public void onResponse(ArrayList<ResponseData> response) {
        if (response.size() == 0) {
            homeViewModel.setListData(new ArrayList<>());
            return;
        }
        if (response.get(0).getClientName() == null) {
            homeViewModel.setListData(new ArrayList<>());
            return;
        }
        homeViewModel.setListData(response);
    }
    @Override
    public Context getContextClass() {
        return this;
    }

    /**
     * Valida la respuesta del token de acceso y realiza acciones en consecuencia.
     *
     * Este método se llama cuando se recibe una respuesta sobre la validez del token de acceso.
     * Si la respuesta es positiva (true), se muestra un mensaje con el token de acceso y se inicia la actividad de procesamiento
     * con los datos del cliente proporcionados.
     *
     * @param response      El resultado de la validación del token de acceso.
     *                      Es true si el token de acceso es válido, false en caso contrario.
     * @param responseData  Los datos del cliente necesarios para iniciar la actividad de procesamiento.
     */
    @Override
    public void validateToken(boolean response, ResponseData responseData) {
        if (response) {
            Toast.makeText(this, "AccessToken: " + response, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProcessActivity.class);
            intent.putExtra("objetCredit", responseData);
            startActivity(intent);
        }
    }

    /**
     * Realiza acciones después de recibir una respuesta sobre el cierre de sesión.
     *
     * Este método se llama cuando se recibe una respuesta sobre el éxito del cierre de sesión.
     * Si la respuesta es positiva (true), se eliminan las preferencias compartidas relacionadas con el inicio de sesión
     * y las fotos, y se redirige al usuario a la actividad principal para iniciar sesión de nuevo.
     * Finalmente, la actividad actual se cierra.
     *
     * @param response  El resultado del cierre de sesión.
     *                  Es true si el cierre de sesión fue exitoso, false en caso contrario.
     */
    @Override
    public void logout(boolean response) {
        if (response) {
            deleteSharedPreferences("LOGIN");
            deleteSharedPreferences("PHOTOS");
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    protected void onResume() {
        presenter.getDataList();
        super.onResume();
    }
}