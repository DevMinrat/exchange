package com.devminrat.exchange.service;

import com.devminrat.exchange.model.Currency;

import java.util.List;


public interface CurrencyService {
    Currency getCurrency(String currencyCode);

    List<Currency> getAllCurrencies();
}
