package com.devminrat.exchange.controller;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.model.ExchangeRate;
import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
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
                    handleGetExchangeRateByCode(resp, exchangeRateCode);
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
//TODO: add validation for "rate"
//TODO: refactoring isValidValues - it gets constants instead of values.
        try {
            if (isValidValues(ExchangeRate.FIELD_BASE_CODE, ExchangeRate.FIELD_TARGET_CODE, ExchangeRate.FIELD_RATE)) {
                ExchangeRate newExchangeRate = exchangeRateService.setExchangeRate(req.getParameter(ExchangeRate.FIELD_BASE_CODE),
                        req.getParameter(ExchangeRate.FIELD_TARGET_CODE), Double.parseDouble(req.getParameter(ExchangeRate.FIELD_RATE)));

                String json = objectMapper.writeValueAsString(newExchangeRate);
                writeCreatedResponse(resp, json);
            } else {
                writeBadRequestResponse(resp, MISSING_FIELD.getMessage());
            }
        } catch (CurrencyNotFoundException e) {
            writeNotFoundResponse(resp, CURRENCY_NOT_FOUND.getMessage());
        } catch (ExchangeRateAlreadyExistsException e) {
            writeConflictResponse(resp, CURRENCY_PAIR_EXIST.getMessage());
        } catch (Exception e) {
            writeInternalServerErrorResponse(resp);
        }
    }

    private void handleGetAllExchangeRates(HttpServletResponse resp) throws IOException {
        List<ExchangeRate> allExchangeRates = exchangeRateService.getAllExchangeRates();
        if (allExchangeRates == null) {
            writeInternalServerErrorResponse(resp);
        } else {
            String json = objectMapper.writeValueAsString(allExchangeRates);
            resp.getWriter().write(json);
        }
    }

    private void handleGetExchangeRateByCode(HttpServletResponse resp, String code) throws IOException {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(code);
        if (exchangeRate == null) {
            writeNotFoundResponse(resp, EXCHANGE_RATE_NOT_FOUND.getMessage());
        } else {
            String json = objectMapper.writeValueAsString(exchangeRate);
            resp.getWriter().write(json);
        }
    }

}