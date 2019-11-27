package com.example.medicheck.data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicheck.R;

import java.util.List;

import static com.example.medicheck.MainActivity.reyclerView;
import static com.example.medicheck.MainActivity.mAdapter;

public class AvisosAdapter extends RecyclerView.Adapter<AvisosAdapter.ViewHolder> {

    private List<Avisos> avisosList;
    private View.OnClickListener listener;
    private Context callerActivity;
    private AvisosRepository repo;

    public AvisosAdapter(Context context, AvisosRepository repo) {
        this.avisosList = repo.obtenerDatos();
        this.callerActivity = context;
        this.repo = repo;
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
        final Avisos aviso = avisosList.get(position);
        if (aviso.getFarmaco().equals("Ibuprofeno")) {
            holder.image.setImageResource(R.drawable.ibuprofeno);
        } else if (aviso.getFarmaco().equals("Omeprazol")) {
            holder.image.setImageResource(R.drawable.omeprazol);
        } else if (aviso.getFarmaco().equals("Paracetamol")) {
            holder.image.setImageResource(R.drawable.paracetamol);
        } else if (aviso.getFarmaco().equals("Ramipril")) {
            holder.image.setImageResource(R.drawable.ramipril);
        } else {
            holder.image.setImageResource(R.drawable.pastillas);
        }
        holder.diaHora.setText(holder.diaHora.getText() + " " + aviso.getAñoMesDia());
        holder.farmaco.setText(holder.farmaco.getText() + " " + aviso.getFarmaco());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(callerActivity);
                dialogo1.setTitle("Confirmacion");
                dialogo1.setMessage("¿Seguro de que desea borrar el aviso del farmaco " + aviso.getFarmaco() + " ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //Borrar
                        repo.delete(aviso);
                        avisosList.remove(aviso);
                        reyclerView.setHasFixedSize(true);
                        reyclerView.setLayoutManager(new LinearLayoutManager(callerActivity));
                        mAdapter = new AvisosAdapter(callerActivity, repo);
                        reyclerView.setAdapter(mAdapter);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return avisosList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView farmaco;
        private TextView diaHora;
        private ImageView image;

        public ViewHolder(View v) {
            super(v);
            farmaco = (TextView) v.findViewById(R.id.farmaco);
            diaHora = (TextView) v.findViewById(R.id.diaHora);
            image = (ImageView) v.findViewById(R.id.imageView);
        }
    }


}
