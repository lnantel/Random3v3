package com.nantel.louis.random3v3;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Louis Nantel on 2017-05-02.
 *
 */

public class UserInfo {

    public String blizName;
    public String uid;
    public int prestige;
    public int level;
    public String avatarURL;
    public HashMap<String, Integer> playtime = new HashMap<>();
    public ArrayList<Preset> presets = new ArrayList<>();

    public UserInfo(){
        //Required empty public constructor
    }

    public UserInfo(String uid, String blizName) {
        this.blizName = blizName;
        this.uid = uid;
    }

    public String getDisplayName(){
        String display;
        if(blizName != null) display = blizName.split("#")[0];
        else return "Unknown";
        return display;
    }

    public boolean equals(UserInfo userInfo){
        return uid.equals(userInfo.uid);
    }
}
