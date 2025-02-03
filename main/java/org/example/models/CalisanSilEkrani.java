package org.example.models;

import javax.swing.*;
import java.awt.*;

// Çalışan Silme Ekranı: Belirli bir kimlik numarası ile çalışan silmek için kullanılan arayüz
public class CalisanSilEkrani extends JFrame {
    private JTextField kimlikAlani; // Kimlik numarası giriş alanı
    private JButton silButonu, geriButonu, yenileButonu; // İşlem butonları
    private JFrame oncekiEkran; // Önceki ekranın referansı

    // Yapıcı metot: Çalışan silme ekranını oluşturur
    public CalisanSilEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Çalışan Sil"); // Pencere başlığı
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
            new CalisanSilEkrani(oncekiEkran); // Yeni ekran oluştur
            dispose(); // Eski pencereyi kapat
        });

        ustPanel.add(geriButonu);
        ustPanel.add(yenileButonu);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panel: Kimlik numarası giriş alanı
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // İçerik aralığı
        gbc.anchor = GridBagConstraints.WEST;

        JLabel kimlikEtiketi = new JLabel("Personel Kimlik No:");
        kimlikEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        kimlikAlani = new JTextField(20); // Giriş alanı
        kimlikAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bileşenleri orta panele ekle
        gbc.gridx = 0;
        gbc.gridy = 0;
        ortaPanel.add(kimlikEtiketi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(kimlikAlani, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel: Uyarı mesajı ve "Sil" butonu
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyariMetni = new JTextArea("Lütfen aşağıdaki talimatlara uygun hareket edin:\n" +
                "- Çalışan kimlik numarasını doğru girin.\n" +
                "- Kimlik sistemde mevcut değilse işlem yapılamaz.");
        uyariMetni.setEditable(false);
        uyariMetni.setFont(new Font("Arial", Font.PLAIN, 14));
        uyariMetni.setBackground(Color.WHITE);
        uyariMetni.setForeground(Color.DARK_GRAY);
        uyariMetni.setLineWrap(true);
        uyariMetni.setWrapStyleWord(true);
        uyariMetni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(uyariMetni, BorderLayout.NORTH);

        silButonu = new JButton("Sil");
        silButonu.setFont(new Font("Arial", Font.BOLD, 16));
        silButonu.setPreferredSize(new Dimension(100, 40));
        silButonu.addActionListener(e -> calisanSilIslemi());
        altPanel.add(silButonu, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    // Çalışan silme işlemi
    private void calisanSilIslemi() {
        String kimlik = kimlikAlani.getText().trim(); // Giriş alanından kimlik numarasını al

        try {
            // Boş giriş kontrolü
            if (kimlik.isEmpty()) {
                throw new IllegalArgumentException("Lütfen personel kimlik numarasını girin!");
            }

            // Çalışan kontrolü ve silme işlemi
            CalisanDAO personelDAO = new CalisanDAO();
            if (!personelDAO.personelVarMi(kimlik)) { // Çalışanın mevcut olup olmadığını kontrol et
                throw new IllegalArgumentException("Hata: Çalışan bulunamadı!");
            }

            personelDAO.personelSil(kimlik); // Çalışanı sil
            JOptionPane.showMessageDialog(this, "Çalışan başarıyla silindi!");

            // Silme işleminden sonra giriş alanını temizle
            kimlikAlani.setText("");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
