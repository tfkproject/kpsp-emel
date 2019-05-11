package com.ta.emilia.kpsp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ta.emilia.kpsp.util.Config;
import com.ta.emilia.kpsp.util.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Toshibha on 17/03/2018.
 */
public class RincianActivity extends AppCompatActivity {
    TextView jwbYa, jwbTdk;
    Button btn_lihat;
    private ProgressDialog pDialog;
    private static String url = Config.HOST+"rincian.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian);

        //buat tombol back di ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String id_pasien = getIntent().getStringExtra("key_id_pasien");
        String sesi = getIntent().getStringExtra("key_sesi");
        String nama = getIntent().getStringExtra("key_nama");

        getSupportActionBar().setTitle("Rincian:");
        getSupportActionBar().setSubtitle(nama);

        jwbYa = (TextView) findViewById(R.id.jy);
        jwbTdk = (TextView) findViewById(R.id.jt);

        btn_lihat = (Button) findViewById(R.id.btnLht);
        btn_lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RincianActivity.this, KuesionerActivity.class);
                startActivity(intent);
            }
        });

        new dapatkanRincian(id_pasien, sesi).execute();
    }

    private class dapatkanRincian extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien, jum_ya, jum_tdk, sesi;

        public dapatkanRincian(String id_pasien, String sesi){
            this.id_pasien = id_pasien;
            this.sesi = sesi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RincianActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url+"?id_pasien="+id_pasien+"&sesi="+sesi, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        jum_ya = ob.getString("jum_y");
                        jum_tdk = ob.getString("jum_t");
                        psn = ob.getString("message");
                    } else {
                        // no data found
                        psn = ob.getString("message");
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            jwbYa.setText("Jawaban Ya : "+jum_ya);
            jwbTdk.setText("Jawaban Tidak : "+jum_tdk);
            pDialog.dismiss();
        }

    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
