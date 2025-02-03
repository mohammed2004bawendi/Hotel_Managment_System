package org.example.models;

import javax.swing.*;
import java.awt.*;

public class OdaSilEkrani extends JFrame {
    private JTextField txtOdaNumarasi;
    private JButton btnSil, geriButton, yenileButton;
    private JFrame oncekiEkran;

    public OdaSilEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        // Ekran başlığını ve kapanma davranışını ayarlıyoruz
        setTitle("Oda Sil");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Ana layout, padding ile birlikte

        // Üst panel, geri ve yenile butonları için
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButton = new JButton("⬅ Geri");
        yenileButton = new JButton("🔄 Yenile");

        // Geri butonuna tıklanırsa önceki ekran görünür, şu anki ekran kapanır
        geriButton.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        // Yenile butonuna tıklanırsa ekran yenilenir
        yenileButton.addActionListener(e -> {
            new OdaSilEkrani(oncekiEkran);
            dispose();
        });

        ustPanel.add(geriButton);
        ustPanel.add(yenileButton);
        add(ustPanel, BorderLayout.NORTH);

        // Ortada oda numarası girişi için input alanı
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding ayarı
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblOdaNumarasi = new JLabel("Oda Numarası:");
        lblOdaNumarasi.setFont(new Font("Arial", Font.BOLD, 16));  // Font ayarı
        txtOdaNumarasi = new JTextField(20);
        txtOdaNumarasi.setFont(new Font("Arial", Font.PLAIN, 16)); // Font ayarı

        // Ortaya öğeleri ekliyoruz
        gbc.gridx = 0;
        gbc.gridy = 0;
        ortaPanel.add(lblOdaNumarasi, gbc);

        gbc.gridx = 1;
        ortaPanel.add(txtOdaNumarasi, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel, uyarı metni ve silme butonu için
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyarıTextArea = new JTextArea("Lütfen geçerli bir oda numarası girin ve doğru işlemi gerçekleştirin.");
        uyarıTextArea.setEditable(false);  // Metin düzenlemesi engelleniyor
        uyarıTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        uyarıTextArea.setBackground(Color.WHITE);
        uyarıTextArea.setForeground(Color.DARK_GRAY);
        uyarıTextArea.setLineWrap(true);  // Satır kaydırma etkin
        uyarıTextArea.setWrapStyleWord(true);  // Kelime bazında satır kaydırma
        uyarıTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Kenar boşluğu
        altPanel.add(uyarıTextArea, BorderLayout.NORTH);

        // Silme butonuna tıklanırsa handleDeleteRoom() fonksiyonu çağrılır
        btnSil = new JButton("Sil");
        btnSil.setFont(new Font("Arial", Font.BOLD, 16));
        btnSil.setPreferredSize(new Dimension(100, 40));
        btnSil.addActionListener(e -> handleDeleteRoom());
        altPanel.add(btnSil, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere boyutlarını ayarlıyoruz
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    // Oda silme işlemi yapılırken kontrol ve hata yönetimi
    private void handleDeleteRoom() {
        try {
            // Oda numarası boş olamaz
            if (txtOdaNumarasi.getText().isEmpty()) {
                throw new IllegalArgumentException("Oda numarası alanı boş bırakılamaz!");
            }

            int odaNumarasi = Integer.parseInt(txtOdaNumarasi.getText());

            // Oda var mı kontrolü
            if (!OdaDAO.odaMevcutMu(odaNumarasi)) {
                JOptionPane.showMessageDialog(this,
                        "Hata: Oda bulunamadı (Oda Numarası: " + odaNumarasi + ")",
                        "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Oda silme işlemi simülasyonu
            OdaDAO.odaSil(odaNumarasi);
            JOptionPane.showMessageDialog(this,
                    "Oda başarıyla silindi (Oda Numarası: " + odaNumarasi + ")",
                    "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            // Silme işleminden sonra input alanını temizle
            txtOdaNumarasi.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen geçerli bir oda numarası girin!",
                    "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Hata: " + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Hata: " + ex.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

}
