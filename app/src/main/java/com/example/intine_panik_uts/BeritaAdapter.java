package com.example.intine_panik_uts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {

    private final ArrayList<Berita> values;
    private final LayoutInflater inflater;
    private NoteDao mNotesDao;
    private Executor executorService;

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
        executorService = Executors.newSingleThreadExecutor();

        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(holder.itemView.getContext());
        mNotesDao = db.noteDao();
        holder.judul.setText(dataBerita.getJudul());

        holder.kategori.setText(dataBerita.getKategori());
        holder.tanggalRilis.setText(dataBerita.getTanggalRilis());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),BeritaDetailActivity.class);
                intent.putExtra("judul",dataBerita.getJudul());
                intent.putExtra("kategori",dataBerita.getKategori());
                intent.putExtra("konten",dataBerita.getKonten());
                intent.putExtra("created_by",dataBerita.getCreatedBy());
                intent.putExtra("berita_key",dataBerita.getBerita_key());
                intent.putExtra("target_usia",dataBerita.getTargetUsia());
                intent.putExtra("note_berita_id","default");

                mNotesDao.getAllNotes().observe((LifecycleOwner) holder.itemView.getContext(), notes -> {
                    boolean isFound = false;
                    for (Note dataNotes : notes){
                            Log.d("", "+++++++++++ ADAPTER BERITA OWNER LOOP ++++++++++++++++++");
                            Log.d("Found Matching Owner: ", dataNotes.getOwner());
                            Log.d("Found Matching User: ", LoginActivity.getUsername);
                            Log.d("Found Matching OWNER: ", dataNotes.getBerita_id());
                            Log.d("", "+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        if(dataNotes.getBerita_id().equals(dataBerita.getBerita_key()) && dataNotes.getOwner().equals(LoginActivity.getUsername)){
                            intent.putExtra("note_berita_id",dataNotes.getBerita_id());
                            intent.putExtra("is_liked",true);
                            isFound = true;
                            view.getContext().startActivity(intent);
                            break;
                        }
                    }
                    if (!isFound){
                        intent.putExtra("note_berita_id","default");
                        intent.putExtra("is_liked",false);
                        view.getContext().startActivity(intent);
                    }
                });
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            kategori = itemView.findViewById(R.id.kategori);
            tanggalRilis = itemView.findViewById(R.id.tanggalRilis);
        }
    }
}