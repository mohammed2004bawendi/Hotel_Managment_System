package org.example.models;
// Çalışan sınıfı, Personel sınıfını genişletir ve Reportable arayüzünü uygular.
// Bu sınıf, bir çalışanın görev ve maaş gibi ek özelliklerini içerir.
public class Calisan extends Personel implements Raporlanabilir {
    private String gorev; // Çalışanın görevi
    private double maas;  // Çalışanın maaşı

    // Yapıcı metot: Çalışan nesnesi oluşturur.
    public Calisan(String isimSoyisim, String kimlikNo, String gorev, double maas) {
        super(isimSoyisim, kimlikNo);
        this.gorev = gorev;
        this.maas = maas;
    }

    // Görev alanını döndürür.
    public String getGorev() {
        return gorev;
    }

    // Görev alanını günceller.
    public void setGorev(String gorev) {
        this.gorev = gorev;
    }

    // Maaş bilgisini döndürür.
    public double getMaas() {
        return maas;
    }

    // Maaş bilgisini günceller.
    public void setMaas(double maas) {
        this.maas = maas;
    }

    // Çalışan için benzersiz bir kimlik oluşturur.
    @Override
    public String benzersizKimlikOlustur() {
        return "CAL-" + getKimlikNo().substring(0, 4) + "-" + gorev.substring(0, 2).toUpperCase();
    }

    // Çalışan bilgilerini içeren bir rapor oluşturur.
    @Override
    public String raporOlustur() {
        return String.format("Çalışan Bilgileri:\nİsim Soyisim: %s\nTC Kimlik: %s\nGörev: %s\nMaaş: %.2f",
                getIsimSoyisim(), getKimlikNo(), gorev, maas);
    }

    // Çalışan bilgilerini yazdırır.
    @Override
    public String toString() {
        return raporOlustur();
    }
}
