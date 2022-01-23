package com.example.moduls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.example.controlers.Airline_table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airline {

    private String name, col1, col2, pays;

    @SuppressLint("Range")
    public Airline(Context context, String name){
        Airline_table airline_table = new Airline_table(context);
        airline_table.createDatabase();
        airline_table.open();
        Cursor cur = airline_table.getAirlineShortcut(name);
        this.name = name;
        if( cur.moveToFirst() ){
            this.col1 = cur.getString(cur.getColumnIndex("Colonne1"));
            this.col2 = cur.getString(cur.getColumnIndex("Colonne2"));
            this.pays = cur.getString(cur.getColumnIndex("Pays"));
        }
        cur.close();
        airline_table.close();
    }

}
