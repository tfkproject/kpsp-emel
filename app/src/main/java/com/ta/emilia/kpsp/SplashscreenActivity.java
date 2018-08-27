package com.ta.emilia.kpsp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class SplashscreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //buat full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hilangkan action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //berapa waktu nampil
        int SPLASH_TIME_OUT = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //langsung masuk activity
                Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(i);

                // tutup activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
