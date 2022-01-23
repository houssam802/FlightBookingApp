package com.example.flight_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.controlers.Airline_table;
import com.example.moduls.Flight;

public class FlightsItemAdapter extends RecyclerView.Adapter<FlightsItemAdapter.FlightsItemHolder> {

    private List<Flight> flightsItems;
    private Context moncontext;
    private Airline_table airline_table;
    private Intent myIntent;
    private String iata_dep, iata_arr;

    FlightsItemAdapter(List<Flight> flightsItems, Context context, String iata_dep, String iata_arr) {
        this.flightsItems = flightsItems;
        this.moncontext = context;
        this.airline_table = new Airline_table(context);
        this.airline_table.createDatabase();
        this.airline_table.open();
        this.myIntent = new Intent(moncontext, ActivityFlightInfo.class);
        this.iata_dep = iata_dep;
        this.iata_arr = iata_arr;
    }

    @NonNull
    @Override
    /*
         RecyclerView appelle cette méthode chaque fois qu'il doit créer un nouveau ViewHolder.
         La méthode crée et initialise le ViewHolder et sa vue associée, mais ne remplit pas le
         contenu de la vue, le ViewHolder n'a pas encore été lié à des données spécifiques.
    */
    public FlightsItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FlightsItemHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.flights_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    @SuppressLint("Range")
    /*
        RecyclerView appelle cette méthode pour associer un ViewHolder à des données.
        La méthode récupère les données appropriées et les utilise pour remplir la disposition
        du ViewHolder. Par exemple, si le RecyclerView affiche une liste de noms, la méthode
        peut trouver le nom approprié dans la liste et remplir le widget TextView du View holder.

    */
    public void onBindViewHolder(@NonNull FlightsItemHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText("N/A €EUR");
        holder.heure_dep.setText(flightsItems.get(position).getHeure_dep());
        holder.icao_dep.setText(flightsItems.get(position).getIcao_dep());
        holder.heure_arr.setText("00:00");
        holder.icao_arr.setText(flightsItems.get(position).getIcao_arr());
        // On cherche a afficher le logo de compagnie chargé de vol, dans la table des compagnies.
        Cursor cur = this.airline_table.getAirlineShortcut(flightsItems.get(position).getCompany());
        // On n'a pas tous les logos des compagnies aériennes.
        // C'est pour cela qu'il faut vérifier l'existence de logo avant l'afficher
        // S'il n'existe, on affiche un logo indiquant que rien trouvé.
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
        cur.close();
        holder.flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent.putExtra("icao_dep", flightsItems.get(position).getIcao_dep());
                myIntent.putExtra("icao_arr", flightsItems.get(position).getIcao_arr());
                myIntent.putExtra("iata_dep", iata_dep);
                myIntent.putExtra("iata_arr", iata_arr);
                myIntent.putExtra("registration", flightsItems.get(position).getRegistration());
                myIntent.putExtra("numero_vol", flightsItems.get(position).getNum_vol());
                myIntent.putExtra("other_airport", flightsItems.get(position).getOther_airport());
                moncontext.startActivity(myIntent);
            }
        });
    }

    @Override
    /*
        RecyclerView appelle cette méthode pour obtenir la taille de l'ensemble de données.
        RecyclerView l'utilise pour déterminer quand il n'y a plus d'éléments à afficher.
    */
    public int getItemCount() {
        return flightsItems.size();
    }

    class FlightsItemHolder extends RecyclerView.ViewHolder{

        private TextView title, heure_dep, icao_dep, heure_arr, icao_arr;
        private ImageView logo;
        private CardView flight;

        FlightsItemHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            heure_dep= itemView.findViewById(R.id.heure_dep);
            heure_arr= itemView.findViewById(R.id.heure_arr);
            icao_dep= itemView.findViewById(R.id.icao_dep);
            icao_arr= itemView.findViewById(R.id.icao_arr);
            logo = itemView.findViewById(R.id.logo);
            flight = itemView.findViewById(R.id.flight);
        }
    }

}
