package com.devminrat.exchange.dao;

import com.devminrat.exchange.model.CurrencyDTO;

import java.util.List;

public interface CurrencyDao {
    CurrencyDTO getCurrency(String currencyCode);
    CurrencyDTO setCurrency(CurrencyDTO currency);
    List<CurrencyDTO> getAllCurrencies();
}
