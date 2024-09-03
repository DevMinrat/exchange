package com.devminrat.exchange.service;

import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.model.ExchangeRate;

import java.util.List;


public interface ExchangeRateService {
    ExchangeRate getExchangeRate(String exchangeRateCode);

    ExchangeRate setExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException;

    ExchangeRate patchExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException;

    List<ExchangeRate> getAllExchangeRates();

    boolean exchangeRateExists(String baseCurrencyId, String targetCurrencyId);
}
