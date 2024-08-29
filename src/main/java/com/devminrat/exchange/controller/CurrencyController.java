package com.devminrat.exchange.controller;

import java.io.*;
import java.util.List;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.service.CurrencyService;
import com.devminrat.exchange.service.CurrencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import static com.devminrat.exchange.util.ResponseUtil.*;

@WebServlet(name = "currencyController", value = "/currencies/*")
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

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
                    writeBadRequestResponse(resp, "Check your URL path");
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
            if (!req.getParameter(Currency.FIELD_NAME).isBlank() && !req.getParameter(Currency.FIELD_CODE).isBlank()
                    && !req.getParameter(Currency.FIELD_SIGN).isBlank()) {
                newCurrency.setName(req.getParameter(Currency.FIELD_NAME));
                newCurrency.setCode(req.getParameter(Currency.FIELD_CODE));
                newCurrency.setSign(req.getParameter(Currency.FIELD_SIGN));
            } else {
                writeBadRequestResponse(resp, "Missing required field");
                return;
            }

            Currency addedCurrency = currencyService.setCurrency(newCurrency);

            if (addedCurrency != null) {
                String json = objectMapper.writeValueAsString(addedCurrency);
                writeCreatedResponse(resp, json);
            } else {
                writeConflictResponse(resp, "Currency already exists");
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
            writeNotFoundResponse(resp, "Currency not found");
        } else {
            String json = objectMapper.writeValueAsString(currency);
            resp.getWriter().write(json);
        }
    }

}