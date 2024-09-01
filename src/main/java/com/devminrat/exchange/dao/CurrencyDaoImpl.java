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
        Currency currency;

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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
                ResultSet rs = stmt.executeQuery("select * from Currencies");

                while (rs.next()) {
                    Currency currency = new Currency(rs.getInt("ID"), rs.getString("FullName"),
                            rs.getString("Code"), rs.getString("Sign"));
                    currencies.add(currency);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return currencies;
    }

    @Override
    public Currency setCurrency(Currency currency) {
        try {
            Class.forName("org.sqlite.JDBC");
            String sql = "insert into Currencies(FullName,Code,Sign) values (?,?,?)";

            try (Connection conn = getConnection()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    pstmt.setString(1, currency.getName());
                    pstmt.setString(2, currency.getCode());
                    pstmt.setString(3, currency.getSign());

                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys();) {
                            if (generatedKeys.next()) {
                                currency.setId(generatedKeys.getInt(1));
                            }
                        }
                    }
                    return currency;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
