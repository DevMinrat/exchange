package com.devminrat.exchange.controller;

import com.devminrat.exchange.model.ExchangeRate;
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
            writeInternalServerErrorResponse(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        try {
            String baseCode = req.getParameter(ExchangeRate.FIELD_TARGET_CODE);
            String targetCode = req.getParameter(ExchangeRate.FIELD_BASE_CODE);
            String rate = req.getParameter(ExchangeRate.FIELD_RATE);

            if (isValidValues(baseCode, targetCode, rate)) {
                ExchangeRate newExchangeRate = exchangeRateService.setExchangeRate(req.getParameter(baseCode),
                        req.getParameter(targetCode), Double.parseDouble(req.getParameter(rate)));

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

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            String[] exchangeRate = pathInfo.split("/");
            if (exchangeRate.length == 2) {
                String exchangeRateCode = exchangeRate[1];
                handleGetExchangeRateByCode(resp, exchangeRateCode);
            } else {
                writeBadRequestResponse(resp, CHECK_URL.getMessage());
            }

            String rate = req.getParameter(ExchangeRate.FIELD_RATE);

            if (isValidValues(rate)) {
                ExchangeRate newExchangeRate = exchangeRateService.patchExchangeRate("", "",
                        Double.parseDouble(req.getParameter(rate)));

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
            writeNotFoundResponse(resp, CURRENCY_PAIR_NOT_FOUND.getMessage());
        } else {
            String json = objectMapper.writeValueAsString(exchangeRate);
            resp.getWriter().write(json);
        }
    }

}