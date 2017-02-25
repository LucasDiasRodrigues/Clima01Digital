package com.apps.lucas.clima01d.Persistencia;

import android.content.Context;
import android.content.SharedPreferences;

import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.Modelo.Weather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Lucas on 24/02/2017.
 */

public class Preferencias {


    private Place meuLocal;
    private Context context;
    private SharedPreferences prefs;

    public Preferencias(Context context){
        this.context = context;
    }


    public void salvarClimaMeuLocal(Place place){
        meuLocal = place;
        prefs = context.getSharedPreferences("clima01d", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("hasPlace",true);
        editor.putInt("temp", meuLocal.getWeather().getTemperatura());
        editor.putInt("umidade", meuLocal.getWeather().getUmidade());
        editor.putString("cidade", meuLocal.getNomeCidade());
        editor.putString("descClima", meuLocal.getWeather().getDescClima());
        editor.putString("icon", meuLocal.getWeather().getIconClima());

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", new Locale("pt", "BR"));
        editor.putString("ultimaAtualizacao", dateFormat.format(calendar.getTime()));
        editor.commit();
    }

    public Place getClimaMeuLocal(){
        prefs = context.getSharedPreferences("clima01d", Context.MODE_PRIVATE);
        if(prefs.getBoolean("hasPlace",false)){
            meuLocal = new Place();
            meuLocal.setNomeCidade(prefs.getString("cidade",""));
            Weather weather = new Weather();
            weather.setTemperatura(prefs.getInt("temp",0));
            weather.setUmidade(prefs.getInt("umidade",0));
            weather.setIconClima(prefs.getString("icon",""));
            weather.setDescClima(prefs.getString("descClima",""));
            meuLocal.setNomeCidade(prefs.getString("cidade",""));
            meuLocal.setWeather(weather);
            return meuLocal;
        } else {
            return null;
        }
    }

    public String getUltimaAtualizacao(){
        prefs = context.getSharedPreferences("clima01d", Context.MODE_PRIVATE);
        if(prefs.getBoolean("hasPlace",false)){
            return prefs.getString("ultimaAtualizacao","");
        } else {
            return null;
        }
    }


    //1 para Celsius - 2 para Fahrenheit
    public void salvarUnidMedida(int unidadeMedida){
        prefs = context.getSharedPreferences("clima01d", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("unidadeMedida", unidadeMedida);
        editor.commit();
    }

    public int getUnidMedida(){
        prefs = context.getSharedPreferences("clima01d", Context.MODE_PRIVATE);
        return prefs.getInt("unidadeMedida",1);
    }











}
