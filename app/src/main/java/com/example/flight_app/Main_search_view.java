package com.example.flight_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.moduls.Airport;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;

import lombok.val;

public class Main_search_view extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static TextView origin_place;
    static TextView origin_place_cd;
    static TextView destination_place;
    static TextView destination_place_cd;

    static String origin_place_icao="LFPO";
    static String destination_place_icao="KJFK";

    TextView go_day_num;
    TextView go_day_name;
    TextView go_month;
    TextView ret_day_num;
    TextView ret_day_name;
    TextView ret_month;

    static String place_type;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (LoginCtrl.email != "") {
            //Intent switchActivityIntent = new Intent(this, LoginCtrl.class);
            //startActivity(switchActivityIntent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_view);


        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_search);

        NavigationView navigationView = findViewById(R.id.nav_view_search);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         origin_place=((TextView) findViewById(R.id.city_name_org));
         origin_place_cd=((TextView) findViewById(R.id.city_cd_org));
         destination_place=((TextView) findViewById(R.id.city_name_dest));
         destination_place_cd=((TextView) findViewById(R.id.city_cd_dest));

         go_day_num=((TextView) findViewById(R.id.go_day_num));
         go_day_name=((TextView) findViewById(R.id.go_day_name));
         go_month=((TextView) findViewById(R.id.go_month));
         ret_day_num=((TextView) findViewById(R.id.ret_day_num));
         ret_day_name=((TextView) findViewById(R.id.ret_day_name));
         ret_month=((TextView) findViewById(R.id.ret_month));

        load_date_choices();
        load_places_infos();

        val voyageur = getResources().getStringArray(R.array.voyageur);
        val arrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item,voyageur);
        ((AutoCompleteTextView) findViewById(R.id.peoples)).setAdapter(arrayAdapter);

        val classes = getResources().getStringArray(R.array.classe);
        val arrayAdapter2 = new ArrayAdapter(getApplicationContext(),R.layout.dropdown_item,classes);
        ((AutoCompleteTextView) findViewById(R.id.classes)).setAdapter(arrayAdapter2);

        load_user_infos(navigationView.getHeaderView(0));

    }


    public void set_flight_type(View view) {
        Switch my_switch = (Switch) findViewById(R.id.flight_type);
        LinearLayout ret_date_layout = ((LinearLayout) findViewById(R.id.back_date));
        if (my_switch.isChecked()) {
            ret_date_layout.setEnabled(false);
            ret_date_layout.setAlpha((float) 0.3);
        } else {
            ret_date_layout.setEnabled(true);
            ret_date_layout.setAlpha((float) 1);
        }
    }

    public void switch_dest_pos(View view) {
        String org_name = (String) origin_place.getText();
        String org_cd = (String) origin_place_cd.getText();
        String tmp_iata = origin_place_icao;
        origin_place_icao = destination_place_icao;
        destination_place_icao = tmp_iata;
        origin_place.setText(destination_place.getText());
        origin_place_cd.setText(destination_place_cd.getText());
        destination_place.setText(org_name);
        destination_place_cd.setText(org_cd);
    }


    public void set_go_date(View view) {
        startActivity(new Intent(Main_search_view.this, Start_date_calendar.class));
    }

    public void set_ret_date(View view) {
        startActivity(new Intent(Main_search_view.this, End_date_calendar.class));
    }

    public void set_origin_place(View view) {
        place_type = "Origine";
        startActivity(new Intent(Main_search_view.this, Set_city_ctrl.class));
    }

    public void set_destination_place(View view) {
        place_type = "Destination";
        startActivity(new Intent(Main_search_view.this, Set_city_ctrl.class));
    }


    public void apply_search(View view) {
        SharedPreferences sharedpreferences = getSharedPreferences("search", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("origine_name", (String) origin_place.getText());
        editor.putString("origine_code", (String) origin_place_cd.getText());
        editor.putString("origine_icao", origin_place_icao);
        editor.putString("destination_name", (String) destination_place.getText());
        editor.putString("destination_code", (String) destination_place_cd.getText());
        editor.putString("destination_icao", destination_place_icao);

        editor.commit();

        System.out.println(sharedpreferences.getString("origine_code", ""));

        Intent intent = new Intent(this, Flights_ads.class);
        startActivity(intent);
    }

    public void load_date_choices(){
        Date jour = new Date();
        String date = jour.getDate() +"/"+(jour.getMonth()+1)+"/20"+(jour.getYear()-100);
        String infos_start_date = date + "--" + End_date_calendar.getweek_day(jour.getDay()) + "--" + End_date_calendar.getMonth(jour.getMonth());
        String[] infos_date_array = infos_start_date.split("--");
        SharedPreferences sharedpreferences = getSharedPreferences("start_date", Context.MODE_PRIVATE);
        if(sharedpreferences!=null){
            String infos = sharedpreferences.getString("start_date_infos","");
            if(infos!=null && infos!="") {
                String[] infos_array = infos.split("--");
                go_day_num.setText(infos_array[0].split("/")[0]);
                go_day_name.setText(infos_array[1]);
                go_month.setText(infos_array[2]);
            }else{
                go_day_num.setText(infos_date_array[0].split("/")[0]);
                go_day_name.setText(infos_date_array[1]);
                go_month.setText(infos_date_array[2]);
                create_session_date(infos_start_date,"start_date","start_date_infos");
            }
        }

        SharedPreferences sharedpreferences2 = getSharedPreferences("end_date", Context.MODE_PRIVATE);
        if(sharedpreferences2!=null){
            String infos = sharedpreferences2.getString("end_date_infos","");
            if(infos!=null && infos!=""){
                String[] infos_array = infos.split("--");
                System.out.println(sharedpreferences2.getAll().toString());
                ret_day_num.setText(infos_array[0].split("/")[0]);
                ret_day_name.setText(infos_array[1]);
                ret_month.setText(infos_array[2]);
            }else{
                ret_day_num.setText(infos_date_array[0].split("/")[0]);
                ret_day_name.setText(infos_date_array[1]);
                ret_month.setText(infos_date_array[2]);
                create_session_date(infos_start_date,"end_date","end_date_infos");
            }

        }
    }


    static void load_place_infos(Airport my_airport){
        if(place_type == "Origine"){
            origin_place.setText(my_airport.getCity());
            origin_place_cd.setText(my_airport.getIata());
            origin_place_icao = my_airport.getIcao();
        }
        if(place_type == "Destination"){
            destination_place.setText(my_airport.getCity());
            destination_place_cd.setText(my_airport.getIata());
            destination_place_icao = my_airport.getIcao();
        }
    }

    public void create_session_date(
            String infos_date , String date_type , String date_type_infos
    ){
        SharedPreferences sharedpreferences = getSharedPreferences(date_type, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(date_type_infos,infos_date);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent switchActivityIntent = new Intent(this, Main_search_view.class);
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(switchActivityIntent);
                break;
            case R.id.fl_booking:
                switchActivityIntent.setClass(this, Main_search_view.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = LoginCtrl.sharedpreferences.edit();
                editor.clear();
                editor.commit();
                this.finish();
                switchActivityIntent.setClass(this, LoginCtrl.class);
                startActivity(switchActivityIntent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void load_user_infos(View view_h){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginCtrl.MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences!=null){
            String full_name = sharedpreferences.getString("full name","");
            String email = sharedpreferences.getString("email","");
            if(full_name!=null && full_name!="" && email!=null && email!="") {
                View header = view_h;
                ((TextView) header.findViewById(R.id.full_name)).setText(full_name);
                ((TextView) header.findViewById(R.id.email)).setText(email);
            }
        }
    }

    public void load_places_infos(){
        SharedPreferences sharedpreferences = getSharedPreferences("search", Context.MODE_PRIVATE);
        if(sharedpreferences!=null){
            String origine_name = sharedpreferences.getString("origine_name","");
            String origine_code = sharedpreferences.getString("origine_code","");
            String destination_name = sharedpreferences.getString("destination_name","");
            String destination_code = sharedpreferences.getString("destination_code","");
            if(origine_name!=null && origine_name!="" && origine_code!=null && origine_code!="") {
                origin_place.setText(origine_name);
                origin_place_cd.setText(origine_code);
                destination_place.setText(destination_name);
                destination_place_cd.setText(destination_code);
            }
        }
    }
}