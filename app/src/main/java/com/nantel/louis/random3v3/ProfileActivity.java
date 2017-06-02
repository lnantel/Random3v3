package com.nantel.louis.random3v3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private Button changeBtn;
    private Button logoutBtn;
    private Button createRoomBtn;
    private Button joinRoomBtn;

    private UserInfo userInfo;

    private TextView levelBar;
    private ImageView starBarStart;
    private ImageView starBarEnd;
    private ImageView[] stars = new ImageView[5];
    private ImageView avatar;
    private TextView blizIntroTxt;
    private TextView blizNameTxt;
    private TextView readyTxt;

    private DatabaseReference dbRef;
    private DatabaseReference userRef;

    private boolean queriedOWAPI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //User is not logged in.
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }

        dbRef = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        userRef = dbRef.child(user.getUid());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/big_noodle_titling_oblique.ttf");

        levelBar = (TextView) findViewById(R.id.bar);
        starBarStart = (ImageView) findViewById(R.id.starBarStart);
        starBarEnd = (ImageView) findViewById(R.id.starBarEnd);
        stars[0] = (ImageView) findViewById(R.id.star1);
        stars[1] = (ImageView) findViewById(R.id.star2);
        stars[2] = (ImageView) findViewById(R.id.star3);
        stars[3] = (ImageView) findViewById(R.id.star4);
        stars[4] = (ImageView) findViewById(R.id.star5);

        avatar = (ImageView) findViewById(R.id.blizAvatarImg);
        blizNameTxt = (TextView) findViewById(R.id.blizNameTxt);
        blizNameTxt.setTypeface(typeface);
        blizIntroTxt = (TextView) findViewById(R.id.blizIntroTxt);
        blizIntroTxt.setTypeface(typeface);
        readyTxt = (TextView) findViewById(R.id.readyTxt);
        readyTxt.setTypeface(typeface);

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setTypeface(typeface);
        createRoomBtn = (Button) findViewById(R.id.createBtn);
        createRoomBtn.setTypeface(typeface);
        joinRoomBtn = (Button) findViewById(R.id.joinBtn);
        joinRoomBtn.setTypeface(typeface);
        changeBtn = (Button) findViewById(R.id.blizNameBtn);
        changeBtn.setTypeface(typeface);

        logoutBtn.setOnClickListener(this);
        createRoomBtn.setOnClickListener(this);
        joinRoomBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(UserInfo.class);
                update();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void update(){
        if(userInfo != null){
            blizNameTxt.setText(userInfo.getDisplayName());

            if(userInfo.avatarURL != null) new DownloadAvatarImage(this, avatar).execute(userInfo.avatarURL);
            if(userInfo.prestige != 0 || userInfo.level != 0) setPrestigeAndLevel(userInfo.prestige, userInfo.level);
            if(!queriedOWAPI){
                new GetOWInfo(userInfo, userRef).execute(userInfo.blizName);
                queriedOWAPI = true;
            }

            createRoomBtn.setClickable(true);
            joinRoomBtn.setClickable(true);
        }else{
            changeBlizName();
            createRoomBtn.setClickable(false);
            joinRoomBtn.setClickable(false);
        }
    }

    private void changeBlizName(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        alert.setTitle("Set Blizzard profile");
        alert.setMessage("Enter your Blizzard username (e.g. Leeroy#1234)");

        final EditText input = new EditText(this);
        input.setTextColor(getResources().getColor(R.color.apptheme_color));
        alert.setView(input);

        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String blizName = input.getText().toString().trim();
                if(validateBlizName(blizName)){
                    UserInfo userInfo = new UserInfo(user.getUid(), blizName);
                    userRef.setValue(userInfo);
                }else{
                    Toast.makeText(ProfileActivity.this, "Invalid Blizzard username.", Toast.LENGTH_SHORT).show();
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

    private boolean validateBlizName(String blizName){
        String[] tokens = blizName.split("#");
        return (tokens.length == 2 && tokens[0].length() > 0 && tokens[1].length() > 0 && tokens[0].matches("^[a-zA-Z0-9]*$") && tokens[1].matches("^[0-9]*$"));
    }

    private void createRoom(){
        RoomInfo room = new RoomInfo(userInfo);
        dbRef.child("rooms").child(room.code).setValue(room);
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.putExtra("code",room.code);
        startActivity(intent);
    }

    private void joinRoom(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        alert.setTitle("Join room");
        alert.setMessage("Enter the room code (4 characters):");

        final EditText input = new EditText(this);
        input.setTextColor(getResources().getColor(R.color.apptheme_color));
        alert.setView(input);

        alert.setPositiveButton("Join", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                String code = input.getText().toString().trim().toUpperCase();
                final DatabaseReference roomRef = dbRef.child("rooms").child(code);
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RoomInfo roomInfo = dataSnapshot.getValue(RoomInfo.class);
                        if(roomInfo == null){
                            //Room doesn't exist
                            Toast.makeText(ProfileActivity.this,"Invalid room code", Toast.LENGTH_SHORT).show();
                        }else{
                            if(roomInfo.addUser(userInfo)){
                                roomRef.setValue(roomInfo);
                                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                intent.putExtra("code",roomInfo.code);
                                startActivity(intent);
                            }else{
                                //Room is full
                                Toast.makeText(ProfileActivity.this,"Room is full", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                //Cancelled.
            }
        });
        alert.show();
    }

    public void setPrestigeAndLevel(int prestige, int level){
        //Set prestige
        starBarStart.setVisibility(View.GONE);
        for(ImageView star : stars)
            star.setVisibility(View.GONE);
        starBarEnd.setVisibility(View.GONE);
        if(prestige != 0){
            starBarStart.setVisibility(View.VISIBLE);
            for(int i = 0; i < prestige && i < stars.length; i++)
                stars[i].setVisibility(View.VISIBLE);
            starBarEnd.setVisibility(View.VISIBLE);
        }
        //Set level
        if(level != 0){
            levelBar.setText(""+level);
        }else{
            levelBar.setText("??");
        }
    }

    @Override
    public void onClick(View v) {
        if(v == logoutBtn){
            firebaseAuth.signOut();
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }
        if(v == createRoomBtn){
            createRoom();
        }
        if(v == joinRoomBtn){
            joinRoom();
        }
        if(v == changeBtn){
            changeBlizName();
        }
    }
}
