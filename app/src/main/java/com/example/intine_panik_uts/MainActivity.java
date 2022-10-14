package com.example.intine_panik_uts;

import androidx.appcompat.app.ActionBar;
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
    private BeritaAdapter beritaAdapter;
    private RecyclerView mainRecyclerView;

    public ArrayList<Berita> listBerita = new ArrayList<Berita>();
    public ArrayList<Berita> listFilteredBerita = new ArrayList<Berita>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide My App Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        Intent intent = getIntent();
        pref_TanggalLahir = intent.getStringExtra("pref_TanggalLahir");
        pref_Kategori = intent.getStringExtra("pref_Kategori");

        addBerita();

        //Ambil tahun lahir dari user
        String TanggalLahirUser[] = pref_TanggalLahir.split("/");
        int TahunLahirUser = 2022-Integer.parseInt(TanggalLahirUser[2]);

        //Toast tahun lahir user
        Toast.makeText(this, "Umur anda: "+String.valueOf(TahunLahirUser), Toast.LENGTH_SHORT).show();

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
        listBerita.add(new Berita(
                "Puan Maharani dan Gambar Sara Lainnya Dijadikan Wallpaper Oleh Mahasiswa",
                "Salah satu mahasiswa di suatu kampus di Yogyakarta memberi wallpaper sara pada semua komputer di suatu lab. Hal ini membuat pihak kampus meluncurkan peraturan baru kepada mahasiswa ketika menggunakan lab komputer.",
                "17",
                "12/12/12",
                "Regional",
                "img_puan"));
        listBerita.add(new Berita(
                "Aksi Kreator Pemain Game Yu Gi Oh!",
                "Kreator game Yu Gi Oh! meninggal dunia dikarenakan menyelamatkan 3 nyawa orang. Pria 60 tahun yang memiliki nama asli Kazuo Takahashi ini ternyata mengembuskan napas terakhir setelah berusaha menolong seorang anak perempuan berusia 11 tahun, ibunda si gadis kecil, dan seorang tentara yang terseret arus. “Dia adalah seorang pahlawan. Ia meninggal dunia saat berusaha menyelamatkan orang lain,” kata Robert Bourgeau kepada media militer Stars and Stripes. Ia melihat seorang wanita Jepang berteriak karena anak perempuannya dan seorang tentara berusia 39 tahun ditelan arus. Orang-orang ini berjarak sekitar 100 yards atau sekitar 91 meter dari pantai, saat ombak setinggi hampir 2 meter menerjang mereka.",
                "17",
                "12/10/22",
                "Gaming",
                "img_yugioh"));
        listBerita.add(new Berita(
                "Artis Rex Orange County Tergugat Pelecehan Seksual",
                "Rex Orange County menggagalkan konsernya dikarenakan tergugat masalah pelecehan seksual 2 kali pada 1 juni 2022 di End West. Pihak yang berwajib terpaksa menghentikan konser. Pelantun lagu Best Friend ini diduga melecehkan perempuan tersebut enam kali dalam rentang waktu dua hari. Awalnya, O'Connor diduga melecehkan perempuan itu dua kali di wilayah West End London pada 1 Juni. Sebelumnya pada Juli, Rex Orange County sempat membatalkan sederet jadwal turnya karena \"situasi pribadi yang tak terduga\". Saat itu, ia tak menjelaskan secara rinci persoalan apa yang tengah ia hadapi. Dalam keterangan yang diunggah di Twitter, O'Connor hanya menjelaskan bahwa ia menginginkan untuk diam di rumah tanpa menyertakan konteks apapun.",
                "5",
                "10/10/22",
                "Artist",
                "img_rex"));
        listBerita.add(new Berita(
                "Seorang Mahasiswa Tewas Jatuh dari Lantai 11 Hotel",
                "Pihak kepolisian mengungkap, korban memiliki identitas inisial TSR. Korban berjenis kelamin laki-laki ini bukanlah warga setempat atau warga Yogyakarta. Hasil identifikasi yang dilakukan oleh pihak kepolisian korban diketahui adalah seorang mahasiswa UGM Yogyakarta. Polisi juga menemukan sebuah surat keterangan dari psikolog di dalam tas yang dibawa oleh korban.",
                "17",
                "11/10/22",
                "Regional",
                "img_seorang"));
        listBerita.add(new Berita(
                "Nintendo Umumkan Game Zelda Terbaru!",
                "Link, adalah karakter utama dari game legendaris The Legend of Zelda. Video trailer tersebut dirilis untuk mengumumkan kehadiran game The Legend of Zelda: Tears of The Kingdom yang akan meluncur pada 12 Mei 2023. Homegraf adalah sesuatu yang sering Anda temukan di bahasa Inggris. Bahwa dua kata yang bertuliskan sama bisa dibaca dengan suara yang berbeda dan karenanya, seringkali memuat pengertian yang bervariasi pula. Biasanya kita butuh keseluruhan kalimat untuk mendapatkan konteks lebih lengkap soal apa yang cocok dan apa yang sebenarnya tengah dibicarakan. Situasi lebih pelik ketika homograf kini kemudian menyangkut pada judul sesuatu, dari buku hingga video game. Hal inilah yang terjadi dengan game Zelda terbaru kemarin – The Legend of Zelda: Tears of the Kingdom.",
                "10",
                "14/09/22",
                "Gaming",
                "img_nintendo"));
        listBerita.add(new Berita(
                "Microsoft Designer Resmi Meluncur, Aplikasi Desain Pesaing Canva",
                "Seperti rumor yang beredar sebelumnya, Microsoft Designer bisa membantu pengguna membuat desain postingan di media sosial, membuat desain undangan, brosur dan desain lainnya. Pengguna bisa membuatnya secara manual atau memanfaatkan template yang disediakan Designer. Berbeda dengan Canva, Microsoft Designer didukung oleh kecerdasan buatan (AI) dan OpenAI DALL-E 2 yang memungkinkan pengguna membuat desain khusus menggunakan teks atau gambar. Berkat dukungan tersebut, Designer juga bisa digunakan di PowerPoint Designer untuk membantu pengguna membuat desain presentasi yang menarik. Pihak Microsoft juga menyatakan bila aplikasi ini sudah tersedia dan bisa dijajal secara gratis. Artinya platform itu bisa menjadi solusi bagi para perusahaan untuk memangkas biaya operasional di tengah kondisi ekonomi saat ini, ketimbang berlangganan Canva.",
                "14",
                "13/10/22",
                "Technology",
                "img_microsoft"));
        listBerita.add(new Berita(
                "Windah Basudara Dipuji Netizen Kumpulkan Rp 300 Juta untuk Siswa SLB",
                "Windah Basudara dihujani pujian netizen usai menggelar live streaming kumpulkan donasi bantu bocah viral di TikTok bernama Rahmat atau lebih dikenal Okky Boy. Dana Rp 300 jutaan berhasil dikumpulkan sang YouTuber. Windah juga mengungkapkan rasa terima kasihnya kepada seluruh Team Jaya Esports, yang sudah membantu dan menemaninya. Ia mengaku, saat sempat kehilangan channel Youtube Windah Basudara, dirinya sangat frustrasi dan gusar.Hal tersebut ia sampaikan di deskripsi pada salah satu tayangan video Youtube berjudul Akhirnya Channel Windah Basudara Kembali! Klarifikasi yang Sebenarnya Terjadi. Sebenarnya, youtuber kondang ini tidak membeberkan secara gamblang terkait penyebab hilangnya kanal miliknya.",
                "14",
                "12/10/22",
                "Gaming",
                "img_windah"));
        listBerita.add(new Berita(
                "Meta Quest Pro Resmi Diumumkan, Fitur Lengkap Harga Spektakuler",
                "Meta Quest Pro dijual dengan harga USD 1.499 atau sekitar Rp 22 juta. Harga tersebut bahkan hampir empat kali lipat lebih mahal dari Meta Quest 2 yang sudah mengalami kenaikan harga menjadi USD 499 atau sekitar Rp 7,6 juta beberapa waktu lalu. Perangkat ini mendukung video 360 derajat 8K 60fps, teknologi 'persepsi serentak', termasuk pelacakan kepala, tangan, dan pengontrol, rekonstruksi 3D, pemetaan ruang otomatis, dan kerapatan piksel tinggi. Chip ini memiliki peningkatan kinerja termal 30 persen dan daya berkelanjutan 50 persen lebih tinggi dibandingkan generasi sebelumnya. Kemudian, pengontrol Meta Quest Touch Pro ditenagai oleh platform Snapdragon 662, yang memungkinkannya melacak melalui beberapa kamera posisi tertanam dan menawarkan latensi sangat rendah ke headset.",
                "12",
                "13/10/22",
                "Technology",
                "img_meta"));
    }
}