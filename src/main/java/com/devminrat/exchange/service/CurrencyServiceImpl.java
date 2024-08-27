package com.devminrat.exchange.service;

import com.devminrat.exchange.dao.CurrencyDao;
import com.devminrat.exchange.dao.CurrencyDaoImpl;
import com.devminrat.exchange.model.Currency;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    CurrencyDao currencyDao = new CurrencyDaoImpl();

    @Override
    public Currency getCurrency(String currencyCode) {
        return currencyDao.getCurrency(currencyCode);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyDao.getAllCurrencies();
    }

    @Override
    public Currency setCurrency(Currency currency) {
        if (currencyDao.currencyExists(currency.getCode())) {
            return null;
        }

        return currencyDao.setCurrency(currency);
    }
}
