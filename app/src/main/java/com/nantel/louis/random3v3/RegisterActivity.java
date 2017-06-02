package com.nantel.louis.random3v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerBtn;
    private EditText editTextEmail;
    private EditText editTextPwd;
    private TextView signInTxt;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.ProgressDialogCustom));
        progressDialog.setCanceledOnTouchOutside(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/big_noodle_titling_oblique.ttf");

        registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setTypeface(typeface);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPwd = (EditText) findViewById(R.id.editTextPwd);
        signInTxt = (TextView) findViewById(R.id.signInTxt);

        registerBtn.setOnClickListener(this);
        signInTxt.setOnClickListener(this);
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String pwd = editTextPwd.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            //pwd is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered
                            //start next activity
                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == registerBtn){
            registerUser();
        }
        if(v == signInTxt){
            startActivity(new Intent(this, LogInActivity.class));
        }
    }
}
