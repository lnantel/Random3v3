package com.nantel.louis.random3v3;

import java.util.ArrayList;

/**
 * Created by Louis Nantel on 2017-05-03.
 *
 */

public class RoomInfo {
    public String code = "";
    private ArrayList<UserInfo> users = new ArrayList<>();
    public ArrayList<Hero> heroes = new ArrayList<>();
    public UserInfo leader;

    public RoomInfo(){
        //Required empty constructor
    }

    public RoomInfo(UserInfo userInfo){
        for(int i = 0; i < 4; i++){
            code += rndChar();
        }
        users.add(userInfo);
        leader = userInfo;
    }

    public ArrayList<UserInfo> getUsers(){
        return users;
    }

    public UserInfo getUser(int pos){
        return users.get(pos);
    }

    private static char rndChar () {
        int rnd = (int) (Math.random() * 26);
        return (char) ('A' + rnd);
    }

    public boolean addUser(UserInfo userInfo){
        if(users.size() < 3){
            users.add(userInfo);
            return true;
        }
        else{
            //Room is full
            return false;
        }
    }

    public void removeUser(String uid){
        for(int i = users.size()-1; i >= 0; i--){
            if(users.get(i).uid.equals(uid)) users.remove(i);
        }
    }

    public void nextLeader(){
        leader = users.get(0);
    }
}
