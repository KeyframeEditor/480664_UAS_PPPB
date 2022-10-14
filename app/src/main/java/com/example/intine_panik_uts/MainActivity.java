package com.example.intine_panik_uts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String pref_TanggalLahir;
    public String pref_Kategori;
    private TextView textViewTanggal;
    private TextView textViewKategori;
    private BeritaAdapter beritaAdapter;
    private RecyclerView mainRecyclerView;

    public ArrayList<Berita> listBerita = new ArrayList<Berita>();
    public ArrayList<Berita> listFilteredBerita = new ArrayList<Berita>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        Intent intent = getIntent();
        pref_TanggalLahir = intent.getStringExtra("pref_TanggalLahir");
        pref_Kategori = intent.getStringExtra("pref_Kategori");

        addBerita();

        //Ambil umur (tahun) dari user
        String TanggalLahirUser[] = pref_TanggalLahir.split("/");
        int TahunLahirUser = 2022-Integer.parseInt(TanggalLahirUser[2]);
        Toast.makeText(this, String.valueOf(TahunLahirUser), Toast.LENGTH_SHORT).show();

        for (Berita dataBerita : listBerita){
            Integer targetUsiaBerita = Integer.valueOf(dataBerita.getTargetUsia());
            if(targetUsiaBerita <= TahunLahirUser && dataBerita.getKategori().toLowerCase().equals(pref_Kategori.toLowerCase())){
                listFilteredBerita.add(dataBerita);
            }
        }

        beritaAdapter = new BeritaAdapter(this,listFilteredBerita);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setAdapter(beritaAdapter);
    }

    private void addBerita(){
        listBerita.add(new Berita("Puan Maharani dan Gambar Sara Lainnya Dijadikan Wallpaper Oleh Mahasiswa","Salah satu mahasiswa di suatu kampus di Yogyakarta memberi wallpaper sara pada semua komputer di suatu lab. Hal ini membuat pihak kampus meluncurkan peraturan baru kepada mahasiswa ketika menggunakan lab komputer.","17","12/12/12","Gaming"));
        listBerita.add(new Berita("Kreator","Kreator game Yu Gi Oh! meninggal dunia dikarenakan menyelamatkan 3 nyawa orang","17","12/12/12","Technology"));
        listBerita.add(new Berita("Artis Rex Orange County Tergugat Pelecehan Seksual","Rex Orange County menggagalkan konsernya dikarenakan tergugat masalah pelecehan seksual 2 kali pada 1 juni 2022 di End West. Pihak yang berwajib terpaksa menghentikan konser.","5","10/10/22","Artist"));
        listBerita.add(new Berita("Seorang Mahasiswa Tewas Jatuh dari Lantai 11 Hotel","Pihak kepolisian mengungkap, korban memiliki identitas inisial TSR. Korban berjenis kelamin laki-laki ini bukanlah warga setempat atau warga Yogyakarta. Hasil identifikasi yang dilakukan oleh pihak kepolisian korban diketahui adalah seorang mahasiswa UGM Yogyakarta. Polisi juga menemukan sebuah surat keterangan dari psikolog di dalam tas yang dibawa oleh korban.","17","11/10/22","Regional"));
        listBerita.add(new Berita("Nintendo Umumkan Game Zelda Terbaru!","Link, adalah karakter utama dari game legendaris The Legend of Zelda. Video trailer tersebut dirilis untuk mengumumkan kehadiran game The Legend of Zelda: Tears of The Kingdom yang akan meluncur pada 12 Mei 2023.","10","14/09/22","Gaming"));
        listBerita.add(new Berita("Microsoft Designer Resmi Meluncur, Aplikasi Desain Pesaing Canva","Seperti rumor yang beredar sebelumnya, Microsoft Designer bisa membantu pengguna membuat desain postingan di media sosial, membuat desain undangan, brosur dan desain lainnya. Pengguna bisa membuatnya secara manual atau memanfaatkan template yang disediakan Designer.","14","13/10/22","Technology"));
        listBerita.add(new Berita("Windah Basudara Dipuji Netizen Kumpulkan Rp 300 Juta untuk Siswa SLB","Windah Basudara dihujani pujian netizen usai menggelar live streaming kumpulkan donasi bantu bocah viral di TikTok bernama Rahmat atau lebih dikenal Okky Boy. Dana Rp 300 jutaan berhasil dikumpulkan sang YouTuber.","14","12/10/22","Gaming"));
        listBerita.add(new Berita("Meta Quest Pro Resmi Diumumkan, Fitur Lengkap Harga Spektakuler"," Meta Quest Pro dijual dengan harga USD 1.499 atau sekitar Rp 22 juta. Harga tersebut bahkan hampir empat kali lipat lebih mahal dari Meta Quest 2 yang sudah mengalami kenaikan harga menjadi USD 499 atau sekitar Rp 7,6 juta beberapa waktu lalu.","12","13/10/22","Technology"));
    }
}