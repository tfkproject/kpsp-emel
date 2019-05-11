package com.ta.emilia.kpsp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ta.emilia.kpsp.DataDetailActivity;
import com.ta.emilia.kpsp.R;
import com.ta.emilia.kpsp.model.ItemPemeriksaan;
import com.ta.emilia.kpsp.model.ItemPemeriksaanPasien;
import com.ta.emilia.kpsp.util.Config;
import com.ta.emilia.kpsp.util.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class AdapterPemeriksaanPasien extends RecyclerView.Adapter<AdapterPemeriksaanPasien.ViewHolder>{

    private static String url = Config.HOST+"hapus_pemeriksaan.php";
    private ProgressDialog pDialog;
    List<ItemPemeriksaanPasien> items;
    Context context;

    public AdapterPemeriksaanPasien(Context context, List<ItemPemeriksaanPasien> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public AdapterPemeriksaanPasien.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pemeriksaan, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtTglPeriksa.setText(items.get(position).getTgl_periksa());
        holder.txtTujuan.setText(items.get(position).getTujuan());
        holder.txtKpsp.setText(items.get(position).getKpsp_ke());
        holder.txtHasil.setText(items.get(position).getHasil());
        holder.btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buka print
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.HOST+"pdf.php?id_periksa="+items.get(position).getId_pemeriksaan()));
                context.startActivity(browserIntent);
            }
        });

        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin ingin menghapus data?");

                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        new hapusData(items.get(position).getId_pemeriksaan()).execute();
                    }
                });

                builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_print, btn_hapus;
        TextView txtTujuan, txtHasil, txtTglPeriksa, txtKpsp;

        public ViewHolder(View itemView){
            super(itemView);
            //disini pemanggilan view
            txtTujuan = (TextView) itemView.findViewById(R.id.txt_tujuan);
            txtKpsp = (TextView) itemView.findViewById(R.id.txt_kpsp_ke);
            txtTglPeriksa = (TextView) itemView.findViewById(R.id.txt_tgl_periksa);
            txtHasil = (TextView) itemView.findViewById(R.id.txt_hasil);

            btn_print = (Button) itemView.findViewById(R.id.btnPrint);
            btn_hapus = (Button) itemView.findViewById(R.id.btnHapus);
        }
    }

    private class hapusData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_periksa;

        public hapusData(String id_periksa){
            this.id_periksa = id_periksa;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_periksa", id_periksa);

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
            if (scs == 1) {
                Toast.makeText(context, ""+psn, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, ""+psn, Toast.LENGTH_SHORT).show();
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


}
