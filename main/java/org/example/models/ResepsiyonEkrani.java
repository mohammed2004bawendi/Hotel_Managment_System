package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResepsiyonEkrani extends JFrame {
    private JButton odaYonetimiButonu, rezervasyonYapButonu;
    private JFrame oncekiEkran;

    public ResepsiyonEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Resepsiyon Ekranı");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BufferedImage resim = null;
        try {
            resim = ImageIO.read(getClass().getClassLoader().getResource("resepsiyon_giris.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        // Arka plan panelini oluşturma
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon arkaPlanResmi = new ImageIcon(getClass().getClassLoader().getResource("resepsiyon_giris.png"));
                if (arkaPlanResmi.getImage() != null) {
                    Image arkaPlanResmiImage = arkaPlanResmi.getImage();
                    g.drawImage(arkaPlanResmiImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Resim bulunamadı: resepsiyon_giris.png");
                }
            }
        };
        arkaPlanPaneli.setLayout(null);

        // Butonları oluşturma
        odaYonetimiButonu = new JButton("Oda Yönetimi Menüsü");
        rezervasyonYapButonu = new JButton("Rezervasyon Menüsü");

        odaYonetimiButonu.setBackground(ortalamaRenk);
        rezervasyonYapButonu.setBackground(ortalamaRenk);

        odaYonetimiButonu.setForeground(Color.WHITE);
        rezervasyonYapButonu.setForeground(Color.WHITE);

        // Butonları panellere ekleme
        arkaPlanPaneli.add(odaYonetimiButonu);
        arkaPlanPaneli.add(rezervasyonYapButonu);

        // Butonlara aksiyon dinleyicileri ekleme
        odaYonetimiButonu.addActionListener(e -> {
            new OdaMenuEkrani(this);
            dispose();
        });

        rezervasyonYapButonu.addActionListener(e -> {
            new RezervasyonMenuEkrani(this);
            dispose();
        });

        // Yeniden boyutlandırma dinleyicisi ekleme
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                butonPozisyonlariniAyarla();
            }
        });

        // Menü çubuğunu oluşturma
        JMenuBar menuBar = new JMenuBar();

        // Geri butonu
        JButton geriButonu = new JButton("⬅ Geri");
        geriButonu.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        // Yenile butonu
        JButton yenileButonu = new JButton("🔄 Yenile");
        yenileButonu.addActionListener(e -> {
            new ResepsiyonEkrani(oncekiEkran); // Bu ekranı yeniden başlat
            dispose();
        });

        // Butonları menü çubuğuna ekle
        menuBar.add(geriButonu);
        menuBar.add(yenileButonu);

        // Menü çubuğunu çerçeveye ekle
        setJMenuBar(menuBar);

        // Paneli çerçeveye ekle
        add(arkaPlanPaneli);

        // Çerçevenin görünürlüğünü ayarla
        setVisible(true);
    }

    private void butonPozisyonlariniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;
        int dikeyMesafe = 70;

        // Buton pozisyonlarını ayarlama
        odaYonetimiButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3, butonGenislik, butonYukseklik);
        rezervasyonYapButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + dikeyMesafe, butonGenislik, butonYukseklik);
    }
}
