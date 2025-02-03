package org.example.models;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RezervasyonDAO {

    private static final DateTimeFormatter TARIH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Rezervasyon tablosunu oluştur
    public static void tabloOlustur() {
        String sql = "CREATE TABLE IF NOT EXISTS rezervasyon (" +
                "rezervasyonNo TEXT PRIMARY KEY, " +
                "isimSoyisim TEXT, " +
                "tcKimlik TEXT, " +
                "tarihBaslangici TEXT, " + // Rezervasyon başlangıç tarihi
                "tarihBitisi TEXT, " +     // Rezervasyon bitiş tarihi
                "odaBilgisi INTEGER" +
                ");";

        try (Connection baglanti= BaglantiYonetici.baglantiGetir(); Statement stmt = baglanti.createStatement()) {
            stmt.execute(sql);
            System.out.println("Rezervasyon tablosu başarıyla oluşturuldu.");
        } catch (SQLException e) {
            System.err.println("Rezervasyon tablosu oluşturulurken hata: " + e.getMessage());
        }
    }

    // Rezervasyon ekle
    public static void rezervasyonEkle(Rezervasyon rezervasyon) {
        String sql = "INSERT INTO rezervasyon (rezervasyonNo, isimSoyisim, tcKimlik, tarihBaslangici, tarihBitisi, odaBilgisi) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rezervasyon.getRezervasyonNo());
            stmt.setString(2, rezervasyon.getIsimSoyisim());
            stmt.setString(3, rezervasyon.getKimlikNo());
            stmt.setString(4, rezervasyon.getTarihBaslangici().format(TARIH_FORMAT)); // Tarih formatı
            stmt.setString(5, rezervasyon.getTarihBitisi().format(TARIH_FORMAT));
            stmt.setInt(6, rezervasyon.getOdaBilgisi());

            stmt.executeUpdate();
            System.out.println("Rezervasyon başarıyla eklendi.");
            OdaDAO.odaDurumunuGuncelle(rezervasyon.getOdaBilgisi(), "Dolu");
            System.out.println("Oda durumu 'Dolu' olarak güncellendi.");
        } catch (SQLException e) {
            System.err.println("Rezervasyon eklenirken hata: " + e.getMessage());
        }
    }

    // Rezervasyon sil
    public static void rezervasyonSil(String rezervasyonNo) {
        String getOdaQuery = "SELECT odaBilgisi FROM rezervasyon WHERE rezervasyonNo = ?";
        String deleteQuery = "DELETE FROM rezervasyon WHERE rezervasyonNo = ?";

        try (Connection conn = BaglantiYonetici.baglantiGetir()) {
            int odaNo = -1;

            // Rezervasyon numarasına göre oda bilgisini al
            try (PreparedStatement getStmt = conn.prepareStatement(getOdaQuery)) {
                getStmt.setString(1, rezervasyonNo);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        odaNo = rs.getInt("odaBilgisi");
                    } else {
                        System.out.println("Rezervasyon bulunamadı.");
                        return;
                    }
                }
            }

            // Rezervasyonu sil
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, rezervasyonNo);
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Rezervasyon başarıyla silindi.");
                    // Oda durumunu "Boş" olarak güncelle
                    OdaDAO.odaDurumunuGuncelle(odaNo, "Boş");
                } else {
                    System.out.println("Rezervasyon bulunamadı.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Rezervasyon silinirken hata: " + e.getMessage());
        }
    }

    // Tüm rezervasyonları getir
    public static List<Rezervasyon> tumRezervasyonlariGetir() {
        List<Rezervasyon> rezervasyonListesi = new ArrayList<>();
        String sql = "SELECT * FROM rezervasyon";

        try (Connection conn = BaglantiYonetici.baglantiGetir();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Rezervasyon rezervasyon = new Rezervasyon(
                        rs.getString("rezervasyonNo"),
                        rs.getString("isimSoyisim"),
                        rs.getString("tcKimlik"),
                        LocalDate.parse(rs.getString("tarihBaslangici"), TARIH_FORMAT),
                        LocalDate.parse(rs.getString("tarihBitisi"), TARIH_FORMAT),
                        rs.getInt("odaBilgisi")
                );
                rezervasyonListesi.add(rezervasyon);
                // Toplam rezervasyon sayısını güncelle
                Rezervasyon.setToplamRezervasyonSayisi(rezervasyonListesi.size());
            }
        } catch (SQLException e) {
            System.err.println("Rezervasyonlar alınırken hata: " + e.getMessage());
        }
        return rezervasyonListesi;
    }

    // Rezervasyon numarasına göre rezervasyon getir
    public static Rezervasyon rezervasyonGetirByNo(String rezervasyonNo) {
        String sql = "SELECT * FROM rezervasyon WHERE rezervasyonNo = ?";
        try (Connection conn = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rezervasyonNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Rezervasyon(
                            rs.getString("rezervasyonNo"),
                            rs.getString("isimSoyisim"),
                            rs.getString("tcKimlik"),
                            LocalDate.parse(rs.getString("tarihBaslangici"), TARIH_FORMAT),
                            LocalDate.parse(rs.getString("tarihBitisi"), TARIH_FORMAT),
                            rs.getInt("odaBilgisi")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Rezervasyon numarasına göre rezervasyon alınırken hata: " + e.getMessage());
        }
        return null;
    }

    // TC Kimlik numarasına göre rezervasyonları getir
    public static List<Rezervasyon> rezervasyonGetirByTcKimlik(String tcKimlik) {
        List<Rezervasyon> rezervasyonListesi = new ArrayList<>();
        String sql = "SELECT * FROM rezervasyon WHERE tcKimlik = ?";

        try (Connection conn = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tcKimlik);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rezervasyon rezervasyon = new Rezervasyon(
                            rs.getString("rezervasyonNo"),
                            rs.getString("isimSoyisim"),
                            rs.getString("tcKimlik"),
                            LocalDate.parse(rs.getString("tarihBaslangici"), TARIH_FORMAT),
                            LocalDate.parse(rs.getString("tarihBitisi"), TARIH_FORMAT),
                            rs.getInt("odaBilgisi")
                    );
                    rezervasyonListesi.add(rezervasyon);
                }
            }
        } catch (SQLException e) {
            System.err.println("TC Kimlik numarasına göre rezervasyonlar alınırken hata: " + e.getMessage());
        }
        return rezervasyonListesi;
    }
}
