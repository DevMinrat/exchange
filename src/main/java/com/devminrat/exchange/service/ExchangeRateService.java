package com.devminrat.exchange.service;

import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.model.ExchangeRateDTO;

import java.util.List;


public interface ExchangeRateService {
    ExchangeRateDTO getExchangeRate(String exchangeRateCode);

    ExchangeRateDTO setExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException;

    ExchangeRateDTO patchExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException;

    List<ExchangeRateDTO> getAllExchangeRates();

    boolean exchangeRateExists(String baseCurrencyId, String targetCurrencyId);
}
