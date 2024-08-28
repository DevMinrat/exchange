package com.devminrat.exchange.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.service.CurrencyService;
import com.devminrat.exchange.service.CurrencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

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
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Bad Request. Check your URL path\"}");
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Unexpected error occurred\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Currency newCurrency = new Currency();
        try {
            if (!req.getParameter("name").isEmpty() && !req.getParameter("code").isEmpty() && !req.getParameter("sign").isEmpty()) {
                newCurrency.setName(req.getParameter("name"));
                newCurrency.setCode(req.getParameter("code"));
                newCurrency.setSign(req.getParameter("sign"));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Missing required field\"}");
                return;
            }

            Currency addedCurrency = currencyService.setCurrency(newCurrency);

            if (addedCurrency != null) {
                String json = objectMapper.writeValueAsString(addedCurrency);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write(json);
            } else {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"error\":\"Currency already exists\"}");
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Unexpected error occurred\"}");
            e.printStackTrace();
        }

    }

    private void handleGetAllCurrencies(HttpServletResponse resp) throws IOException {
        List<Currency> allCurrencies = currencyService.getAllCurrencies();
        if (allCurrencies == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal Server Error\"}");
        } else {
            String json = objectMapper.writeValueAsString(allCurrencies);
            resp.getWriter().write(json);
        }
    }

    private void handleGetCurrencyByCode(HttpServletResponse resp, String code) throws IOException {
        Currency currency = currencyService.getCurrency(code);
        if (currency == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"Currency not found\"}");
        } else {
            String json = objectMapper.writeValueAsString(currency);
            resp.getWriter().write(json);
        }
    }

}