package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RezervasyonMenuEkrani extends JFrame {
    private JFrame oncekiEkran; // Önceki ekran
    private JButton rezervasyonYapButton, rezervasyonSilButton, rezervasyonListeleButton;

    public RezervasyonMenuEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Rezervasyon Menü Ekranı");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pencere boyutu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pencere kapatıldığında uygulamayı sonlandır

        // Arka plan resmi yükleme
        BufferedImage resim = null;
        try {
            resim = ImageIO.read(getClass().getClassLoader().getResource("rezervasyon_menu.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Resmin ortalama rengini hesapla
        int genislik = resim.getWidth();
        int yukseklik = resim.getHeight();
        long toplamKirmizi = 0, toplamYesil = 0, toplamMavi = 0;

        for (int x = 0; x < genislik; x++) {
            for (int y = 0; y < yukseklik; y++) {
                Color pikselRengi = new Color(resim.getRGB(x, y));
                toplamKirmizi += pikselRengi.getRed();
                toplamYesil += pikselRengi.getGreen();
                toplamMavi += pikselRengi.getBlue();
            }
        }

        int ortalamaKirmizi = (int) (toplamKirmizi / (genislik * yukseklik));
        int ortalamaYesil = (int) (toplamYesil / (genislik * yukseklik));
        int ortalamaMavi = (int) (toplamMavi / (genislik * yukseklik));

        Color ortalamaRenk = new Color(ortalamaKirmizi, ortalamaYesil, ortalamaMavi);

        // Arka plan resmini çizen özel bir JPanel oluştur
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Arka plan resmini yükle ve çiz
                ImageIcon arkaPlanIkon = new ImageIcon(getClass().getClassLoader().getResource("rezervasyon_menu.png"));
                if (arkaPlanIkon.getImage() != null) {
                    Image arkaPlanResmi = arkaPlanIkon.getImage();
                    g.drawImage(arkaPlanResmi, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Resim bulunamadı!");
                }
            }
        };
        arkaPlanPaneli.setLayout(null); // Özel pozisyonlandırma için null layout kullan
        setContentPane(arkaPlanPaneli);

        // Menü çubuğu oluştur
        JMenuBar menuCubugu = new JMenuBar();

        // Geri Butonu
        JButton geriButonu = new JButton("⬅ Geri");
        geriButonu.addActionListener((ActionEvent e) -> {
            oncekiEkran.setVisible(true); // Önceki ekranı göster
            dispose(); // Mevcut ekranı kapat
        });

        // Yenile Butonu
        JButton yenileButonu = new JButton("🔄 Yenile");
        yenileButonu.addActionListener((ActionEvent e) -> {
            new RezervasyonMenuEkrani(oncekiEkran); // Bu ekranı yeniden başlat
            dispose(); // Mevcut ekranı kapat
        });

        // Menü çubuğuna butonları ekle
        menuCubugu.add(geriButonu);
        menuCubugu.add(yenileButonu);

        // Menü çubuğunu pencereye ekle
        setJMenuBar(menuCubugu);

        // Butonlar
        rezervasyonYapButton = new JButton("Rezervasyon Yap");
        rezervasyonSilButton = new JButton("Rezervasyon Sil");
        rezervasyonListeleButton = new JButton("Rezervasyon Listele");

        // Butonlara renkler ver
        rezervasyonYapButton.setBackground(ortalamaRenk);
        rezervasyonSilButton.setBackground(ortalamaRenk);
        rezervasyonListeleButton.setBackground(ortalamaRenk);

        rezervasyonYapButton.setForeground(Color.WHITE);
        rezervasyonSilButton.setForeground(Color.WHITE);
        rezervasyonListeleButton.setForeground(Color.WHITE);

        // Butonları arka plan paneline ekle
        arkaPlanPaneli.add(rezervasyonYapButton);
        arkaPlanPaneli.add(rezervasyonSilButton);
        arkaPlanPaneli.add(rezervasyonListeleButton);

        // Pencere boyutunu değiştirme olaylarını dinle
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                butonPozisyonlariniAyarla();
            }
        });

        // İlk buton pozisyonları
        butonPozisyonlariniAyarla();

        // Rezervasyon yap butonuna tıklandığında rezervasyon yapma ekranını aç
        rezervasyonYapButton.addActionListener(e -> {
            new RezervasyonYapEkrani(this);
            dispose();
        });

        // Rezervasyon sil butonuna tıklandığında rezervasyon silme ekranını aç
        rezervasyonSilButton.addActionListener(e -> {
            new RezervasyonSilEkrani(this);
            dispose();
        });

        // Rezervasyon listele butonuna tıklandığında rezervasyonları listeleme ekranını aç
        rezervasyonListeleButton.addActionListener(e -> {
            new RezervasyonListeEkrani(this);
            dispose();
        });

        setVisible(true);
    }

    // Butonların pozisyonlarını pencere boyutuna göre ayarla
    private void butonPozisyonlariniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;

        // Butonları merkezi pozisyonlarda ayarla
        rezervasyonYapButton.setBounds((genislik - butonGenislik) / 2, yukseklik / 3, butonGenislik, butonYukseklik);
        rezervasyonSilButton.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 70, butonGenislik, butonYukseklik);
        rezervasyonListeleButton.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 140, butonGenislik, butonYukseklik);
    }
}
