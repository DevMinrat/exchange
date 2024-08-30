package com.devminrat.exchange.constants;

public enum ErrorMessage {
    CHECK_URL("Check your URL path"),
    MISSING_FIELD("Missing required field"),
    CURRENCY_EXIST("Currency already exists"),
    CURRENCY_NOT_FOUND("Currency not found");

    private final String message;

    ErrorMessage(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

}
