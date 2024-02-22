package com.fjd.creditosmovil.activities.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fjd.creditosmovil.R;
import com.fjd.creditosmovil.activities.home.models.ResponseData;
import com.fjd.creditosmovil.util.contracts.ShowMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeListAdapter extends  RecyclerView.Adapter<HomeListAdapter.ViewHolder>{
    Context context;
    ArrayList<ResponseData> lista;
    ItemOnClickListener listener;
    ArrayList<ResponseData> copia;
    ShowMessages messages;
    //DAO dao;

    public HomeListAdapter(Context context, ArrayList<ResponseData> lista, ItemOnClickListener listener, ShowMessages messages) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
        this.messages = messages;
        copia = new ArrayList<>();
        copia.addAll(lista);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_data_model, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tvName.append(lista.get(i).getClientName());
        holder.tvDni.append(String.valueOf(lista.get(i).getClientDni()));
        holder.tvCreditId.append(String.valueOf(lista.get(i).getCreditId()));
        holder.tvBiometricId.append(String.valueOf(lista.get(i).getTempBiometricsId()));
        holder.tvExpiration.append(String.valueOf(lista.get(i).getExpirationDate()));
        holder.itemView.setOnClickListener(v -> listener.OnclickItem(lista.get(i)));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public interface ItemOnClickListener {
        void OnclickItem(ResponseData entity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDni, tvExpiration, tvCreditId, tvBiometricId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDni = itemView.findViewById(R.id.tv_dni);
            tvCreditId = itemView.findViewById(R.id.tv_credit_id);
            tvBiometricId = itemView.findViewById(R.id.tv_biometric_id);
            tvExpiration = itemView.findViewById(R.id.tv_expirtation);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public Boolean filter(final String buscar) {
        boolean r = false;
        if (buscar.length() == 0) {
            lista.clear();
            lista.addAll(copia);
        } else {
            lista.clear();
            List<ResponseData> collect = copia.stream().
                    filter(i -> (i.getClientName()+" "+ i.getCreditId() +" "+i.getTempBiometricsId() +" " +i.getClientDni()).toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors
                            .toList());
            lista.addAll(collect);
            r = true;
        }
        notifyDataSetChanged();
        return r;
    }
}
