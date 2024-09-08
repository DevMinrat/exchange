package com.devminrat.exchange.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseUtil {

    private static DataSource ds;

    static {
        try {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/exchangeDB");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
