package com.devminrat.exchange.service;

import com.devminrat.exchange.model.Currency;

import java.util.List;

public interface CurrencyService {
    Currency getCurrency(String currencyCode);

    Currency setCurrency(Currency currency);

    List<Currency> getAllCurrencies();

    boolean currencyExists(String currencyCode);
}
