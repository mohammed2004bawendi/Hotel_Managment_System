package org.example.models;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RezervasyonListeEkrani extends JFrame {
    private JFrame oncekiEkran;
    private JTextArea textArea;
    private JButton yenileButon, geriButon;

    public RezervasyonListeEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Tüm Rezervasyonları Gör");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Ana layout, padding ile

        // Üst paneldeki navigasyon butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButon = new JButton("⬅ Geri");
        yenileButon = new JButton("🔄 Yenile");

        // Geri butonu işlemi
        geriButon.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        // Yenile butonu işlemi
        yenileButon.addActionListener(e -> {
            new RezervasyonListeEkrani(oncekiEkran);
            dispose();
        });

        ustPanel.add(geriButon);
        ustPanel.add(yenileButon);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panelde metin alanı
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Alt panelde bilgi metni
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea bilgiTextArea = new JTextArea("Rezervasyon bilgileri başarıyla yüklendi.\n" +
                "- Eğer rezervasyon kaydı yoksa uygun bir mesaj görüntülenecektir.\n" +
                "- Tüm kayıtlar doğru şekilde sıralanmıştır.");
        bilgiTextArea.setEditable(false);
        bilgiTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        bilgiTextArea.setBackground(Color.WHITE);
        bilgiTextArea.setForeground(Color.DARK_GRAY);
        bilgiTextArea.setLineWrap(true);
        bilgiTextArea.setWrapStyleWord(true);
        bilgiTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(bilgiTextArea, BorderLayout.CENTER);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);

        // Ekran açıldığında otomatik olarak rezervasyonları yükle
        rezervasyonlariYukle();
    }

    private void rezervasyonlariYukle() {
        try {
            List<Rezervasyon> rezervasyonlar = RezervasyonDAO.tumRezervasyonlariGetir();
            if (rezervasyonlar.isEmpty()) {
                textArea.setText("Hiçbir rezervasyon bulunamadı!");
            } else {
                textArea.setText("Rezervasyonlar:\n");
                for (Rezervasyon rezervasyon : rezervasyonlar) {
                    textArea.append(rezervasyon.raporOlustur() + "\n\n");
                }

                // Toplam rezervasyon sayısını ekle
                textArea.append("---------------------------------\n");
                textArea.append("Toplam Rezervasyon Sayısı: " + Rezervasyon.getToplamRezervasyonSayisi() + "\n");
            }
        } catch (Exception ex) {
            textArea.setText("Hata: " + ex.getMessage());
        }
    }
}
