package com.example.medicheck.data;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvisosRepository {
    List<Avisos> listaAvisos;
    Database datos;

    public AvisosRepository(Context context) {
        listaAvisos = new ArrayList<>();
        Database datos = new Database(context);
        this.datos = datos;
        //datos.agregar(new Avisos(LocalDate.of(2019, 11, 11), "Paracetamol"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 11, 11), "Paracetamol"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 8, 11), "asd"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 12, 11), "omperazol"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 9, 11), "qwerty"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 9, 11), "aaaaaa"));
        guardarDatos();
        int mesActual = LocalDate.now().getMonthValue();
        int diaActual=  LocalDate.now().getDayOfMonth();
        int borrados=0;
        for (int i = 0; i < listaAvisos.size(); i++) {
            int mes = listaAvisos.get(i).getAñoMesDia().getMonthValue();
            int dia = listaAvisos.get(i).getAñoMesDia().getDayOfMonth();
            if (mes < mesActual - 1) {
                borrados++;
                Toast.makeText(context,"Total borrados por antiguedad: "+borrados, Toast.LENGTH_SHORT).show();
                listaAvisos.remove(i);
                i--;
            } else if (mes == 11 && mesActual == 1) {
                borrados++;
                Toast.makeText(context, "Total borrados por antiguedad: "+borrados, Toast.LENGTH_SHORT).show();
                listaAvisos.remove(i);
                i--;
            } else if (mes == 12 && mesActual == 2) {
                borrados++;
                Toast.makeText(context, "Total borrados por antiguedad: "+borrados, Toast.LENGTH_SHORT).show();
                listaAvisos.remove(i);
                i--;
            }else if (mes ==mesActual && dia==diaActual){

            }
        }
    }


    public void guardarDatos() {
        for (Avisos avi : listaAvisos) {
            datos.agregar(avi);
        }
    }

    public List<Avisos> obtenerDatosBD() {
        return datos.obtener();
    }

    public List<Avisos> obtenerDatos() {
        return listaAvisos;
    }

    public void insert(Avisos aviso) {
        boolean esta = false;
        for (Avisos avi : listaAvisos) {
            if (aviso.farmaco.equals(avi.farmaco) && aviso.añoMesDia.equals(avi.añoMesDia)) {
                esta = true;
            }
        }
        if (!esta) {
            listaAvisos.add(0, aviso);
            datos.agregar(aviso);
        }

        //listaAvisos=obtenerDatosBD();
    }
}