package com.example.intine_panik_uts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "judul")
    private String judul;

    @ColumnInfo(name = "berita_id")
    private String berita_id;

    @ColumnInfo(name = "owner")
    private String owner;


    @Ignore
    public Note(){
    }

    @Ignore
    public Note(String judul, String berita_id, String owner) {
        this.judul = judul;
        this.berita_id = berita_id;
        this.owner = owner;
    }

    public Note(int id, String judul, String berita_id, String owner) {
        this.id = id;
        this.judul = judul;
        this.berita_id = berita_id;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getBerita_id() {
        return berita_id;
    }

    public void setBerita_id(String berita_id) {
        this.berita_id = berita_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    @Override
    public String toString() {
        return this.judul;
    }
}
