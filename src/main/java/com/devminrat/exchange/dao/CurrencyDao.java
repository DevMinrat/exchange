package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.Currency;

import java.util.List;

public interface CurrencyDao {
    Currency getCurrency(String currencyCode);
    List<Currency> getAllCurrencies();
}
