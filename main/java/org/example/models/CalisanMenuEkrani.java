package org.example.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Çalışan Menü Ekranı: Çalışanlarla ilgili işlemleri gerçekleştirmek için kullanılan ana menü ekranı.
public class CalisanMenuEkrani extends JFrame {
    private JFrame oncekiEkran; // Önceki ekranın referansı
    private JButton calisanEkleButonu, calisanSilButonu, calisanListeleButonu; // İşlem butonları

    // Yapıcı metot: Çalışan menü ekranını oluşturur.
    public CalisanMenuEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Çalışan Menü Ekranı"); // Pencere başlığı
        setExtendedState(JFrame.MAXIMIZED_BOTH);// Başlangıç boyutu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pencere kapandığında uygulama açık kalır
        setLayout(null); // Serbest düzen kullanılıyor

        // Arkaplan resmini yükle ve ortalama rengini hesapla
        BufferedImage arkaPlanResmi = null;
        try {
            arkaPlanResmi = ImageIO.read(getClass().getClassLoader().getResource("calisan_menu.png"));
        } catch (IOException e) {
            throw new RuntimeException("Arka plan resmi yüklenemedi.", e);
        }

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

        // Arkaplan paneli oluştur
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Arkaplan resmini yükle ve panele çiz
                ImageIcon arkaPlanIkonu = new ImageIcon(getClass().getClassLoader().getResource("calisan_menu.png"));
                if (arkaPlanIkonu.getImage() != null) {
                    Image arkaPlanGorsel = arkaPlanIkonu.getImage();
                    g.drawImage(arkaPlanGorsel, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("Resim bulunamadı: calisan_menu.png");
                }
            }
        };
        arkaPlanPaneli.setLayout(null); // Serbest düzen
        arkaPlanPaneli.setBounds(0, 0, getWidth(), getHeight()); // Tüm pencereyi kaplayacak şekilde ayarlanır

        // Menü çubuğu oluştur ve Geri/Yenile butonları ekle
        JMenuBar menuCubugu = new JMenuBar();
        JButton geriButonu = new JButton("⬅ Geri");
        geriButonu.addActionListener((ActionEvent e) -> {
            oncekiEkran.setVisible(true); // Önceki ekranı görünür yap
            dispose(); // Bu ekranı kapat
        });

        JButton yenileButonu = new JButton("🔄 Yenile");
        yenileButonu.addActionListener((ActionEvent e) -> {
            new CalisanMenuEkrani(oncekiEkran); // Yeni ekran oluştur
            dispose(); // Eski ekranı kapat
        });

        menuCubugu.add(geriButonu);
        menuCubugu.add(yenileButonu);
        setJMenuBar(menuCubugu);

        // Çalışan işlemleri için butonlar oluştur
        calisanEkleButonu = new JButton("Çalışan Ekle");
        calisanSilButonu = new JButton("Çalışan Sil");
        calisanListeleButonu = new JButton("Çalışanları Listele");

        // Butonların başlangıç pozisyonlarını ayarla
        calisanEkleButonu.setBounds(300, 100, 200, 40);
        calisanSilButonu.setBounds(300, 170, 200, 40);
        calisanListeleButonu.setBounds(300, 240, 200, 40);

        // Buton renklerini ayarla
        calisanEkleButonu.setBackground(ortalamaRenk);
        calisanSilButonu.setBackground(ortalamaRenk);
        calisanListeleButonu.setBackground(ortalamaRenk);

        calisanEkleButonu.setForeground(Color.WHITE);
        calisanSilButonu.setForeground(Color.WHITE);
        calisanListeleButonu.setForeground(Color.WHITE);

        // Butonları arkaplan paneline ekle
        arkaPlanPaneli.add(calisanEkleButonu);
        arkaPlanPaneli.add(calisanSilButonu);
        arkaPlanPaneli.add(calisanListeleButonu);

        // Buton aksiyonlarını tanımla
        calisanEkleButonu.addActionListener(e -> {
            new CalisanEkleEkrani(this); // Çalışan ekleme ekranını aç
            dispose();
        });
        calisanSilButonu.addActionListener(e -> {
            new CalisanSilEkrani(this); // Çalışan silme ekranını aç
            dispose();
        });
        calisanListeleButonu.addActionListener(e -> {
            new CalisanListeleEkrani(this); // Çalışan listeleme ekranını aç
            dispose();
        });

        // Arkaplan panelini pencereye ekle
        add(arkaPlanPaneli);

        // Pencere boyutu değiştiğinde arkaplan ve buton pozisyonlarını yeniden ayarla
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                arkaPlanPaneli.setBounds(0, 0, getWidth(), getHeight()); // Arkaplan panelini yeniden boyutlandır
                arkaPlanPaneli.repaint(); // Arkaplan görüntüsünü yeniden çiz
                butonPozisyonlariniAyarla(); // Buton pozisyonlarını yeniden hesapla
            }
        });

        setVisible(true); // Pencereyi görünür yap
    }

    // Pencere boyutuna göre buton pozisyonlarını yeniden hesaplayan metot
    private void butonPozisyonlariniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        int butonGenislik = 200;
        int butonYukseklik = 40;

        calisanEkleButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3, butonGenislik, butonYukseklik);
        calisanSilButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 70, butonGenislik, butonYukseklik);
        calisanListeleButonu.setBounds((genislik - butonGenislik) / 2, yukseklik / 3 + 140, butonGenislik, butonYukseklik);
    }
}
