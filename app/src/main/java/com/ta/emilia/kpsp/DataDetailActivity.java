package com.ta.emilia.kpsp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Toshibha on 30/03/2018.
 */
public class DataDetailActivity extends AppCompatActivity {

    Button btn_print;

    TextView txtNama, txtTglLahir, txtJk, txtPddk, txtOrtu, txtAlamat, txtTglPeriksa, txtTujuan, txtKpsp, txtHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pasien);

        //buat tombol back di ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final String id_periksa = getIntent().getStringExtra("key_id_periksa");
        String nama = getIntent().getStringExtra("key_nama");
        String tgl_lahir = getIntent().getStringExtra("key_tgl_lahir");
        String jk = getIntent().getStringExtra("key_jk");
        String pddkn = getIntent().getStringExtra("key_pddkn");
        String n_ortu = getIntent().getStringExtra("key_n_ortu");
        String alamat = getIntent().getStringExtra("key_alamat");
        String tgl_periksa = getIntent().getStringExtra("key_tgl_periksa");
        String tujuan = getIntent().getStringExtra("key_tujuan");
        String kpsp_ke = getIntent().getStringExtra("key_kpsp_ke");
        String hasil = getIntent().getStringExtra("key_hasil");

        getSupportActionBar().setTitle("Data Pemeriksaan:");
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
        txtTujuan = (TextView) findViewById(R.id.txt_tujuan);
        txtTujuan.setText(tujuan);
        txtKpsp = (TextView) findViewById(R.id.txt_kpsp_ke);
        txtKpsp.setText(kpsp_ke);
        txtHasil = (TextView) findViewById(R.id.txt_hasil);
        txtHasil.setText(hasil);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
