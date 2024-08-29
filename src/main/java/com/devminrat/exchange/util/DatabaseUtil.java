package com.devminrat.exchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseUtil {
    //TODO: before deploy - refactoring to avoid strict path
    private static final String URL = "jdbc:sqlite:E:\\JAVA\\practice\\_prj\\exchange\\exchange.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
