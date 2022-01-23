package com.example.controlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.example.moduls.User;

import java.io.IOException;

public class Users_table{

        private final Context mContext;
        private static final String id_userCol = "id_user";
        private static final String FnameCol = "Fname";
        private static final String LnameCol = "Lname";
        private static final String emailCol = "email";
        private static final String mdpCol = "mdp";
        private static SQLiteDatabase mDb;
        private Generate_DB mDbHelper;

        public Users_table(Context context) {
            this.mContext = context;
            mDbHelper = new Generate_DB(mContext,"users.db");
        }

        public Users_table createDatabase() throws SQLException {
            try {
                mDbHelper.createDataBase();
            } catch (IOException mIOException) {
                System.out.println(mIOException.toString() + "  UnableToCreateDatabase");
                throw new Error("UnableToCreateDatabase");
            }
            return this;
        }

        public Users_table open() throws SQLException {
            try {
                mDbHelper.openDataBase();
                mDbHelper.close();
                mDb = mDbHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                throw mSQLException;
            }
            return this;
        }

        public void close() {
            mDbHelper.close();
        }

    public Cursor getTestData() {
        try {
            String sql = "select * from users limit 10";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

        public static long signin(User user) {
            ContentValues values = new ContentValues();
            values.put(FnameCol, user.getFname());
            values.put(LnameCol, user.getLname());
            values.put(emailCol, user.getEmail());
            values.put(mdpCol, User.crypt(user.getMdp()));

            System.out.println(values);
            long newRowId=-1;
            try{
                newRowId = mDb.insertOrThrow("users",null, values);
            }catch(SQLiteConstraintException e){
                if(e.toString().contains("users.email")) newRowId=-2;
            }
            return newRowId;
        }


    public static User login(User user) {
        try {
            String sql = "select * from users where email=\""+user.getEmail()+"\"";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                while (mCur.moveToNext()) {
                    user.setId(mCur.getInt(0));
                    user.setLname(mCur.getString(2));
                    user.setFname(mCur.getString(1));
                    if(User.decrypt(user.getMdp(), mCur.getString(4))) return user;
                    else return null;
                }
            }else return null;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        return null;
    }


}