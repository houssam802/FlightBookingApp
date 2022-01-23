package com.example.flight_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.moduls.Airport;
import com.example.moduls.Flights;

public class ActivityFlightInfo extends AppCompatActivity {

    Bundle b;
    String icao_dep, icao_arr, iata_dep, iata_arr, registration, num_vol, other_airport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.flightsRecyclerView);
        TextView textView = (TextView) findViewById(R.id.trip);
        TextView textView2 = (TextView) findViewById(R.id.valprix);

        b = getIntent().getExtras();

        if(b != null){
            icao_dep = b.getString("icao_dep");
            icao_arr = b.getString("icao_arr");
            iata_dep = b.getString("iata_dep");
            iata_arr = b.getString("iata_arr");
            registration = b.getString("registration");
            num_vol = b.getString("numero_vol");
            // Au cas où il y a une escale.
            other_airport = b.getString("other_airport");
        }
        textView2.setText("N/A €EUR");

        Airport airportdep = new Airport(this, icao_dep);
        Airport airportarr = new Airport(this, icao_arr);

        textView.setText(airportdep.getCity() + "  -  " + airportarr.getCity());

        getSupportActionBar().setTitle(iata_dep + " - " + iata_arr);

        Flights flights = new Flights(this, icao_dep, icao_arr, registration, num_vol, other_airport);
        recyclerView.setAdapter(new FlightInfoAdapter(flights.flights, this));
    }


}