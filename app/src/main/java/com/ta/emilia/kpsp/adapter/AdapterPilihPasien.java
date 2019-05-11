package com.ta.emilia.kpsp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ta.emilia.kpsp.DataDetailActivity;
import com.ta.emilia.kpsp.PeriksaBaruActivity;
import com.ta.emilia.kpsp.R;
import com.ta.emilia.kpsp.model.ItemPemeriksaan;

import java.util.List;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class AdapterPilihPasien extends RecyclerView.Adapter<AdapterPilihPasien.ViewHolder>{

    List<ItemPemeriksaan> items;
    Context context;

    public AdapterPilihPasien(Context context, List<ItemPemeriksaan> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public AdapterPilihPasien.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_pemeriksaan, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PeriksaBaruActivity.class);
                intent.putExtra("key_id_pasien", items.get(position).getId_pasien());
                intent.putExtra("key_nama", items.get(position).getNama());
                context.startActivity(intent);
            }
        });

        holder.txtNama.setText(items.get(position).getNama());
        holder.txtTglLahir.setText(items.get(position).getTgl_lahir());
        holder.txtTglPeriksa.setText(items.get(position).getTgl_periksa());
        holder.txtNamaOrtu.setText(items.get(position).getN_ortu());
        holder.txtKpsp.setText("");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView txtNama, txtTglLahir, txtTglPeriksa, txtKpsp, txtNamaOrtu;

        public ViewHolder(View itemView){
            super(itemView);
            //disini pemanggilan view
            card = (CardView) itemView.findViewById(R.id.cardView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtTglLahir = (TextView) itemView.findViewById(R.id.txt_tgl_lahir);
            txtTglPeriksa = (TextView) itemView.findViewById(R.id.txt_tgl_periksa);
            txtKpsp = (TextView) itemView.findViewById(R.id.txt_kpsp);
            txtNamaOrtu = (TextView) itemView.findViewById(R.id.txt_nama_ortu);
        }
    }

}
