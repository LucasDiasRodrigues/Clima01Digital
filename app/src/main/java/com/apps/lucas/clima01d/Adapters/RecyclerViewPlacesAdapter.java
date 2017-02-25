package com.apps.lucas.clima01d.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.lucas.clima01d.Activities.DetalhesActivity;
import com.apps.lucas.clima01d.Modelo.Place;
import com.apps.lucas.clima01d.R;

import java.util.List;

/**
 * Created by Lucas on 22/02/2017.
 */

public class RecyclerViewPlacesAdapter extends RecyclerView.Adapter<RecyclerViewPlacesAdapter.MyViewHolder> {

    private static List<Place> places;
    private static Context context;
    private LayoutInflater layoutInflater;


    public RecyclerViewPlacesAdapter(List<Place> places, Context context) {
        this.places = places;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_cidades, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtCapital.setText(places.get(position).getNomeCidade());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtCapital;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtCapital = (TextView) itemView.findViewById(R.id.txtCapital);
        }


        @Override
        public void onClick(View view) {
            Place placeSelecionado = places.get(getAdapterPosition());
            Intent intent = new Intent(context, DetalhesActivity.class);
            intent.putExtra("place", placeSelecionado);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity)context, txtCapital, "place");
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }


        }
    }


}
