package com.fjd.creditosmovil.activities.home.MVP;

import android.content.Context;


import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;


public class HomePresenter  implements HomeContract.Presenter{

    private HomeContract.View view;
    HomeInteractor homeInteractor;

    /**
     * Constructor de la clase HomePresenter.
     * Este constructor inicializa un nuevo objeto HomePresenter con la vista proporcionada
     * y crea una nueva instancia de HomeInteractor para manejar la lógica de negocio relacionada con la vista.
     *
     * @param view La vista asociada al presentador.
     *             Proporciona la interfaz de usuario con la que interactúa el presentador.
     */
    public HomePresenter(HomeContract.View view) {
        this.view = view;
        homeInteractor = new HomeInteractor();
    }

    /**
     * Obtiene una lista de datos mediante una solicitud al interactor y maneja la respuesta.
     * Este método solicita al interactor la recuperación de una lista de datos.
     * Muestra un indicador de carga mientras espera la respuesta del interactor.
     * Una vez que se recibe la respuesta, se procesa y se envía a la vista para su presentación.
     * Finalmente, oculta el indicador de carga después de completar la operación.
     */
    @Override
    public void getDataList() {
      try {
          view.showMessages().showLoader("");
          homeInteractor.retriveResponseData(new HomeContract.CallbackParams() {
              @Override
              public void onResponse(ArrayList<ResponseData> response) {
                  view.onResponse(response);
              }

              @Override
              public Context getContextClass() {
                  return view.getContextClass();
              }

              @Override
              public ShowMessages showMessages() {
                  return view.showMessages();
              }
          });
          view.showMessages().hideLoader();
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    /**
     * Valida un token hash mediante una solicitud al interactor y maneja la respuesta.
     * Este método solicita al interactor validar un token hash especificado.
     * Muestra un indicador de carga mientras espera la respuesta del interactor.
     * Una vez que se recibe la respuesta, se procesa para determinar si el token es válido o no.
     * Luego, se llama al método de la vista correspondiente para manejar el resultado de la validación.
     */
    @Override
    public void validateToken(String tokenHash, ResponseData responseData){
       try {
           view.showMessages().showLoader("");
           homeInteractor.retriveValidateToken(tokenHash, new HomeContract.CallbackParams() {
               @Override
               public void onResponse(ArrayList<ResponseData> response) {
                   view.validateToken(response.get(0).getS_1().equalsIgnoreCase("1"), responseData);
               }

               @Override
               public Context getContextClass() {
                   return view.getContextClass();
               }

               @Override
               public ShowMessages showMessages() {
                   return view.showMessages();
               }
           });
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    /**
     * Realiza un cierre de sesión mediante una solicitud al interactor y maneja la respuesta.
     * Este método solicita al interactor realizar un cierre de sesión.
     * Muestra un indicador de carga mientras espera la respuesta del interactor.
     * Una vez que se recibe la respuesta, se procesa para determinar si el cierre de sesión fue exitoso o no.
     * Luego, se llama al método de la vista correspondiente para manejar el resultado del cierre de sesión.
     */
    @Override
    public void logout() {
        try {
            view.showMessages().showLoader("");
            homeInteractor.retriveLogout(new HomeContract.CallbackParams() {
                @Override
                public void onResponse(ArrayList<ResponseData> response) {
                    view.logout(response.get(0).getS_1().equalsIgnoreCase("1"));
                }

                @Override
                public Context getContextClass() {
                    return view.getContextClass();
                }

                @Override
                public ShowMessages showMessages() {
                    return view.showMessages();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
