package com.nantel.louis.random3v3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Louis Nantel on 2017-05-05.
 *
 */

public class OWAPI {

    public static String getJSONBlob(String blizName){
        blizName = blizName.replace('#','-');
        String result = "";

        URL url = null;
        try {
            url = new URL("https://owapi.net/api/v3/u/"+blizName+"/blob");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            is = url.openStream();
            int ptr;
            StringBuffer buffer = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                buffer.append((char)ptr);
            }
            is.close();
            result = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getAvatarURL(String jsonString) throws JSONException{
        JSONObject blob = new JSONObject(jsonString);
        JSONObject region = blob.getJSONObject("us");
        JSONObject stats = region.getJSONObject("stats");
        JSONObject quickplay = stats.getJSONObject("quickplay");
        JSONObject overall_stats = quickplay.getJSONObject("overall_stats");

        return overall_stats.getString("avatar");
    }

    public static int[] getPrestigeAndLevel(String jsonString) throws JSONException{
        int[] result = new int[2];

        JSONObject blob = new JSONObject(jsonString);
        JSONObject region = blob.getJSONObject("us");
        JSONObject stats = region.getJSONObject("stats");
        JSONObject quickplay = stats.getJSONObject("quickplay");
        JSONObject overall_stats = quickplay.getJSONObject("overall_stats");
        result[0] = overall_stats.getInt("prestige");
        result[1] = overall_stats.getInt("level");

        return result;
    }

    public static HashMap<String, Integer> getPlaytime(String jsonString) throws JSONException{
        HashMap<String, Integer> result = new HashMap<>();

        JSONObject blob = new JSONObject(jsonString);
        JSONObject region = blob.getJSONObject("us");
        JSONObject heroes = region.getJSONObject("heroes");
        JSONObject playtime = heroes.getJSONObject("playtime");

        JSONObject quickplay = playtime.getJSONObject("quickplay");
        JSONObject competitive = playtime.getJSONObject("competitive");

        result.put("pharah", quickplay.getInt("pharah")+competitive.getInt("pharah"));
        result.put("sombra", quickplay.getInt("sombra")+competitive.getInt("sombra"));
        result.put("mercy", quickplay.getInt("mercy")+competitive.getInt("mercy"));
        result.put("zarya", quickplay.getInt("zarya")+competitive.getInt("zarya"));
        result.put("dva", quickplay.getInt("dva")+competitive.getInt("dva"));
        result.put("symmetra", quickplay.getInt("symmetra")+competitive.getInt("symmetra"));
        result.put("widowmaker", quickplay.getInt("widowmaker")+competitive.getInt("widowmaker"));
        result.put("reaper", quickplay.getInt("reaper")+competitive.getInt("reaper"));
        result.put("tracer", quickplay.getInt("tracer")+competitive.getInt("tracer"));
        result.put("ana", quickplay.getInt("ana")+competitive.getInt("ana"));
        result.put("zenyatta", quickplay.getInt("zenyatta")+competitive.getInt("zenyatta"));
        result.put("junkrat", quickplay.getInt("junkrat")+competitive.getInt("junkrat"));
        result.put("roadhog", quickplay.getInt("roadhog")+competitive.getInt("roadhog"));
        result.put("mccree", quickplay.getInt("mccree")+competitive.getInt("mccree"));
        result.put("orisa", quickplay.getInt("orisa")+competitive.getInt("orisa"));
        result.put("lucio", quickplay.getInt("lucio")+competitive.getInt("lucio"));
        result.put("torbjorn", quickplay.getInt("torbjorn")+competitive.getInt("torbjorn"));
        result.put("mei", quickplay.getInt("mei")+competitive.getInt("mei"));
        result.put("reinhardt", quickplay.getInt("reinhardt")+competitive.getInt("reinhardt"));
        result.put("hanzo", quickplay.getInt("hanzo")+competitive.getInt("hanzo"));
        result.put("genji", quickplay.getInt("genji")+competitive.getInt("genji"));
        result.put("winston", quickplay.getInt("winston")+competitive.getInt("winston"));
        result.put("bastion", quickplay.getInt("bastion")+competitive.getInt("bastion"));
        result.put("soldier76", quickplay.getInt("soldier76")+competitive.getInt("soldier76"));

        return result;
    }
}
