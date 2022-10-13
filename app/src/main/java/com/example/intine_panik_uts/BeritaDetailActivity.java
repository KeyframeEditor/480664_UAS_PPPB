package com.example.intine_panik_uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class BeritaDetailActivity extends AppCompatActivity {

    TextView judul;
    TextView kategori;
    TextView konten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_detail);

        judul = findViewById(R.id.judul);
        kategori = findViewById(R.id.kategori);
        konten = findViewById(R.id.konten);

        Intent intent = getIntent();
        String textJudul = intent.getStringExtra("judul");
        String textKategori = intent.getStringExtra("kategori");
        String textKonten = intent.getStringExtra("konten");

        judul.setText(textJudul);
        kategori.setText(textKategori);
        konten.setText(textKonten);
    }
}