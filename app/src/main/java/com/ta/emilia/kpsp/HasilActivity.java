package com.ta.emilia.kpsp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
 * Created by Toshibha on 10/03/2018.
 */
public class HasilActivity extends AppCompatActivity {
    Button btn_rincian, btn_print;
    private ProgressDialog pDialog;
    private TextView txtHasil, txtKet;

    private static String url = Config.HOST+"dapatkan_hasil.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        //buat tombol back di ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final String id_periksa = getIntent().getStringExtra("key_id_periksa");
        final String id_pasien = getIntent().getStringExtra("key_id_pasien");
        String bulan = getIntent().getStringExtra("key_bulan");
        final String nama = getIntent().getStringExtra("key_nama");

        actionBar.setTitle("Hasil:");
        actionBar.setSubtitle(nama);

        new dapatkanHasil(id_periksa, id_pasien, bulan).execute();

        txtHasil = (TextView) findViewById(R.id.hasil);
        txtKet = (TextView) findViewById(R.id.ket);

        btn_rincian = (Button) findViewById(R.id.btnRincian);
        btn_rincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HasilActivity.this, RincianActivity.class);
                intent.putExtra("key_id_pasien", id_pasien);
                intent.putExtra("key_nama", nama);
                startActivity(intent);
            }
        });

        btn_print = (Button) findViewById(R.id.btnPrint);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buka print
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://203.153.21.11/app/kpsp-emel/api/pdf.php?id_periksa="+id_periksa));
                startActivity(browserIntent);
            }
        });

    }

    private class dapatkanHasil extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_periksa, id_pasien, bulan, hasil, ket;

        public dapatkanHasil(String id_periksa, String id_pasien, String bulan){
            this.id_periksa = id_periksa;
            this.id_pasien = id_pasien;
            this.bulan = bulan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HasilActivity.this);
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
                    String response = Request.post(url+"?id_periksa="+id_periksa+"&id_pasien="+id_pasien+"&bulan="+bulan, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        hasil = ob.getString("hasil");
                        ket = ob.getString("ket");
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
            txtHasil.setText(hasil);
            txtKet.setText(ket);
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
