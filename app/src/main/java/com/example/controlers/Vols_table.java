package com.example.controlers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class Vols_table {
    private final Context mContext;
    private SQLiteDatabase mDb;
    private Generate_DB mDbHelper;

    public Vols_table(Context context) {
        this.mContext = context;
        mDbHelper = new Generate_DB(mContext,"Vols.db");
    }

    public Vols_table createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            System.out.println(mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public Vols_table open() throws SQLException {
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

    public Cursor get_vols(
            String start_airport,String end_airport
            ,String start_date,String end_date
    ) {
        try {
            String sql =
                    "SELECT * from Vols WHERE (icao_arr like '"+end_airport+"' and icao_dep like '"+start_airport+"')"+
                    "and Jours like '"+start_date+"'\n" +
                    "UNION\n" +
                    "SELECT * from Vols WHERE (icao_arr like '"+start_airport+"' and icao_dep like '"+end_airport+"')"+
                    "and Jours like '"+end_date+"'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

}
