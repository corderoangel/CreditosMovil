package com.fjd.creditosmovil.activities.process.mvp;

import android.content.Context;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;

public interface ProcessContract {
    interface View {
        ShowMessages showMessages();
        void onResponse(boolean response);
        Context getContextClass();
    }

    interface Presenter {
        void sendBiometric(String type, String idBiometric, ResponseData responseData);
    }

    interface CallbackParams {
        void onResponse(ArrayList<ResponseData> response);
        Context getContextClass();
        ShowMessages showMessages();
    }

}
