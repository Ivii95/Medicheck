package com.example.medicheck.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvisosRepository {
    List<Avisos> listaAvisos;

    public AvisosRepository() {
        listaAvisos = new ArrayList<>();
        listaAvisos.add(new Avisos(LocalDate.of(2019, 11, 11), "Paracetamol"));
    }

    public List<Avisos> obtenerDatos() {
        return listaAvisos;
    }

    public void insert(Avisos aviso) {
        for (Avisos avi : listaAvisos) {
            if (aviso.farmaco.equals(avi.farmaco) && aviso.añoMesDia.equals(avi.añoMesDia)) {
            }else{
                listaAvisos.add(0, aviso);
            }
        }
    }
}

/*-Tiene contraseña?
* silencio incomodo *
+Pues eso es una buena pregunta para la que yo debería dar una buena respuesta

JAJAJAJAJAJJAJAJAJAJ*/