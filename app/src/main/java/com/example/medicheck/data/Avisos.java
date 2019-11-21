package com.example.medicheck.data;

import java.time.LocalDateTime;

public class Avisos {
    LocalDateTime diaHora;
    String farmaco;

    public Avisos(LocalDateTime diaHora, String farmaco) {
        this.diaHora = diaHora;
        this.farmaco = farmaco;
    }

    public Avisos() {
    }

    public LocalDateTime getDiaHora() {
        return diaHora;
    }

    public void setDiaHora(LocalDateTime diaHora) {
        this.diaHora = diaHora;
    }

    public String getFarmaco() {
        return farmaco;
    }

    public void setFarmaco(String farmaco) {
        this.farmaco = farmaco;
    }
}
