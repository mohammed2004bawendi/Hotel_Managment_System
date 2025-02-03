package org.example.models;

import javax.swing.*;
import java.awt.*;

// Çalışan Ekle Ekranı: Yeni çalışan eklemek için kullanılan arayüz.
public class CalisanEkleEkrani extends JFrame {
    private JTextField isimAlani, kimlikAlani, gorevAlani, maasAlani; // Giriş alanları
    private JButton ekleButonu, geriButonu, yenileButonu; // İşlem butonları
    private JFrame oncekiEkran; // Önceki ekranın referansı

    // Yapıcı metot: Ekranı oluşturur ve gerekli bileşenleri ekler.
    public CalisanEkleEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Çalışan Ekle"); // Pencere başlığı
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
            new CalisanEkleEkrani(oncekiEkran); // Yeni ekran oluştur
            dispose(); // Eski pencereyi kapat
        });

        ustPanel.add(geriButonu);
        ustPanel.add(yenileButonu);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panel: Giriş alanları
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel isimEtiketi = new JLabel("İsim Soyisim:");
        isimEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        isimAlani = new JTextField(20);
        isimAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel kimlikEtiketi = new JLabel("Kimlik:");
        kimlikEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        kimlikAlani = new JTextField(20);
        kimlikAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel gorevEtiketi = new JLabel("Görev:");
        gorevEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        gorevAlani = new JTextField(20);
        gorevAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel maasEtiketi = new JLabel("Maaş:");
        maasEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        maasAlani = new JTextField(20);
        maasAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        // Giriş alanlarını düzenle ve ekle
        gbc.gridx = 0;
        gbc.gridy = 0;
        ortaPanel.add(isimEtiketi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(isimAlani, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        ortaPanel.add(kimlikEtiketi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(kimlikAlani, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        ortaPanel.add(gorevEtiketi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(gorevAlani, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        ortaPanel.add(maasEtiketi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(maasAlani, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel: Uyarı mesajı ve "Ekle" butonu
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyariMetni = new JTextArea("Lütfen aşağıdaki talimatlara uygun hareket edin:\n" +
                "- İsim ve görev yalnızca harflerden oluşmalı.\n" +
                "- Kimlik yalnızca sayılardan oluşmalı.\n" +
                "- Maaş geçerli bir sayı olmalı.");
        uyariMetni.setEditable(false);
        uyariMetni.setFont(new Font("Arial", Font.PLAIN, 14));
        uyariMetni.setBackground(Color.WHITE);
        uyariMetni.setForeground(Color.DARK_GRAY);
        uyariMetni.setLineWrap(true);
        uyariMetni.setWrapStyleWord(true);
        uyariMetni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(uyariMetni, BorderLayout.NORTH);

        ekleButonu = new JButton("Ekle");
        ekleButonu.setFont(new Font("Arial", Font.BOLD, 16));
        ekleButonu.setPreferredSize(new Dimension(100, 40));
        ekleButonu.addActionListener(e -> calisanEkleIslemi());
        altPanel.add(ekleButonu, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    // Çalışan ekleme işlemi
    private void calisanEkleIslemi() {
        String isim = isimAlani.getText().trim();
        String kimlik = kimlikAlani.getText().trim();
        String gorev = gorevAlani.getText().trim();
        String maasMetni = maasAlani.getText().trim();

        try {
            // Boş alan kontrolü
            if (isim.isEmpty() || kimlik.isEmpty() || gorev.isEmpty() || maasMetni.isEmpty()) {
                throw new IllegalArgumentException("Lütfen tüm alanları doldurun!");
            }

            // Giriş doğrulamaları
            if (!isim.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ ]+")) {
                throw new IllegalArgumentException("İsim sadece harflerden oluşmalı!");
            }
            if (!kimlik.matches("\\d+")) {
                throw new IllegalArgumentException("Kimlik sadece rakamlardan oluşmalı!");
            }
            if (!gorev.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ ]+")) {
                throw new IllegalArgumentException("Görev sadece harflerden oluşmalı!");
            }
            // TC Kimlik doğrulaması
            Rezervasyon tempRezervasyon = new Rezervasyon();
            tempRezervasyon.setKimlikNo(kimlik);
            tempRezervasyon.validateTcKimlik(); // TC Kimlik doğrulama


            double maas = Double.parseDouble(maasMetni);
            if (maas <= 0) {
                throw new IllegalArgumentException("Maaş 0'dan büyük olmalı!");
            }

            // Çalışanı ekle
            CalisanDAO personelDAO = new CalisanDAO();
            Calisan yeniCalisan = new Calisan(isim, kimlik, gorev, maas);
            personelDAO.personelEkle(yeniCalisan);

            JOptionPane.showMessageDialog(this, "Çalışan başarıyla eklendi!");
            isimAlani.setText("");
            kimlikAlani.setText("");
            gorevAlani.setText("");
            maasAlani.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Maaş geçerli bir sayı olmalı!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Bilinmeyen bir hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
