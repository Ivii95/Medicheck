package com.example.medicheck.data;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicheck.R;

import java.util.List;

public class AvisosAdapter extends RecyclerView.Adapter<AvisosAdapter.ViewHolder> {

    private List<Avisos> avisosList;

    public AvisosAdapter(List<Avisos> avisos) {
        this.avisosList = avisos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_avisos, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String farmaco = avisosList.get(position).getFarmaco();
        String dia = avisosList.get(position).getAñoMesDia().toString();
        holder.diaHora.setText(holder.diaHora.getText() + " " + dia);
        holder.farmaco.setText(holder.farmaco.getText() + " " + farmaco);

    }

    @Override
    public int getItemCount() {
        return avisosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView farmaco;
        private TextView diaHora;

        public ViewHolder(View v) {
            super(v);
            farmaco = (TextView) v.findViewById(R.id.farmaco);
            diaHora = (TextView) v.findViewById(R.id.diaHora);
        }
    }


}
