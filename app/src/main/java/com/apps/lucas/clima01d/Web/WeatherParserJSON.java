package com.apps.lucas.clima01d.Web;

import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.Modelo.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Lucas on 22/02/2017.
 */

public class WeatherParserJSON {

    public Place Parser(String data) {
        Weather weather = new Weather();
        Place place = new Place();

        try {
            JSONObject jo = new JSONObject(data);
            JSONObject joMain = jo.getJSONObject("main");
            JSONObject joSys = jo.getJSONObject("sys");
            JSONObject joCoord = jo.getJSONObject("coord");
            JSONArray jaWeather = jo.getJSONArray("weather");
            JSONObject joWeather = jaWeather.getJSONObject(0);

            place.setNomeCidade(jo.getString("name"));

            //main
            weather.setTemperatura((int)joMain.getDouble("temp"));
            weather.setTempMin((int)joMain.getDouble("temp_min"));
            weather.setTempMax((int)joMain.getDouble("temp_max"));
            weather.setPressao(joMain.getInt("pressure"));
            weather.setUmidade(joMain.getInt("humidity"));

            //weather
            weather.setTituloClima(joWeather.getString("main"));
            weather.setDescClima(joWeather.getString("description"));
            weather.setIconClima(joWeather.getString("icon"));

            //sys
            place.setNomePais(joSys.getString("country"));
            place.setSunrise(joSys.getInt("sunrise"));
            place.setSunset(joSys.getInt("sunset"));

            //coord
            place.setLat(joCoord.getDouble("lat"));
            place.setLng(joCoord.getDouble("lon"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        place.setWeather(weather);

        return place;
    }

}
