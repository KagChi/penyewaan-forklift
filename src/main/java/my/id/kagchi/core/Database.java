package my.id.kagchi.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Connection connection;

    public static Connection getConnection() {
        String user = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/db_forklift?useSSL=false";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> executeStatement(String sql) throws SQLException {
        Connection conn = Database.getConnection();

        if (conn == null || !conn.isValid(2)) {
            throw new SQLException("Failed to establish a valid connection to the database.");
        }

        Statement stmt = conn.createStatement();

        try (ResultSet rs = stmt.executeQuery(sql)) {
            List<Map<String, Object>> result = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                result.add(row);
            }

            return result;
        } finally {
            stmt.close();
            conn.close();
        }
    }
}
