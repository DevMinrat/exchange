package com.devminrat.exchange;

import java.io.*;
import java.util.List;

import com.devminrat.exchange.model.Currency;
import com.devminrat.exchange.service.CurrencyService;
import com.devminrat.exchange.service.CurrencyServiceImpl;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Currency currency = currencyService.getCurrency("AUD");
        List<Currency> allCurrencies = currencyService.getAllCurrencies();

        out.println("<html><body>");
        out.println("<h1>" + currency.toString() + "</h1>");
        out.println("<h2> ----------------------------------------- </h2>");
        for (Currency curr : allCurrencies) {
            out.println("<h2>" + curr.toString() + "</h2>");
        }
        out.println("</body></html>");
    }

}