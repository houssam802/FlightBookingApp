package com.example.controlers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class Airline_table {

    private final Context mContext;
    private SQLiteDatabase mDb;
    private final Generate_DB mDbHelper;

    public Airline_table(Context context) {
        this.mContext = context;
        mDbHelper = new Generate_DB(mContext,"ICAOAIRLINES.db");
    }

    public Airline_table createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public Airline_table open() throws SQLException {
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

    public Cursor getAirlineShortcut(String name){
        String[] strArr = name.split(" ");
        String firstWord = strArr[0];
        String lastWord = strArr[strArr.length - 1];
        try {
            String sql = "select * from ICAOAIRLINES where Nom_de_la_compagnie like ('"+
                    firstWord + "%') AND Nom_de_la_compagnie like ('%" + lastWord + "');";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            mSQLException.printStackTrace();
        }
        return null;
    }

}
