package org.example.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Rezervasyon extends Personel implements Raporlanabilir {
    private static int toplamRezervasyonSayisi = 0; // Statik alan: toplam rezervasyon sayısını takip eder
    private String rezervasyonNo;
    private LocalDate tarihBaslangici; // Rezervasyon başlangıç tarihi
    private LocalDate tarihBitisi;     // Rezervasyon bitiş tarihi
    private int odaBilgisi;

    // Yapıcı metod (Constructor)
    public Rezervasyon(String isimSoyisim, String kimlikNo, LocalDate tarihBaslangici, LocalDate tarihBitisi, int odaBilgisi) {
        super(isimSoyisim, kimlikNo);
        this.rezervasyonNo = benzersizKimlikOlustur();
        this.tarihBaslangici = tarihBaslangici;
        this.tarihBitisi = tarihBitisi;
        this.odaBilgisi = odaBilgisi;
    }

    public Rezervasyon(String rezervasyonNo, String isimSoyisim, String kimlikNo, LocalDate tarihBaslangici, LocalDate tarihBitisi, int odaBilgisi) {
        super(isimSoyisim, kimlikNo);
        this.rezervasyonNo = rezervasyonNo;
        this.tarihBaslangici = tarihBaslangici;
        this.tarihBitisi = tarihBitisi;
        this.odaBilgisi = odaBilgisi;
    }

    public Rezervasyon(){

    }



    // Getters ve Setters

    public static int getToplamRezervasyonSayisi() {
        return toplamRezervasyonSayisi;
    }

    public static void setToplamRezervasyonSayisi(int sayi) {
        toplamRezervasyonSayisi = sayi;
    }

    public String getRezervasyonNo() {
        return rezervasyonNo;
    }

    public void setRezervasyonNo(String rezervasyonNo) {
        this.rezervasyonNo = rezervasyonNo;
    }

    public LocalDate getTarihBaslangici() {
        return tarihBaslangici;
    }

    public void setTarihBaslangici(LocalDate tarihBaslangici) {
        this.tarihBaslangici = tarihBaslangici;
    }

    public LocalDate getTarihBitisi() {
        return tarihBitisi;
    }

    public void setTarihBitisi(LocalDate tarihBitisi) {
        this.tarihBitisi = tarihBitisi;
    }

    public int getOdaBilgisi() {
        return odaBilgisi;
    }

    public void setOdaBilgisi(int odaBilgisi) {
        this.odaBilgisi = odaBilgisi;
    }


    @Override
    public String raporOlustur() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Rezervasyon No: %s\nİsim Soyisim: %s\nTC Kimlik: %s\nTarih Başlangıcı: %s\nTarih Bitişi: %s\nOda No: %d",
                rezervasyonNo, getIsimSoyisim(), getKimlikNo(),
                tarihBaslangici.format(formatter), tarihBitisi.format(formatter), odaBilgisi);
    }

    // Detayları görüntüleme metodu
    public String detaylariGoster() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format(
                "Rezervasyon Detayları:\n" +
                        "-----------------------\n" +
                        "Rezervasyon No: %s\n" +
                        "İsim Soyisim: %s\n" +
                        "TC Kimlik: %s\n" +
                        "Tarih Başlangıcı: %s\n" +
                        "Tarih Bitişi: %s\n" +
                        "Oda No: %d",
                rezervasyonNo, getIsimSoyisim(), getKimlikNo(),
                tarihBaslangici.format(formatter), tarihBitisi.format(formatter), odaBilgisi
        );
    }


    @Override
    public String benzersizKimlikOlustur() {
        // Temel ID oluşturma metodunu kullanarak benzersiz ID oluşturuyoruz
        String temelId = Personel.temelKimlikOlustur(getKimlikNo(), getIsimSoyisim());
        return "REZ-" + temelId + "-" + odaBilgisi;
    }

    @Override
    public String toString() {
        return raporOlustur();
    }
}
