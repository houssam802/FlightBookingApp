package com.example.flight_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.controlers.Airports_table;

public class Set_city_ctrl extends AppCompatActivity{

    TextView place_type;
    EditText place_name;
    static Context my_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_city_ctrl);
        Set_city_ctrl.my_context = this;

        place_type = (TextView) findViewById(R.id.place_type);
        place_type.setText(Main_search_view.place_type);

        place_name = (EditText) findViewById(R.id.place_name);
        place_name.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                          loadAvailableFlights(place_name.getText().toString());
                          place_name.clearFocus();
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.getAction() == KeyEvent.ACTION_UP) {
                        // do your stuff
                        return false;
                    }
                return false;
            }

        });
    }

    @SuppressWarnings("Range")
    public void loadAvailableFlights(String place){
        RecyclerView AirportsRecyclerView = ( RecyclerView ) findViewById(R.id.airports_by_city);
        Airports_table Airports = new Airports_table(this);
        Airports.createDatabase();
        Airports.open();
        AirportsRecyclerView.setAdapter(new AirportsItem_adapter(my_context,Airports.getAirports_codes_by_city(place)));
    }


    public void close_activity(View view) {
        finish();
    }


}