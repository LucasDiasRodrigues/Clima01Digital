package com.apps.lucas.clima01d.Web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Lucas on 03/06/2015.
 */
public class HttpConnection {

    private static final String OPEN_WEATHER_MAP_API = "cb3aa28b4c5be5eeec0f37d64e49363b";

    public static String getWeatherDataWeb(String url) {
        String answer = "";

        try {
            URL endereco = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) endereco.openConnection();

            conn.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            conn.setRequestMethod("GET");
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(10 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            /*/Enviar
            String urlParameters =
                    "method=" + URLEncoder.encode("", "UTF-8") +
                            "&json=" + URLEncoder.encode("", "UTF-8");

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();*/



            //Receber
            int resposta = conn.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                answer = bytesToString(is);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return answer;
    }

    public static String bytesToString(InputStream is) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bigBuffer.write(buffer, 0, bytesLidos);
        }
        return new String(bigBuffer.toByteArray(), "UTF-8");
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
