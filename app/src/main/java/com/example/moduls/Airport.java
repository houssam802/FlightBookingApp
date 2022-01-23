package com.example.moduls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.example.controlers.Airports_table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airport {
    String city,airport,iata, icao, latitude, longitude;

    public Airport(){

    }

    @SuppressLint("Range")
    public Airport(Context context, String icao){
        Airports_table airport_table = new Airports_table(context);
        airport_table.createDatabase();
        airport_table.open();
        Cursor cur = airport_table.getAirportPos(icao);
        if( cur.moveToNext() ){
            this.airport = cur.getString(cur.getColumnIndex("name"));
            this.city = cur.getString(cur.getColumnIndex("city"));
            this.latitude = cur.getString(cur.getColumnIndex("lat"));
            this.longitude = cur.getString(cur.getColumnIndex("lng"));
            this.iata = cur.getString(cur.getColumnIndex("iata"));
            this.icao = cur.getString(cur.getColumnIndex("icao"));
        }
        cur.close();
        airport_table.close();
    }
}
