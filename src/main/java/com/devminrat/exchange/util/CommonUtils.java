package com.devminrat.exchange.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public final class CommonUtils {
    private CommonUtils() {
    }

    public static Map<String, String> splitCurrenciesCode(String code) {
        final int REQUIRED_LENGTH = 6;
        final int SPLIT_INDEX = 3;

        if (code != null && code.trim().length() == REQUIRED_LENGTH) {
            String baseCode = code.substring(0, SPLIT_INDEX);
            String targetCode = code.substring(SPLIT_INDEX);

            Map<String, String> currencyCodes = new HashMap<>();
            currencyCodes.put("baseCode", baseCode);
            currencyCodes.put("targetCode", targetCode);
            return currencyCodes;
        } else {
            return null;
        }
    }

    public static Double roundDecimal(Double d, int decimalPlaces) {
        return BigDecimal.valueOf(d).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();
    }
}
