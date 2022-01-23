package com.example.moduls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import com.example.controlers.Flight_table;

public class Companies {

    public ArrayList<Airline> companies = new ArrayList<>();

    @SuppressLint("Range")
    public Companies(Context context){
        Flight_table flight_table = new Flight_table(context);
        flight_table.createDatabase();
        flight_table.open();
        Cursor cur = flight_table.getFirstTenAvailableCompanies();
        Airline airline;
        while(cur.moveToNext()){
            airline = new Airline(context, cur.getString(cur.getColumnIndex("Compagnies")));
            companies.add(airline);
        }
        cur.close();
    }

}
