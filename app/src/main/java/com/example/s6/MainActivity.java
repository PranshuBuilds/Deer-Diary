package com.example.s6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    Button login;
    TextInputEditText user;
    TextInputEditText pass;
    FirebaseAuth mfirebase;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        login = findViewById(R.id.login);
        user= findViewById(R.id.user);
        pass= findViewById(R.id.password);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,PatternActivity.class));
//                finish();
//            }
//        });

        LinearLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String emailid = user.getText().toString();
//                String pwd = pass.getText().toString();
//
//                if (emailid.isEmpty() || pwd.isEmpty()) {
//                    Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_LONG).show();
//                } else {
//                    mfirebase.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(MainActivity.this, "Error log in", Toast.LENGTH_LONG).show();
//
//                            } else {
//                                startActivity(new Intent(MainActivity.this, PatternActivity.class));
//
//                            }
//                        }
//                    });
//
//                }
                startActivity(new Intent(MainActivity.this, PatternActivity.class));

            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mfirebase.getCurrentUser();
                if (mFirebaseUser != null) {
//                    Toast.makeText(MainActivity.this, "fields are empty", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, PatternActivity.class));

                } else {
                    Toast.makeText(MainActivity.this, "Please log in", Toast.LENGTH_LONG).show();

                }
            }
        };
    }
}
