package com.example.flight_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moduls.Airport;

import java.util.List;

public class AirportsItem_adapter extends RecyclerView.Adapter<AirportsItem_adapter.AirportsItemHolder> {

    private List<Airport> airportsItems;
    static public Airport my_Airport;

    static String city,airport,iata,icao;
    static Context my_context;

    AirportsItem_adapter(Context context, List<Airport> flightsItems) {
        this.airportsItems = flightsItems;
        my_context = context;
    }

    @NonNull
    @Override
    public AirportsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AirportsItemHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_search_city,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AirportsItemHolder holder, int position) {
        holder.airport_code.setText(airportsItems.get(position).getIata());
        holder.city_name.setText(airportsItems.get(position).getCity());
        holder.airport_name.setText(airportsItems.get(position).getAirport());
        holder.city_airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout my_layout = (LinearLayout) v;
                LinearLayout v1 = (LinearLayout) my_layout.getChildAt(0);
                for (int i = 0; i < v1.getChildCount(); i++) {
                    View v1ch = v1.getChildAt(i);
                    if (v1ch instanceof TextView) {
                        my_Airport = getAirport((String) ((TextView) v1ch).getText());
                        Main_search_view.load_place_infos(my_Airport);
                        Activity activity = (Activity) my_context;
                        activity.finish();
                    }
                }

            }
        });
    }


    public Airport getAirport(String iata){
        for(int i=0;i<airportsItems.size();i++){
            if(iata==airportsItems.get(i).getIata()){
                return airportsItems.get(i);
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return airportsItems.size();
    }

    class AirportsItemHolder extends RecyclerView.ViewHolder{

        private TextView airport_code,city_name,airport_name;
        private LinearLayout city_airport;

        @SuppressLint("ResourceType")
        AirportsItemHolder(@NonNull View itemView){
            super(itemView);
            airport_code = itemView.findViewById(R.id.airport_code);
            city_name= itemView.findViewById(R.id.city_name);
            airport_name= itemView.findViewById(R.id.airport_name);
            city_airport=itemView.findViewById(R.id.city_airport);
        }
    }


}
