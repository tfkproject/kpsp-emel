package com.ta.emilia.kpsp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ta.emilia.kpsp.adapter.AdapterKuesioner;
import com.ta.emilia.kpsp.model.ItemKuesioner;
import com.ta.emilia.kpsp.util.Config;
import com.ta.emilia.kpsp.util.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toshibha on 02/03/2018.
 */
public class KuesionerActivity extends AppCompatActivity {

    AdapterKuesioner adapterKuesioner;
    List<ItemKuesioner> items;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ProgressDialog pDialog;

    private static String url = Config.HOST+"ambilkuesioner.php";
    private static String url_sesi = Config.HOST+"ambil_sesi_quest.php";
    private static String url_insert_kues = Config.HOST+"insert_kues.php";

    private String bulan, ss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuesioner);

        final String nama = getIntent().getStringExtra("key_nama_pasien");
        final String id_pasien = getIntent().getStringExtra("key_id_pasien");
        final String id_periksa = getIntent().getStringExtra("key_id_periksa");

        getSupportActionBar().setTitle("Client:");
        getSupportActionBar().setSubtitle(nama);

        //panggil RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //set LayoutManager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        //set isi kuesioner
        new ambilKues(id_pasien).execute();

        //ambil sesi
        new ambilSesi(id_pasien).execute();


        //set adapter
        adapterKuesioner = new AdapterKuesioner(getApplicationContext(), items, new AdapterKuesioner.tombolListener() {
            @Override
            public void onSelected(int position, String id_kuesioner, String jawaban) {
                //insert tbl_kuespasien
                //Toast.makeText(KuesionerActivity.this, "ID Kues: "+id_kuesioner + ", ID Client: "+id_pasien+", Jawaban: "+jawaban, Toast.LENGTH_SHORT).show();
                int sesi_ = Integer.valueOf(ss) + 1;
                String sesi = String.valueOf(sesi_);
                new insertKues(id_kuesioner, id_pasien, jawaban, sesi).execute();
                items.remove(position);
                if(items.isEmpty()){
                    pDialog.dismiss();
                    //Toast.makeText(KuesionerActivity.this, "ID Rentang: "+bulan, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(KuesionerActivity.this, HasilActivity.class);
                    intent.putExtra("key_id_periksa", id_periksa);
                    intent.putExtra("key_id_pasien", id_pasien);
                    intent.putExtra("key_bulan", bulan);
                    intent.putExtra("key_sesi", sesi);
                    intent.putExtra("key_nama", nama);
                    startActivity(intent);
                    finish();
                }
            }
        });
        recyclerView.setAdapter(adapterKuesioner);

    }

    private class ambilKues extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien;

        public ambilKues(String id_pasien){
            this.id_pasien = id_pasien;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KuesionerActivity.this);
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
                    String response = Request.post(url+"?id_pasien="+id_pasien, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("field");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString("id_kuesioner");
                            String url_gambar = c.getString("gambar");
                            String pertanyaan = c.getString("pertanyaan");
                            bulan = c.getString("bulan");


                            items.add(new ItemKuesioner(id, url_gambar, pertanyaan));

                        }
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
            adapterKuesioner.notifyDataSetChanged();
            pDialog.dismiss();
        }

    }

    private class ambilSesi extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien;

        public ambilSesi(String id_pasien){
            this.id_pasien = id_pasien;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url_sesi+"?id_pasien="+id_pasien, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        ss = ob.getString("sesi");
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

        }

    }

    private class insertKues extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_kues, id_pasien, jawaban, sesi;

        public insertKues(String id_kues, String id_pasien, String jawaban, String sesi){
            this.id_kues = id_kues;
            this.id_pasien = id_pasien;
            this.jawaban = jawaban;
            this.sesi = sesi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KuesionerActivity.this);
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
                    String response = Request.post(url_insert_kues+"?id_kues="+id_kues+"&id_pasien="+id_pasien+"&jawaban="+jawaban+"&sesi="+sesi, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        String id_kuespasien = ob.getString("id_kuespasien");
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
            adapterKuesioner.notifyDataSetChanged();
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

    private void dialogBox(String judul, String pesan){
        //ini munculkan dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(KuesionerActivity.this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        dialogBox("Selesaikan kuesioner!", "Maaf, anda belum bisa keluar. Mohon selesaikan kuesioner terlebih dahulu.");
    }
}
