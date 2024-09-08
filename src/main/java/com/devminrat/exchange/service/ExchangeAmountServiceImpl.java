package com.devminrat.exchange.service;

import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.model.ExchangeAmountDTO;
import com.devminrat.exchange.model.ExchangeRateDTO;

import static com.devminrat.exchange.constants.ErrorMessage.CURRENCY_NOT_FOUND;
import static com.devminrat.exchange.util.CommonUtils.roundDecimal;

public class ExchangeAmountServiceImpl implements ExchangeAmountService {
    private final ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();

    @Override
    public ExchangeAmountDTO getExchangeAmount(String baseCurrencyCode, String targetCurrencyCode, Double amount) throws CurrencyNotFoundException {
        ExchangeAmountDTO directRate = getAmountAtDirectRate(baseCurrencyCode, targetCurrencyCode, amount);
        if (directRate != null) return directRate;

        ExchangeAmountDTO exchangeRate = getAmountAtReverseRate(baseCurrencyCode, targetCurrencyCode, amount);
        if (exchangeRate != null) return exchangeRate;

        ExchangeAmountDTO baseExchangeRate = getAmountAtCrossRate(baseCurrencyCode, targetCurrencyCode, amount);
        if (baseExchangeRate != null) return baseExchangeRate;

        return null;
    }

    private ExchangeAmountDTO getAmountAtDirectRate(String baseCurrencyCode, String targetCurrencyCode, Double amount) {
        ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRate(baseCurrencyCode + targetCurrencyCode);
        if (exchangeRate != null) {
            Double convertedAmount = roundDecimal(amount * exchangeRate.getRate(), 6);
            return new ExchangeAmountDTO(exchangeRate, amount, convertedAmount);
        }
        return null;
    }

    private ExchangeAmountDTO getAmountAtReverseRate(String baseCurrencyCode, String targetCurrencyCode, Double amount) {
        ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRate(targetCurrencyCode + baseCurrencyCode);
        if (exchangeRate != null) {
            exchangeRate.setRate(roundDecimal(1 / exchangeRate.getRate(), 6));
            Double convertedAmount = roundDecimal(amount * exchangeRate.getRate(), 6);
            return new ExchangeAmountDTO(exchangeRate, amount, convertedAmount);
        }
        return null;
    }

    private ExchangeAmountDTO getAmountAtCrossRate(String baseCurrencyCode, String targetCurrencyCode, Double amount) throws CurrencyNotFoundException {
        ExchangeRateDTO baseExchangeRate = exchangeRateService.getExchangeRate("USD" + baseCurrencyCode);
        ExchangeRateDTO targetExchangeRate = exchangeRateService.getExchangeRate("USD" + targetCurrencyCode);

        if (baseExchangeRate != null && targetExchangeRate != null) {
            Double crossRate = roundDecimal(targetExchangeRate.getRate() / baseExchangeRate.getRate(), 2);
            Double convertedAmount = roundDecimal(amount * crossRate, 6);
            return new ExchangeAmountDTO(baseExchangeRate.getTargetCurrency(), targetExchangeRate.getTargetCurrency(), crossRate, amount, convertedAmount);
        } else {
            throw new CurrencyNotFoundException(CURRENCY_NOT_FOUND.getMessage());
        }
    }
}
