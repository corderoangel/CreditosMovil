package com.fjd.creditosmovil.activities.home.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fjd.creditosmovil.activities.home.MVP.HomeContract;
import com.fjd.creditosmovil.activities.home.MVP.HomePresenter;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.activities.homeGallery.homeGalleryActivity;
import com.fjd.creditosmovil.databinding.ActivityHomeBinding;
import com.fjd.creditosmovil.util.SnackbarUtil;
import com.fjd.creditosmovil.util.Tools;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomeContract.Presenter presenter;
    ActivityHomeBinding binding;
    HomeViewModel homeViewModel;
    HomeListAdapter listAdapter;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new HomePresenter(this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.listData.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        homeViewModel.getListData().observe(this, dataListModels ->{
            listAdapter = new HomeListAdapter(this, dataListModels, this::startAddBiometric, showMessages());
            binding.listData.setAdapter(listAdapter);
        });

        binding.swipeRefreshLayout.setOnRefreshListener( () -> {
            presenter.getDataList();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    void startAddBiometric(ResponseData dataListModel){
        if (!Tools.isNetworkAvailable(this)){
            showMessages().showWarning("No tienes conexión a internet");
            return;
        }
        startActivity(new Intent(this, homeGalleryActivity.class));
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
        homeViewModel.setListData(response);
    }

    @Override
    public Context getContextClass() {
        return this;
    }

    @Override
    protected void onResume() {
        presenter.getDataList();
        super.onResume();
    }
}