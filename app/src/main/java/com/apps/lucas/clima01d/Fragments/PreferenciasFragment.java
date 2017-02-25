package com.apps.lucas.clima01d.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.apps.lucas.clima01d.Activities.MainActivity;
import com.apps.lucas.clima01d.Persistencia.Preferencias;
import com.apps.lucas.clima01d.R;

/**
 * Created by Lucas on 24/02/2017.
 */

public class PreferenciasFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rbUnidadeMedida;
    private Preferencias preferencias;
    private int unidadeMedida;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.layout_preferencias, container, false);
        rbUnidadeMedida = (RadioGroup) fragment.findViewById(R.id.radioGroupMetric);
        rbUnidadeMedida.setOnCheckedChangeListener(this);
        preferencias = new Preferencias(getActivity());
        preencheCampos();

        return fragment;
    }

    public void preencheCampos() {
        if (preferencias.getUnidMedida() == 1) {
            rbUnidadeMedida.check(R.id.radioCelsius);
            unidadeMedida = 1;
        } else if (preferencias.getUnidMedida() == 2) {
            rbUnidadeMedida.check(R.id.radioFah);
            unidadeMedida = 2;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.radioCelsius) {
            unidadeMedida = 1;
            preferencias.salvarUnidMedida(unidadeMedida);
        } else if (i == R.id.radioFah) {
            unidadeMedida = 2;
            preferencias.salvarUnidMedida(unidadeMedida);
        }

        Activity activity = getActivity();
        if(activity instanceof MainActivity){
            ((MainActivity) activity).atualizaWeather();
        }

    }
}
