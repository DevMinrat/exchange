package com.devminrat.exchange.controller;

import com.devminrat.exchange.model.ExchangeAmountDTO;
import com.devminrat.exchange.model.ExchangeRateDTO;
import com.devminrat.exchange.service.ExchangeAmountService;
import com.devminrat.exchange.service.ExchangeAmountServiceImpl;
import com.devminrat.exchange.service.ExchangeRateService;
import com.devminrat.exchange.service.ExchangeRateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.devminrat.exchange.constants.ErrorMessage.*;
import static com.devminrat.exchange.util.ResponseUtil.*;
import static com.devminrat.exchange.util.ValidationUtil.isValidValues;

@WebServlet(name = "exchangeController", value = "/exchange")
public class ExchangeAmountController extends HttpServlet {
    private final String FIELD_FROM = "from";
    private final String FIELD_TO = "to";
    private final String FIELD_AMOUNT = "amount";

    private final ExchangeAmountService exchangeAmountService = new ExchangeAmountServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String baseCurrencyCode = req.getParameter(FIELD_FROM);
        String targetCurrencyCode = req.getParameter(FIELD_TO);
        String amount = req.getParameter(FIELD_AMOUNT);

        if (isValidValues(baseCurrencyCode, targetCurrencyCode, amount)) {
            ExchangeAmountDTO res = exchangeAmountService.getExchangeAmount(baseCurrencyCode, targetCurrencyCode, Double.parseDouble(amount));
            String json = objectMapper.writeValueAsString(res);
            resp.getWriter().write(json);
        } else {
            writeBadRequestResponse(resp, CHECK_URL.getMessage());
        }

    }

}