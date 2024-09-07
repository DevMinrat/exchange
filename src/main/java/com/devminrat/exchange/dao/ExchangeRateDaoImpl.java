package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.CurrencyDTO;
import com.devminrat.exchange.model.ExchangeRateDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.devminrat.exchange.util.DatabaseUtil.getConnection;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
    @Override
    public ExchangeRateDTO getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        String sql = "SELECT e.ID, e.BaseCurrencyId, e.TargetCurrencyId, e.Rate, " +
                "bc.ID AS BaseID, bc.Code AS BaseCode, bc.FullName AS BaseName, bc.Sign AS BaseSign, " +
                "tc.ID AS TargetID,  tc.Code AS TargetCode, tc.FullName AS TargetName, tc.Sign AS TargetSign " +
                "FROM ExchangeRates e " +
                "JOIN Currencies bc ON e.BaseCurrencyId = bc.ID " +
                "JOIN Currencies tc ON e.TargetCurrencyId = tc.ID " +
                "where bc.Code=? and tc.Code=?;";

        ExchangeRateDTO exchangeRate;

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, baseCurrencyCode);
                    ps.setString(2, targetCurrencyCode);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            exchangeRate = createExchangeRate(rs);
                        } else {
                            return null;
                        }
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRateDTO setExchangeRate(Integer baseCurrencyID, Integer targetCurrencyID, Double rate) {
        String sql = "INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, Rate) VALUES(?,?,?)";

        ExchangeRateDTO exchangeRate = new ExchangeRateDTO();

        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//                pstmt.execute("PRAGMA foreign_keys = ON;");

                pstmt.setInt(1, baseCurrencyID);
                pstmt.setInt(2, targetCurrencyID);
                pstmt.setDouble(3, rate);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            exchangeRate.setId(generatedKeys.getInt(1));
                        }
                    }
                }
                return exchangeRate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ExchangeRateDTO patchExchangeRate(Integer baseCurrencyID, Integer targetCurrencyID, Double rate) {
        String sql = "UPDATE ExchangeRates SET Rate=? WHERE BaseCurrencyId=? and TargetCurrencyId=? RETURNING ID";

        ExchangeRateDTO exchangeRate = new ExchangeRateDTO();

        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, rate);
                pstmt.setInt(2, baseCurrencyID);
                pstmt.setInt(3, targetCurrencyID);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        exchangeRate.setId(rs.getInt("ID"));
                        return exchangeRate;
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ExchangeRateDTO> getAllExchangeRates() {
        String sql = "SELECT e.ID, e.BaseCurrencyId, e.TargetCurrencyId, e.Rate, " +
                "bc.ID AS BaseID, bc.Code AS BaseCode, bc.FullName AS BaseName, bc.Sign AS BaseSign, " +
                "tc.ID AS TargetID,  tc.Code AS TargetCode, tc.FullName AS TargetName, tc.Sign AS TargetSign " +
                "FROM ExchangeRates e " +
                "JOIN Currencies bc ON e.BaseCurrencyId = bc.ID " +
                "JOIN Currencies tc ON e.TargetCurrencyId = tc.ID";

        List<ExchangeRateDTO> exchangeRates = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = getConnection()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    ExchangeRateDTO exchangeRate = createExchangeRate(rs);
                    exchangeRates.add(exchangeRate);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    private static ExchangeRateDTO createExchangeRate(ResultSet rs) throws SQLException {
        CurrencyDTO baseCurrency = new CurrencyDTO(rs.getInt("BaseID"), rs.getString("BaseCode"),
                rs.getString("BaseName"), rs.getString("BaseSign"));
        CurrencyDTO targetCurrency = new CurrencyDTO(rs.getInt("TargetID"), rs.getString("TargetCode"),
                rs.getString("TargetName"), rs.getString("TargetSign"));

        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(rs.getInt("id"), baseCurrency, targetCurrency, rs.getDouble("Rate"));
        return exchangeRate;
    }
}
