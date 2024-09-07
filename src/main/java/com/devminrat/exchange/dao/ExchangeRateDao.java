package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.ExchangeRateDTO;

import java.util.List;

public interface ExchangeRateDao {
    ExchangeRateDTO getExchangeRate(String baseCurrency, String targetCurrency);

    ExchangeRateDTO setExchangeRate(Integer baseCurrencyID, Integer targetCurrencyID, Double rate);

    ExchangeRateDTO patchExchangeRate(Integer baseCurrencyID, Integer targetCurrencyID, Double rate);

    List<ExchangeRateDTO> getAllExchangeRates();
}
