package com.apps.lucas.clima01d.Activities;

import android.content.Intent;
import android.location.Location;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.Persistencia.Preferencias;
import com.apps.lucas.clima01d.R;
import com.apps.lucas.clima01d.Web.GetWeatherTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetalhesActivity extends AppCompatActivity {

    private TextView txtLocal;
    private TextView txtData;
    private TextView txtTemperatura;
    private TextView txtClima;
    private TextView txtUmidade;
    private ImageView imgClima;
    private ProgressBar progressBar;

    private Place place;

    private Preferencias preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        place = (Place) intent.getSerializableExtra("place");

        txtLocal = (TextView) findViewById(R.id.txtLocal);
        txtData = (TextView) findViewById(R.id.txtData);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtClima = (TextView) findViewById(R.id.txtClima);
        txtUmidade = (TextView) findViewById(R.id.txtUmidade);
        imgClima = (ImageView) findViewById(R.id.imgClima);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("pt", "BR"));
        txtData.setText(dateFormat.format(calendar.getTime()));

        txtLocal.setText(place.getNomeCidade());

        getWeather(place);

    }

    //Solicitar clima a API
    public void getWeather(Place place) {
        preferencias = new Preferencias(this);
        GetWeatherTask task = new GetWeatherTask(place, this, true, preferencias.getUnidMedida());
        task.execute();
    }

    public void PreencherCampos(Place place) {

        txtTemperatura.setText(place.getWeather().getTemperatura() + "º");
        txtClima.setText(place.getWeather().getDescClima());
        txtUmidade.setText(place.getWeather().getUmidade() + "%");

        progressBar.setVisibility(View.GONE);

        if (place.getWeather().getIconClima() != null)
            setWeatherIcon(place);
    }

    public void setWeatherIcon(Place place) {
        //Icon
        Log.i("Icon == ", place.getWeather().getIconClima());
        switch (place.getWeather().getIconClima()) {
            //Dia
            case "01d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clear));
                break;
            case "02d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_few_clouds));
                break;
            case "03d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clouds));
                break;
            case "04d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clouds));
                break;
            case "09d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_showers_scattered_day));
                break;
            case "10d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_rain_day));
                break;
            case "11d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_storm_day));
                break;
            case "13d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_snow));
                break;
            case "50d":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_mist));
                break;

            //Noite
            case "01n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clear_night));
                break;
            case "02n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_few_clouds_night));
                break;
            case "03n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clouds_night));
                break;
            case "04n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_clouds_night));
                break;
            case "09n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_showers_scattered_night));
                break;
            case "10n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_rain_night));
                break;
            case "11n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_storm_night));
                break;
            case "13n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_snow));
                break;
            case "50n":
                imgClima.setImageDrawable(getResources().getDrawable(R.drawable.weather_mist));
                break;
        }


    }

    public void semResposta(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else  if (id == R.id.action_refresh) {
            getWeather(place);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
