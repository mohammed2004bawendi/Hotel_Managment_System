package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class YoneticiEkrani extends JFrame {
    private JFrame oncekiEkran;
    private JButton calisanMenuButonu, odaMenuButonu, rezervasyonMenuButonu;

    public YoneticiEkrani(JFrame oncekiEkran) throws IOException {
        this.oncekiEkran = oncekiEkran;

        setTitle("Yönetici Ekranı");
        setExtendedState(JFrame.MAXIMIZED_BOTH);        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Basitlik için null layout kullanıldı.

        BufferedImage resim = ImageIO.read(getClass().getClassLoader().getResource("yonetici_menu.png"));

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

        // Dinamik bir arka plan resmi eklemek için panel oluşturma
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Resmi yükle
                java.net.URL resimUrl = getClass().getClassLoader().getResource("yonetici_menu.png");
                if (resimUrl != null) {
                    ImageIcon arkaPlanIkon = new ImageIcon(resimUrl);
                    Image arkaPlanResmi = arkaPlanIkon.getImage();
                    // Resmi panel boyutuna göre ölçeklendir
                    g.drawImage(arkaPlanResmi, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Resim kaynaklarda bulunamadı!");
                }
            }
        };
        arkaPlanPaneli.setLayout(null); // Butonları manuel olarak yerleştireceğiz
        arkaPlanPaneli.setBounds(0, 0, getWidth(), getHeight()); // Panelin tamamını kaplamasını sağla

        // Menü çubuğu oluşturma
        JMenuBar menuCubuğu = new JMenuBar();
        JButton geriButonu = new JButton("⬅ Geri");
        geriButonu.addActionListener((ActionEvent e) -> {
            oncekiEkran.setVisible(true);
            dispose();
        });
        JButton yenileButonu = new JButton("🔄 Yenile");
        yenileButonu.addActionListener((ActionEvent e) -> {
            try {
                new YoneticiEkrani(oncekiEkran);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });
        menuCubuğu.add(geriButonu);
        menuCubuğu.add(yenileButonu);
        setJMenuBar(menuCubuğu);

        // Ekranda kullanılacak butonları oluşturma
        calisanMenuButonu = new JButton("Çalışan Menü");
        odaMenuButonu = new JButton("Oda Menü");
        rezervasyonMenuButonu = new JButton("Rezervasyon Menü");

        // İlk buton konumlarını belirleme
        calisanMenuButonu.setBounds(300, 150, 200, 40);
        odaMenuButonu.setBounds(300, 250, 200, 40);
        rezervasyonMenuButonu.setBounds(300, 350, 200, 40);

        calisanMenuButonu.setBackground(ortalamaRenk);
        odaMenuButonu.setBackground(ortalamaRenk);
        rezervasyonMenuButonu.setBackground(ortalamaRenk);

        calisanMenuButonu.setForeground(Color.WHITE);
        odaMenuButonu.setForeground(Color.WHITE);
        rezervasyonMenuButonu.setForeground(Color.WHITE);

        // Butonları arka plan paneline ekleme
        arkaPlanPaneli.add(calisanMenuButonu);
        arkaPlanPaneli.add(odaMenuButonu);
        arkaPlanPaneli.add(rezervasyonMenuButonu);

        // Buton eylemleri
        calisanMenuButonu.addActionListener(e -> {
            new CalisanMenuEkrani(this);
            dispose();});
        odaMenuButonu.addActionListener(e -> {
            new OdaMenuEkrani(this);
            dispose();
        });
        rezervasyonMenuButonu.addActionListener(e -> {
            new RezervasyonMenuEkrani(this);
            dispose();
        });

        // Arka plan panelini frame'e ekleme
        add(arkaPlanPaneli);

        // Yeniden boyutlandırma dinleyicileri
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                arkaPlanPaneli.setBounds(0, 0, getWidth(), getHeight()); // Arka plan panelini yeniden boyutlandır
                arkaPlanPaneli.repaint(); // Yeniden boyutlandırılmış resmi tekrar çiz
                butonKonumlariniAyarla(); // Butonların yeni konumlarını hesapla
            }
        });

        setVisible(true);
    }

    private void butonKonumlariniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;

        // Buton konumlarını pencere boyutuna göre dinamik olarak ayarla
        calisanMenuButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3, butonGenislik, butonYukseklik);
        odaMenuButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 70, butonGenislik, butonYukseklik);
        rezervasyonMenuButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 140, butonGenislik, butonYukseklik);
    }
}
