package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OdaMenuEkrani extends JFrame {
    private JFrame oncekiEkran;  // Önceki ekranı tutan değişken
    private JButton odaEkleButonu, odaSilButonu, odaListeleButonu;

    // Yapıcı metod: OdaMenuEkrani ekranını başlatır
    public OdaMenuEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Oda Menü Ekranı");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ekran boyutları
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Ana düzen (BorderLayout)

        // Arka plan resmini yükle ve ortalama renk hesapla
        BufferedImage resim = null;
        try {
            resim = ImageIO.read(getClass().getClassLoader().getResource("calisan_menu.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Resmin her pikselinin rengini hesaplayarak ortalama renkleri bulma
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

        // Arka plan paneli oluştur
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Arka plan resmini çizme
                ImageIcon arkaPlanIkon = new ImageIcon(getClass().getClassLoader().getResource("oda_menu.png"));
                if (arkaPlanIkon.getImage() != null) {
                    Image arkaPlanResmi = arkaPlanIkon.getImage();
                    g.drawImage(arkaPlanResmi, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Resim bulunamadı!");
                }
            }
        };

        arkaPlanPaneli.setLayout(null);  // Manuel buton yerleştirme için null layout kullanıyoruz

        // Menü çubuğu oluştur (Geri ve Yenile butonları)
        JMenuBar menuCubugu = new JMenuBar();
        JButton geriButonu = new JButton("⬅ Geri");
        geriButonu.addActionListener((ActionEvent e) -> {
            oncekiEkran.setVisible(true); // Önceki ekranı göster
            dispose(); // Bu ekranı kapat
        });

        JButton yenileButonu = new JButton("🔄 Yenile");
        yenileButonu.addActionListener((ActionEvent e) -> {
            new OdaMenuEkrani(oncekiEkran); // Ekranı yeniden başlat
            dispose(); // Bu ekranı kapat
        });

        menuCubugu.add(geriButonu);
        menuCubugu.add(yenileButonu);
        setJMenuBar(menuCubugu);  // Menü çubuğunu ekle

        // Butonları oluştur
        odaEkleButonu = new JButton("Oda Ekle");
        odaSilButonu = new JButton("Oda Sil");
        odaListeleButonu = new JButton("Oda Listele");

        // Butonların konumlarını ayarla
        odaEkleButonu.setBounds(300, 100, 200, 40);
        odaSilButonu.setBounds(300, 170, 200, 40);
        odaListeleButonu.setBounds(300, 240, 200, 40);

        // Butonların arka plan rengini ortalama renk ile ayarla
        odaEkleButonu.setBackground(ortalamaRenk);
        odaSilButonu.setBackground(ortalamaRenk);
        odaListeleButonu.setBackground(ortalamaRenk);

        // Butonların yazı rengini beyaz yap
        odaEkleButonu.setForeground(Color.WHITE);
        odaSilButonu.setForeground(Color.WHITE);
        odaListeleButonu.setForeground(Color.WHITE);

        // Butonları arka plan paneline ekle
        arkaPlanPaneli.add(odaEkleButonu);
        arkaPlanPaneli.add(odaSilButonu);
        arkaPlanPaneli.add(odaListeleButonu);

        // Butonlara tıklama olayları ekle
        odaEkleButonu.addActionListener(e -> {
            new OdaEkleEkrani(this); // Oda ekleme ekranını aç
            dispose(); // Bu ekranı kapat
        });

        odaSilButonu.addActionListener(e -> {
            new OdaSilEkrani(this); // Oda silme ekranını aç
            dispose(); // Bu ekranı kapat
        });

        odaListeleButonu.addActionListener(e -> {
            new OdaListeleEkrani(this); // Oda listeleme ekranını aç
            dispose(); // Bu ekranı kapat
        });

        // Arka plan panelini ekrana yerleştir
        add(arkaPlanPaneli, BorderLayout.CENTER);

        // Pencere boyutu değiştiğinde butonların yerlerini ayarlamak için dinleyici ekle
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                butonYerleriniAyarla();
            }
        });

        setVisible(true); // Ekranı görünür yap
    }

    // Butonların konumlarını pencere boyutuna göre dinamik olarak ayarlar
    private void butonYerleriniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;

        // Butonları dinamik olarak ortalayacak şekilde yerleştir
        odaEkleButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3, butonGenislik, butonYukseklik);
        odaSilButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 70, butonGenislik, butonYukseklik);
        odaListeleButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 140, butonGenislik, butonYukseklik);
    }
}
