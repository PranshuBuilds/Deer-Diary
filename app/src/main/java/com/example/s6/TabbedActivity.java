package com.example.s6;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



public class TabbedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , TextWatcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

//        getWindow().setFlags(Window.setStatusBarColor(R.color.mdtp_transparent_black),WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        Window.setStatusBarColor(R.color.mdtp_transparent_black);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.remindersFragment)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this,PatternActivity.class));

            super.onBackPressed();
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                Toast.makeText(TabbedActivity.this, "Home page", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent intn =new Intent(Intent.ACTION_SEND);
                intn.setType("text/plain");
                String shareBody= "DownLoad my leatest app";
                String shareSub="My Adhar App\nhttps://play.google.com/store/apps/details?id=com.crazy.loanaadharguide";
                intn.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intn.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intn,"SHARE"));
                break;
            case R.id.about:
                //

                break;
            case R.id.Privacy:
                //
                break;
            case R.id.exit:
                final AlertDialog.Builder builder=new AlertDialog.Builder(TabbedActivity.this);
                builder.setMessage("Really want to exit ??");
                builder.setCancelable(true);
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });builder.setPositiveButton("exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
                break;
        }


        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
