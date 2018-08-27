package com.ta.emilia.kpsp.model;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class ItemPemeriksaan {

    String id_pemeriksaan, nama, tgl_lahir, jk, pddkn, n_ortu, alamat, tgl_periksa, tujuan, kpsp_ke, hasil;

    public ItemPemeriksaan(String id_pemeriksaan, String nama, String tgl_lahir, String jk, String pddkn, String n_ortu, String alamat, String tgl_periksa, String tujuan, String kpsp_ke, String hasil) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.nama = nama;
        this.tgl_lahir = tgl_lahir;
        this.jk = jk;
        this.pddkn = pddkn;
        this.n_ortu = n_ortu;
        this.alamat = alamat;
        this.tgl_periksa = tgl_periksa;
        this.tujuan = tujuan;
        this.kpsp_ke = kpsp_ke;
        this.hasil = hasil;
    }

    public String getId_pemeriksaan() {
        return id_pemeriksaan;
    }

    public String getNama() {
        return nama;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public String getJk() {
        return jk;
    }

    public String getPddkn() {
        return pddkn;
    }

    public String getN_ortu() {
        return n_ortu;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTgl_periksa() {
        return tgl_periksa;
    }

    public String getTujuan() {
        return tujuan;
    }

    public String getKpsp_ke() {
        return kpsp_ke;
    }

    public String getHasil() {
        return hasil;
    }
}
