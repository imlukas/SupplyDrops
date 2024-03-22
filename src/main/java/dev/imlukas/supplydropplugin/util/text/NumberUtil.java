package dev.imlukas.supplydropplugin.util.text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {

    private NumberUtil() {
    }

    public static String formatDouble(double number) {
        return formatDouble(number, 0);
    }

    public static String formatDouble(double number, int decimalPlaces) {
        if (decimalPlaces == 0) {
            return String.valueOf((int) number);
        }

        NumberFormat numberFormat = new DecimalFormat("#." + "#".repeat(Math.max(0, decimalPlaces)));
        return numberFormat.format(number);
    }
}
