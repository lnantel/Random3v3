package com.nantel.louis.random3v3;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Louis Nantel on 2017-05-06.
 *
 */

public class SettingsCategory {
    public String name;
    public ArrayList<String> settings;

    public SettingsCategory(String name){
        this.name = name;
    }

    public static ArrayList<SettingsCategory> makeSettings(ArrayList<Preset> presets){
        ArrayList<SettingsCategory> result = new ArrayList<>();

        ArrayList<String> compList = new ArrayList<>();
        compList.add("At least one healer");
        compList.add("At least one tank");
        compList.add("At least one DPS");

        SettingsCategory comp = new SettingsCategory("Team comp");
        comp.settings = compList;
        result.add(comp);

        ArrayList<String> heroList = new ArrayList<>();
        //Offense heroesOn
        heroList.add("Genji");
        heroList.add("McCree");
        heroList.add("Pharah");
        heroList.add("Reaper");
        heroList.add("Soldier:76");
        heroList.add("Sombra");
        heroList.add("Tracer");

        //Defense heroesOn
        heroList.add("Bastion");
        heroList.add("Hanzo");
        heroList.add("Junkrat");
        heroList.add("Mei");
        heroList.add("Torbjörn");
        heroList.add("Widowmaker");

        //Tank heroesOn
        heroList.add("D.Va");
        heroList.add("Orisa");
        heroList.add("Reinhardt");
        heroList.add("Roadhog");
        heroList.add("Winston");
        heroList.add("Zarya");

        //Support heroesOn
        heroList.add("Ana");
        heroList.add("Lúcio");
        heroList.add("Mercy");
        heroList.add("Symmetra");
        heroList.add("Zenyatta");

        Collections.sort(heroList);
        SettingsCategory heroes = new SettingsCategory("Heroes");
        heroes.settings = heroList;
        result.add(heroes);

        ArrayList<String> rolesList = new ArrayList<>();
        rolesList.add("Offense");
        rolesList.add("Defense");
        rolesList.add("Tank");
        rolesList.add("Support");

        SettingsCategory roles = new SettingsCategory("Roles");
        roles.settings = rolesList;
        result.add(roles);

        ArrayList<String> presetList = new ArrayList<>();
        for(Preset p : presets){
            presetList.add(p.name);
        }

        if(presetList.size() > 0){
            SettingsCategory presetsSettings = new SettingsCategory("Presets");
            presetsSettings.settings = presetList;
            result.add(presetsSettings);
        }
        return result;
    }
}
