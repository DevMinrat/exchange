package com.devminrat.exchange.util;

import java.util.Set;

public final class ValidationUtil {
    private ValidationUtil() {
    }

    public static boolean isValidValues(String... values) {
        if (values == null) {
            return false;
        }

        for (String value : values) {
            if (!isValidValue(value)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidValue(final String value) {
        return value != null && !value.isBlank();
    }
}
