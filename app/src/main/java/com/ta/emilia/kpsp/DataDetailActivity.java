package com.ta.emilia.kpsp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.ta.emilia.kpsp.adapter.AdapterPemeriksaanPasien;
import com.ta.emilia.kpsp.model.ItemPemeriksaanPasien;
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
 * Created by Toshibha on 30/03/2018.
 */
public class DataDetailActivity extends AppCompatActivity {

    TextView txtNama, txtTglLahir, txtJk, txtPddk, txtOrtu, txtAlamat, txtTglPeriksa;
    String[] rentang_detik = new String[5];
    private ProgressDialog pDialog;

    List<ItemPemeriksaanPasien> items;
    AdapterPemeriksaanPasien adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    ArrayList<Entry> entries;
    private LineChart chart;

    private static String url = Config.HOST+"data_pemeriksaan_pasien.php";
    private static String url_graf = Config.HOST+"untuk_graf.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pasien);

        //buat tombol back di ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String nama = getIntent().getStringExtra("key_nama");
        String tgl_lahir = getIntent().getStringExtra("key_tgl_lahir");
        String jk = getIntent().getStringExtra("key_jk");
        String pddkn = getIntent().getStringExtra("key_pddkn");
        String n_ortu = getIntent().getStringExtra("key_n_ortu");
        String alamat = getIntent().getStringExtra("key_alamat");
        String tgl_periksa = getIntent().getStringExtra("key_tgl_periksa");

        getSupportActionBar().setTitle("Data Pemeriksaan");
        getSupportActionBar().setSubtitle(nama);

        txtNama = (TextView) findViewById(R.id.txt_nama);
        txtNama.setText(nama);
        txtTglLahir = (TextView) findViewById(R.id.txt_tgl_lahir);
        txtTglLahir.setText(tgl_lahir);
        txtJk = (TextView) findViewById(R.id.txt_jk);
        txtJk.setText(jk);
        txtPddk = (TextView) findViewById(R.id.txt_pddk);
        txtPddk.setText(pddkn);
        txtOrtu = (TextView) findViewById(R.id.txt_n_ortu);
        txtOrtu.setText(n_ortu);
        txtAlamat = (TextView) findViewById(R.id.txt_alamat);
        txtAlamat.setText(alamat);
        txtTglPeriksa = (TextView) findViewById(R.id.txt_tgl_periksa);
        txtTglPeriksa.setText(tgl_periksa);

        //panggil RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //set LayoutManager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        //new ambilData().execute();

        //set adapter
        adapter = new AdapterPemeriksaanPasien(DataDetailActivity.this, items);
        recyclerView.setAdapter(adapter);

        chart = (LineChart) findViewById(R.id.chart);

        entries = new ArrayList<>();
    }

    private class ambilData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien;

        public ambilData(String id_pasien){
            this.id_pasien = id_pasien;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DataDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_pasien", id_pasien);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("field");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String id_periksa = c.getString("id_periksa");
                            String id_pasien = c.getString("id_pasien");
                            String tgl_periksa = c.getString("tgl_periksa");
                            String kpsp_ke = c.getString("kpsp_ke");
                            String tujuan = c.getString("tujuan");
                            String hasil = c.getString("hasil");

                            items.add(new ItemPemeriksaanPasien(id_periksa, id_pasien, tujuan, tgl_periksa, kpsp_ke, hasil));

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
            adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }

    }

    private class ambilGrafik extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien;

        public ambilGrafik(String id_pasien){
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
                detail.put("id_pasien", id_pasien);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url_graf, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("field");

                        entries.clear();
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String tanggal = c.getString("tanggal");
                            int hasil = c.getInt("hasil");

                            entries.add(new Entry(i, hasil));
                            rentang_detik[i] = tanggal;
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
            adapter.notifyDataSetChanged();
            pDialog.dismiss();

            LineDataSet dataSet = new LineDataSet(entries, "Perkembangan KPSP");
            dataSet.setColor(ContextCompat.getColor(DataDetailActivity.this, R.color.colorPrimary));
            dataSet.setValueTextColor(ContextCompat.getColor(DataDetailActivity.this, R.color.colorPrimaryDark));

            //****
            // Controlling X axis
            XAxis xAxis = chart.getXAxis();
            // Set the xAxis position to bottom. Default is top
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //Customizing x axis value
            //final String[] rentang_detik = new String[]{"Test ke 1", "Test ke 2", "Test ke 3", "Test ke 4", "Test ke 5"};

            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return rentang_detik[(int) value];
                }
            };
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            //***
            // Controlling right side of y axis
            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(false);

            //***
            // Controlling left side of y axis
            YAxis yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setGranularity(1f);

            // Setting Data
            LineData data = new LineData(dataSet);
            chart.setData(data);
            chart.animateX(500);
            //refresh
            chart.invalidate();
            chart.getDescription().setText("1 = Penyimpangan\n2 = Meragukan\n3 = Sesuai");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_pasien, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == android.R.id.home){
            finish();
        }
        if(id_menu == R.id.action_refresh){
            items.clear();
            String id_pasien = getIntent().getStringExtra("key_id_pasien");
            new ambilData(id_pasien).execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        items.clear();
        String id_pasien = getIntent().getStringExtra("key_id_pasien");
        new ambilData(id_pasien).execute();
        new ambilGrafik(id_pasien).execute();
    }
}
