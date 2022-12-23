package com.example.intine_panik_uts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BeritaDetailActivity extends AppCompatActivity {

    private TextView judul;
    private TextView kategori;
    private TextView konten;
    private TextView createdBy;
    private FloatingActionButton buttonLike;
    private FloatingActionButton buttonEdit;
    private FloatingActionButton buttonDelete;
    private FloatingActionButton buttonBack;

    private NoteDao mNotesDao;
    private Executor executorService;

    private SharedPreferences mSharedPref;
    private final String sharedPrefFile = "com.example.intine_panik_uts";
    private String getUsername;
    private final String USERNAME_KEY = "usernamekey";

    private boolean local_like;

    DatabaseReference databaseReferenceBerita = FirebaseDatabase.getInstance().getReference("beritaDB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("like", "like", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        ArrayList<Note> listBerita = new ArrayList<Note>();

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        executorService = Executors.newSingleThreadExecutor();
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(this);
        mNotesDao = db.noteDao();

        String username = LoginActivity.getUsername;

        judul = findViewById(R.id.judul);
        kategori = findViewById(R.id.kategori);
        konten = findViewById(R.id.konten);
        createdBy = findViewById(R.id.createdBy);

        buttonLike = findViewById(R.id.buttonLike);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonBack = findViewById(R.id.buttonBack);

        mSharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUsername = mSharedPref.getString(USERNAME_KEY,"");

        Intent intent = getIntent();
        String textJudul = intent.getStringExtra("judul");
        String textKategori = intent.getStringExtra("kategori");
        String textKonten = intent.getStringExtra("konten");
        String textCreatedBy = intent.getStringExtra("created_by");
        String textTargetUsia = intent.getStringExtra("target_usia");
        String berita_key = intent.getStringExtra("berita_key");
        String note_berita_id = intent.getStringExtra("note_berita_id");

        Bundle bundle = getIntent().getExtras();
        boolean is_liked = bundle.getBoolean("is_liked");

        if (username.equals(textCreatedBy)){
            buttonEdit.show();
            buttonDelete.show();
        }else{
            buttonEdit.hide();
            buttonDelete.hide();
        }

        Log.d("GET INTENT ID BERITA", note_berita_id);
        Log.d("BERITA IS LIKED", Boolean.toString(is_liked));

        //set like button color
        if (!is_liked){
            buttonLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            local_like = false;
        }else{
            buttonLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
            local_like = true;
        }

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(local_like){ //ketika state like sebelumnya LIKED, maka eksekusi ini:
                    mNotesDao.getAllNotes().observe((LifecycleOwner) view.getContext(), notes -> {
                        for (Note dataNotes : notes){
                            if(dataNotes.getBerita_id().equals(note_berita_id)){
                                deleteData(dataNotes);
                                break;
                            }
                        }
                        Log.d("LIKE", "DELETED");
                    });
                    buttonLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                }else if(!local_like){ //ketika state like sebelumnya TIDAK LIKE, maka eksekusi ini:
                    Note note = new Note(textJudul, berita_key, username);
                    Log.d("LIKE", "LIKED");
                    Log.d("LIKED BY", username);
                    insertData(note);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(BeritaDetailActivity.this, "like");
                    builder.setContentTitle(username+" menyukai berita!");
                    builder.setContentText(textJudul);
                    builder.setSmallIcon(R.drawable.ic_baseline_newspaper_24);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(BeritaDetailActivity.this);
                    managerCompat.notify(1, builder.build());

                    buttonLike.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_200)));
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeritaDetailActivity.this,TambahBeritaActivity.class);
                intent.putExtra("action","Edit");
                intent.putExtra("berita_key",berita_key);
                intent.putExtra("edit_judul",textJudul);
                intent.putExtra("edit_kategori",textKategori);
                intent.putExtra("edit_konten",textKonten);
                intent.putExtra("edit_target_usia",textTargetUsia);

                startActivityForResult(intent,4);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceBerita.child(berita_key).removeValue();
                finish();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        judul.setText(textJudul);
        kategori.setText(textKategori);
        konten.setText(textKonten);
        createdBy.setText("Created by: "+textCreatedBy);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }

    private void insertData(Note note){
        executorService.execute(() -> mNotesDao.insert(note));
    }

    private void deleteData(Note note){
        executorService.execute(() -> mNotesDao.delete(note));
    }
}
