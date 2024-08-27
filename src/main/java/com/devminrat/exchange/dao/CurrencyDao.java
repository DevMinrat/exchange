package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.Currency;

import java.util.List;

public interface CurrencyDao {
    Currency getCurrency(String currencyCode);
    Currency setCurrency(Currency currency);
    List<Currency> getAllCurrencies();

    boolean currencyExists(String currencyCode);
}
