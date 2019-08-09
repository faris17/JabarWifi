package com.jabar.jabarwifi.model;

public class Data {

    public Data(){

    }
    String kategori, tanggal;
    int uang;

    public Data(String kategori, String tanggal, int uang) {
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.uang = uang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getUang() {
        return uang;
    }

    public void setUang(int uang) {
        this.uang = uang;
    }

}
