package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.devminrat.exchange.util.DatabaseUtil.getConnection;

public class CurrencyDaoImpl implements CurrencyDao {

    @Override
    public Currency getCurrency(String currencyCode) {
        String sql = "select * from Currencies where code=?";
        Currency currency = new Currency();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, currencyCode);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        currency = new Currency(rs.getInt("ID"), rs.getString("FullName"),
                                rs.getString("Code"), rs.getString("Sign"));
                    } else {
                        return null;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currency;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Currencies");

                while (rs.next()) {
                    Currency currency = new Currency(rs.getInt("ID"), rs.getString("FullName"),
                            rs.getString("Code"), rs.getString("Sign"));
                    currencies.add(currency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencies;
    }
}
