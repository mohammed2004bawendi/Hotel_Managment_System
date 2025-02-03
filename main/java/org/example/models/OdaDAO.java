package org.example.models;

import java.sql.*;
import java.util.ArrayList;

// Oda veritabanı işlemlerini yöneten sınıf
public class OdaDAO {

    // Oda tablosunu oluşturan metot
    public static void odaTablosunuOlustur() {
        String sql = "CREATE TABLE IF NOT EXISTS oda (" +
                "odaNumarasi INTEGER PRIMARY KEY, " +
                "kapasite INTEGER, " +
                "fiyat REAL, " +
                "durum TEXT, " +
                "manzara TEXT)";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Oda tablosu başarıyla oluşturuldu.");
        } catch (SQLException e) {
            System.err.println("Oda tablosu oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    // Yeni bir oda ekleyen metot
    public static void odaEkle(OdaDTO oda) {
        String sql = "INSERT INTO oda (odaNumarasi, kapasite, fiyat, durum, manzara) VALUES (?, ?, ?, ?, ?)";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setInt(1, oda.getOdaNumarasi());
            stmt.setInt(2, oda.getKapasite());
            stmt.setDouble(3, oda.getFiyat());
            stmt.setString(4, oda.getDurum());
            stmt.setString(5, oda.getManzara());
            stmt.executeUpdate();
            System.out.println("Oda başarıyla eklendi.");
        } catch (SQLException e) {
            System.err.println("Oda eklenirken hata oluştu: " + e.getMessage());
        }
    }

    // Odanın durumunu güncelleyen metot
    public static void odaDurumunuGuncelle(int odaNumarasi, String yeniDurum) {
        String sql = "UPDATE oda SET durum = ? WHERE odaNumarasi = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setString(1, yeniDurum);
            stmt.setInt(2, odaNumarasi);
            int guncellenenSatirSayisi = stmt.executeUpdate();
            if (guncellenenSatirSayisi > 0) {
                System.out.println("Oda durumu başarıyla güncellendi.");
            } else {
                System.out.println("Oda bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Oda durumu güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    // Odanın rezervasyon durumunu kontrol eden metot
    public static boolean odaRezerveEdildiMi(int odaNumarasi) {
        String sql = "SELECT durum FROM oda WHERE odaNumarasi = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setInt(1, odaNumarasi);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String durum = rs.getString("durum");
                    return durum != null && durum.equalsIgnoreCase("dolu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Oda rezervasyon durumu kontrol edilirken hata oluştu: " + e.getMessage());
        }
        return false;
    }

    // Belirli bir odayı bulan metot
    public static OdaDTO odaBul(int odaNumarasi) {
        String sql = "SELECT * FROM oda WHERE odaNumarasi = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setInt(1, odaNumarasi);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OdaDTO(
                            rs.getInt("odaNumarasi"),
                            rs.getInt("kapasite"),
                            rs.getDouble("fiyat"),
                            rs.getString("durum"),
                            rs.getString("manzara")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Oda bulunurken hata oluştu: " + e.getMessage());
        }
        return null;
    }

    // Bir odayı silen metot
    public static void odaSil(int odaNumarasi) {
        String sql = "DELETE FROM oda WHERE odaNumarasi = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setInt(1, odaNumarasi);
            int silinenSatirSayisi = stmt.executeUpdate();
            if (silinenSatirSayisi > 0) {
                System.out.println("Oda başarıyla silindi.");
            } else {
                System.out.println("Oda bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Oda silinirken hata oluştu: " + e.getMessage());
        }
    }

    // Odanın mevcut olup olmadığını kontrol eden metot
    public static boolean odaMevcutMu(int odaNumarasi) {
        String sql = "SELECT COUNT(*) FROM oda WHERE odaNumarasi = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setInt(1, odaNumarasi);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Oda mevcutluğu kontrol edilirken hata oluştu: " + e.getMessage());
        }
        return false;
    }

    // Tüm odaları listeleyen metot
    public static ArrayList<OdaDTO> tumOdalarListesiniGetir() {
        ArrayList<OdaDTO> odaListesi = new ArrayList<>();
        String sql = "SELECT * FROM oda";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OdaDTO oda = new OdaDTO(
                        rs.getInt("odaNumarasi"),
                        rs.getInt("kapasite"),
                        rs.getDouble("fiyat"),
                        rs.getString("durum"),
                        rs.getString("manzara")
                );
                odaListesi.add(oda);
            }
        } catch (SQLException e) {
            System.err.println("Oda listesi getirilirken hata oluştu: " + e.getMessage());
        }
        return odaListesi;
    }
}
