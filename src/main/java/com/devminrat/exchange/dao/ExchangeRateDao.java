package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.model.ExchangeRate;

import java.util.List;

public interface ExchangeRateDao {
    ExchangeRate getExchangeRate(String baseCurrency, String targetCurrency);

    ExchangeRate setExchangeRate(ExchangeRate exchangeRate);

    List<ExchangeRate> getAllExchangeRates();
}
