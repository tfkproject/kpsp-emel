package com.ta.emilia.kpsp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ta.emilia.kpsp.R;
import com.ta.emilia.kpsp.model.ItemKuesioner;

import java.util.List;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class AdapterKuesioner extends RecyclerView.Adapter<AdapterKuesioner.ViewHolder>{

    List<ItemKuesioner> items;
    Context context;
    tombolListener listener;

    public AdapterKuesioner (Context context, List<ItemKuesioner> items, tombolListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public AdapterKuesioner.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kuesioner, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //disini set view
        Glide.with(context).load(items.get(position).getLink_gambar()).into(holder.imageSoal);
        holder.textSoal.setText(items.get(position).getSoal());

        holder.btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelected(position, items.get(position).getId(), "Y");
            }
        });

        holder.btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelected(position, items.get(position).getId(), "T");
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView imageSoal;
        TextView textSoal;
        Button btnYa, btnTidak;

        public ViewHolder(View itemView){
            super(itemView);
            //disini pemanggilan view
            card = (CardView) itemView.findViewById(R.id.cardView) ;
            imageSoal = (ImageView) itemView.findViewById(R.id.iv_soal);
            textSoal = (TextView) itemView.findViewById(R.id.tv_soal);

            btnYa = (Button) itemView.findViewById(R.id.btn_kues_ya);
            btnTidak = (Button) itemView.findViewById(R.id.btn_kues_tdk);
        }
    }

    public interface tombolListener {
        void onSelected(int position, String id_kuesioner, String jawaban);
    }
}
