package com.apps.lucas.clima01d.Activities;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.lucas.clima01d.Fragments.ListaCapitaisFragment;
import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.Persistencia.Preferencias;
import com.apps.lucas.clima01d.Fragments.PreferenciasFragment;
import com.apps.lucas.clima01d.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.apps.lucas.clima01d.Web.GetWeatherTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView txtLocal;
    private TextView txtData;
    private TextView txtTemperatura;
    private TextView txtClima;
    private TextView txtUmidade;
    private TextView txtUltimaAtualizacao;
    private ImageView imgClima;
    private DrawerLayout drawer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout layoutUltAtualizacao;
    private ProgressBar progress;


    //Location
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

    //Persistencia
    private Preferencias preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencias = new Preferencias(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtLocal = (TextView) findViewById(R.id.txtLocal);
        txtData = (TextView) findViewById(R.id.txtData);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtClima = (TextView) findViewById(R.id.txtClima);
        txtUmidade = (TextView) findViewById(R.id.txtUmidade);
        txtUltimaAtualizacao = (TextView) findViewById(R.id.txtUltimaAtualizacao);
        imgClima = (ImageView) findViewById(R.id.imgClima);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizaWeather();
            }
        });
        layoutUltAtualizacao = (LinearLayout) findViewById(R.id.layoutUltAtualizacao);
        progress = (ProgressBar) findViewById(R.id.progress);

        //Instancia do GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        iniciaListaCapitais();
        iniciaNavPreferencias();
    }

    public void iniciaNavPreferencias() {
        PreferenciasFragment fragmentPreferencias = new PreferenciasFragment();
        android.support.v4.app.FragmentTransaction transactionPreferencias = getSupportFragmentManager().beginTransaction();
        transactionPreferencias.replace(R.id.frameNavLayout, fragmentPreferencias);
        transactionPreferencias.commit();
    }

    public void iniciaListaCapitais() {
        ListaCapitaisFragment fragmentListaCapitais = new ListaCapitaisFragment();
        android.support.v4.app.FragmentTransaction transactionListaCapitais = getSupportFragmentManager().beginTransaction();
        transactionListaCapitais.replace(R.id.frameLayout, fragmentListaCapitais);
        transactionListaCapitais.commit();

        //Volta o scroll para o topo
        txtTemperatura.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_prefs) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    //Solicitar clima a API
    public void getWeather(Location location) {
        Place place = new Place();
        place.setLat(location.getLatitude());
        place.setLng(location.getLongitude());
        GetWeatherTask task = new GetWeatherTask(place, this, false, preferencias.getUnidMedida());
        task.execute();
    }

    public void atualizaWeather() {
        if (mLastLocation != null) {
            getWeather(mLastLocation);
        }
    }

    public void PreencherCampos(Place place) {

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("pt", "BR"));
        txtData.setText(dateFormat.format(calendar.getTime()));

        txtLocal.setText(place.getNomeCidade());
        txtTemperatura.setText(place.getWeather().getTemperatura() + "º");
        txtClima.setText(place.getWeather().getDescClima());
        txtUmidade.setText(place.getWeather().getUmidade() + "%");

        if (place.getWeather().getIconClima() != null)
            setWeatherIcon(place);

        mSwipeRefreshLayout.setRefreshing(false);
        layoutUltAtualizacao.setVisibility(View.GONE);

        progress.setVisibility(View.GONE);

        salvarClimaMeuLocal(place);
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

    public void preencherUltAtualizacao(){

        Place place = preferencias.getClimaMeuLocal();

        if(place != null) {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("pt", "BR"));
            txtData.setText(dateFormat.format(calendar.getTime()));

            txtLocal.setText(place.getNomeCidade());
            txtTemperatura.setText(place.getWeather().getTemperatura() + "º");
            txtClima.setText(place.getWeather().getDescClima());
            txtUmidade.setText(place.getWeather().getUmidade() + "%");

            if (place.getWeather().getIconClima() != null)
                setWeatherIcon(place);

            mSwipeRefreshLayout.setRefreshing(false);

            layoutUltAtualizacao.setVisibility(View.VISIBLE);
            String aux = preferencias.getUltimaAtualizacao();
            progress.setVisibility(View.GONE);
            if(aux != null){
                txtUltimaAtualizacao.setText(preferencias.getUltimaAtualizacao());
            }
        } else {
            Toast.makeText(this, R.string.semResposta, Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }

    //Salva o clima nas preferencias
    public void salvarClimaMeuLocal(Place place){
        Preferencias preferencias = new Preferencias(this);
        preferencias.salvarClimaMeuLocal(place);
    }


    //Metodos GoogleApiClient
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permissao para versao de android >= 6.0
            checkPermission();

        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Log.i("LOcalizacao", mLastLocation.toString());
                getWeather(mLastLocation);
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Location", "Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Location", "Failed");
    }


    //Permissoes para versoes do android >= 6.0
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // Se a requisicao
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  startLocationUpdates();
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        Log.i("LOcalizacao", mLastLocation.toString());
                        getWeather(mLastLocation);
                    }
                } else {
                    new android.app.AlertDialog.Builder(this).setTitle(getResources().getString(R.string.permissaoNegada))
                            .setMessage(getString(R.string.permissaoNegadaMsg))
                            .setPositiveButton(getString(R.string.permitir), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermission();
                                }
                            })
                            .setNegativeButton(getString(R.string.negar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        }

    }

    public void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }
}
