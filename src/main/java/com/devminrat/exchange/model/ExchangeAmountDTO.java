package com.devminrat.exchange.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExchangeAmountDTO extends ExchangeRateDTO {
    @JsonIgnore
    private Integer id;

    private Double amount;
    private Double convertedAmount;

    public ExchangeAmountDTO() {
    }

    public ExchangeAmountDTO(CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, Double rate, Double amount, Double convertedAmount) {
        super(null, baseCurrency, targetCurrency, rate);
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public ExchangeAmountDTO(ExchangeRateDTO dto, Double amount, Double convertedAmount) {
        super(dto.getId(), dto.getBaseCurrency(), dto.getTargetCurrency(), dto.getRate());
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    @Override
    public String toString() {
        return "ExchangeDTO{" +
                super.toString() +
                "amount=" + amount +
                ", convertedAmount=" + convertedAmount +
                '}';
    }
}
