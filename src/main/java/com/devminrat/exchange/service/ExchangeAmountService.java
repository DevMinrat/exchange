package com.devminrat.exchange.service;

import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.model.ExchangeAmountDTO;
import com.devminrat.exchange.model.ExchangeRateDTO;

import java.util.List;


public interface ExchangeAmountService {
    ExchangeAmountDTO getExchangeAmount(String baseExchangeCode, String targetExchangeCode, Double amount);

//    List<ExchangeRateDTO> getAllExchangeRates();

//    boolean exchangeRateExists(String baseCurrencyId, String targetCurrencyId);
}
