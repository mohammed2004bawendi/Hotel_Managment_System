package org.example.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Veritabanı bağlantısını yönetmek için kullanılan sınıf
public class BaglantiYonetici {

    private static Connection baglanti; // Veritabanı bağlantısı nesnesi

    // Veritabanı bağlantısını döndüren metot
    public static Connection baglantiGetir() {
        try {
            // Eğer bağlantı yoksa ya da kapalıysa, yeni bir bağlantı oluştur
            if (baglanti == null || baglanti.isClosed()) {
                String url = "jdbc:sqlite:C:/Users/lenovo/Desktop/Proje/untitled/mydb.db";// Veritabanı dosyasının yolu
                baglanti = DriverManager.getConnection(url);
                System.out.println("Bağlantı Başarılı"); // Başarılı bağlantı mesajı
            }
        } catch (SQLException e) {
            // Bağlantı sırasında bir hata olursa, hatayı yazdır
            System.err.println("Veritabanı Bağlantısı Hatası: " + e.getMessage());
        }
        return baglanti;
    }

    // Veritabanı bağlantısını kapatan metot
    public static void baglantiKapat() {
        try {
            // Eğer bağlantı açık ise kapat
            if (baglanti != null && !baglanti.isClosed()) {
                baglanti.close();
                System.out.println("Bağlantı Kapatıldı"); // Kapatma mesajı
            }
        } catch (SQLException e) {
            // Bağlantı kapatma sırasında bir hata olursa, hatayı yazdır
            System.err.println("Bağlantı Kapatma Hatası: " + e.getMessage());
        }
    }
}
