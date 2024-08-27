package com.devminrat.exchange.controller;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.service.CurrencyService;
import com.devminrat.exchange.service.CurrencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "currencyController", value = "/currency/*")
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleGetAllCurrencies(resp);
        } else {
            String[] currencies = pathInfo.split("/");
            if (currencies.length == 2) {
                String currencyCode = currencies[1];
                handleGetCurrencyByCode(resp, currencyCode);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Currency not found\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Currency newCurrency = new Currency();

        newCurrency.setName(req.getParameter("name"));
        newCurrency.setCode(req.getParameter("code"));
        newCurrency.setSign(req.getParameter("sign"));

        Currency addedCurrency = currencyService.setCurrency(newCurrency);

        if (addedCurrency != null) {
            String json = objectMapper.writeValueAsString(addedCurrency);
            resp.getWriter().write(json);
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"Currency already exists\"}");
        }

    }

    private void handleGetAllCurrencies(HttpServletResponse resp) throws IOException {
        List<Currency> allCurrencies = currencyService.getAllCurrencies();
        String json = objectMapper.writeValueAsString(allCurrencies);
        resp.getWriter().write(json);
    }

    private void handleGetCurrencyByCode(HttpServletResponse resp, String code) throws IOException {
        Currency currency = currencyService.getCurrency(code);
        String json = objectMapper.writeValueAsString(currency);
        resp.getWriter().write(json);
    }

}