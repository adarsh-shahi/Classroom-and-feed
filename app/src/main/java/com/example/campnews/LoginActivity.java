package com.example.campnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.spark.submitbutton.SubmitButton;

public class LoginActivity extends AppCompatActivity {

    private SubmitButton loginButton;
    private EditText collegeID,pass;
    private FirebaseAuth mAuth;
    private String parth = " parthdarekar512@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        collegeID=findViewById(R.id.enterId);
        pass=findViewById(R.id.enterPassword);
        loginButton=findViewById(R.id.login_button);
        mAuth=FirebaseAuth.getInstance();

//        loginButton.setOnKeyListener(new View.OnKeyListener()
//        {
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                if (event.getAction() == KeyEvent.ACTION_DOWN)
//                {
//                    switch (keyCode)
//                    {
//                        case KeyEvent.KEYCODE_DPAD_CENTER:
//                        case KeyEvent.KEYCODE_ENTER:
//                            goToMainActivity();
//                            return true;
//                        default:
//                            break;
//                    }
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              goToMainActivity();
                }
        });

    }

    private void login(String email,String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this,"You are logged in successfully",Toast.LENGTH_LONG).show();
                }
                else {
                    String message=task.getException().toString();
                    Toast.makeText(LoginActivity.this,"Error: "+message,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goToMainActivity() {

        String id = collegeID.getText().toString();
        String password = pass.getText().toString();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter your mail and password to login", Toast.LENGTH_LONG).show();
        } else {
            if (id.equals("IF-D1936")) {
                login("parthdarekar512@gmail.com", password);
            } else if (id.equals("IF-D1915")) {
                login("adityabagal65@gmail.com", password);
            } else if (id.equals("IF-D1928")) {
                login("sushilhivale@gmail.com", password);
            } else if (id.equals("IF-18026")) {
                login("nikhiloswal12@gmail.com", password);
            } else {
                Toast.makeText(LoginActivity.this, " Please enter Correct College ID", Toast.LENGTH_SHORT).show();
            }


        }
    }
}