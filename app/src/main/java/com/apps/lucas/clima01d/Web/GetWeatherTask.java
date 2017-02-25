package com.apps.lucas.clima01d.Web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.apps.lucas.clima01d.Activities.DetalhesActivity;
import com.apps.lucas.clima01d.Activities.MainActivity;
import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.R;

/**
 * Created by Lucas on 22/02/2017.
 */

public class GetWeatherTask extends AsyncTask<Object, Object, Object> {
    private static String WEATHER_URL;
    private Place place;
    private Activity activity;
    private boolean id;

    public GetWeatherTask(Place place, Activity activity, boolean hasId, int unidadeMedida) {
        this.place = place;
        this.activity = activity;
        if (hasId)
            id = true;
        if (unidadeMedida == 1)
            WEATHER_URL = activity.getResources().getString(R.string.weather_URL_Cel);
        else if (unidadeMedida == 2)
            WEATHER_URL = activity.getResources().getString(R.string.weather_URL_Fah);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... objects) {

        if (HttpConnection.isNetworkAvailable(activity)) {

            String url;
            if (id) {
                url = WEATHER_URL + "id=" + place.getIdCidade();
                Log.i("url = ", url);
            } else {
                url = WEATHER_URL + "lat=" + place.getLat() + "&lon=" + place.getLng();
                Log.i("url = ", url);
            }

            String answer = HttpConnection.getWeatherDataWeb(url);

            WeatherParserJSON json = new WeatherParserJSON();
            Place place = json.Parser(answer);
            Log.i("Task", answer);

            return place;

        } else {
            //sem conexao
            String erro = "semconexao";
            Log.i("CONECXAO", "DESCONECTADO");
            return erro;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (o.equals("semconexao")) {
            Toast.makeText(activity, R.string.conexaoIndosponivel, Toast.LENGTH_LONG).show();
        }else if (o instanceof Place){
            if (((Place) o).getWeather().getTemperatura() != 0 && activity instanceof MainActivity) {
                ((MainActivity) activity).PreencherCampos((Place) o);
            } else if (((Place) o).getWeather().getTemperatura() != 0 && activity instanceof DetalhesActivity) {
                ((DetalhesActivity) activity).PreencherCampos((Place) o);
            } else if (((Place) o).getWeather().getTemperatura() == 0 && activity instanceof MainActivity){
                ((MainActivity) activity).preencherUltAtualizacao();
            } else if (((Place) o).getWeather().getTemperatura() == 0 && activity instanceof DetalhesActivity){
                Toast.makeText(activity, R.string.semResposta, Toast.LENGTH_SHORT).show();
                ((DetalhesActivity) activity).semResposta();
            } else {
                Toast.makeText(activity, R.string.semResposta, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
