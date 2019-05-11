package com.ta.emilia.kpsp.model;

/**
 * Created by Toshibha on 10/03/2018.
 */
public class ItemPemeriksaanPasien {

    String id_pemeriksaan, id_pasien, tgl_periksa, tujuan, kpsp_ke, hasil;

    public ItemPemeriksaanPasien(String id_pemeriksaan, String id_pasien, String tujuan, String tgl_periksa, String kpsp_ke, String hasil) {
        this.id_pemeriksaan = id_pemeriksaan;
        this.id_pasien = id_pasien;
        this.tujuan = tujuan;
        this.tgl_periksa = tgl_periksa;
        this.kpsp_ke = kpsp_ke;
        this.hasil = hasil;
    }

    public String getId_pemeriksaan() {
        return id_pemeriksaan;
    }

    public String getId_pasien(){return id_pasien;}

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
