package com.example.s6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.WindowManager;

public class NewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        NavController notes = Navigation.findNavController(this, R.id.fragment);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
