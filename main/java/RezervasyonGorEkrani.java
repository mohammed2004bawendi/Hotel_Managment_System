import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class RezervasyonGorEkrani extends JFrame {
    private JFrame oncekiEkran;

    public RezervasyonGorEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Rezervasyonları Gör");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

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
            new RezervasyonGorEkrani(oncekiEkran); // Bu ekranı yeniden başlat
            dispose(); // Mevcut ekranı kapat
        });

        // Menü çubuğuna butonları ekle
        menuCubugu.add(geriButonu);
        menuCubugu.add(yenileButonu);

        // Menü çubuğunu pencereye ekle
        setJMenuBar(menuCubugu);

        // Bileşenler
        JLabel tcKimlikLabel = new JLabel("TC Kimlik Numarası:");
        JTextField tcKimlikAlan = new JTextField();
        JButton gorButonu = new JButton("Rezervasyonu Gör");

        JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 80, 350, 250);
        textArea.setEditable(false);

        tcKimlikLabel.setBounds(20, 20, 150, 30);
        tcKimlikAlan.setBounds(170, 20, 150, 30);
        gorButonu.setBounds(100, 50, 200, 30);

        add(tcKimlikLabel);
        add(tcKimlikAlan);
        add(gorButonu);
        add(textArea);

        // Rezervasyonu Gör butonuna tıklandığında
        gorButonu.addActionListener(e -> {
            String tcKimlik = tcKimlikAlan.getText();  // Kullanıcıdan TC Kimlik Numarasını alıyoruz

            // Veritabanına bağlanarak bu TC Kimlik Numarası'na ait rezervasyonları çekiyoruz
            try (Connection baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/otel_db", "root", "password")) {
                String sorgu = "SELECT * FROM rezervasyon WHERE tcKimlik = ?";
                PreparedStatement sorguHazirla = baglanti.prepareStatement(sorgu);
                sorguHazirla.setString(1, tcKimlik);  // TC Kimlik Numarası ile sorgulama

                ResultSet sonuc = sorguHazirla.executeQuery();

                // Eğer veritabanından rezervasyon varsa, ekrana yazdırıyoruz
                if (sonuc.next()) {
                    textArea.setText(""); // Önceki verileri temizle
                    do {
                        textArea.append("Rezervasyon No: " + sonuc.getDouble("rezervasyonNo") + "\n");
                        textArea.append("Oda No: " + sonuc.getInt("odaBilgisi") + ", Tarih: " + sonuc.getString("kalinacakTarihler") + "\n\n");
                    } while (sonuc.next());
                } else {
                    textArea.setText("Bu TC Kimlik Numarası ile yapılmış bir rezervasyon bulunamadı.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage());
            }
        });

        setVisible(true); // Ekranı görünür hale getir
    }
}
