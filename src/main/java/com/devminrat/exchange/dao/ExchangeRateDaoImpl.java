package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.devminrat.exchange.util.DatabaseUtil.getConnection;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
    @Override
    public ExchangeRate getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        String sql = "SELECT e.ID, e.BaseCurrencyId, e.TargetCurrencyId, e.Rate, " +
                "bc.ID AS BaseID, bc.Code AS BaseCode, bc.FullName AS BaseName, bc.Sign AS BaseSign, " +
                "tc.ID AS TargetID,  tc.Code AS TargetCode, tc.FullName AS TargetName, tc.Sign AS TargetSign " +
                "FROM ExchangeRates e " +
                "JOIN Currencies bc ON e.BaseCurrencyId = bc.ID " +
                "JOIN Currencies tc ON e.TargetCurrencyId = tc.ID " +
                "where bc.Code=? and tc.Code=?;";

        ExchangeRate exchangeRate = new ExchangeRate();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, baseCurrencyCode);
                    ps.setString(2, targetCurrencyCode);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            exchangeRate = getExchangeRate(rs);
                        }
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRate setExchangeRate(ExchangeRate exchangeRate) {
//        stmt.execute("PRAGMA foreign_keys = ON;");
        return null;
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        String sql = "SELECT e.ID, e.BaseCurrencyId, e.TargetCurrencyId, e.Rate, " +
                "bc.ID AS BaseID, bc.Code AS BaseCode, bc.FullName AS BaseName, bc.Sign AS BaseSign, " +
                "tc.ID AS TargetID,  tc.Code AS TargetCode, tc.FullName AS TargetName, tc.Sign AS TargetSign " +
                "FROM ExchangeRates e " +
                "JOIN Currencies bc ON e.BaseCurrencyId = bc.ID " +
                "JOIN Currencies tc ON e.TargetCurrencyId = tc.ID";

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    ExchangeRate exchangeRate = getExchangeRate(rs);
                    exchangeRates.add(exchangeRate);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    private static ExchangeRate getExchangeRate(ResultSet rs) throws SQLException {
        Currency baseCurrency = new Currency(rs.getInt("BaseID"), rs.getString("BaseCode"),
                rs.getString("BaseName"), rs.getString("BaseSign"));
        Currency targetCurrency = new Currency(rs.getInt("TargetID"), rs.getString("TargetCode"),
                rs.getString("TargetName"), rs.getString("TargetSign"));

        ExchangeRate exchangeRate = new ExchangeRate(rs.getInt("id"), baseCurrency, targetCurrency, rs.getDouble("Rate"));
        return exchangeRate;
    }
}
