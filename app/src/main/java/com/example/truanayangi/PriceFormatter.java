package com.example.truanayangi;

import java.text.NumberFormat;
import java.util.Locale;

public final class PriceFormatter {
    private PriceFormatter() {
    }

    public static String format(long price) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " đ";
    }
}
