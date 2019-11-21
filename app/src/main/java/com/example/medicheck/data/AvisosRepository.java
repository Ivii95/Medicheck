package com.example.medicheck.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvisosRepository {
    List listaAvisos = null;

    public List<Avisos> obtenerDatos() {
        listaAvisos = new ArrayList<String>();
        listaAvisos.add(new Avisos(
                LocalDateTime.of(LocalDate.of(2019, 11, 11), LocalTime.of(11, 30))
                , "Paracetamol"));
        return listaAvisos;
    }

}
