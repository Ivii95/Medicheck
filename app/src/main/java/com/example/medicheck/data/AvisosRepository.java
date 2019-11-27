package com.example.medicheck.data;

import android.content.Context;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.List;

public class AvisosRepository {
    public static List<Avisos> listaAvisos;
    Database datos;
    SaveTxt archivoTxt;

    public AvisosRepository(Context context) {
        datos = new Database(context, null, null, 0);
        listaAvisos = obtenerDatosBD();
        if (listaAvisos.isEmpty()) {
            //datos.updateDB();
            inicializarPorDefecto();
        } else {
            int borrados = 0;
            int mesActual = LocalDate.now().getMonthValue();
            for (int i = 0; i < listaAvisos.size(); i++) {
                int mes = listaAvisos.get(i).getAñoMesDia().getMonthValue();
                if (mes < mesActual - 1) {
                    borrados++;
                    Toast.makeText(context, "Total borrados por antiguedad: " + borrados, Toast.LENGTH_SHORT).show();
                    datos.eliminar(listaAvisos.get(i).id);
                    listaAvisos.remove(i);
                    i--;
                } else if (mes == 11 && mesActual == 1) {
                    borrados++;
                    Toast.makeText(context, "Total borrados por antiguedad: " + borrados, Toast.LENGTH_SHORT).show();
                    datos.eliminar(listaAvisos.get(i).id);
                    listaAvisos.remove(i);
                    i--;
                } else if (mes == 12 && mesActual == 2) {
                    borrados++;
                    Toast.makeText(context, "Total borrados por antiguedad: " + borrados, Toast.LENGTH_SHORT).show();
                    datos.eliminar(listaAvisos.get(i).id);
                    listaAvisos.remove(i);
                    i--;
                }
            }
        }
    }

    public String leerDatos() {
        listaAvisos = datos.obtener();
        return listaAvisos.get(0).getFarmaco();
    }

    public void inicializarPorDefecto() {
        listaAvisos.add(new Avisos(LocalDate.of(2019, 11, 20), "Paracetamol"));
        listaAvisos.add(new Avisos(LocalDate.of(2019, 12, 21), "Omeprazol"));
        for (Avisos aviso : listaAvisos) {
            datos.agregar(aviso);
        }
    }

    public void delete(Avisos aviso) {
        datos.eliminar(aviso.getId());
        listaAvisos.remove(aviso);
    }

    private List<Avisos> obtenerDatosBD() {
        return datos.obtener();
    }

    public List<Avisos> obtenerDatos() {
        return listaAvisos;
    }

    public void insert(Avisos aviso) {
        boolean esta = false;
        for (Avisos avi : listaAvisos) {
            if (aviso.farmaco.equals(avi.farmaco) && aviso.añoMesDia.equals(avi.añoMesDia) && !esta) {
                esta = true;
            }
        }
        if (!esta) {
            listaAvisos.add(0, aviso);
            datos.agregar(aviso);
        }
    }

    public static Avisos comprobarFecha() {
        Avisos aviso = null;
        int mesActual = LocalDate.now().getMonthValue();
        int diaActual = LocalDate.now().getDayOfMonth();
        for (int i = 0; i < listaAvisos.size(); i++) {
            int mes = listaAvisos.get(i).getAñoMesDia().getMonthValue();
            int dia = listaAvisos.get(i).getAñoMesDia().getDayOfMonth();
            if (dia == diaActual && mes == mesActual) {
                aviso = listaAvisos.get(i);
                listaAvisos.remove(aviso);
            }
        }
        return aviso;
    }
}