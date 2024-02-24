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

    void startAddBiometric(ResponseData dataListModel) {
        if (!Tools.isNetworkAvailable(this)) {
            showMessages().showWarning("No tienes conexión a internet");
            return;
        }
        verifyAccess(dataListModel);
    }
    void verifyAccess(ResponseData responseData){
        VerifyAccessViewBinding accessViewBinding = VerifyAccessViewBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.ic_app));
        builder.setTitle("Verificar Acceso!")
                .setCancelable(false)
                .setMessage("Ingresa el token de acceso que fue enviado a tu correo registrado en el sistema.")
                .setView(accessViewBinding.getRoot())
                .setPositiveButton("Ok", (dialog, id) ->{
                  String tokenHash = Tools.getTextsET(accessViewBinding.etTokenAccess);
                  presenter.validateToken(tokenHash, responseData);
                })
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void logOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.ic_app));
        builder.setTitle("¡Cerrar Sesión!")
                .setCancelable(false)
                .setMessage("¿Estas seguro de que deseas salir?")
                .setPositiveButton("Ok", (dialog, id) ->{
                    deleteSharedPreferences("LOGIN");
                })
                .setNegativeButton("Cancelar", ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog =  builder.create();
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
                Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
                SnackbarUtil.danger(binding.getRoot(), err, HomeActivity.this);
            }

            @Override
            public void showSuccess(String success) {
                SnackbarUtil.success(binding.getRoot(), success, HomeActivity.this);
            }

            @Override
            public void showWarning(String warn) {
                SnackbarUtil.warring(binding.getRoot(), warn, HomeActivity.this);
            }
        };
    }

    @Override
    public void onResponse(ArrayList<ResponseData> response) {
        if (response.get(0).getClientName() == null){
            return;
        }
        homeViewModel.setListData(response);
    }

    @Override
    public Context getContextClass() {
        return this;
    }

    @Override
    public void validateToken(boolean response, ResponseData responseData) {
       if (response){
           Toast.makeText(this, "AccessToken: "+ response, Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(this, ProcessActivity.class);
           intent.putExtra("objetCredit", responseData);
           startActivity(intent);
       }
    }

    @Override
    protected void onResume() {
        presenter.getDataList();
        super.onResume();
    }
}