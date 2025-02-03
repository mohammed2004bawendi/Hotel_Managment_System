package org.example.models;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OdaListeleEkrani extends JFrame {
    private JTextArea txtAreaOdalar;
    private JButton geriButonu, yenileButonu;
    private JFrame oncekiEkran;

    // Yapıcı metod: OdaListeleEkrani nesnesini başlatır
    public OdaListeleEkrani(JFrame oncekiEkran) {
        this.oncekiEkran = oncekiEkran;

        setTitle("Odaları Listele");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Ana düzen, padding ile

        // Üst paneli oluştur
        ustPaneliOlustur();

        // Orta paneli oluştur (odaların listeleneceği yer)
        ortaPaneliOlustur();

        // Alt paneli oluştur (uyarı mesajı)
        altPaneliOlustur();

        // Çerçeve boyutları ve görünürlük ayarları
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(400, 300));
        setVisible(true);

        // Ekran açıldığında odaları otomatik listele
        odalariListele();
    }

    // Üst paneldeki butonları oluşturur
    private void ustPaneliOlustur() {
        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geriButonu = new JButton("⬅ Geri");
        yenileButonu = new JButton("🔄 Yenile");

        // Geri butonuna tıklama işlemi
        geriButonu.addActionListener(e -> {
            oncekiEkran.setVisible(true); // Önceki ekranı görünür yap
            dispose(); // Bu ekranı kapat
        });

        // Yenile butonuna tıklama işlemi
        yenileButonu.addActionListener(e -> {
            new OdaListeleEkrani(oncekiEkran); // Yeniden OdaListeleEkrani oluştur
            dispose(); // Bu ekranı kapat
        });

        ustPanel.add(geriButonu);
        ustPanel.add(yenileButonu);
        add(ustPanel, BorderLayout.NORTH);
    }

    // Orta panelde odaların listeleneceği alanı oluşturur
    private void ortaPaneliOlustur() {
        txtAreaOdalar = new JTextArea();
        txtAreaOdalar.setEditable(false); // Kullanıcı düzenlemesini engelle
        txtAreaOdalar.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtAreaOdalar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Dikey kaydırma çubuğunu her zaman göster
        add(scrollPane, BorderLayout.CENTER);
    }

    // Alt paneldeki uyarı mesajlarını oluşturur
    private void altPaneliOlustur() {
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        JTextArea uyariTextArea = new JTextArea("Bu ekranda odalar otomatik olarak listelenir.\n" +
                "- 'Yenile' butonuyla listeyi güncelleyebilirsiniz.\n" +
                "- Eğer kayıtlı oda bulunmazsa, ilgili mesaj görüntülenir.");
        uyariTextArea.setEditable(false); // Kullanıcı düzenlemesini engelle
        uyariTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        uyariTextArea.setBackground(Color.WHITE);
        uyariTextArea.setForeground(Color.DARK_GRAY);
        uyariTextArea.setLineWrap(true); // Satır sonu geldiğinde kelimeyi sarmaya başla
        uyariTextArea.setWrapStyleWord(true); // Kelime tam olarak sarılacak şekilde
        uyariTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // İç boşlukları ayarla
        altPanel.add(uyariTextArea, BorderLayout.CENTER);

        add(altPanel, BorderLayout.SOUTH);
    }

    // Odaları listeleyen metod
    private void odalariListele() {
        txtAreaOdalar.setText(""); // Önceki veriyi temizle
        ArrayList<OdaDTO> odalar = OdaDAO.tumOdalarListesiniGetir(); // Oda bilgilerini al

        if (odalar.isEmpty()) {
            txtAreaOdalar.setText("Kayıtlı oda bulunamadı."); // Eğer oda yoksa mesaj ver
        } else {
            for (OdaDTO oda : odalar) {
                txtAreaOdalar.append(
                        "Oda No: " + oda.getOdaNumarasi() +
                                ", Kapasite: " + oda.getKapasite() +
                                ", Fiyat: " + oda.getFiyat() +
                                ", Durum: " + oda.getDurum() +
                                ", Manzara: " + oda.getManzara() + "\n"
                );
            }
        }
    }
}
