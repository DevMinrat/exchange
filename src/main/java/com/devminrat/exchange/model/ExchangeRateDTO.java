package com.devminrat.exchange.model;

public class ExchangeRateDTO {
    public static final String FIELD_BASE_CODE = "baseCurrencyCode";
    public static final String FIELD_TARGET_CODE = "targetCurrencyCode";
    public static final String FIELD_RATE = "rate";

    private Integer id;
    private CurrencyDTO baseCurrency;
    private CurrencyDTO targetCurrency;
    private Double rate;

    public ExchangeRateDTO() {
    }

    public ExchangeRateDTO(Integer id, CurrencyDTO baseCurrency, CurrencyDTO targetCurrency, Double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CurrencyDTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyDTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyDTO getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyDTO targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrency=" + baseCurrency +
                ", targetCurrency=" + targetCurrency +
                ", rate=" + rate +
                '}';
    }
}
