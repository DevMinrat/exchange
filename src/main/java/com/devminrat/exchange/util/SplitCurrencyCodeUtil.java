package com.devminrat.exchange.util;

import java.util.HashMap;
import java.util.Map;

public final class SplitCurrencyCodeUtil {
    private SplitCurrencyCodeUtil() {
    }
    //TODO: check weaknesses
    public static Map<String, String> splitCode(String code) {
        Map<String, String> currencyCodes = new HashMap<>();

        final int requiredLength = 6;
        final int splitNum = 3;

        if (code != null && code.length() == requiredLength) {
            String baseCode = code.substring(0, splitNum);
            String targetCode = code.substring(splitNum);
            currencyCodes.put("baseCode", baseCode);
            currencyCodes.put("targetCode", targetCode);
            return currencyCodes;
        } else {
            return null;
        }
    }
}
