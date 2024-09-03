package com.devminrat.exchange.constants;

public enum ErrorMessage {
    CHECK_URL("Check your URL path"),
    MISSING_FIELD("Missing required field"),
    CURRENCY_EXIST("Currency already exists"),
    CURRENCY_NOT_FOUND("Currency/currencies not found"),

    CURRENCY_PAIR_NOT_FOUND("Currency pair not found"),
    CURRENCY_PAIR_EXIST("Currency pair already exists");

    private final String message;

    ErrorMessage(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

}
