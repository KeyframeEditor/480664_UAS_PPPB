package com.example.intine_panik_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.intine_panik_uts.database.Note;
//import com.example.intine_panik_uts.database.NoteDao;
//import com.example.intine_panik_uts.database.NoteRoomDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TambahBeritaActivity extends AppCompatActivity {

    private Spinner spinnerPreferensi;
    private String textSpinnerPreferensi;
    private Button btnSubmit;
    private EditText editTextJudul;
    private EditText editTextkonten;
    private EditText editTextTargetUsia;
    private TextView textTitle;

    DatabaseReference databaseReferenceBerita = FirebaseDatabase.getInstance().getReference("beritaDB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnSubmit = findViewById(R.id.btnSubmit);
        editTextJudul = findViewById(R.id.editTextJudulBerita);
        editTextkonten = findViewById(R.id.editTextIsiBerita);
        editTextTargetUsia = findViewById(R.id.editTextTargetUsia);
        textTitle = findViewById(R.id.textTitle);

        spinnerPreferensi = findViewById(R.id.spinnerPreferensi);
        //set array to an adapter to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tambah_berita_array, android.R.layout.simple_spinner_item);
        spinnerPreferensi.setAdapter(adapter);

        Intent intent = getIntent();
        String textAction = intent.getStringExtra("action");
        String berita_key = intent.getStringExtra("berita_key");
        System.out.println(textAction);

        if (!Objects.equals(textAction, "Tambah")){
            System.out.println("+++++++++++++++++++++++++");
            String edit_judul = intent.getStringExtra("edit_judul");
            String edit_kategori = intent.getStringExtra("edit_kategori");
            String edit_konten = intent.getStringExtra("edit_konten");
            String edit_target_usia = intent.getStringExtra("edit_target_usia");

            editTextJudul.setText(edit_judul);
            spinnerPreferensi.setSelection(adapter.getPosition(edit_kategori));
            editTextkonten.setText(edit_konten);
            editTextTargetUsia.setText(edit_target_usia);
        }

        textTitle.setText(textAction+" Berita");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pref_Kategori = String.valueOf(spinnerPreferensi.getSelectedItem());
                String judul = editTextJudul.getText().toString();
                String konten = editTextkonten.getText().toString();
                String targetUsia = editTextTargetUsia.getText().toString();
                String tanggalBeritaDibuat = formattedDate;

                Berita berita = new Berita();
                    berita.setJudul(judul);
                    berita.setKonten(konten);
                    berita.setTargetUsia(targetUsia);
                    berita.setTanggalRilis(tanggalBeritaDibuat);
                    berita.setKategori(pref_Kategori);
                    berita.setCreatedBy(LoginActivity.getUsername);

                if (berita_key.equals("default")){
                    databaseReferenceBerita.push().setValue(berita);
                    Toast.makeText(TambahBeritaActivity.this, "Berhasil menambahkan berita", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReferenceBerita.child(berita_key).setValue(berita);
                    Toast.makeText(TambahBeritaActivity.this, "Berhasil edit berita", Toast.LENGTH_SHORT).show();
                }
                Intent sec_intent = new Intent();
                setResult(RESULT_OK, sec_intent);
                finish();
            }
        });
    }

}