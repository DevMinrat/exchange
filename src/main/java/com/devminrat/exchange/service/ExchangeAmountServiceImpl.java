package com.devminrat.exchange.service;

import com.devminrat.exchange.dao.ExchangeRateDao;
import com.devminrat.exchange.dao.ExchangeRateDaoImpl;
import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.model.CurrencyDTO;
import com.devminrat.exchange.model.ExchangeAmountDTO;
import com.devminrat.exchange.model.ExchangeRateDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static com.devminrat.exchange.constants.ErrorMessage.CURRENCY_NOT_FOUND;
import static com.devminrat.exchange.constants.ErrorMessage.CURRENCY_PAIR_EXIST;
import static com.devminrat.exchange.util.SplitCurrencyCodeUtil.splitCode;

public class ExchangeAmountServiceImpl implements ExchangeAmountService {
    private final ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
    private final CurrencyService currencyService = new CurrencyServiceImpl();

    @Override
    public ExchangeAmountDTO getExchangeAmount(String baseCurrencyCode, String targetCurrencyCode, Double amount) {
        //TODO: refactor this, too large and unreadable.
        ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRate(baseCurrencyCode + targetCurrencyCode);
        if (exchangeRate != null) {

            Double convertedAmount = new BigDecimal(amount * exchangeRate.getRate()).setScale(6, RoundingMode.HALF_UP).doubleValue();

            return new ExchangeAmountDTO(exchangeRate, amount, convertedAmount);
        } else {
            exchangeRate = exchangeRateService.getExchangeRate(targetCurrencyCode + baseCurrencyCode);
            if (exchangeRate != null) {

                exchangeRate.setRate(BigDecimal.valueOf(1 / exchangeRate.getRate()).setScale(6, RoundingMode.HALF_UP).doubleValue());

                Double convertedAmount = new BigDecimal(amount * exchangeRate.getRate()).setScale(6, RoundingMode.HALF_UP).doubleValue();

                return new ExchangeAmountDTO(exchangeRate, amount, convertedAmount);
            } else
                return null;
        }
    }

//    @Override
//    public boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
//        return getExchangeRate(baseCurrencyCode + targetCurrencyCode) != null;
//    }
}
