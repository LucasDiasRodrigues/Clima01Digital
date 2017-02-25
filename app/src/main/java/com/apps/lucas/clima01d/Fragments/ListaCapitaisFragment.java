package com.apps.lucas.clima01d.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.R;
import com.apps.lucas.clima01d.Adapters.RecyclerViewPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 23/02/2017.
 */

public class ListaCapitaisFragment extends Fragment {

    private RecyclerView recyclerCapitais;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_lista_cidades, container, false);

        recyclerCapitais = (RecyclerView) fragment.findViewById(R.id.recycler_view_capitais);
        recyclerCapitais.setHasFixedSize(true);
        recyclerCapitais.setNestedScrollingEnabled(false);
        recyclerCapitais.setFocusable(false);

        preencheLista();

        return fragment;
    }


    public void preencheLista() {
        List<Place> mListCapitais = getListCapitais();

        RecyclerViewPlacesAdapter adapter = new RecyclerViewPlacesAdapter(mListCapitais, getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerCapitais.setLayoutManager(mLayoutManager);
        recyclerCapitais.setAdapter(adapter);
    }

    public List<Place> getListCapitais() {
        List<Place> mListCapitais = new ArrayList<>();

        String[] capitais = getResources().getStringArray(R.array.lista_capitais_br);
        String[] idCapital = getResources().getStringArray(R.array.lista_capitais_ID);
        for (int i = 0; i < capitais.length; i++) {
            Place place = new Place();
            place.setNomeCidade(capitais[i]);
            place.setIdCidade(idCapital[i]);
            Log.i("loc", "id = " + place.getIdCidade());
            mListCapitais.add(place);
        }

        return mListCapitais;
    }

}
