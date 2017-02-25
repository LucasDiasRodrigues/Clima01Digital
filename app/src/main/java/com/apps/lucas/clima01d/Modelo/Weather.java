package com.apps.lucas.clima01d.Modelo;

import java.io.Serializable;

/**
 * Created by Lucas on 22/02/2017.
 */

public class Weather implements Serializable{

    private int temperatura;
    private int tempMax;
    private int tempMin;
    private int pressao;
    private int umidade;

    private String tituloClima;
    private String descClima;
    private String iconClima;


    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getPressao() {
        return pressao;
    }

    public void setPressao(int pressao) {
        this.pressao = pressao;
    }

    public int getUmidade() {
        return umidade;
    }

    public void setUmidade(int umidade) {
        this.umidade = umidade;
    }

    public String getTituloClima() {
        return tituloClima;
    }

    public void setTituloClima(String tituloClima) {
        this.tituloClima = tituloClima;
    }

    public String getDescClima() {
        return descClima;
    }

    public void setDescClima(String descClima) {
        this.descClima = descClima;
    }

    public String getIconClima() {
        return iconClima;
    }

    public void setIconClima(String iconClima) {
        this.iconClima = iconClima;
    }
}
