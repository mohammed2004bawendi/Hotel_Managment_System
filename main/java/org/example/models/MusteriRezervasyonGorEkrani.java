package org.example.models;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Müşteri Rezervasyon Görüntüleme Ekranı
public class MusteriRezervasyonGorEkrani extends JFrame {
    private JTextField tcKimlikAlani; // TC Kimlik giriş alanı
    private JTextArea sonucAlani; // Rezervasyon sonuçlarını göstermek için metin alanı
    private JButton listeleButonu, geriButonu, yenileButonu; // İşlem butonları
    private JFrame oncekiEkran; // Önceki ekranın referansı

    // Yapıcı metot: Müşteri rezervasyon görüntüleme ekranını oluşturur
    public MusteriRezervasyonGorEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Rezervasyonlarımı Gör"); // Pencere başlığı
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Kapatıldığında ana ekran açık kalır
        setLayout(new BorderLayout(10, 10)); // Ana düzen

        // Üst panel: Geri ve Yenile butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButonu = new JButton("⬅ Geri");
        yenileButonu = new JButton("🔄 Yenile");

        // Geri butonu aksiyonu
        geriButonu.addActionListener(e -> {
            oncekiEkran.setVisible(true); // Önceki ekranı görünür yap
            dispose(); // Bu pencereyi kapat
        });

        // Yenile butonu aksiyonu
        yenileButonu.addActionListener(e -> {
            new MusteriRezervasyonGorEkrani(oncekiEkran); // Yeni ekran oluştur
            dispose(); // Eski pencereyi kapat
        });

        ustPanel.add(geriButonu);
        ustPanel.add(yenileButonu);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panel: Giriş alanı ve sonuçlar
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // TC Kimlik giriş alanı
        JLabel tcKimlikEtiketi = new JLabel("TC Kimlik:");
        tcKimlikEtiketi.setFont(new Font("Arial", Font.BOLD, 16));
        tcKimlikAlani = new JTextField();
        tcKimlikAlani.setFont(new Font("Arial", Font.PLAIN, 16));

        // Giriş alanını orta panele ekle
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 0;
        ortaPanel.add(tcKimlikEtiketi, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ortaPanel.add(tcKimlikAlani, gbc);

        // Sonuçlar için metin alanı
        sonucAlani = new JTextArea();
        sonucAlani.setEditable(false);
        sonucAlani.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane kaydirmaCubugu = new JScrollPane(sonucAlani);
        kaydirmaCubugu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        ortaPanel.add(kaydirmaCubugu, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel: Bilgilendirme metni ve Listele butonu
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea bilgiMetni = new JTextArea("Lütfen TC Kimlik numaranızı girerek 'Listele' butonuna basınız.\n" +
                "- Eğer rezervasyon kaydı yoksa uygun bir mesaj göreceksiniz.\n" +
                "- Tüm kayıtlar doğru şekilde sıralanmıştır.");
        bilgiMetni.setEditable(false);
        bilgiMetni.setFont(new Font("Arial", Font.PLAIN, 14));
        bilgiMetni.setBackground(Color.WHITE);
        bilgiMetni.setForeground(Color.DARK_GRAY);
        bilgiMetni.setLineWrap(true);
        bilgiMetni.setWrapStyleWord(true);
        bilgiMetni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(bilgiMetni, BorderLayout.NORTH);

        listeleButonu = new JButton("Listele");
        listeleButonu.setFont(new Font("Arial", Font.BOLD, 16));
        listeleButonu.setPreferredSize(new Dimension(0, 40));
        listeleButonu.addActionListener(e -> rezervasyonlariListele());
        altPanel.add(listeleButonu, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(500, 350));
        setVisible(true);
    }

    // Rezervasyonları listeleme işlemi
    private void rezervasyonlariListele() {
        String tcKimlik = tcKimlikAlani.getText().trim(); // TC Kimlik girişini al

        try {
            if (tcKimlik.isEmpty()) {
                throw new IllegalArgumentException("TC Kimlik numarası boş bırakılamaz!"); // Boş kontrolü
            }

            // TC Kimlik doğrulaması
            Rezervasyon geciciRezervasyon = new Rezervasyon();
            geciciRezervasyon.setKimlikNo(tcKimlik);
            geciciRezervasyon.validateTcKimlik();

            // Rezervasyonları al ve sonucu göster
            List<Rezervasyon> rezervasyonListesi = RezervasyonDAO.rezervasyonGetirByTcKimlik(tcKimlik);

            if (rezervasyonListesi.isEmpty()) {
                sonucAlani.setText("Hiçbir rezervasyon bulunamadı!");
            } else {
                sonucAlani.setText("Rezervasyonlarınız:\n");
                for (Rezervasyon rezervasyon : rezervasyonListesi) {
                    sonucAlani.append(rezervasyon.detaylariGoster() + "\n\n");
                }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
