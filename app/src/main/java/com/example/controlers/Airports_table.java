package com.example.controlers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.moduls.Airport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Airports_table {
    private final Context mContext;
    private SQLiteDatabase mDb;
    private Generate_DB mDbHelper;

    public Airports_table(Context context) {
        this.mContext = context;
        mDbHelper = new Generate_DB(mContext,"airports_by_cities.db");
    }

    public Airports_table createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            System.out.println(mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public Airports_table open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Airport> getAirports_codes_by_city(String city) {
        Cursor mCur = null;
        try {
            String sql = "SELECT city,name,iata,icao FROM airports_by_city "
                        +" WHERE city like '%"+city+"%' LIMIT 6;";
            mCur = mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        List<Airport> airports = new ArrayList<Airport>();
        while (mCur.moveToNext()) {
            Airport airport = new Airport();
            airport.setAirport(mCur.getString(1));
            airport.setCity(mCur.getString(0));
            airport.setIcao(mCur.getString(3));
            airport.setIata(mCur.getString(2));
            airports.add(airport);
        }
        return airports;
    }

    public Cursor getAirportPos(String icao){
        try {
            String sql = "select * from airports_by_city where icao='" + icao + "';";
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return null;
    }

}

