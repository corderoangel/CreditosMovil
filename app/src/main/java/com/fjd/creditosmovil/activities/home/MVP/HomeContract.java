package com.fjd.creditosmovil.activities.home.MVP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;

public interface HomeContract {
    interface View {
        ShowMessages showMessages();
        void onResponse(ArrayList<ResponseData> response);
        Context getContextClass();
    }

    interface Presenter {
        void executeSend();
    }

    interface CallbackParams {
        void onResponse(ArrayList<ResponseData> response);
        Context getContextClass();
        ShowMessages showMessages();
    }
}
