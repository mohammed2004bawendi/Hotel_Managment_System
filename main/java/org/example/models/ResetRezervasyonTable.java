package org.example.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetRezervasyonTable {

    private static final String URL = "jdbc:sqlite:C:/Users/lenovo/Desktop/Proje/untitled/mydb.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("✅ تم الاتصال بقاعدة البيانات بنجاح!");

                // 1. حذف الجدول القديم إذا كان موجودًا
                String dropTable = "DROP TABLE IF EXISTS rezervasyon;";
                executeUpdate(conn, dropTable);
                System.out.println("✅ تم حذف الجدول 'rezervasyon' (إن وُجد).");

                // 2. إنشاء الجدول الجديد
                String createTable = "CREATE TABLE rezervasyon ("
                        + "rezervasyonNo TEXT PRIMARY KEY, "
                        + "isimSoyisim TEXT NOT NULL, "
                        + "tcKimlik TEXT NOT NULL, "
                        + "tarihBaslangici TEXT NOT NULL, "
                        + "tarihBitisi TEXT NOT NULL, "
                        + "odaBilgisi INTEGER, "
                        + "FOREIGN KEY (odaBilgisi) REFERENCES oda(odaNumarasi)"
                        + ");";

                executeUpdate(conn, createTable);
                System.out.println("✅ تم إنشاء الجدول 'rezervasyon' بنجاح!");

                // 3. إدخال بيانات اختبارية (اختياري)
                String insertTestData = "INSERT INTO rezervasyon (rezervasyonNo, isimSoyisim, tcKimlik, tarihBaslangici, tarihBitisi, odaBilgisi) "
                        + "VALUES ('REZ-001-1', 'Ahmet Yilmaz', '123456789', '2024-06-01', '2024-06-10', 1);";

                executeUpdate(conn, insertTestData);
                System.out.println("✅ تم إدخال بيانات اختبارية في الجدول 'rezervasyon'.");
            }
        } catch (SQLException e) {
            System.out.println("❌ حدث خطأ: " + e.getMessage());
        }
    }

    // دالة مساعدة لتنفيذ أوامر SQL
    private static void executeUpdate(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
