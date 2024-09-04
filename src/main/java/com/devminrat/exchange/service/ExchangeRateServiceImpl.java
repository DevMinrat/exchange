package com.devminrat.exchange.service;

import com.devminrat.exchange.dao.ExchangeRateDao;
import com.devminrat.exchange.dao.ExchangeRateDaoImpl;
import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.model.ExchangeRate;

import java.util.List;
import java.util.Map;

import static com.devminrat.exchange.constants.ErrorMessage.CURRENCY_NOT_FOUND;
import static com.devminrat.exchange.constants.ErrorMessage.CURRENCY_PAIR_EXIST;
import static com.devminrat.exchange.util.SplitCurrencyCodeUtil.splitCode;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();

    @Override
    public ExchangeRate getExchangeRate(String exchangeRateCode) {
        Map<String, String> currencyCodes = splitCode(exchangeRateCode);

        if (currencyCodes != null) {
            return exchangeRateDao.getExchangeRate(currencyCodes.get("baseCode"), currencyCodes.get("targetCode"));
        } else {
            return null;
        }
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDao.getAllExchangeRates();
    }

    @Override
    public ExchangeRate setExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException {
        ExchangeRate exchangeRate;
        Currency baseCurrency = currencyService.getCurrency(baseCurrencyCode);
        Currency targetCurrency = currencyService.getCurrency(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new CurrencyNotFoundException(CURRENCY_NOT_FOUND.getMessage());
        }

        if (exchangeRateExists(baseCurrencyCode, targetCurrencyCode)) {
            throw new ExchangeRateAlreadyExistsException(CURRENCY_PAIR_EXIST.getMessage());
        }

        exchangeRate = exchangeRateDao.setExchangeRate(baseCurrency.getId(), targetCurrency.getId(), rate);
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(rate);
        return exchangeRate;
    }

    @Override
    public ExchangeRate patchExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws CurrencyNotFoundException, ExchangeRateAlreadyExistsException {
        ExchangeRate exchangeRate;
        Currency baseCurrency = currencyService.getCurrency(baseCurrencyCode);
        Currency targetCurrency = currencyService.getCurrency(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            throw new CurrencyNotFoundException(CURRENCY_NOT_FOUND.getMessage());
        }

        exchangeRate = exchangeRateDao.patchExchangeRate(baseCurrency.getId(), targetCurrency.getId(), rate);
        if (exchangeRate != null) {
            exchangeRate.setBaseCurrency(baseCurrency);
            exchangeRate.setTargetCurrency(targetCurrency);
            exchangeRate.setRate(rate);
            return exchangeRate;
        } else {
            return null;
        }
    }

    @Override
    public boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
        return getExchangeRate(baseCurrencyCode + targetCurrencyCode) != null;
    }
}
