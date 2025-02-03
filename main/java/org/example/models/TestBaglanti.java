package org.example.models;

import java.sql.Connection;

public class TestBaglanti {
    public static void main(String[] args) {
        // Veritabanı bağlantısını al
        Connection baglanti = BaglantiYonetici.baglantiGetir();

        // Bağlantı başarılıysa mesaj ver
        if (baglanti != null) {
            System.out.println("Veritabanı bağlantısı hazır!");
        }

        // Bağlantıyı kapat
        BaglantiYonetici.baglantiKapat();
    }
}
