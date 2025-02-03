package org.example.models;
// Personel sınıfı, tüm personel bilgilerini saklamak için kullanılan temel sınıftır
public abstract class Personel {
    private String isimSoyisim;   // Personelin ismi ve soyismi
    private String kimlikNo;      // Personelin kimlik numarası

    // Constructor: Personel oluşturulurken istenen bilgileri alır
    public Personel(String isimSoyisim, String kimlikNo) {
        this.isimSoyisim = isimSoyisim;
        this.kimlikNo = kimlikNo;
    }

    public Personel() {}

    // Getter ve Setter metotları
    public String getIsimSoyisim() {
        return isimSoyisim;
    }

    public void setIsimSoyisim(String isimSoyisim) {
        this.isimSoyisim = isimSoyisim;
    }

    public String getKimlikNo() {
        return kimlikNo;
    }

    public void setKimlikNo(String kimlikNo) {
        this.kimlikNo = kimlikNo;
    }

    // Statik yardımcı metot: Kimlik numarası ve isim soyisimden temel ID üretir
    public static String temelKimlikOlustur(String kimlikNo, String isimSoyisim) {
        // Kimlik numarasının ilk 4 hanesini alır
        String first4Tc = kimlikNo.substring(0, 4);
        // İsim soyismin ilk 3 harfini alır
        String first3Name = isimSoyisim.replaceAll("\\s+", "").substring(0, Math.min(3, isimSoyisim.length()));
        // Temel ID'yi oluşturur
        return first4Tc + first3Name.toUpperCase();
    }



    // Abstract metot: Her personel türü için benzersiz ID oluşturulmasını sağlar
    public abstract String benzersizKimlikOlustur();

    // TC Kimlik numarasını doğrulayan genel metot
    public void validateTcKimlik() throws GecersizTcKimlikException {
        // Eğer kimlik numarası geçersizse hata fırlatılır
        if (kimlikNo == null || !kimlikNo.matches("\\d{11}")) {
            throw new GecersizTcKimlikException("Geçersiz TC Kimlik numarası: " + kimlikNo);
        }
    }

    // Personel bilgilerini string olarak döndürür
    @Override
    public String toString() {
        return String.format("İsim Soyisim: %s, Kimlik No: %s", isimSoyisim, kimlikNo);
    }
}
