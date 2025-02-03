package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Müşteri Ekranı: Müşteri tarafından gerçekleştirilebilecek işlemleri içeren ana menü ekranı
public class MusteriEkrani extends JFrame {
    private JButton rezervasyonGorButonu, odaListeleButonu, rezervasyonSilButonu, rezervasyonYapButonu; // İşlem butonları

    // Yapıcı metot: Müşteri ekranını oluşturur
    public MusteriEkrani(JFrame oncekiEkran) {
        setTitle("Müşteri Ekranı"); // Pencere başlığı
        setExtendedState(JFrame.MAXIMIZED_BOTH); // يجعل النافذة ملء الشاشة
        // Başlangıç boyutu
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Pencere kapandığında ana ekran açık kalır

        try {
            // Arka plan resmini yükle ve ortalama rengini hesapla
            BufferedImage arkaPlanResmi = ImageIO.read(getClass().getClassLoader().getResource("musteri_giris.png"));

            int genislik = arkaPlanResmi.getWidth();
            int yukseklik = arkaPlanResmi.getHeight();
            long toplamKirmizi = 0, toplamYesil = 0, toplamMavi = 0;

            for (int x = 0; x < genislik; x++) {
                for (int y = 0; y < yukseklik; y++) {
                    Color pikselRenk = new Color(arkaPlanResmi.getRGB(x, y));
                    toplamKirmizi += pikselRenk.getRed();
                    toplamYesil += pikselRenk.getGreen();
                    toplamMavi += pikselRenk.getBlue();
                }
            }

            int ortalamaKirmizi = (int) (toplamKirmizi / (genislik * yukseklik));
            int ortalamaYesil = (int) (toplamYesil / (genislik * yukseklik));
            int ortalamaMavi = (int) (toplamMavi / (genislik * yukseklik));

            Color ortalamaRenk = new Color(ortalamaKirmizi, ortalamaYesil, ortalamaMavi);

            // Arka plan paneli
            JPanel arkaPlanPaneli = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon arkaPlanIkonu = new ImageIcon(getClass().getClassLoader().getResource("musteri_giris.png"));
                    if (arkaPlanIkonu.getImage() != null) {
                        Image arkaPlanGorsel = arkaPlanIkonu.getImage();
                        g.drawImage(arkaPlanGorsel, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        System.err.println("Resim bulunamadı: musteri_giris.png");
                    }
                }
            };
            arkaPlanPaneli.setLayout(null);

            // İşlem butonları oluştur
            rezervasyonGorButonu = new JButton("Rezervasyonlarımı Gör");
            odaListeleButonu = new JButton("Tüm Odaları Gör");
            rezervasyonSilButonu = new JButton("Rezervasyon Sil");
            rezervasyonYapButonu = new JButton("Rezervasyon Yap");

            // Buton renklerini ayarla
            rezervasyonGorButonu.setBackground(ortalamaRenk);
            odaListeleButonu.setBackground(ortalamaRenk);
            rezervasyonSilButonu.setBackground(ortalamaRenk);
            rezervasyonYapButonu.setBackground(ortalamaRenk);

            rezervasyonGorButonu.setForeground(Color.WHITE);
            odaListeleButonu.setForeground(Color.WHITE);
            rezervasyonSilButonu.setForeground(Color.WHITE);
            rezervasyonYapButonu.setForeground(Color.WHITE);

            // Başlangıç buton pozisyonları
            rezervasyonGorButonu.setBounds(300, 150, 200, 40);
            odaListeleButonu.setBounds(300, 250, 200, 40);
            rezervasyonSilButonu.setBounds(300, 350, 200, 40);
            rezervasyonYapButonu.setBounds(300, 450, 200, 40);

            // Butonları ekle
            arkaPlanPaneli.add(rezervasyonGorButonu);
            arkaPlanPaneli.add(odaListeleButonu);
            arkaPlanPaneli.add(rezervasyonSilButonu);
            arkaPlanPaneli.add(rezervasyonYapButonu);

            // Buton aksiyonları
            rezervasyonGorButonu.addActionListener(e -> {
                new MusteriRezervasyonGorEkrani(this); // Rezervasyon görüntüleme ekranını aç
                dispose();
            });

            odaListeleButonu.addActionListener(e -> {
                new OdaListeleEkrani(this); // Oda listeleme ekranını aç
                dispose();
            });

            rezervasyonSilButonu.addActionListener(e -> {
                new RezervasyonSilEkrani(this); // Rezervasyon silme ekranını aç
                dispose();
            });

            rezervasyonYapButonu.addActionListener(e -> {
                new RezervasyonYapEkrani(this); // Rezervasyon yapma ekranını aç
                dispose();
            });

            // Arka plan panelini ekle
            add(arkaPlanPaneli);

            // Pencere yeniden boyutlandığında buton pozisyonlarını ayarla
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    butonPozisyonlariniAyarlama();
                }
            });

            // Menü çubuğu oluştur ve ekle
            JMenuBar menuCubugu = new JMenuBar();
            JButton geriButonu = new JButton("⬅ Geri");
            geriButonu.addActionListener(e -> {
                oncekiEkran.setVisible(true); // Önceki ekranı göster
                dispose();
            });

            JButton yenileButonu = new JButton("🔄 Yenile");
            yenileButonu.addActionListener(e -> {
                new MusteriEkrani(oncekiEkran); // Bu ekranı yeniden başlat
                dispose();
            });

            menuCubugu.add(geriButonu);
            menuCubugu.add(yenileButonu);
            setJMenuBar(menuCubugu);

            setVisible(true); // Pencereyi görünür yap

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Arka plan resmi yüklenirken hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Buton pozisyonlarını pencere boyutuna göre dinamik olarak ayarlayan metot
    private void butonPozisyonlariniAyarlama() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;

        rezervasyonGorButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 5, butonGenislik, butonYukseklik);
        odaListeleButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 5 + 70, butonGenislik, butonYukseklik);
        rezervasyonSilButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 5 + 140, butonGenislik, butonYukseklik);
        rezervasyonYapButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 5 + 210, butonGenislik, butonYukseklik);
    }
}
