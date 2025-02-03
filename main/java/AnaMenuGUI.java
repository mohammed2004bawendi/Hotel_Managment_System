import org.example.models.MusteriEkrani;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * AnaMenuGUI: Otel Y
 * önetim Sistemi Ana Menü Arayüzü
 * Bu sınıf, kullanıcıların Yönetici, Resepsiyon ve Müşteri girişlerini yapabilecekleri ana menüyü sağlar.
 */
public class AnaMenuGUI extends JFrame {
    private JButton yoneticiButonu, resepsiyonButonu, musteriButonu;

    /**
     * Ana Menü arayüzünü başlatır ve gerekli bileşenleri oluşturur.
     */
    public AnaMenuGUI() throws IOException {
        setTitle("Otel Yönetim Sistemi - Ana Menü"); // Pencere başlığı
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pencereyi tam ekran yapar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pencere kapatıldığında uygulama sonlanır

        // Arka plan resmi yüklenir ve ortalama renk hesaplanır
        BufferedImage resim = ImageIO.read(getClass().getClassLoader().getResource("menu_giris.png"));
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

        // Arka plan paneli tanımlanır
        JPanel arkaPlanPaneli = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon arkaPlanResmi = new ImageIcon(getClass().getClassLoader().getResource("menu_giris.png"));
                if (arkaPlanResmi.getImage() != null) {
                    g.drawImage(arkaPlanResmi.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        arkaPlanPaneli.setLayout(null);

        // Butonlar oluşturulur
        yoneticiButonu = new JButton("Yönetici Girişi");
        resepsiyonButonu = new JButton("Resepsiyon Girişi");
        musteriButonu = new JButton("Müşteri Girişi");

        // Butonların konumu ve boyutları belirlenir
        yoneticiButonu.setBounds(300, 150, 200, 40);
        resepsiyonButonu.setBounds(300, 250, 200, 40);
        musteriButonu.setBounds(300, 350, 200, 40);

        // Butonların arka plan renkleri ayarlanır
        yoneticiButonu.setBackground(ortalamaRenk);
        resepsiyonButonu.setBackground(ortalamaRenk);
        musteriButonu.setBackground(ortalamaRenk);

        yoneticiButonu.setForeground(Color.WHITE);
        resepsiyonButonu.setForeground(Color.WHITE);
        musteriButonu.setForeground(Color.WHITE);

        // Butonlar panele eklenir
        arkaPlanPaneli.add(yoneticiButonu);
        arkaPlanPaneli.add(resepsiyonButonu);
        arkaPlanPaneli.add(musteriButonu);

        // Butonlara tıklama olayları atanır
        yoneticiButonu.addActionListener(e -> {
            if (dogrulama("Yönetici")) {
                try {
                    new org.example.models.YoneticiEkrani(this);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        resepsiyonButonu.addActionListener(e -> {
            if (dogrulama("Resepsiyon")) {
                new org.example.models.ResepsiyonEkrani(this);
                dispose();
            }
        });

        musteriButonu.addActionListener(e -> {
            new MusteriEkrani(this);
            dispose();
        });

        add(arkaPlanPaneli);

        // Pencere boyutu değiştiğinde butonların yeniden hizalanması
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                butonPozisyonlariniAyarla();
            }
        });

        setVisible(true);
    }

    /**
     * Kullanıcı doğrulama işlemi
     * @param rol Giriş yapılacak rol (Yönetici/Resepsiyon)
     * @return Doğrulama sonucu (true/false)
     */
    private boolean dogrulama(String rol) {
        String beklenenKullaniciAdi = rol.equals("Yönetici") ? "admin" : "resepsiyon";
        String beklenenSifre = "1234";

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel kullaniciAdiLabeli = new JLabel("Kullanıcı Adı:");
        JTextField kullaniciAdiAlani = new JTextField();
        JLabel sifreLabeli = new JLabel("Şifre:");
        JPasswordField sifreAlani = new JPasswordField();

        panel.add(kullaniciAdiLabeli);
        panel.add(kullaniciAdiAlani);
        panel.add(sifreLabeli);
        panel.add(sifreAlani);

        int sonuc = JOptionPane.showConfirmDialog(null, panel,
                rol + " Girişi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (sonuc == JOptionPane.OK_OPTION) {
            String kullaniciAdi = kullaniciAdiAlani.getText().trim();
            String sifre = new String(sifreAlani.getPassword());

            if (kullaniciAdi.equals(beklenenKullaniciAdi) && sifre.equals(beklenenSifre)) {
                JOptionPane.showMessageDialog(null, rol + " giriş başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    /**
     * Butonların pencere boyutuna göre hizalanması
     */
    private void butonPozisyonlariniAyarla() {
        int genislik = getWidth();
        int yukseklik = getHeight();

        yoneticiButonu.setBounds((genislik - 200) / 2, yukseklik / 3, 200, 40);
        resepsiyonButonu.setBounds((genislik - 200) / 2, yukseklik / 3 + 70, 200, 40);
        musteriButonu.setBounds((genislik - 200) / 2, yukseklik / 3 + 140, 200, 40);
    }

    public static void main(String[] args) throws IOException {
        new AnaMenuGUI();
    }
}
