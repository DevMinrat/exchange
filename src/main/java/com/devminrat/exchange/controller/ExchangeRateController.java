package com.devminrat.exchange.controller;

import com.devminrat.exchange.model.ExchangeRateDTO;
import com.devminrat.exchange.exceptions.CurrencyNotFoundException;
import com.devminrat.exchange.exceptions.ExchangeRateAlreadyExistsException;
import com.devminrat.exchange.service.ExchangeRateService;
import com.devminrat.exchange.service.ExchangeRateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.devminrat.exchange.constants.ErrorMessage.*;
import static com.devminrat.exchange.util.ResponseUtil.*;
import static com.devminrat.exchange.util.CommonUtils.splitCurrenciesCode;
import static com.devminrat.exchange.util.ValidationUtil.isValidValues;

@WebServlet(name = "exchangeRateController", value = "/exchangeRates/*")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

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
            writeInternalServerErrorResponse(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            String baseCode = req.getParameter(ExchangeRateDTO.FIELD_BASE_CODE);
            String targetCode = req.getParameter(ExchangeRateDTO.FIELD_TARGET_CODE);
            String rate = req.getParameter(ExchangeRateDTO.FIELD_RATE);

            if (isValidValues(baseCode, targetCode, rate)) {
                ExchangeRateDTO newExchangeRate = exchangeRateService.setExchangeRate(baseCode,
                        targetCode, Double.parseDouble(rate));

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
            writeInternalServerErrorResponse(e, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        final double rate;

        try {
            String[] path = pathInfo.split("/");
            Map<String, String> currencyCodes = splitCurrenciesCode(path[1]);
            BufferedReader reader = req.getReader();

            rate = Double.parseDouble(reader.readLine().replaceAll("[^\\d.]", ""));

            if (currencyCodes != null) {
                ExchangeRateDTO updatedExchangeRate = exchangeRateService.patchExchangeRate(
                        currencyCodes.get("baseCode"), currencyCodes.get("targetCode"), rate);

                String json = objectMapper.writeValueAsString(updatedExchangeRate);
                writeCreatedResponse(resp, json);
            } else {
                writeBadRequestResponse(resp, CHECK_URL.getMessage());
            }

        } catch (NumberFormatException e) {
            writeBadRequestResponse(resp, MISSING_FIELD.getMessage());
        } catch (CurrencyNotFoundException e) {
            writeNotFoundResponse(resp, CURRENCY_NOT_FOUND.getMessage());
        } catch (ExchangeRateAlreadyExistsException e) {
            writeConflictResponse(resp, CURRENCY_PAIR_EXIST.getMessage());
        } catch (Exception e) {
            writeInternalServerErrorResponse(e, resp);
        }
    }

    private void handleGetAllExchangeRates(HttpServletResponse resp) throws IOException {
        List<ExchangeRateDTO> allExchangeRates = exchangeRateService.getAllExchangeRates();
        if (allExchangeRates == null) {
            writeInternalServerErrorResponse(resp);
        } else {
            String json = objectMapper.writeValueAsString(allExchangeRates);
            resp.getWriter().write(json);
        }
    }

    private void handleGetExchangeRateByCode(HttpServletResponse resp, String code) throws IOException {
        ExchangeRateDTO exchangeRate = exchangeRateService.getExchangeRate(code);
        if (exchangeRate == null) {
            writeNotFoundResponse(resp, CURRENCY_PAIR_NOT_FOUND.getMessage());
        } else {
            String json = objectMapper.writeValueAsString(exchangeRate);
            resp.getWriter().write(json);
        }
    }

}