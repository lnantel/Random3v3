package com.nantel.louis.random3v3;

import java.util.ArrayList;

/**
 * Created by Louis Nantel on 2017-05-07.
 *
 */

public class Preset {
    public boolean oneHealer;
    public boolean oneTank;
    public boolean oneDPS;
    public ArrayList<Hero> heroesOn = new ArrayList<>();

    public String name;

    public Preset(){
        //Required empty public constructor
    }

    public Preset(String name, ArrayList<Hero> heroesOn, boolean oneHealer, boolean oneTank, boolean oneDPS){
        this.name = name;
        this.heroesOn = heroesOn;
        this.oneHealer = oneHealer;
        this.oneTank = oneTank;
        this.oneDPS = oneDPS;
    }
}
