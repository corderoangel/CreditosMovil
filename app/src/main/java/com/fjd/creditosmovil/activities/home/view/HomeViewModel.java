package com.fjd.creditosmovil.activities.home.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fjd.creditosmovil.activities.home.models.ResponseData;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ResponseData>> mutableLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<ResponseData>> getListData() {
        return mutableLiveData;
    }

    public void setListData(ArrayList<ResponseData> list) {
        mutableLiveData.setValue(list);
    }
}
