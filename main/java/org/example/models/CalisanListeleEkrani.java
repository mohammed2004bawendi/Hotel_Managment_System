package org.example.models;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Çalışan Listeleme Ekranı: Tüm çalışanları listelemek için kullanılan arayüz
public class CalisanListeleEkrani extends JFrame {
    final private JTextArea textAlani; // Çalışan bilgilerini görüntüleme alanı
    final private JButton geriButonu, yenileButonu; // Geri ve Yenile butonları
    final private JFrame oncekiEkran; // Önceki ekranın referansı

    // Yapıcı metot: Ekranı oluşturur ve gerekli bileşenleri ekler
    public CalisanListeleEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Çalışanları Listele"); // Pencere başlığı
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında ana ekran açık kalır
        setLayout(new BorderLayout(10, 10)); // Ana düzen

        // Üst panel: Geri ve Yenile butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButonu = new JButton("⬅ Geri");
        yenileButonu = new JButton("🔄 Yenile");

        geriButonu.addActionListener(e -> {
            oncekiEkran.setVisible(true); // Önceki ekranı görünür yap
            dispose(); // Bu pencereyi kapat
        });

        yenileButonu.addActionListener(e -> {
            new CalisanListeleEkrani(oncekiEkran); // Yeni ekran oluştur
            dispose(); // Eski pencereyi kapat
        });

        ustPanel.add(geriButonu);
        ustPanel.add(yenileButonu);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panel: Çalışan bilgilerini görüntüleme alanı
        JPanel ortaPanel = new JPanel(new BorderLayout());
        textAlani = new JTextArea(); // Çalışan bilgilerini gösterecek alan
        textAlani.setEditable(false); // Alan sadece okunabilir
        textAlani.setFont(new Font("Arial", Font.PLAIN, 14));
        textAlani.setBackground(Color.WHITE);
        textAlani.setForeground(Color.DARK_GRAY);

        JScrollPane kaydirmaCubugu = new JScrollPane(textAlani);
        kaydirmaCubugu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ortaPanel.add(kaydirmaCubugu, BorderLayout.CENTER);

        // Çalışan bilgilerini doldur
        calisanBilgileriniDoldur();

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel: Uyarı metni
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyariMetni = new JTextArea("Çalışan bilgileri başarıyla yüklendi.\n" +
                "- Çalışan kaydı yoksa ilgili mesaj görüntülenir.\n" +
                "- Tüm kayıtlar doğru şekilde sıralanmıştır.");
        uyariMetni.setEditable(false);
        uyariMetni.setFont(new Font("Arial", Font.PLAIN, 14));
        uyariMetni.setBackground(Color.WHITE);
        uyariMetni.setForeground(Color.DARK_GRAY);
        uyariMetni.setLineWrap(true);
        uyariMetni.setWrapStyleWord(true);
        uyariMetni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(uyariMetni, BorderLayout.CENTER);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    // Çalışan bilgilerini doldurur ve görüntüleme alanına ekler
    private void calisanBilgileriniDoldur() {
        try {
            CalisanDAO personelDAO = new CalisanDAO(); // Çalışan veritabanı işlemleri
            List<Personel> personelListesi = personelDAO.tumPersonelleriGetir(); // Tüm çalışanları getir

            if (personelListesi.isEmpty()) {
                textAlani.setText("Hiç çalışan kaydı bulunamadı!"); // Kayıt yoksa mesaj göster
            } else {
                textAlani.setText("Tüm Çalışanlar:\n");
                for (Personel personel : personelListesi) {
                    if (personel instanceof Raporlanabilir) { // Çalışan raporlanabilir mi kontrol et
                        Raporlanabilir raporlanabilir = (Raporlanabilir) personel; // Raporlanabilir olarak işle
                        textAlani.append(raporlanabilir.raporOlustur() + "\n\n"); // Raporu görüntüle
                    } else {
                        textAlani.append(personel.toString() + "\n\n"); // Normal çalışan bilgilerini göster
                    }
                }
            }
        } catch (Exception ex) {
            textAlani.setText("Hata: " + ex.getMessage()); // Hata durumunda mesaj göster
        }
    }
}
