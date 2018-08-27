package com.ta.emilia.kpsp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageButton tombol1, tombol2, tombol3;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tombol1 = (ImageButton) findViewById(R.id.btn1);
        tombol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BiodataActivity.class);
                startActivity(intent);
            }
        });

        tombol2 = (ImageButton) findViewById(R.id.btn2);
        tombol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });

        tombol3 = (ImageButton) findViewById(R.id.btn3);
        tombol3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TentangActivity.class);
                startActivity(intent);
            }
        });

        //cek login
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        if(!session.isLoggedIn()){
            finish();
        }

        //ambil data user
        HashMap<String, String> user = session.getUserDetails();
        String id_user = user.get(SessionManager.KEY_ID);
        String nm_user = user.get(SessionManager.KEY_USER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == R.id.action_logout){
            session.logoutUser();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
