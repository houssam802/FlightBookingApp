package com.example.moduls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flight {

    private String company, icao_dep, icao_arr, avion, heure_dep, heure_arr, num_vol, registration, jour, jour_arr;
    private String other_airport;

}
