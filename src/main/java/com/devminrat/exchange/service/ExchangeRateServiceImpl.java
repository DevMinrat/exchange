package com.devminrat.exchange.service;

import com.devminrat.exchange.dao.ExchangeRateDao;
import com.devminrat.exchange.dao.ExchangeRateDaoImpl;
import com.devminrat.exchange.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();

    @Override
    public ExchangeRate getExchangeRate(String exchangeRateCode) {
        final int requiredLength = 6;

        if (exchangeRateCode != null && exchangeRateCode.length() == requiredLength) {
            String baseCode = exchangeRateCode.substring(0, 3);
            String targetCode = exchangeRateCode.substring(3);
            System.out.println(baseCode + " " + targetCode);
            return exchangeRateDao.getExchangeRate(baseCode, targetCode);
        } else {
            return null;
        }
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAllExchangeRates();
    }

    @Override
    public ExchangeRate setExchangeRate(ExchangeRate exchangeRate) {
//        if (exchangeRateExists(exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId())) {
//            return null;
//        }
//
//        return exchangeRateDao.setExchangeRate(exchangeRate);
        return null;
    }

    @Override
    public boolean exchangeRateExists(String baseCurrencyId, String targetCurrencyId) {
        return getExchangeRate(baseCurrencyId + targetCurrencyId) != null;
    }
}
