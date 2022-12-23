package com.example.intine_panik_uts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private String pref_TanggalLahir;
    public String pref_Kategori;
    private BeritaAdapter beritaAdapter;
    private RecyclerView mainRecyclerView;
    private Spinner spinnerPreferensi;
    private String textSpinnerPreferensi;
    private FloatingActionButton buttonTambahBerita;
    private FloatingActionButton buttonLogout;
    private ExtendedFloatingActionButton buttonBeritaDisukai;
    private ImageView buttonRefresh;

    DatabaseReference databaseReferenceBerita = FirebaseDatabase.getInstance().getReference("beritaDB");

    public ArrayList<Berita> listBerita = new ArrayList<Berita>();
    public ArrayList<Berita> listFilteredBerita = new ArrayList<Berita>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPreferensi = findViewById(R.id.spinnerPreferensi);
        //set array to an adapter to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_item);
        spinnerPreferensi.setAdapter(adapter);

        buttonTambahBerita = findViewById(R.id.buttonTambahBerita);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonBeritaDisukai = findViewById(R.id.buttonBeritaDisukai);

        buttonTambahBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TambahBeritaActivity.class);
                intent.putExtra("action","Tambah");
                intent.putExtra("berita_key","default");
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sec_intent = new Intent();
                sec_intent.putExtra("logged_in",false);
                setResult(RESULT_OK, sec_intent);
                finish();
            }
        });

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

        spinnerPreferensi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pref_Kategori = String.valueOf(spinnerPreferensi.getSelectedItem());
                listBerita.clear();
                listFilteredBerita.clear();
                databaseReferenceBerita.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot currentData : snapshot.getChildren()){
                                Berita berita = new Berita();
                                berita.setCreatedBy(currentData.child("createdBy").getValue().toString());
                                berita.setJudul(currentData.child("judul").getValue().toString());
                                berita.setKategori(currentData.child("kategori").getValue().toString());
                                berita.setKonten(currentData.child("konten").getValue().toString());
                                berita.setTanggalRilis(currentData.child("tanggalRilis").getValue().toString());
                                berita.setTargetUsia(currentData.child("targetUsia").getValue().toString());
                                berita.setBerita_key(currentData.getKey().toString());
                                listBerita.add(berita);
                            }
                        }
                        for (Berita dataBerita : listBerita){
                            Integer targetUsiaBerita = Integer.valueOf(dataBerita.getTargetUsia());
                            if(targetUsiaBerita <= TahunLahirUser && dataBerita.getKategori().toLowerCase().equals(pref_Kategori.toLowerCase())){
                                listFilteredBerita.add(dataBerita);
                                beritaAdapter = new BeritaAdapter(MainActivity.this,listFilteredBerita);
                                mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                mainRecyclerView.setAdapter(beritaAdapter);
                            }else if(targetUsiaBerita <= TahunLahirUser && pref_Kategori.equals("Semua Kategori")){
                                listFilteredBerita.add(dataBerita);
                                beritaAdapter = new BeritaAdapter(MainActivity.this,listFilteredBerita);
                                mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                mainRecyclerView.setAdapter(beritaAdapter);
                            }
                            if (beritaAdapter != null) {
                                beritaAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Error! "+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        });

        buttonBeritaDisukai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BeritaLikeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addBerita(){

//        listBerita.add(new Berita(
//                "Puan Maharani dan Gambar Sara Lainnya Dijadikan Wallpaper Oleh Mahasiswa",
//                "Salah satu mahasiswa di suatu kampus di Yogyakarta memberi wallpaper sara pada semua komputer di suatu lab. Hal ini membuat pihak kampus meluncurkan peraturan baru kepada mahasiswa ketika menggunakan lab komputer.",
//                "18",
//                "12/12/12",
//                "Regional",
//                "1"));
//        listBerita.add(new Berita(
//                "Aksi Kreator Pemain Game Yu Gi Oh!",
//                "Kreator game Yu Gi Oh! meninggal dunia dikarenakan menyelamatkan 3 nyawa orang. Pria 60 tahun yang memiliki nama asli Kazuo Takahashi ini ternyata mengembuskan napas terakhir setelah berusaha menolong seorang anak perempuan berusia 11 tahun, ibunda si gadis kecil, dan seorang tentara yang terseret arus. “Dia adalah seorang pahlawan. Ia meninggal dunia saat berusaha menyelamatkan orang lain,” kata Robert Bourgeau kepada media militer Stars and Stripes. Ia melihat seorang wanita Jepang berteriak karena anak perempuannya dan seorang tentara berusia 39 tahun ditelan arus. Orang-orang ini berjarak sekitar 100 yards atau sekitar 91 meter dari pantai, saat ombak setinggi hampir 2 meter menerjang mereka.",
//                "18",
//                "12/10/22",
//                "Gaming",
//                "img_yugioh"));
//        listBerita.add(new Berita(
//                "Artis Rex Orange County Tergugat Pelecehan Seksual",
//                "Rex Orange County menggagalkan konsernya dikarenakan tergugat masalah pelecehan seksual 2 kali pada 1 juni 2022 di End West. Pihak yang berwajib terpaksa menghentikan konser. Pelantun lagu Best Friend ini diduga melecehkan perempuan tersebut enam kali dalam rentang waktu dua hari. Awalnya, O'Connor diduga melecehkan perempuan itu dua kali di wilayah West End London pada 1 Juni. Sebelumnya pada Juli, Rex Orange County sempat membatalkan sederet jadwal turnya karena \"situasi pribadi yang tak terduga\". Saat itu, ia tak menjelaskan secara rinci persoalan apa yang tengah ia hadapi. Dalam keterangan yang diunggah di Twitter, O'Connor hanya menjelaskan bahwa ia menginginkan untuk diam di rumah tanpa menyertakan konteks apapun.",
//                "18",
//                "10/10/22",
//                "Artist",
//                "img_rex"));
//        listBerita.add(new Berita(
//                "Seorang Mahasiswa Tewas Jatuh dari Lantai 11 Hotel",
//                "Pihak kepolisian mengungkap, korban memiliki identitas inisial TSR. Korban berjenis kelamin laki-laki ini bukanlah warga setempat atau warga Yogyakarta. Hasil identifikasi yang dilakukan oleh pihak kepolisian korban diketahui adalah seorang mahasiswa UGM Yogyakarta. Polisi juga menemukan sebuah surat keterangan dari psikolog di dalam tas yang dibawa oleh korban.",
//                "18",
//                "11/10/22",
//                "Regional",
//                "img_seorang"));
//        listBerita.add(new Berita(
//                "Nintendo Umumkan Game Zelda Terbaru!",
//                "Link, adalah karakter utama dari game legendaris The Legend of Zelda. Video trailer tersebut dirilis untuk mengumumkan kehadiran game The Legend of Zelda: Tears of The Kingdom yang akan meluncur pada 12 Mei 2023. Homegraf adalah sesuatu yang sering Anda temukan di bahasa Inggris. Bahwa dua kata yang bertuliskan sama bisa dibaca dengan suara yang berbeda dan karenanya, seringkali memuat pengertian yang bervariasi pula. Biasanya kita butuh keseluruhan kalimat untuk mendapatkan konteks lebih lengkap soal apa yang cocok dan apa yang sebenarnya tengah dibicarakan. Situasi lebih pelik ketika homograf kini kemudian menyangkut pada judul sesuatu, dari buku hingga video game. Hal inilah yang terjadi dengan game Zelda terbaru kemarin – The Legend of Zelda: Tears of the Kingdom.",
//                "10",
//                "14/09/22",
//                "Gaming",
//                "img_nintendo"));
//        listBerita.add(new Berita(
//                "Microsoft Designer Resmi Meluncur, Aplikasi Desain Pesaing Canva",
//                "Seperti rumor yang beredar sebelumnya, Microsoft Designer bisa membantu pengguna membuat desain postingan di media sosial, membuat desain undangan, brosur dan desain lainnya. Pengguna bisa membuatnya secara manual atau memanfaatkan template yang disediakan Designer. Berbeda dengan Canva, Microsoft Designer didukung oleh kecerdasan buatan (AI) dan OpenAI DALL-E 2 yang memungkinkan pengguna membuat desain khusus menggunakan teks atau gambar. Berkat dukungan tersebut, Designer juga bisa digunakan di PowerPoint Designer untuk membantu pengguna membuat desain presentasi yang menarik. Pihak Microsoft juga menyatakan bila aplikasi ini sudah tersedia dan bisa dijajal secara gratis. Artinya platform itu bisa menjadi solusi bagi para perusahaan untuk memangkas biaya operasional di tengah kondisi ekonomi saat ini, ketimbang berlangganan Canva.",
//                "18",
//                "13/10/22",
//                "Technology",
//                "img_microsoft"));
//        listBerita.add(new Berita(
//                "Windah Basudara Dipuji Netizen Kumpulkan Rp 300 Juta untuk Siswa SLB",
//                "Windah Basudara dihujani pujian netizen usai menggelar live streaming kumpulkan donasi bantu bocah viral di TikTok bernama Rahmat atau lebih dikenal Okky Boy. Dana Rp 300 jutaan berhasil dikumpulkan sang YouTuber. Windah juga mengungkapkan rasa terima kasihnya kepada seluruh Team Jaya Esports, yang sudah membantu dan menemaninya. Ia mengaku, saat sempat kehilangan channel Youtube Windah Basudara, dirinya sangat frustrasi dan gusar.Hal tersebut ia sampaikan di deskripsi pada salah satu tayangan video Youtube berjudul Akhirnya Channel Windah Basudara Kembali! Klarifikasi yang Sebenarnya Terjadi. Sebenarnya, youtuber kondang ini tidak membeberkan secara gamblang terkait penyebab hilangnya kanal miliknya.",
//                "10",
//                "12/10/22",
//                "Gaming",
//                "img_windah"));
//        listBerita.add(new Berita(
//                "Meta Quest Pro Resmi Diumumkan, Fitur Lengkap Harga Spektakuler",
//                "Meta Quest Pro dijual dengan harga USD 1.499 atau sekitar Rp 22 juta. Harga tersebut bahkan hampir empat kali lipat lebih mahal dari Meta Quest 2 yang sudah mengalami kenaikan harga menjadi USD 499 atau sekitar Rp 7,6 juta beberapa waktu lalu. Perangkat ini mendukung video 360 derajat 8K 60fps, teknologi 'persepsi serentak', termasuk pelacakan kepala, tangan, dan pengontrol, rekonstruksi 3D, pemetaan ruang otomatis, dan kerapatan piksel tinggi. Chip ini memiliki peningkatan kinerja termal 30 persen dan daya berkelanjutan 50 persen lebih tinggi dibandingkan generasi sebelumnya. Kemudian, pengontrol Meta Quest Touch Pro ditenagai oleh platform Snapdragon 662, yang memungkinkannya melacak melalui beberapa kamera posisi tertanam dan menawarkan latensi sangat rendah ke headset.",
//                "10",
//                "13/10/22",
//                "Technology",
//                "img_meta"));
//        listBerita.add(new Berita(
//                "Blizzard Bagi-Bagi Item Gratis untuk Pemain Overwatch 2",
//                "Sejak pertama kali meluncur, game ini sudah mengalami beberapa masalah, salah satunya adalah serangan siber DDoS (Distributed Denial-of-Service). Lalu, masalah lain dialami dua orisinal game ini, yakni Bastion dan Torbjron. Akibatnya, Bastion harus dihapus lebih dulu untuk perbaikan, sedangkan Torbjorn masih bisa dimainkan meski terbatas untuk mode Quick Play. Menyadari ada masalah ini, seperti dikutip dari Gamespot, Kamis (13/10/2022), Blizzard pun memberikan kompensasi bagi para pemain berupa item in-game yang bisa diperoleh secara gratis. Item gratis ini akan diberikan bagi para pemain yang login antara 25 Oktober hingga akhir musim pertama, yang saat ini dijadwalkan berakhir pada 6 Desember. Adapun item yang diberikan adalah Health Pack Weapon Charm dan skin Cursed Captain untuk Reaper.",
//                "18",
//                "13/10/22",
//                "Gaming",
//                "img_overwatch"));
//        listBerita.add(new Berita(
//                "Presiden Jokowi Panggil Seluruh Pejabat Polri ke Istana",
//                "Presiden Joko Widodo (Jokowi) mengundang Kapolri Jenderal Listyo Sigit Prabowo beserta seluruh jajaran pejabat Polri ke Istana Kepresidenan hari ini, Jumat (14/10/2022) pukul 14.00 WIB. Kepala Sekretaris Presiden (Kasetpres) Heru Budi Hartono mengatakan, Presiden Jokowi akan memberikan arahan secara langsung kepada seluruh pejabat Polri dalam pertemuan tersebut. Kendati demikian, pihaknya enggan memberikan keterangan lebih lanjut perihal arahan Presiden Jokowi kepada jajaran pejabat Polri tersebut.",
//                "18",
//                "14/10/22",
//                "Regional",
//                "img_jokowi"));
//        listBerita.add(new Berita(
//                "Google Rilis Tiga Laptop “Gaming” Chromebook",
//                "Google berpartner dengan tiga produsen yakni Asus, Acer dan Lenovo untuk menghadirkan laptop “Gaming” Chromebook pertama mereka. Tapi, jangan salah paham dulu ya. Gaming yang dimaksud di sini adalah cloud gaming sehingga spesifikasinya tak semewah laptop gaming lain dengan prosesor kelas desktop maupun diskrit GPU. Kendati demikian, laptop Chromebook Gaming tersebut membawa sejumlah spesifikasi kunci seperti minimal layar 120Hz, dukungan Wi-Fi 6/6E, keyboard anti-ghosting, tak lupa sentuhan desain khas laptop gaming. Spesifikasi itu membuat pengalaman bermain cloud game macam GeForce Now, Amazon Luna atau Xbox Cloud jadi lebih menyenangkan tentunya, ketimbang Chromebook biasa.",
//                "10",
//                "13/10/22",
//                "Technology",
//                "img_google"));
//        listBerita.add(new Berita(
//                "Inovasi Teknologi Informasi Memacu Mutu Pendidikan",
//                "Inovasi berbasis teknologi informasi menjadi faktor penting untuk meningkatkan kualitas pendidikan. Pandemi Covid-19 dalam 2,5 tahun terakhir telah mempercepat transformasi metode pembelajaran dengan teknologi digital. Pandemi Covid-19 mempercepat transformasi metode pembelajaran dengan menggunakan teknologi digital. Inovasi berbasis teknologi informasi diharapkan bukan sekadar metode alternatif mengatasi keterbatasan akibat pandemi, tetapi dioptimalkan untuk meningkatkan kualitas pendidikan.",
//                "10",
//                "11/10/22",
//                "Technology",
//                "img_pendidikan"));
//        listBerita.add(new Berita(
//                "Intel Bicara soal Investasi di Indonesia dan Strategi Hadapi Kelangkaan Chip",
//                "Perusahaan teknologi asal Amerika Serikat, Intel, bicara soal investasi di Indonesia. Intel menunjukkan ketertarikannya berinvestasi di Tanah Air. Menurut Steve Long, Corporate Vice President, Sales and Marketing Group & General Manager Asia Pacific & Japan Intel Corporation, Indonesia adalah pasar yang sangat menarik bagi perusahaan multinasional termasuk Intel. Ia menilai Indonesia memiliki sejumlah aspek yang punya potensi, seperti pertumbuhan PDB jangka panjang, peningkatan penetrasi internet, serta memiliki tenaga kerja muda yang berbakat.",
//                "18",
//                "16/10/22",
//                "Technology",
//                "img_intel"));
//        listBerita.add(new Berita(
//                "Nvidia Resmikan RTX 4080 dan RTX 4090",
//                "Nvidia meresmikan kartu grafis dari seri RTX 40 terbarunya, yaitu RTX 4080 dan RTX 4090. Sekencang apa dan berapa harganya? Lini RTX 40 ini sudah dirumorkan sejak berbulan-bulan lalu dan baru kali ini diperkenalkan ke publik. RTX 4090 baru mulai dijual pada 12 Oktober mendatang sementara RTX 4080 dijual mulai November.",
//                "18",
//                "21/09/22",
//                "Technology",
//                "img_nvidia"));
//        listBerita.add(new Berita(
//                "Tanggapan OURA Soal Roster O2 & WORLD Ikut Turnamen Mobile Legends (ML)",
//                "Komunitas Mobile Legends saat ini sering memperbincangkan mengenai roster WORLD yang kembali mengikuti turnamen. Tentunya hal tersebut juga membawa antusias dari para penggemar. Berikut Tanggapan OURA Soal Roster O2 & WORLD Ikut Turnamen Mobile Legends (ML). Roster WORLD merupakan roster dari tim Evos Legends pada Season 4 MPL ID, dengan roster tersebut pun mereka berhasil menjuarai turnamen internasional bertaraf dunia seperti M1 World Championship. Tentunya tim tersebut merupakan salah satu tim yang cukup sukses di pro scene Mobile Legends.",
//                "10",
//                "11/10/22",
//                "Artist",
//                "img_ml"));
//        listBerita.add(new Berita(
//                "Tidak Masalah Salah Jurusan, Konten Kreator Aji VAS Berikan Hikmahnya Lewat YouTube Danang Giri Sadewa",
//                "Sebagai konten kreator pendidikan dan seorang lulusan dari Universitas Gadjah Mada, Danang Giri Sadewa kerap membagikan video yang berkaitan dengan pendidikan, khususnya dunia perkuliahan. Ia memiliki beberapa segmen dalam YouTube Channel miliknya seperti Campus Tour baik di Indonesia maupun luar negeri, beropini tentang dunia pendidikan Indonesia, berbincang dengan pelajar atau mahasiswa Indonesia, dan lainnya. Seperti pada video yang diunggah tanggal 27 September 2022, melalui segmen Perspektif, Danang Gini berkesempatan untuk berbincang-bincang dengan salah seorang konten kreator yang berfokus pada konten bengkel dan spesialis motor Vespa yaitu Jati Wahyu Aji.",
//                "18",
//                "13/10/22",
//                "Artist",
//                "img_aji"));
//        listBerita.add(new Berita(
//                "Unja tingkatkan kapasitas mahasiswa JPOK dengan pelatihan konten kreator",
//                "Mahasiswa jurusan Pendidikan Olahraga dan Kepelatihan (JPOK) FKIP Unja mengadakan pelatihan konten kreator untuk peningkatan kapasitas mahasiswa dalam memanfaatkan perangkat gawai untuk kegiatan-kegiatan positif. Dari siaran pers yang diterima di Jambi, Kamis, Ketua JPOK FKIP Unja Dr. Palminal mengatakan, pelatihan-pelatihan soft skill seperti ini sangat berguna bagi mahasiswa dalam pengembangan minat dan bakat, apalagi, aktivitas mahasiswa JPOK dalam perkuliahannya banyak yang bisa dijadikan konten ringan namun bermuatan informasi dan mengedukasi",
//                "18",
//                "13/10/22",
//                "Artist",
//                "img_unja"));
//        listBerita.add(new Berita(
//                "Kreator Youtube Minecraft 'Dream' Akhirnya Menunjukkan Wajah Aslinya",
//                "Kemunculan Dream di Youtube sejak tahun 2014 disambut dengan luar biasa oleh para penggemar Minecraft, namun tidak ada yang tahu bagaimana wajah aslinya. Kini pemilik Youtube Channel Dream tersebut menampakkan wajahnya pada sebuah video berdurasi kurang dari 6 menit. Video yang diunggahnya pada tanggal 3 Oktober 2022 lalu telah ditonton 33 juta kali, hanya dalam waktu 3 hari. Hal tersebut cukup mengejutkan para penggemar maupun para subscriber Youtube Channel-nya. Bagaimana tidak? Setelah bertahun tahun menjaga identitasnya hingga mendapatkan puluhan juta subscribers, ia memutuskan untuk menunjukkan siapa dirinya.",
//                "10",
//                "13/10/22",
//                "Gaming",
//                "img_dream"));
    }

//    private void tambahBerita(){ //test masukin 1 data doang
//        Berita berita = new Berita();
//
//        berita.setJudul("Unja tingkatkan kapasitas mahasiswa JPOK dengan pelatihan konten kreator");
//        berita.setKonten("Mahasiswa jurusan Pendidikan Olahraga dan Kepelatihan (JPOK) FKIP Unja mengadakan pelatihan konten kreator untuk peningkatan kapasitas mahasiswa dalam memanfaatkan perangkat gawai untuk kegiatan-kegiatan positif. Dari siaran pers yang diterima di Jambi, Kamis, Ketua JPOK FKIP Unja Dr. Palminal mengatakan, pelatihan-pelatihan soft skill seperti ini sangat berguna bagi mahasiswa dalam pengembangan minat dan bakat, apalagi, aktivitas mahasiswa JPOK dalam perkuliahannya banyak yang bisa dijadikan konten ringan namun bermuatan informasi dan mengedukasi");
//        berita.setTargetUsia("18");
//        berita.setTanggalRilis("13/10/22");
//        berita.setKategori("Artist");
//        berita.setCreatedBy("test1");
//
//        databaseReferenceBerita.push().setValue(berita);
//        Toast.makeText(this, "Berhasil Menambahkan Berita", Toast.LENGTH_SHORT).show();
//    }

    //Spinner Rules
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        textSpinnerPreferensi = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showSpinnerText(){
        if (!Objects.equals(textSpinnerPreferensi, "Gaming")){
            Toast.makeText(this, "Happened", Toast.LENGTH_SHORT).show();
        }
    }
}