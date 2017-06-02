package com.nantel.louis.random3v3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Louis Nantel on 2017-05-02.
 *
 */

class Hero {
    public String name;
    public String owName;
    public String category;
    public int drawableId;
    public ArrayList<String> tags = new ArrayList<>();

    public Hero(){

    }

    private Hero(String owName, String name, String category, int drawableId, String... tags){
        this.owName = owName;
        this.name = name;
        this.category = category;
        this.drawableId = drawableId;
        this.tags.addAll(Arrays.asList(tags));
    }

    static ArrayList<Hero> makeHeroes(){
        ArrayList<Hero> heroes = new ArrayList<>();

        //Offense heroes
        heroes.add(new Hero("genji", "Genji","Offense", R.drawable.genji_portrait, "DPS"));
        heroes.add(new Hero("mccree", "McCree", "Offense", R.drawable.mccree_portrait, "DPS"));
        heroes.add(new Hero("pharah", "Pharah", "Offense", R.drawable.pharah_portrait, "DPS"));
        heroes.add(new Hero("reaper", "Reaper", "Offense", R.drawable.reaper_portrait, "DPS"));
        heroes.add(new Hero("soldier76", "Soldier:76", "Offense", R.drawable.soldier76_portrait, "DPS"));
        heroes.add(new Hero("sombra", "Sombra", "Offense", R.drawable.sombra_portrait, "DPS"));
        heroes.add(new Hero("tracer", "Tracer", "Offense", R.drawable.tracer_portrait, "DPS"));

        //Defense heroes
        heroes.add(new Hero("bastion", "Bastion", "Defense", R.drawable.bastion_portrait, "DPS"));
        heroes.add(new Hero("hanzo", "Hanzo", "Defense", R.drawable.hanzo_portrait, "DPS"));
        heroes.add(new Hero("junkrat", "Junkrat", "Defense", R.drawable.junkrat_portrait, "DPS"));
        heroes.add(new Hero("mei", "Mei", "Defense", R.drawable.mei_portrait, "DPS"));
        heroes.add(new Hero("torbjorn", "Torbjörn", "Defense", R.drawable.torbjorn_portrait, "DPS"));
        heroes.add(new Hero("widowmaker", "Widowmaker", "Defense", R.drawable.widowmaker_portrait, "DPS"));

        //Tank heroes
        heroes.add(new Hero("dva", "D.Va", "Tank", R.drawable.dva_portrait));
        heroes.add(new Hero("orisa", "Orisa", "Tank", R.drawable.orisa_portrait));
        heroes.add(new Hero("reinhardt", "Reinhardt", "Tank", R.drawable.reinhardt_portrait));
        heroes.add(new Hero("roadhog", "Roadhog", "Tank", R.drawable.roadhog_portrait));
        heroes.add(new Hero("winston", "Winston", "Tank", R.drawable.winston_portrait));
        heroes.add(new Hero("zarya", "Zarya", "Tank", R.drawable.zarya_portrait, "Choo choo"));

        //Support heroes
        heroes.add(new Hero("ana", "Ana", "Support", R.drawable.ana_portrait, "Healer"));
        heroes.add(new Hero("lucio", "Lúcio", "Support", R.drawable.lucio_portrait, "Healer"));
        heroes.add(new Hero("mercy", "Mercy", "Support", R.drawable.mercy_portrait, "Healer"));
        heroes.add(new Hero("symmetra", "Symmetra", "Support", R.drawable.symmetra_portrait, "Choo choo", "DPS"));
        heroes.add(new Hero("zenyatta", "Zenyatta", "Support", R.drawable.zenyatta_portrait, "Choo choo", "Healer"));

        return heroes;
    }
}
