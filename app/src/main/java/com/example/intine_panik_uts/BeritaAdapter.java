package com.example.intine_panik_uts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {

    private final ArrayList<Berita> values;
    private final LayoutInflater inflater;

    public BeritaAdapter(Context context, ArrayList<Berita> values) {
        this.values = values;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BeritaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_berita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaAdapter.ViewHolder holder, int position) {
        final Berita dataBerita = values.get(position);
        holder.judul.setText(dataBerita.getJudul());
        holder.kategori.setText(dataBerita.getKategori());
        holder.tanggalRilis.setText(dataBerita.getTanggalRilis());

        //display image rule
        int imageResource = holder.itemView.getContext().getResources().getIdentifier("@drawable/"+dataBerita.getImgName(), null, "com.example.intine_panik_uts");
        holder.gambarBerita.setImageResource(imageResource);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),BeritaDetailActivity.class);
                intent.putExtra("judul",dataBerita.getJudul());
                intent.putExtra("kategori",dataBerita.getKategori());
                intent.putExtra("konten",dataBerita.getKonten());
                intent.putExtra("gambar",dataBerita.getImgName());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul;
        TextView kategori;
        TextView tanggalRilis;
        ImageView gambarBerita;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            kategori = itemView.findViewById(R.id.kategori);
            tanggalRilis = itemView.findViewById(R.id.tanggalRilis);
            gambarBerita = itemView.findViewById(R.id.gambarBerita);
        }
    }
}