package com.example.medicheck.data;

import java.time.LocalDate;
import java.time.LocalTime;

public class Avisos {
    int id;
    LocalDate añoMesDia;
    String farmaco;

    public Avisos(LocalDate añoMesDia, String farmaco) {
        this.añoMesDia = añoMesDia;
        this.farmaco = farmaco;
    }

    public Avisos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getAñoMesDia() {
        return añoMesDia;
    }

    public void setAñoMesDia(LocalDate añoMesDia) {
        this.añoMesDia = añoMesDia;
    }


    public String getFarmaco() {
        return farmaco;
    }

    public void setFarmaco(String farmaco) {
        this.farmaco = farmaco;
    }
}
