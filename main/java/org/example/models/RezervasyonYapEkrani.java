package org.example.models;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RezervasyonYapEkrani extends JFrame {
    private JTextField isimField, tcField, tarihBaslangiciField, tarihBitisiField, odaField;
    private JButton rezervasyonButton, geriButton, yenileButton;
    private JFrame oncekiEkran;

    public RezervasyonYapEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Rezervasyon Yap");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Üst paneldeki gezinme butonları
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButton = new JButton("⬅ Geri");
        yenileButton = new JButton("🔄 Yenile");

        geriButton.addActionListener(e -> {
            oncekiEkran.setVisible(true);
            dispose();
        });

        yenileButton.addActionListener(e -> {
            new RezervasyonYapEkrani(oncekiEkran);
            dispose();
        });

        ustPanel.add(geriButton);
        ustPanel.add(yenileButton);
        add(ustPanel, BorderLayout.NORTH);

        // Ortada kullanıcı girişi için panel
        JPanel ortadakiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel isimLabel = new JLabel("İsim Soyisim:");
        isimField = new JTextField(20);

        JLabel tcLabel = new JLabel("TC Kimlik:");
        tcField = new JTextField(20);

        JLabel tarihBaslangiciLabel = new JLabel("Tarih Başlangıcı (yyyy-MM-dd):");
        tarihBaslangiciField = new JTextField(20);

        JLabel tarihBitisiLabel = new JLabel("Tarih Bitişi (yyyy-MM-dd):");
        tarihBitisiField = new JTextField(20);

        JLabel odaLabel = new JLabel("Oda No:");
        odaField = new JTextField(20);

        addInput(ortadakiPanel, gbc, isimLabel, isimField, 0);
        addInput(ortadakiPanel, gbc, tcLabel, tcField, 1);
        addInput(ortadakiPanel, gbc, tarihBaslangiciLabel, tarihBaslangiciField, 2);
        addInput(ortadakiPanel, gbc, tarihBitisiLabel, tarihBitisiField, 3);
        addInput(ortadakiPanel, gbc, odaLabel, odaField, 4);

        add(ortadakiPanel, BorderLayout.CENTER);

        // Alt paneldeki uyarılar ve butonlar
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyarilarTextArea = new JTextArea(
                "Lütfen aşağıdaki talimatlara uygun hareket edin:\n" +
                        "- İsim sadece harflerden oluşmalı.\n" +
                        "- Tarih yyyy-MM-dd formatında olmalı (örnek: 2024-06-01).\n" +
                        "- Oda numarası mevcut ve uygun olmalıdır.");
        uyarilarTextArea.setEditable(false);
        uyarilarTextArea.setLineWrap(true);
        uyarilarTextArea.setWrapStyleWord(true);
        altPanel.add(uyarilarTextArea, BorderLayout.NORTH);

        rezervasyonButton = new JButton("Rezervasyon Yap");
        rezervasyonButton.setFont(new Font("Arial", Font.BOLD, 16));
        rezervasyonButton.addActionListener(e -> rezervasyonEkle());
        altPanel.add(rezervasyonButton, BorderLayout.SOUTH);

        add(altPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);
    }

    private void addInput(JPanel panel, GridBagConstraints gbc, JLabel label, JTextField field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void rezervasyonEkle() {
        String isim = isimField.getText().trim();
        String tc = tcField.getText().trim();
        String tarihBaslangiciText = tarihBaslangiciField.getText().trim();
        String tarihBitisiText = tarihBitisiField.getText().trim();
        String odaText = odaField.getText().trim();

        try {
            if (isim.isEmpty() || tc.isEmpty() || tarihBaslangiciText.isEmpty() || tarihBitisiText.isEmpty() || odaText.isEmpty()) {
                throw new IllegalArgumentException("Tüm alanları doldurmanız gerekiyor!");
            }

            if (!isim.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ ]+")) {
                throw new IllegalArgumentException("İsim sadece harflerden oluşmalı!");
            }

            // TC Kimlik doğrulaması
            Rezervasyon tempRezervasyon = new Rezervasyon();
            tempRezervasyon.setKimlikNo(tc);
            tempRezervasyon.validateTcKimlik(); // TC Kimlik doğrulama

            int odaNo = Integer.parseInt(odaText);
            if (!OdaDAO.odaMevcutMu(odaNo)) {
                throw new IllegalArgumentException("Bu oda mevcut değil!");
            }

            if (OdaDAO.odaRezerveEdildiMi(odaNo)) {
                throw new IllegalArgumentException("Oda zaten dolu!");
            }

            LocalDate tarihBaslangici = parseDate(tarihBaslangiciText);
            LocalDate tarihBitisi = parseDate(tarihBitisiText);

            if (!tarihBaslangici.isBefore(tarihBitisi)) {
                throw new IllegalArgumentException("Tarih Başlangıcı, Tarih Bitişinden önce olmalıdır!");
            }

            Rezervasyon rezervasyon = new Rezervasyon(isim, tc, tarihBaslangici, tarihBitisi, odaNo);
            String rezervasyonNo = rezervasyon.benzersizKimlikOlustur(); // Rezervasyon numarasını oluştur
            rezervasyon.setRezervasyonNo(rezervasyonNo);

            RezervasyonDAO.rezervasyonEkle(rezervasyon); // Rezervasyon veritabanına ekle

            // Odanın durumunu "Dolu" olarak güncelle
            OdaDAO.odaDurumunuGuncelle(odaNo, "Dolu");

            JOptionPane.showMessageDialog(this, "Rezervasyon başarıyla yapıldı! Rezervasyon No: " + rezervasyonNo);

            // Alanları temizle
            isimField.setText("");
            tcField.setText("");
            tarihBaslangiciField.setText("");
            tarihBitisiField.setText("");
            odaField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Oda numarası geçerli bir sayı olmalı!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Tarih yyyy-MM-dd formatında olmalı!", "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | GecersizTcKimlikException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parseDate(String tarihText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(tarihText, formatter);
    }
}
