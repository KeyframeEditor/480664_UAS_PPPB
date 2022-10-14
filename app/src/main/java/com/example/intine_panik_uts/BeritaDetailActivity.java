package com.example.intine_panik_uts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class BeritaDetailActivity extends AppCompatActivity {

    TextView judul;
    TextView kategori;
    TextView konten;
    ImageView gambarBerita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        judul = findViewById(R.id.judul);
        kategori = findViewById(R.id.kategori);
        konten = findViewById(R.id.konten);
        gambarBerita = findViewById(R.id.gambarBerita);

        Intent intent = getIntent();
        String textJudul = intent.getStringExtra("judul");
        String textKategori = intent.getStringExtra("kategori");
        String textKonten = intent.getStringExtra("konten");
        String namaGambar = intent.getStringExtra("gambar");

        judul.setText(textJudul);
        kategori.setText(textKategori);
        konten.setText(textKonten);
        int imageResource = getResources().getIdentifier("@drawable/"+namaGambar, null, "com.example.intine_panik_uts");
        gambarBerita.setImageResource(imageResource);
    }
}