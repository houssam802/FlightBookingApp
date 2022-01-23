package com.example.flight_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import lombok.SneakyThrows;

public class End_date_calendar extends AppCompatActivity {

    CalendarView my_calendar;
    static String infos_start_date="";

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_date_calendar);

        my_calendar = (CalendarView) findViewById(R.id.end_date_calendar);

        SharedPreferences sharedpreferences = getSharedPreferences("end_date", Context.MODE_PRIVATE);
        if(sharedpreferences!=null) {
            String infos = sharedpreferences.getString("end_date_infos", "");
            if(infos!=null && infos!="") {
                String[] infos_array = infos.split("--");
                my_calendar.setDate(getDate(infos_array[0]).getTime());
                my_calendar.setMinDate(getDate(infos_array[0]).getTime());
            }
        }


        SharedPreferences sharedpreferences2 = getSharedPreferences("start_date", Context.MODE_PRIVATE);
        if(sharedpreferences2!=null) {
            String infos = sharedpreferences2.getString("start_date_infos", "");
            if(infos!=null && infos!="") {
                String[] infos_array = infos.split("--");
                my_calendar.setMinDate(getDate(infos_array[0]).getTime());
            }
        }

        my_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SneakyThrows
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth +"/"+(month+1)+"/"+year;
                infos_start_date = date + "--" + getweek_day(getDate(date).getDay()) + "--" + getMonth(month);
            }
        });
    }


    public void set_return_date(View view) {
        create_session_return_date();
        this.finish();
        startActivity(new Intent(End_date_calendar.this, Main_search_view.class));
    }

    public void create_session_return_date() {
        if (infos_start_date == "") {
            Date jour = new Date(my_calendar.getDate());
            String date = jour.getDate() +"/"+(jour.getMonth()+1)+"/20"+(jour.getYear()-100);
            infos_start_date = date + "--" + getweek_day(jour.getDay()) + "--" + getMonth(jour.getMonth());
        }

        SharedPreferences sharedpreferences = getSharedPreferences("end_date", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("end_date_infos",infos_start_date);
        editor.commit();
    }


        public static String getMonth(int month){
        switch (month){
            case 0 : return "janv.";
            case 1 : return "févr.";
            case 2 : return "mars.";
            case 3 : return "avr.";
            case 4 : return "mai";
            case 5 : return "juin";
            case 6 : return "juil.";
            case 7 : return "aout";
            case 8 : return "sept.";
            case 9 : return "oct.";
            case 10 : return "nov.";
            case 11 : return "déc.";
            default: return "";
        }
    }

    public static String getweek_day(int day){
        switch (day){
            case 0 : return "dimanche";
            case 1 : return "lundi";
            case 2 : return "mardi";
            case 3 : return "mercredi";
            case 4 : return "jeudi";
            case 5 : return "vendredi";
            case 6 : return "samedi";
            default: return "";
        }
    }

    public static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date jour = format.parse(date);
        return jour;
    }


}