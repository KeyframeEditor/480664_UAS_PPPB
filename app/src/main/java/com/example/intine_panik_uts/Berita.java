package com.example.intine_panik_uts;

public class Berita {
    private String judul;
    private String konten;
    private String targetUsia;
    private String tanggalRilis;
    private String kategori;

    public Berita(String judul, String konten, String targetUsia, String tanggalRilis, String kategori) {
        this.judul = judul;
        this.konten = konten;
        this.targetUsia = targetUsia;
        this.tanggalRilis = tanggalRilis;
        this.kategori = kategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public String getTargetUsia() {
        return targetUsia;
    }

    public void setTargetUsia(String targetUsia) {
        this.targetUsia = targetUsia;
    }

    public String getTanggalRilis() {
        return tanggalRilis;
    }

    public void setTanggalRilis(String tanggalRilis) {
        this.tanggalRilis = tanggalRilis;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    //    judul, konten, minimum target usia, tanggal rilis, kategori,  link gambar, dan lain lain
}
