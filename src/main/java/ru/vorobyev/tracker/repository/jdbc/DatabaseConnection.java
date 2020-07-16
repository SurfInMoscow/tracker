package ru.vorobyev.tracker.repository.jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;

    private Connection connection;

    private DatabaseConnection() {
        init();
    }

    private void init() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("jdbc/db.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Class.forName(properties.getProperty("db.className"));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        } else if (databaseConnection.getConnection().isClosed()) {
            databaseConnection = new DatabaseConnection();
        }

        return  databaseConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
