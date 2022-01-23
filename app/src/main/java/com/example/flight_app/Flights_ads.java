package com.example.flight_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moduls.Flights;

public class Flights_ads extends AppCompatActivity {

    String icao_dep, icao_arr, iata_dep, iata_arr, jour_dep, jour_arr;

    LinearLayout nothingfound;
    RecyclerView flightsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights_ads);

        SharedPreferences sharedpreferences = getSharedPreferences("search", Context.MODE_PRIVATE);

        icao_dep = sharedpreferences.getString("origine_icao", "");
        icao_arr = sharedpreferences.getString("destination_icao", "");
        iata_arr = sharedpreferences.getString("origine_code", "");
        iata_dep = sharedpreferences.getString("destination_code", "");

        getSupportActionBar().setTitle(iata_dep + " - " + iata_arr);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        loadAvailableFlights();

    }

    @SuppressWarnings("Range")
    public void loadAvailableFlights(){
        flightsRecyclerView = ( RecyclerView ) findViewById(R.id.flightsRecyclerView);
        nothingfound = ( LinearLayout ) findViewById(R.id.nothingfound);
        Flights flights = new Flights(this, icao_dep, icao_arr);
        // Vérifier s'il y a un vol trouvé ou non.
        if( flights.flights.size() != 0 ){
            flightsRecyclerView.setVisibility(View.VISIBLE);
            nothingfound.setVisibility(View.GONE);
            flightsRecyclerView.setAdapter(new FlightsItemAdapter(flights.flights, this, iata_dep, iata_arr));
        } else {
            nothingfound.setVisibility(View.VISIBLE);
            flightsRecyclerView.setVisibility(View.GONE);
        }
    }

    public void goBack(View v){
        finish();
    }

}