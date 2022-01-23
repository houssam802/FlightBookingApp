package com.example.flight_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.controlers.Airline_table;
import com.example.moduls.Airport;
import com.example.moduls.Flight;

import java.util.List;

import lombok.NonNull;

public class FlightInfoAdapter extends RecyclerView.Adapter<FlightInfoAdapter.FlightInfoHolder>  {
    private List<Flight> flightsItems;
    private Context moncontext;
    private Airline_table airline_table;

    FlightInfoAdapter(List<Flight> flights, Context context) {
        this.flightsItems = flights;
        this.moncontext = context;
        this.airline_table = new Airline_table(context);
        this.airline_table.createDatabase();
        this.airline_table.open();
    }

    @NonNull
    @Override
    public FlightInfoAdapter.FlightInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlightInfoAdapter.FlightInfoHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.flight_info,
                        parent,
                        false
                )
        );
    }

    @Override
    @SuppressLint("Range")
    public void onBindViewHolder(@NonNull FlightInfoHolder holder, int position) {
        holder.compagnie.setText(flightsItems.get(position).getCompany() + " " + flightsItems.get(position).getRegistration());
        holder.heure_dep.setText(flightsItems.get(position).getHeure_dep());
        holder.icao_dep.setText(flightsItems.get(position).getIcao_dep());
        holder.heure_arr.setText( flightsItems.get(position).getHeure_arr()!=null ? flightsItems.get(position).getHeure_arr() : "00:00");
        holder.icao_arr.setText(flightsItems.get(position).getIcao_arr());
        holder.jour_dep.setText("JJ Month, " + flightsItems.get(position).getJour());
        String jour_arr = flightsItems.get(position).getJour_arr()!=null ? flightsItems.get(position).getJour_arr() : "Day";
        holder.jour_arr.setText("JJ Month, " + jour_arr);
        Cursor cur = this.airline_table.getAirlineShortcut(flightsItems.get(position).getCompany());
        if( cur.moveToFirst() ){
            String col1 = cur.getString(cur.getColumnIndex("Colonne1"));
            String col2 = cur.getString(cur.getColumnIndex("Colonne2"));
            String shortcut = col2!=null ? col2 : col1;
            Resources res = moncontext.getResources();
            int resId = res.getIdentifier( shortcut!=null ? shortcut.toLowerCase() : "notfound", "drawable", moncontext.getPackageName());
            if(resId != 0){
                holder.logo.setImageResource(resId);
            } else {
                holder.logo.setImageResource(res.getIdentifier("notfound", "drawable", moncontext.getPackageName()));
            }
        }
        Airport airportdep = new Airport(moncontext, flightsItems.get(position).getIcao_dep());
        holder.dep.setText(airportdep.getCity());
        Airport airportarr = new Airport(moncontext, flightsItems.get(position).getIcao_arr());
        holder.arr.setText(airportarr.getCity());
        cur.close();
    }

    @Override
    public int getItemCount() {
        return flightsItems.size();
    }

    class FlightInfoHolder extends RecyclerView.ViewHolder{

        private TextView compagnie, heure_dep, icao_dep, heure_arr, icao_arr, dep, jour_dep, arr, jour_arr;
        private ImageView logo;

        public FlightInfoHolder(@NonNull View itemView){
            super(itemView);
            compagnie = itemView.findViewById(R.id.compagnie);
            heure_dep= itemView.findViewById(R.id.heure_dep);
            heure_arr= itemView.findViewById(R.id.heure_arr);
            icao_dep= itemView.findViewById(R.id.icao_dep);
            icao_arr= itemView.findViewById(R.id.icao_arr);
            dep = itemView.findViewById(R.id.dep);
            jour_dep = itemView.findViewById(R.id.jour_dep);
            arr = itemView.findViewById(R.id.arr);
            jour_arr = itemView.findViewById(R.id.jour_arr);
            logo = itemView.findViewById(R.id.logo);
        }
    }

}
