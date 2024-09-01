package com.devminrat.exchange.service;

import com.devminrat.exchange.model.ExchangeRate;

import java.util.List;


public interface ExchangeRateService {
    ExchangeRate getExchangeRate(String exchangeRateCode);

    ExchangeRate setExchangeRate(ExchangeRate exchangeRate);

    List<ExchangeRate> getAllExchangeRates();

    boolean exchangeRateExists(String baseCurrencyId, String targetCurrencyId);
}
