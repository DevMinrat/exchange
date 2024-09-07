package com.devminrat.exchange.service;

import com.devminrat.exchange.dao.CurrencyDao;
import com.devminrat.exchange.dao.CurrencyDaoImpl;
import com.devminrat.exchange.model.CurrencyDTO;

import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    CurrencyDao currencyDao = new CurrencyDaoImpl();

    @Override
    public CurrencyDTO getCurrency(String currencyCode) {
        return currencyDao.getCurrency(currencyCode);
    }

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        return currencyDao.getAllCurrencies();
    }

    @Override
    public CurrencyDTO setCurrency(CurrencyDTO currency) {
        if (currencyExists(currency.getCode())) {
            return null;
        }

        return currencyDao.setCurrency(currency);
    }

    @Override
    public boolean currencyExists(String currencyCode) {
        return getCurrency(currencyCode) != null;
    }
}
