package com.devminrat.exchange.service;

import com.devminrat.exchange.model.CurrencyDTO;

import java.util.List;

public interface CurrencyService {
    CurrencyDTO getCurrency(String currencyCode);

    CurrencyDTO setCurrency(CurrencyDTO currency);

    List<CurrencyDTO> getAllCurrencies();

    boolean currencyExists(String currencyCode);
}
