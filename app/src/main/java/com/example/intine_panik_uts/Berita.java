package com.example.intine_panik_uts;

public class Berita {
    private String judul;
    private String konten;
    private String targetUsia;
    private String tanggalRilis;
    private String kategori;
    private String createdBy;
    private String berita_key;

    public String getBerita_key() {
        return berita_key;
    }

    public void setBerita_key(String berita_key) {
        this.berita_key = berita_key;
    }
//    public Berita(String judul, String konten, String targetUsia, String tanggalRilis, String kategori, String createdBy) {
//        this.judul = judul;
//        this.konten = konten;
//        this.targetUsia = targetUsia;
//        this.tanggalRilis = tanggalRilis;
//        this.kategori = kategori;
//        this.createdBy = createdBy;
//    }

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    //    judul, konten, minimum target usia, tanggal rilis, kategori,  link gambar, dan lain lain
}
