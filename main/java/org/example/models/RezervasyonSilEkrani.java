package org.example.models;

import javax.swing.*;
import java.awt.*;

public class RezervasyonSilEkrani extends JFrame {
    private JTextField txtRezervasyonNo;
    private JLabel bilgiLabel;

    private JButton silButton, geriButton, yenileButton;
    private JFrame oncekiEkran;

    public RezervasyonSilEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Rezervasyon Sil");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Ana düzen, padding ile

        // Üst panelde gezinme butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButton = new JButton("⬅ Geri");
        yenileButton = new JButton("🔄 Yenile");

        // Geri butonuna tıklanınca önceki ekranı göster ve mevcut ekranı kapat
        geriButton.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        // Yenile butonuna tıklanınca mevcut ekranı yenile
        yenileButton.addActionListener(e -> {
            new RezervasyonSilEkrani(oncekiEkran);
            dispose();
        });

        ustPanel.add(geriButton);
        ustPanel.add(yenileButton);
        add(ustPanel, BorderLayout.NORTH);

        // Orta panelde giriş alanları
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblRezervasyonNo = new JLabel("Rezervasyon No:");
        lblRezervasyonNo.setFont(new Font("Arial", Font.BOLD, 16));
        txtRezervasyonNo = new JTextField(20);
        txtRezervasyonNo.setFont(new Font("Arial", Font.PLAIN, 16));

        // Orta panele bileşenleri ekle
        gbc.gridx = 0;
        gbc.gridy = 0;
        ortaPanel.add(lblRezervasyonNo, gbc);

        gbc.gridx = 1;
        ortaPanel.add(txtRezervasyonNo, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panelde uyarı metni ve "Sil" butonu
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea uyarıMetinArea = new JTextArea("Lütfen aşağıdaki talimatlara uygun hareket edin:\n" +
                "- Rezervasyon numarasını doğru bir şekilde giriniz.\n" +
                "- Silme işlemi geri alınamaz.");
        uyarıMetinArea.setEditable(false);
        uyarıMetinArea.setFont(new Font("Arial", Font.PLAIN, 14));
        uyarıMetinArea.setBackground(Color.WHITE);
        uyarıMetinArea.setForeground(Color.DARK_GRAY);
        uyarıMetinArea.setLineWrap(true);
        uyarıMetinArea.setWrapStyleWord(true);
        uyarıMetinArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(uyarıMetinArea, BorderLayout.NORTH);

        // Bilgi label'ı
        bilgiLabel = new JLabel(""); // Durum mesajları için boş label

        silButton = new JButton("Sil");
        silButton.setFont(new Font("Arial", Font.BOLD, 16));
        silButton.setPreferredSize(new Dimension(100, 40));
        silButton.addActionListener(e -> rezervasyonuSil());
        altPanel.add(silButton, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Çerçeve özelliklerini ayarla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    // Rezervasyonu silme işlemi
    private void rezervasyonuSil() {
        String rezervasyonNoStr = txtRezervasyonNo.getText().trim();

        try {
            if (rezervasyonNoStr.isEmpty()) {
                throw new IllegalArgumentException("Rezervasyon numarası boş bırakılamaz!");
            }

            String rezervasyonNo = txtRezervasyonNo.getText().trim();

            // Rezervasyon var mı kontrol et
            Rezervasyon rezervasyon = RezervasyonDAO.rezervasyonGetirByNo(String.valueOf(rezervasyonNo));
            if (rezervasyon == null) {
                JOptionPane.showMessageDialog(this, "Rezervasyon bulunamadı! Lütfen doğru bir rezervasyon numarası giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Rezervasyonu sil
            RezervasyonDAO.rezervasyonSil(String.valueOf(rezervasyonNo));

            // Oda durumunu "Boş" olarak güncelle
            OdaDAO.odaDurumunuGuncelle(rezervasyon.getOdaBilgisi(), "Boş");

            JOptionPane.showMessageDialog(this, "Rezervasyon başarıyla silindi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

            // Başarılı silme işlemi sonrası giriş alanını temizle
            txtRezervasyonNo.setText("");
            bilgiLabel.setText("Rezervasyon başarıyla silindi.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir rezervasyon numarası giriniz! (Örneğin: 123)", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
