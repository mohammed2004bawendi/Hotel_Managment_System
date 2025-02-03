package org.example.models;

// Geçersiz TC Kimlik Numarası durumunda fırlatılacak özel bir istisna sınıfı
public class GecersizTcKimlikException extends Exception {

    // Yapıcı metot: Hata mesajını üst sınıfa iletir
    public GecersizTcKimlikException(String mesaj) {
        super(mesaj); // Hata mesajını üst sınıfın (Exception) yapıcı metotuna iletir
    }
}
