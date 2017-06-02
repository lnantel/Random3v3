package com.nantel.louis.random3v3;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference userRef;
    UserInfo userInfo;
    DatabaseReference roomRef;

    String code;
    RoomInfo roomInfo;
    ArrayList<UserInfo> userInfos = new ArrayList<>();

    ArrayList<Hero> heroes = Hero.makeHeroes();
    ArrayList<Hero> heroesOn = Hero.makeHeroes();
    ArrayList<Hero> heroesOff = new ArrayList<Hero>();

    Button generateBtn;
    Button savePresetBtn;

    public boolean oneHealer = false;
    public boolean oneTank = false;
    public boolean oneDPS = false;

    TextView roomCodeTxt;
    TextView[] playerTxt = new TextView[3];
    TextView[] heroTxt = new TextView[3];
    ImageView[] heroImg = new ImageView[3];

    TextView settingsTxt;
    ExpandableListView settingsListView;
    SettingsAdapter settingsAdapter;
    ArrayList<Preset> userPresets = new ArrayList<>();
    ArrayList<SettingsCategory> settingsData = SettingsCategory.makeSettings(userPresets);

    ValueEventListener roomEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            roomInfo = dataSnapshot.getValue(RoomInfo.class);
            if(roomInfo != null) updateLayout();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener userEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userInfo = dataSnapshot.getValue(UserInfo.class);
            if(userInfo != null) updateSettings();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code = getIntent().getStringExtra("code");

        firebaseAuth = FirebaseAuth.getInstance();
        roomRef = FirebaseDatabase.getInstance().getReference().child("rooms").child(code);

        user = firebaseAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/big_noodle_titling_oblique.ttf");

        settingsTxt = (TextView) findViewById(R.id.settingsTxt);
        settingsTxt.setTypeface(typeface);
        settingsListView = (ExpandableListView) findViewById(R.id.settingsListView);
        //settingsAdapter = new SettingsAdapter(this, settingsData);
        //settingsListView.setAdapter(settingsAdapter);

        roomCodeTxt = (TextView) findViewById(R.id.roomCodeTxt);
        roomCodeTxt.setText("Room code: " + code);
        roomCodeTxt.setTypeface(typeface);

        savePresetBtn = (Button) findViewById(R.id.savePresetBtn);

        generateBtn = (Button) findViewById(R.id.generateBtn);
        generateBtn.setTypeface(typeface);

        playerTxt[0] = (TextView) findViewById(R.id.player1);
        playerTxt[1] = (TextView) findViewById(R.id.player2);
        playerTxt[2] = (TextView) findViewById(R.id.player3);
        for(TextView t : playerTxt) t.setTypeface(typeface);

        heroTxt[0] = (TextView) findViewById(R.id.hero1);
        heroTxt[1] = (TextView) findViewById(R.id.hero2);
        heroTxt[2] = (TextView) findViewById(R.id.hero3);
        for(TextView t : heroTxt) t.setTypeface(typeface);

        heroImg[0] = (ImageView) findViewById(R.id.heroImg1);
        heroImg[1] = (ImageView) findViewById(R.id.heroImg2);
        heroImg[2] = (ImageView) findViewById(R.id.heroImg3);

        generateBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                generate();
            }
        });
        savePresetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreset();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        roomRef.addValueEventListener(roomEventListener);
        userRef.addValueEventListener(userEventListener);
    }

    private void updateSettings(){
        userPresets = userInfo.presets;
        settingsData = SettingsCategory.makeSettings(userPresets);
        settingsAdapter = new SettingsAdapter(this, settingsData);
        settingsListView.setAdapter(settingsAdapter);
    }

    private void updateLayout(){
        //Set player names
        int i = 0;
        userInfos = roomInfo.getUsers();
        while(i < userInfos.size() && i < 3){
            playerTxt[i].setText(userInfos.get(i).getDisplayName());
            i++;
        }
        while(i<3){
            playerTxt[i].setText("Waiting...");
            i++;
        }
        //Set heroesOn
        i = 0;
        ArrayList<Hero> picks = roomInfo.heroes;
        while(i < userInfos.size() && i < picks.size() && i < 3){
            heroTxt[i].setText(picks.get(i).name + " (" + userInfos.get(i).playtime.get(picks.get(i).owName) + "h)");
            heroImg[i].setImageResource(picks.get(i).drawableId);
            i++;
        }
        while(i < 3){
            heroTxt[i].setText("");
            heroImg[i].setImageResource(R.drawable.empty_portrait);
            i++;
        }
        //Set generate button if room is full
        if(userInfos.size() == 3){
            if(user.getUid().equals(roomInfo.leader.uid)) {
                generateBtn.setVisibility(View.VISIBLE);
                roomCodeTxt.setVisibility(View.GONE);
            } else{
                roomCodeTxt.setText("Room leader: "+roomInfo.leader.getDisplayName());
                generateBtn.setVisibility(View.GONE);
                roomCodeTxt.setVisibility(View.VISIBLE);
            }
        }else{
            generateBtn.setVisibility(View.GONE);
            roomCodeTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause(){
        leaveRoom();
        finish();
        super.onPause();
    }

    private void leaveRoom(){
        roomRef.removeEventListener(roomEventListener);
        roomInfo.removeUser(user.getUid());
        if(roomInfo.getUsers().size() == 0)
            roomRef.removeValue();
        else if(user.getUid().equals(roomInfo.leader.uid)){
            roomInfo.nextLeader();
            roomRef.setValue(roomInfo);
        } else
            roomRef.setValue(roomInfo);
        finish();
    }

    private void savePreset(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        alert.setTitle("Set preset name");
        alert.setMessage("Enter a name for these settings (max 15 characters)");

        final EditText input = new EditText(this);
        input.setTextColor(getResources().getColor(R.color.apptheme_color));
        alert.setView(input);

        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String presetName = input.getText().toString().trim().toLowerCase();

                boolean alreadyExists = false;
                for(Preset p : userPresets){
                    if(presetName.equals(p.name)){
                        Toast.makeText(MainActivity.this,"A preset with this name already exists", Toast.LENGTH_SHORT).show();
                        alreadyExists = true;
                    }
                }

                boolean tooLong = false;
                if(presetName.length() > 15){
                    Toast.makeText(MainActivity.this,"Names must be shorter than 16 characters", Toast.LENGTH_SHORT).show();
                    tooLong = true;
                }
                if(!tooLong && !alreadyExists){
                    userInfo.presets.add(new Preset(presetName,heroesOn,oneHealer,oneTank,oneDPS));
                    userRef.setValue(userInfo);
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                //Cancelled.
            }
        });
        alert.show();
    }

    private void generate(){
        if(heroesOn.size() >= 3){
            Collections.shuffle(heroesOn);
            ArrayList<Hero> randomPicks = new ArrayList<>();

            if(oneHealer){
                Hero h = getRole("Healer");
                if(h != null)
                    randomPicks.add(h);
                else
                    Toast.makeText(this,"No healers in roster", Toast.LENGTH_SHORT).show();
            }
            if(oneTank){
                Hero h = getRole("Tank");
                if(h != null)
                    randomPicks.add(h);
                else
                    Toast.makeText(this,"No tanks in roster", Toast.LENGTH_SHORT).show();
            }
            if(oneDPS){
                Hero h = getRole("DPS");
                if(h != null)
                    randomPicks.add(h);
                else
                    Toast.makeText(this,"No DPS in roster", Toast.LENGTH_SHORT).show();
            }
            int j = 0; //index of heroesOn list
            while(randomPicks.size() < 3 && j < heroesOn.size()){
                Hero h = heroesOn.get(j++);
                if(!randomPicks.contains(h)) randomPicks.add(h);
            }
            if(randomPicks.size() == 3){
                Collections.shuffle(randomPicks);

                roomInfo.heroes = new ArrayList<>();
                for(int i = 0; i < randomPicks.size(); i++){
                    roomInfo.heroes.add(randomPicks.get(i));
                }
                roomRef.setValue(roomInfo);
            }else{
                Toast.makeText(this,"Current team comp settings incompatible with hero selection", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Allow at least 3 heroes",Toast.LENGTH_SHORT).show();
        }
    }

    private Hero getRole(String role){
        for(Hero h : heroesOn){
            if(h.category.equals(role) || h.tags.contains(role)){
                return h;
            }
        }
        return null;
    }

    public void setHero(String name, boolean on){
        if(on){
            //If hero is in heroesOff, remove it and add it to heroesOn
            Hero hero = null;
            for(Hero h : heroesOff){
                if(name.equals(h.name)) hero = h;
            }
            if(hero != null){
                heroesOff.remove(hero);
                heroesOn.add(hero);
            }
        }else{
            //If hero is in heroesOn, remove it and add it to heroesOff
            Hero hero = null;
            for(Hero h : heroesOn){
                if(name.equals(h.name)) hero = h;
            }
            if(hero != null){
                heroesOn.remove(hero);
                heroesOff.add(hero);
            }
        }
    }

    public void setHeroList(ArrayList<Hero> heroList, boolean on){
        for(Hero h : heroList){
            setHero(h.name,on);
        }
    }

    public void setAllHeroes(boolean on){
        for(Hero h : heroes){
            setHero(h.name,on);
        }
    }

    public void setRole(String category, boolean on){
        if(on){
            for(Hero h : heroes){
                if(h.category.equals(category)) setHero(h.name,true);
            }
        }else{
            for(Hero h : heroes){
                if(h.category.equals(category)) setHero(h.name,false);
            }
        }
    }

    public void setPreset(String presetName, boolean on){
        if(on){
            Preset preset = null;
            for(Preset p : userPresets){
                if(presetName.equals(p.name)) preset = p;
            }
            if(preset != null){
                oneHealer = preset.oneHealer;
                oneTank = preset.oneTank;
                oneDPS = preset.oneDPS;
                setAllHeroes(false);
                setHeroList(preset.heroesOn,true);
            }
        }else{
            setAllHeroes(true);
            oneHealer = false;
            oneTank = false;
            oneDPS = false;
        }
    }

    public void deletePreset(String presetName){
        Preset preset = null;
        for(Preset p : userInfo.presets){
            if(presetName.equals(p.name)) preset = p;
        }
        if(preset != null){
            userInfo.presets.remove(preset);
            userRef.setValue(userInfo);
        }
    }

    public boolean getRoleStatus(String role){
        for(Hero h : heroesOn){
            if(h.category.equals(role)) return true;
        }
        return false;
    }

    public boolean getPresetStatus(String presetName){
        Preset preset = null;
        for(Preset p : userPresets){
            if(presetName.equals(p.name)) preset = p;
        }
        if(preset != null){
            for(Hero h : heroesOn){
                boolean found = false;
                for(Hero hp : preset.heroesOn){
                    if(h.name.equals(hp.name)) found = true;
                }
                if(!found) return false;
            }
            for(Hero h : heroesOff){
                boolean found = false;
                for(Hero hp : preset.heroesOn){
                    if(h.name.equals(hp.name)) found = true;
                }
                if(found) return false;
            }
            return oneHealer == preset.oneHealer && oneTank == preset.oneTank && oneDPS == preset.oneDPS;
        }else
            return false;
    }
}
