package com.ta.emilia.kpsp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.emilia.kpsp.util.Config;
import com.ta.emilia.kpsp.util.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toshibha on 02/03/2018.
 */
public class BiodataActivity extends AppCompatActivity {
    private Calendar kalender;
    private int hari, bulan, tahun;
    static final int DATE_DIALOG_LHR = 1;
    static final int DATE_DIALOG_PRK = 2;

    TextView txt_tanggal, txt_tgl_periksa;
    EditText txt_Nama, txt_Pddk, txt_NamaOrtu, txt_Alamat;
    Button btnTgl, btnTglPrk, btn_simpan;

    Spinner spPddk, spJenisK, spTujuan;

    long umur_bulan, umur_hari;

    private ProgressDialog pDialog;

    String tgl_lahir, tgl_periksa;

    private static String url = Config.HOST+"inputbiodata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        //buat tombol back di ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // kalender
        kalender    = Calendar.getInstance();
        hari        = kalender.get(Calendar.DAY_OF_MONTH);
        bulan       = kalender.get(Calendar.MONTH);
        tahun       = kalender.get(Calendar.YEAR);

        txt_Nama = (EditText) findViewById(R.id.txtNama);
        //txt_Pddk = (EditText) findViewById(R.id.txtPddk);

        // Spinner Drop down elements
        List<String> pddk = new ArrayList<String>();
        pddk.add("Pilih Pendidikan");
        pddk.add("PAUD");
        pddk.add("TK");

        // Creating adapter for spinner
        ArrayAdapter<String> pddkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pddk);

        // Drop down layout style - list view with radio button
        pddkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPddk = (Spinner) findViewById(R.id.spPddk);
        spPddk.setAdapter(pddkAdapter);

        txt_NamaOrtu = (EditText) findViewById(R.id.txtNamaOrtu);
        txt_Alamat = (EditText) findViewById(R.id.txtAlmt);

        txt_tanggal = (TextView) findViewById(R.id.txtTgl);

        btnTgl = (Button) findViewById(R.id.btnTgl);
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDialog(DATE_DIALOG_LHR); }
        });

        // Spinner Drop down elements
        List<String> jk = new ArrayList<String>();
        jk.add("Pilih Jenis Kelamin");
        jk.add("Laki-laki");
        jk.add("Perempuan");

        // Creating adapter for spinner
        ArrayAdapter<String> jkAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jk);

        // Drop down layout style - list view with radio button
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner JK (isi liat di values > string>
        spJenisK = (Spinner) findViewById(R.id.spJk);
        spJenisK.setAdapter(jkAdapter);

        txt_tgl_periksa = (TextView) findViewById(R.id.txtTgp);

        btnTglPrk = (Button) findViewById(R.id.btnTglPrk);
        btnTglPrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_PRK);
            }
        });

        // Spinner Drop down elements
        List<String> tj = new ArrayList<String>();
        tj.add("Pilih Tujuan");
        tj.add("Skrinning perkembangan anak");
        tj.add("Skrinning potensi umum");
        tj.add("Skrinning kesiapan masuk sekolah");

        // Creating adapter for spinner
        ArrayAdapter<String> tjAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tj);

        // Drop down layout style - list view with radio button
        tjAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner Tujuan (isi liat di values > string>
        spTujuan = (Spinner) findViewById(R.id.spTjn);
        spTujuan.setAdapter(tjAdapter);

        btn_simpan = (Button) findViewById(R.id.btnSimpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = txt_Nama.getText().toString();
                //tgl_lahir;
                String jk = String.valueOf(spJenisK.getSelectedItem());
                String bln = String.valueOf(umur_bulan);
                String hri = String.valueOf(umur_hari);;
                String pddk = txt_Pddk.getText().toString();
                String ortu = txt_NamaOrtu.getText().toString();
                String alamat = txt_Alamat.getText().toString();
                //tgl_periksa;
                String tujuan = String.valueOf(spTujuan.getSelectedItem());

                new prosesDaftar(nama, tgl_lahir, jk, bln, hri, pddk, ortu, alamat, tgl_periksa, tujuan).execute();
            }
        });


    }

    private class prosesDaftar extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien, id_periksa;
        private String nama, tanggal_lhr, jenis_klm, bulan, hari, pddk, nm_ortu, alamat, tanggal_prk, tujuan;

        public prosesDaftar(String nama, String tanggal_lhr, String jenis_klm, String bulan, String hari, String pddk, String nm_ortu, String alamat, String tanggal_prk, String tujuan){
            this.nama = nama;
            this.tanggal_lhr = tanggal_lhr;
            this.jenis_klm = jenis_klm;
            this.bulan = bulan;
            this.hari = hari;
            this.pddk = pddk;
            this.nm_ortu = nm_ortu;
            this.alamat = alamat;
            this.tanggal_prk = tanggal_prk;
            this.tujuan = tujuan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BiodataActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("nama", nama);
                detail.put("tgl_lahir", tanggal_lhr);
                detail.put("jk", jenis_klm);
                detail.put("bulan", bulan);
                detail.put("hari", hari);
                detail.put("pddkn", pddk);
                detail.put("n_ortu", nm_ortu);
                detail.put("almt", alamat);
                detail.put("tgl_prk", tanggal_prk);
                detail.put("tujuan", tujuan);

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
                        psn = ob.getString("message");
                        id_pasien = ob.getString("id_pasien");
                        id_periksa = ob.getString("id_periksa");
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
            pDialog.dismiss();
            if(scs == 1){
                Intent intent = new Intent(BiodataActivity.this, KuesionerActivity.class);
                intent.putExtra("key_id_pasien", id_pasien);
                intent.putExtra("key_id_periksa", id_periksa);
                intent.putExtra("key_nama_pasien", nama);
                //intent.putExtra("key_bulan_pasien", bulan);
                startActivity(intent);
            }
            else{
                Toast.makeText(BiodataActivity.this, psn, Toast.LENGTH_SHORT).show();
            }
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
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_LHR:
                return new DatePickerDialog(this, datePickerListenerLhr, tahun, bulan, hari);
            case DATE_DIALOG_PRK:
                return new DatePickerDialog(this, datePickerListenerPrk, tahun, bulan, hari);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerLhr = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            NumberFormat f = new DecimalFormat("00");
            tgl_lahir = selectedYear + "-" + (f.format(selectedMonth + 1)) + "-" + f.format(selectedDay);
            txt_tanggal.setText(tgl_lahir);
            //lanjut proses umur disini (dalam bulan dan hari)
            kalkulasikanUmur(tgl_lahir);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerPrk = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            NumberFormat f = new DecimalFormat("00");
            tgl_periksa = selectedYear + "-" + (f.format(selectedMonth + 1)) + "-" + f.format(selectedDay);
            txt_tgl_periksa.setText(tgl_periksa);
        }
    };

    private void kalkulasikanUmur(String tanggal_lahir){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //A = deteksi tanggal sekarang
        long tgl_now = kalender.getTime().getTime();

        //B = deteksi tanggal lahir
        try {
            long tgl_lahir = df.parse(tanggal_lahir).getTime();

            //Umur = A - B;
            long umur = tgl_now - tgl_lahir;

            //pecahkan kedalam bulan dan hari
            umur_hari = 0;
            umur_bulan = 0;
            if (umur > 0) {
                umur_hari = (umur / 86400000) % 30;
                umur_bulan = ((umur / 86400000) - hari) / 30;
            }

            if (umur_bulan < 3 || umur_bulan > 75) {
                if (umur_bulan < 3) {
                    dialogBox("Dibawah 3 bulan!", "Maaf, umur anak anda "+ String.valueOf(umur_bulan) +" bulan "+ String.valueOf(umur_hari) +" hari, masih berusia di bawah 3 bulan maka belum dapat melakukan KPSP");
                } else {
                    dialogBox("Diluar batas!", "Sepertinya umur anak anda telah melampaui batas usia untuk dapat melakukan KPSP maka disarankan untuk konsultasi langsung ke dokter dengan dokter spesialis anak atau ke rumah sakit dengan fasilitas klinik tumbuh kembang.");
                }
            }
            //ketentuan pendidikan //
            if(umur_bulan >= 36 && umur_bulan <=48 ){ //36 = 3 th, 48 = 4 th
                spPddk.setSelection(1); // 1 = PAUD
            }
            if(umur_bulan > 48 && umur_bulan <= 72 ){ //48 = 4 th, 72 = 6 th
                spPddk.setSelection(2); // 2 = TK
            }
            // ketentuan tujuan //
            if(umur_bulan >= 0 && umur_bulan <= 72){
                spTujuan.setSelection(1);
            }
            if(umur_bulan >= 12 && umur_bulan <= 72){ // 12 = 1 tahun
                spTujuan.setSelection(2);
            }
            if(umur_bulan >= 66 && umur_bulan <= 72){ //66 = 5,5 tahun
                spTujuan.setSelection(3);
            }
            Toast.makeText(BiodataActivity.this, "Umur anak anda " + String.valueOf(umur_bulan) + " Bulan " + String.valueOf(umur_hari) + " Hari", Toast.LENGTH_LONG).show();

            //Toast.makeText(BiodataActivity.this, "Sekarang tgl: "+df.format(tgl), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void dialogBox(String judul, String pesan){
        //ini munculkan dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(BiodataActivity.this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
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

