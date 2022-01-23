package com.example.moduls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import com.example.controlers.Flight_table;

public class Flights {
    public ArrayList<Flight> flights = new ArrayList<>();

    @SuppressLint("Range")
    public Flights(Context context){
        Flight_table flight_table = new Flight_table(context);
        flight_table.createDatabase();
        flight_table.open();
        Cursor cur = flight_table.getTestData();
        Flight flight;
        while( cur.moveToNext() ){
            flight = new Flight();
            flight.setAvion(cur.getString(cur.getColumnIndex("Avion")));
            flight.setCompany(cur.getString(cur.getColumnIndex("Compagnies")));
            flight.setHeure_dep(cur.getString(cur.getColumnIndex("Heure_DEP")));
            flight.setJour(cur.getString(cur.getColumnIndex("Jours")));
            flight.setIcao_dep(cur.getString(cur.getColumnIndex("ICAO_DEP")));
            flight.setIcao_arr(cur.getString(cur.getColumnIndex("ICAO_ARR")));
            flight.setNum_vol(cur.getString(cur.getColumnIndex("Numero_Vol")));
            flight.setRegistration(cur.getString(cur.getColumnIndex("Registration")));
            flights.add(flight);
        }
        cur.close();
        flight_table.close();
    }

    @SuppressLint("Range")
    public Flights(Context context, String icao_dep, String icao_arr){
        Flight_table flight_table = new Flight_table(context);
        flight_table.createDatabase();
        flight_table.open();
        Cursor cur = flight_table.getFlightByICAO(icao_dep, icao_arr);
        Flight flight;
        while( cur.moveToNext() ){
            if( cur.getString(cur.getColumnIndex("dep")).contains(icao_dep) ){
                flight = new Flight();
                flight.setAvion(cur.getString(cur.getColumnIndex("Avion")));
                flight.setCompany(cur.getString(cur.getColumnIndex("Compagnies")));
                flight.setHeure_dep(cur.getString(cur.getColumnIndex("HH")));
                flight.setJour(cur.getString(cur.getColumnIndex("JJ")));
                flight.setIcao_dep(cur.getString(cur.getColumnIndex("dep")));
                flight.setIcao_arr(icao_arr);
                flight.setNum_vol(cur.getString(cur.getColumnIndex("num")));
                flight.setRegistration(cur.getString(cur.getColumnIndex("reg")));
                String other_airport = cur.getString(cur.getColumnIndex("arr"));
                flight.setOther_airport(other_airport);
                flights.add(flight);
            }
        }
        cur.close();
        flight_table.close();
    }

    @SuppressLint("Range")
    public Flights(Context context, String icao_dep, String icao_arr, String reg, String num, String other_airport ){
        Flight_table flight_table = new Flight_table(context);
        flight_table.createDatabase();
        flight_table.open();
        Cursor cur = flight_table.getFlightInfo(icao_dep, icao_arr, reg, num, other_airport);
        Boolean thereIsAnotherAirport = !other_airport.contains(icao_arr);
        Flight flight;
        if( cur != null){

            while( cur.moveToNext() ){
                if( !cur.getString(cur.getColumnIndex("ICAO_DEP")).contains(icao_dep) ) {
                    flight = new Flight();
                    flight.setAvion(cur.getString(cur.getColumnIndex("Avion")));
                    flight.setCompany(cur.getString(cur.getColumnIndex("Compagnies")));
                    flight.setHeure_dep(cur.getString(cur.getColumnIndex("HH")));
                    flight.setHeure_arr(cur.getString(cur.getColumnIndex("Heure_DEP")));
                    flight.setJour(cur.getString(cur.getColumnIndex("JJ")));
                    flight.setJour_arr(cur.getString(cur.getColumnIndex("Jours")));
                    flight.setIcao_dep(cur.getString(cur.getColumnIndex("dep")));
                    flight.setIcao_arr(cur.getString(cur.getColumnIndex("arr")));
                    flight.setNum_vol(cur.getString(cur.getColumnIndex("num")));
                    flight.setRegistration(cur.getString(cur.getColumnIndex("reg")));
                    flights.add(flight);
                }
                if( cur.getString(cur.getColumnIndex("ICAO_ARR")).contains(icao_arr) ) {
                    if( thereIsAnotherAirport && !cur.getString(cur.getColumnIndex("ICAO_DEP")).contains(icao_dep) ){
                        flight = new Flight();
                        flight.setAvion(cur.getString(cur.getColumnIndex("Avion")));
                        flight.setCompany(cur.getString(cur.getColumnIndex("Compagnies")));
                        flight.setHeure_dep(cur.getString(cur.getColumnIndex("Heure_DEP")));
                        flight.setJour(cur.getString(cur.getColumnIndex("Jours")));
                        flight.setIcao_dep(cur.getString(cur.getColumnIndex("ICAO_DEP")));
                        flight.setIcao_arr(cur.getString(cur.getColumnIndex("ICAO_ARR")));
                        flight.setNum_vol(cur.getString(cur.getColumnIndex("Numero_Vol")));
                        flight.setRegistration(cur.getString(cur.getColumnIndex("Registration")));
                        flights.add(flight);
                    }
                    if( !thereIsAnotherAirport ){
                        flight = new Flight();
                        flight.setAvion(cur.getString(cur.getColumnIndex("Avion")));
                        flight.setCompany(cur.getString(cur.getColumnIndex("Compagnies")));
                        flight.setHeure_dep(cur.getString(cur.getColumnIndex("Heure_DEP")));
                        flight.setJour(cur.getString(cur.getColumnIndex("Jours")));
                        flight.setIcao_dep(cur.getString(cur.getColumnIndex("ICAO_DEP")));
                        flight.setIcao_arr(cur.getString(cur.getColumnIndex("ICAO_ARR")));
                        flight.setNum_vol(cur.getString(cur.getColumnIndex("Numero_Vol")));
                        flight.setRegistration(cur.getString(cur.getColumnIndex("Registration")));
                        flights.add(flight);
                    }
                }

            }
            cur.close();
        }
        flight_table.close();
    }

}
