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
    private final ExchangeAmountService exchangeAmountService = new ExchangeAmountServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (isValidValues(baseCurrencyCode, targetCurrencyCode, amount)) {
            var res = exchangeAmountService.getExchangeAmount(baseCurrencyCode, targetCurrencyCode, Double.parseDouble(amount));

            String json = objectMapper.writeValueAsString(res);
            resp.getWriter().write(json);


            // 2) get BA from exchangeRates, then reverse rate and calculate convertedAmount = amount * rate
            // 3) get USD-A and USD-B, then calculate rate and calculate convertedAmount = amount * rate
        } else {
            writeBadRequestResponse(resp, CHECK_URL.getMessage());
        }

    }

}