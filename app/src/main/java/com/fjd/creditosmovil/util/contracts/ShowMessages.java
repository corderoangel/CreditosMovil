package com.fjd.creditosmovil.util.contracts;

public interface ShowMessages {
    void showLoader(String str);
    void hideLoader();
    void showErrors(String err);
    void showSuccess(String success);
    void showWarning(String warn);
    interface syncCallback {
        void syncParams();
        void syncOrders();
    }
}
