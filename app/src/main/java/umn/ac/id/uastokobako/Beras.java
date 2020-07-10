package umn.ac.id.uastokobako;

public class Beras {
    private String namaBarang;
    private Integer harga;
    private Integer stok;
    private String gambarUrl;

    public Beras() {
    }

    public Beras(String namaBarang, Integer harga, Integer stok, String gambarUrl) {
        this.namaBarang = namaBarang;
        this.harga = harga;
        this.stok = stok;
        this.gambarUrl = gambarUrl;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }
}
