package org.example.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalisanDAO {

    // Personel kaydını ekleyen metot
    public void personelEkle(Personel personel) {
        String sql = "INSERT INTO Calisan (isimSoyisim, kimlikNo, gorevAlani, salary) VALUES (?, ?, ?, ?)";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setString(1, personel.getIsimSoyisim());
            stmt.setString(2, personel.getKimlikNo());

            // Eğer personel Calisan türündeyse, görev ve maaş bilgilerini ekle
            if (personel instanceof Calisan) {
                Calisan calisan = (Calisan) personel; // Tür dönüşümü
                stmt.setString(3, calisan.getGorev());
                stmt.setDouble(4, calisan.getMaas());
            } else {
                stmt.setNull(3, Types.VARCHAR); // gorevAlani için null değeri
                stmt.setNull(4, Types.DOUBLE);  // salary için null değeri
            }

            stmt.executeUpdate();
            System.out.println("Personel başarıyla eklendi!");
        } catch (SQLException e) {
            System.err.println("Personel eklerken hata: " + e.getMessage());
        }
    }

    // Tüm personelleri döndüren metot
    public List<Personel> tumPersonelleriGetir() {
        List<Personel> personelListesi = new ArrayList<>();
        String sql = "SELECT * FROM Calisan"; // Veritabanındaki tüm verileri getiren SQL sorgusu
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             Statement stmt = baglanti.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String isimSoyisim = rs.getString("isimSoyisim");
                String kimlikNo = rs.getString("kimlikNo");
                String gorevAlani = rs.getString("gorevAlani");
                double maas = rs.getDouble("salary");

                // Eğer görev ve maaş bilgileri mevcutsa, Calisan nesnesi oluştur
                if (gorevAlani != null && maas > 0) {
                    personelListesi.add(new Calisan(isimSoyisim, kimlikNo, gorevAlani, maas));
                } else {
                    personelListesi.add(new Personel(isimSoyisim, kimlikNo) {


                        @Override
                        public String benzersizKimlikOlustur() {
                            return "";
                        }
                    }); // Sadece temel personel bilgisi
                }
            }
        } catch (SQLException e) {
            System.err.println("Personelleri getirirken hata: " + e.getMessage());
        }
        return personelListesi;
    }

    // Personel kimlik numarasına göre personel olup olmadığını kontrol eden metot
    public boolean personelVarMi(String kimlikNo) {
        String sql = "SELECT COUNT(*) FROM Calisan WHERE kimlikNo = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setString(1, kimlikNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Personel varlığını kontrol ederken hata: " + e.getMessage());
        }
        return false;
    }

    // Kimlik numarasına göre personeli silen metot
    public void personelSil(String kimlikNo) {
        String sql = "DELETE FROM Calisan WHERE kimlikNo = ?";
        try (Connection baglanti = BaglantiYonetici.baglantiGetir();
             PreparedStatement stmt = baglanti.prepareStatement(sql)) {
            stmt.setString(1, kimlikNo);
            int silinenSatirSayisi = stmt.executeUpdate();
            if (silinenSatirSayisi > 0) {
                System.out.println("Personel başarıyla silindi!");
            } else {
                System.out.println("Personel bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Personel silinirken hata: " + e.getMessage());
        }
    }
}
