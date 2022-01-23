package com.example.controlers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class Flight_table {

    private final Context mContext;
    private SQLiteDatabase mDb;
    private final Generate_DB mDbHelper;

    public Flight_table(Context context) {
        this.mContext = context;
        mDbHelper = new Generate_DB(mContext,"Vols.db");
    }

    public Flight_table createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            System.out.println(mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public Flight_table open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData() {
        try {
            String sql = "select * from Vols limit 20";
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return null;
    }

    public Cursor getFirstTenAvailableCompanies(){
        try {
            String sql = "select Compagnies from Vols group by Compagnies limit 11";
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return null;
    }

    public Cursor getFlightInfo(String icao_dep, String icao_arr, String reg, String num_vol, String other_airport) {
        try {
            String sql = "select *\n" +
                    "from ( select icao_dep as dep, icao_arr as arr, Numero_Vol as num, Registration as reg, Heure_DEP as HH, Jours as JJ \n" +
                    "from Vols where ICAO_DEP=\"" + icao_dep + "\"\n" +
                    "AND ICAO_ARR=\"" + other_airport + "\" AND Registration = \"" + reg + "\" AND Numero_Vol = \"" + num_vol + "\" )\n" +
                    "INNER JOIN Vols fli ON fli.ICAO_ARR = \"" + icao_arr + "\" AND ( fli.ICAO_DEP = arr OR fli.ICAO_DEP = \"" + icao_dep + "\" )\n" +
                    "AND fli.Registration = reg AND fli.Numero_Vol = num\n" +
                    "GROUP BY num, reg, dep, arr, HH;";
            Cursor mCur = mDb.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return null;
    }

    public Cursor getFlightByICAO( String icao_dep, String icao_arr ){
        Cursor cur = null;
        try {
            String sql = "select *\n" +
                    "from ( select icao_dep as dep, icao_arr as arr, Numero_Vol as num, Registration as reg, Heure_DEP as HH, Jours as JJ \n" +
                    "from Vols where ICAO_DEP=\"" + icao_dep + "\" )\n" +
                    "INNER JOIN Vols fli ON fli.ICAO_ARR = \"" + icao_arr + "\" AND ( fli.ICAO_DEP = arr OR fli.ICAO_DEP = \"" + icao_dep + "\" )\n" +
                    "AND fli.Registration = reg AND fli.Numero_Vol = num\n" +
                    "GROUP BY num, reg, JJ;";
            cur = mDb.rawQuery(sql, null);
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return cur;
    }


}
