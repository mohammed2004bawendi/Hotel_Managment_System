package org.example.models;

import javax.swing.*;
import java.awt.*;

public class OdaEkleEkrani extends JFrame {
    private JTextField odaNumarasiField, kapasiteField, fiyatField, durumField, manzaraField;
    private JButton ekleButton, geriButton, yenileButton;
    private JFrame oncekiEkran;

    public OdaEkleEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Oda Ekle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Üst panel, gezinme butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButton = new JButton("⬅ Geri");
        yenileButton = new JButton("🔄 Yenile");

        geriButton.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        yenileButton.addActionListener(e -> {
            new OdaEkleEkrani(oncekiEkran);
            dispose();
        });

        ustPanel.add(geriButton);
        ustPanel.add(yenileButton);
        add(ustPanel, BorderLayout.NORTH);

        // Ortadaki panel, giriş alanları
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel odaNumarasiLabel = new JLabel("Oda Numarası:");
        odaNumarasiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        odaNumarasiField = new JTextField(20);
        odaNumarasiField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel kapasiteLabel = new JLabel("Kapasite:");
        kapasiteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        kapasiteField = new JTextField(20);
        kapasiteField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel fiyatLabel = new JLabel("Fiyat:");
        fiyatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        fiyatField = new JTextField(20);
        fiyatField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel durumLabel = new JLabel("Durum:");
        durumLabel.setFont(new Font("Arial", Font.BOLD, 16));
        durumField = new JTextField(20);
        durumField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel manzaraLabel = new JLabel("Manzara:");
        manzaraLabel.setFont(new Font("Arial", Font.BOLD, 16));
        manzaraField = new JTextField(20);
        manzaraField.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        ortaPanel.add(odaNumarasiLabel, gbc);

        gbc.gridx = 1;
        ortaPanel.add(odaNumarasiField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        ortaPanel.add(kapasiteLabel, gbc);

        gbc.gridx = 1;
        ortaPanel.add(kapasiteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        ortaPanel.add(fiyatLabel, gbc);

        gbc.gridx = 1;
        ortaPanel.add(fiyatField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        ortaPanel.add(durumLabel, gbc);

        gbc.gridx = 1;
        ortaPanel.add(durumField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        ortaPanel.add(manzaraLabel, gbc);

        gbc.gridx = 1;
        ortaPanel.add(manzaraField, gbc);

        add(ortaPanel, BorderLayout.CENTER);

        // Alt panel, uyarı metni ve buton
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyariMetni = new JTextArea("Lütfen aşağıdaki bilgileri doğru girin:\n" +
                "- Oda numarası, kapasite, fiyat, durum ve manzara alanları boş olamaz.\n" +
                "- Kapasite ve fiyat sayısal olmalıdır.");
        uyariMetni.setEditable(false);
        uyariMetni.setFont(new Font("Arial", Font.PLAIN, 14));
        uyariMetni.setBackground(Color.WHITE);
        uyariMetni.setForeground(Color.DARK_GRAY);
        uyariMetni.setLineWrap(true);
        uyariMetni.setWrapStyleWord(true);
        uyariMetni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        altPanel.add(uyariMetni, BorderLayout.NORTH);

        ekleButton = new JButton("Ekle");
        ekleButton.setFont(new Font("Arial", Font.BOLD, 16));
        ekleButton.setPreferredSize(new Dimension(100, 40));
        ekleButton.addActionListener(e -> odaEkle());
        altPanel.add(ekleButton, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        // Pencere özelliklerini ayarlama
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    private void odaEkle() {
        try {
            // Oda numarası kontrolü
            if (odaNumarasiField.getText().isEmpty()) {
                throw new IllegalArgumentException("Oda Numarası boş olamaz!");
            }
            int odaNumarasi = Integer.parseInt(odaNumarasiField.getText());

            // Kapasite kontrolü
            if (kapasiteField.getText().isEmpty()) {
                throw new IllegalArgumentException("Kapasite boş olamaz!");
            }
            int kapasite = Integer.parseInt(kapasiteField.getText());
            if (kapasite <= 0) {
                throw new IllegalArgumentException("Kapasite 0'dan büyük olmalıdır!");
            }

            // Fiyat kontrolü
            if (fiyatField.getText().isEmpty()) {
                throw new IllegalArgumentException("Fiyat boş olamaz!");
            }
            double fiyat = Double.parseDouble(fiyatField.getText());
            if (fiyat <= 0) {
                throw new IllegalArgumentException("Fiyat 0'dan büyük olmalıdır!");
            }

            // Durum kontrolü
            String durum = durumField.getText();
            if (durum.isEmpty()) {
                throw new IllegalArgumentException("Durum boş olamaz!");
            }

            // Manzara kontrolü
            String manzara = manzaraField.getText();
            if (manzara.isEmpty()) {
                throw new IllegalArgumentException("Manzara boş olamaz!");
            }

            // Odanın var olup olmadığını kontrol etme
            if (OdaDAO.odaMevcutMu(odaNumarasi)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Bu oda zaten mevcut (Oda Numarası: " + odaNumarasi + ")",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Odayı veritabanına ekleme
            OdaDTO oda = new OdaDTO(odaNumarasi, kapasite, fiyat, durum, manzara);
            OdaDAO.odaEkle(oda);

            JOptionPane.showMessageDialog(
                    this,
                    "Oda başarıyla eklendi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Alanları temizleme
            alanlariTemizle();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lütfen geçerli sayısal değerler girin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Bir hata oluştu: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void alanlariTemizle() {
        odaNumarasiField.setText("");
        kapasiteField.setText("");
        fiyatField.setText("");
        durumField.setText("");
        manzaraField.setText("");
    }
}
