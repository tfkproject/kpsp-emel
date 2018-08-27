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
import com.ta.emilia.kpsp.R;
import com.ta.emilia.kpsp.model.ItemPemeriksaan;

import java.util.List;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class AdapterPemeriksaan extends RecyclerView.Adapter<AdapterPemeriksaan.ViewHolder>{

    List<ItemPemeriksaan> items;
    Context context;

    public AdapterPemeriksaan(Context context, List<ItemPemeriksaan> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public AdapterPemeriksaan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_pemeriksaan, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DataDetailActivity.class);
                intent.putExtra("key_id_periksa", items.get(position).getId_pemeriksaan());
                intent.putExtra("key_nama", items.get(position).getNama());
                intent.putExtra("key_tgl_lahir", items.get(position).getTgl_lahir());
                intent.putExtra("key_jk", items.get(position).getJk());
                intent.putExtra("key_pddkn", items.get(position).getPddkn());
                intent.putExtra("key_n_ortu", items.get(position).getN_ortu());
                intent.putExtra("key_alamat", items.get(position).getAlamat());
                intent.putExtra("key_tgl_periksa", items.get(position).getTgl_periksa());
                intent.putExtra("key_tujuan", items.get(position).getTujuan());
                intent.putExtra("key_kpsp_ke", items.get(position).getKpsp_ke());
                intent.putExtra("key_hasil", items.get(position).getHasil());
                context.startActivity(intent);
            }
        });

        holder.txtNama.setText(items.get(position).getNama());
        holder.txtTglLahir.setText(items.get(position).getTgl_lahir());
        holder.txtTglPeriksa.setText(items.get(position).getTgl_periksa());
        holder.txtKpsp.setText(items.get(position).getKpsp_ke());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView txtNama, txtTglLahir, txtTglPeriksa, txtKpsp;

        public ViewHolder(View itemView){
            super(itemView);
            //disini pemanggilan view
            card = (CardView) itemView.findViewById(R.id.cardView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtTglLahir = (TextView) itemView.findViewById(R.id.txt_tgl_lahir);
            txtTglPeriksa = (TextView) itemView.findViewById(R.id.txt_tgl_periksa);
            txtKpsp = (TextView) itemView.findViewById(R.id.txt_kpsp);
        }
    }

}
