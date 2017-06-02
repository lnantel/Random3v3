package com.nantel.louis.random3v3;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by Louis Nantel on 2017-05-05.
 *
 */

public class GetOWInfo extends AsyncTask<String, Void, String> {
    private UserInfo userInfo;
    private DatabaseReference userRef;

    public GetOWInfo(UserInfo userInfo, DatabaseReference userRef){
        this.userInfo = userInfo;
        this.userRef = userRef;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonString;
        jsonString = OWAPI.getJSONBlob(params[0]);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String result){
        int[] levels = new int[2];
        try {
            levels = OWAPI.getPrestigeAndLevel(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = null;
        try {
            url = OWAPI.getAvatarURL(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> playtime = new HashMap<>();
        try{
            playtime = OWAPI.getPlaytime(result);
        }catch(JSONException e){
            e.printStackTrace();
        }

        userInfo.prestige = levels[0];
        userInfo.level = levels[1];
        userInfo.avatarURL = url;
        userInfo.playtime = playtime;
        userRef.setValue(userInfo);
    }
}
