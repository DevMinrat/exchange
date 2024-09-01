package com.devminrat.exchange.controller;

import com.devminrat.exchange.model.ExchangeRate;
import com.devminrat.exchange.service.ExchangeRateService;
import com.devminrat.exchange.service.ExchangeRateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.devminrat.exchange.constants.ErrorMessage.*;
import static com.devminrat.exchange.util.ResponseUtil.*;
import static com.devminrat.exchange.util.ValidationUtil.isValidValues;

@WebServlet(name = "exchangeRateController", value = "/exchangeRates/*")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAllExchangeRates(resp);
            } else {
                String[] exchangeRate = pathInfo.split("/");
                if (exchangeRate.length == 2) {
                    String exchangeRateCode = exchangeRate[1];
                    handleGetCurrencyByCode(resp, exchangeRateCode);
                } else {
                    writeBadRequestResponse(resp, CHECK_URL.getMessage());
                }
            }
        } catch (Exception e) {
            writeInternalServerErrorResponse(resp);
        }
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        Currency newCurrency = new Currency();
//        try {
//            if (isValidValues(Currency.FIELD_NAME, Currency.FIELD_CODE, Currency.FIELD_SIGN)) {
//                newCurrency.setName(req.getParameter(Currency.FIELD_NAME));
//                newCurrency.setCode(req.getParameter(Currency.FIELD_CODE));
//                newCurrency.setSign(req.getParameter(Currency.FIELD_SIGN));
//            } else {
//                writeBadRequestResponse(resp, MISSING_FIELD.getMessage());
//                return;
//            }
//
//            Currency addedCurrency = currencyService.setCurrency(newCurrency);
//
//            if (addedCurrency != null) {
//                String json = objectMapper.writeValueAsString(addedCurrency);
//                writeCreatedResponse(resp, json);
//            } else {
//                writeConflictResponse(resp, CURRENCY_EXIST.getMessage());
//            }
//
//        } catch (Exception e) {
//            writeInternalServerErrorResponse(resp);
//        }
//
//    }

    private void handleGetAllExchangeRates(HttpServletResponse resp) throws IOException {
        List<ExchangeRate> allExchangeRates = exchangeRateService.getAllExchangeRates();
        if (allExchangeRates == null) {
            writeInternalServerErrorResponse(resp);
        } else {
            String json = objectMapper.writeValueAsString(allExchangeRates);
            resp.getWriter().write(json);
        }
    }

    private void handleGetCurrencyByCode(HttpServletResponse resp, String code) throws IOException {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(code);
        if (exchangeRate == null) {
            writeNotFoundResponse(resp, CURRENCY_NOT_FOUND.getMessage());
        } else {
            String json = objectMapper.writeValueAsString(exchangeRate);
            resp.getWriter().write(json);
        }
    }

}