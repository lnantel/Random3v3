package com.nantel.louis.random3v3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signInBtn;
    private EditText editTextEmail;
    private EditText editTextPwd;
    private TextView signUpTxt;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.ProgressDialogCustom));
        progressDialog.setCanceledOnTouchOutside(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/big_noodle_titling_oblique.ttf");

        signInBtn = (Button) findViewById(R.id.signInButton);
        signInBtn.setTypeface(typeface);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPwd = (EditText) findViewById(R.id.editTextPwd);
        signUpTxt = (TextView) findViewById(R.id.signUpTxt);

        signInBtn.setOnClickListener(this);
        signUpTxt.setOnClickListener(this);
    }

    public void userLogin(){
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

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            startActivity(new Intent(LogInActivity.this, ProfileActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LogInActivity.this,"Log in failed, please try again",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == signInBtn){
            userLogin();
        }
        if(v == signUpTxt){
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
