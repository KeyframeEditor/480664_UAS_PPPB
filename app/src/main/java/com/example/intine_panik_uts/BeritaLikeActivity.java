package com.example.intine_panik_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BeritaLikeActivity extends AppCompatActivity {

    private BeritaAdapter beritaAdapter;
    private RecyclerView mainRecyclerView;
    public static ArrayList<Berita> listBeritaLiked = new ArrayList<Berita>();

    private NoteDao mNotesDao;
    private Executor executorService;
//    private ListView listView;
    DatabaseReference databaseReferenceBerita = FirebaseDatabase.getInstance().getReference("beritaDB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_like);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

//        listView = findViewById(R.id.listView);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        executorService = Executors.newSingleThreadExecutor();

        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(this);
        mNotesDao = db.noteDao();

        getAllNotes();

        mNotesDao.getAllNotes().observe(this, notes -> {
            listBeritaLiked.clear();
            for (Note dataNote : notes){
                if(LoginActivity.getUsername.equals(dataNote.getOwner())){
                    Log.d(LoginActivity.getUsername, dataNote.getOwner());
                    for (Berita dataBerita : MainActivity.listFilteredBerita){
                        Log.d(dataBerita.getBerita_key(), dataNote.getBerita_id());
                        if (dataBerita.getBerita_key().equals(dataNote.getBerita_id())){
                            listBeritaLiked.add(dataBerita);
                            Log.d("BERITA DISUKAI", dataBerita.getJudul());
                        }
                    }
                }
            }
            if (beritaAdapter != null) {
                beritaAdapter.notifyDataSetChanged();
            }
            beritaAdapter = new BeritaAdapter(BeritaLikeActivity.this,listBeritaLiked);
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(BeritaLikeActivity.this));
            mainRecyclerView.setAdapter(beritaAdapter);
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Note item = (Note) adapterView.getAdapter().getItem(i);
//                deleteData(item);
//                return true;
//            }
//        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Note item = (Note) adapterView.getAdapter().getItem(i);
//
//                databaseReferenceBerita.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.hasChildren()){
//                            for (DataSnapshot currentData : snapshot.getChildren()){
//                                if (currentData.getKey().toString().equals(item.getBerita_id())){
//                                    Intent intent = new Intent(BeritaLikeActivity.this,BeritaDetailActivity.class);
//                                    intent.putExtra("judul",currentData.child("judul").getValue().toString());
//                                    intent.putExtra("kategori",currentData.child("kategori").getValue().toString());
//                                    intent.putExtra("konten",currentData.child("konten").getValue().toString());
//                                    intent.putExtra("created_by",currentData.child("createdBy").getValue().toString());
//                                    intent.putExtra("berita_key",currentData.getKey().toString());
//                                    intent.putExtra("target_usia",currentData.child("targetUsia").getValue().toString());
//                                    startActivity(intent);
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(BeritaLikeActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });


    }

    private void getAllNotes(){
        mNotesDao.getAllNotes().observe(this, notes -> {
//            ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
//                    android.R.layout.simple_expandable_list_item_1, notes);
//            listView.setAdapter(adapter);
        });
    }


    private void deleteData(Note note){
        executorService.execute(() -> mNotesDao.delete(note));
    }
}