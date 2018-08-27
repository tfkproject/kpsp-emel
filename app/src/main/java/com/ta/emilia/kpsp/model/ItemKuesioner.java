package com.ta.emilia.kpsp.model;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class ItemKuesioner {

    String id, link_gambar, soal;

    public ItemKuesioner (String id, String link_gambar, String soal) {
        this.id = id;
        this.link_gambar = link_gambar;
        this.soal = soal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getSoal() {
        return soal;
    }
}
