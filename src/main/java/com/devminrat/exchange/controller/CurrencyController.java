package com.devminrat.exchange.controller;

import java.io.*;
import java.util.List;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.service.CurrencyService;
import com.devminrat.exchange.service.CurrencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static com.devminrat.exchange.constants.ErrorMessage.*;
import static com.devminrat.exchange.util.ResponseUtil.*;
import static com.devminrat.exchange.util.ValidationUtil.isValidValues;

@WebServlet(name = "currencyController", value = "/currencies/*")
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    //TODO: use CurrencyNotFoundException

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                handleGetAllCurrencies(resp);
            } else {
                String[] currencies = pathInfo.split("/");
                if (currencies.length == 2) {
                    String currencyCode = currencies[1];
                    handleGetCurrencyByCode(resp, currencyCode);
                } else {
                    writeBadRequestResponse(resp, CHECK_URL.getMessage());
                }
            }
        } catch (Exception e) {
            writeInternalServerErrorResponse(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Currency newCurrency = new Currency();

        try {
            String nameField = req.getParameter(Currency.FIELD_NAME);
            String codeField = req.getParameter(Currency.FIELD_CODE);
            String signField = req.getParameter(Currency.FIELD_SIGN);

            if (isValidValues(nameField, codeField, signField)) {
                newCurrency.setName(nameField);
                newCurrency.setCode(codeField);
                newCurrency.setSign(signField);
            } else {
                writeBadRequestResponse(resp, MISSING_FIELD.getMessage());
                return;
            }

            Currency addedCurrency = currencyService.setCurrency(newCurrency);

            if (addedCurrency != null) {
                String json = objectMapper.writeValueAsString(addedCurrency);
                writeCreatedResponse(resp, json);
            } else {
                writeConflictResponse(resp, CURRENCY_EXIST.getMessage());
            }

        } catch (Exception e) {
            writeInternalServerErrorResponse(resp);
        }

    }

    private void handleGetAllCurrencies(HttpServletResponse resp) throws IOException {
        List<Currency> allCurrencies = currencyService.getAllCurrencies();
        if (allCurrencies == null) {
            writeInternalServerErrorResponse(resp);
        } else {
            String json = objectMapper.writeValueAsString(allCurrencies);
            resp.getWriter().write(json);
        }
    }

    private void handleGetCurrencyByCode(HttpServletResponse resp, String code) throws IOException {
        Currency currency = currencyService.getCurrency(code);
        if (currency == null) {
            writeNotFoundResponse(resp, CURRENCY_NOT_FOUND.getMessage());
        } else {
            String json = objectMapper.writeValueAsString(currency);
            resp.getWriter().write(json);
        }
    }

}