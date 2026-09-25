package com.example.truanayangi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExampleUnitTest {
    @Test
    public void searchIgnoresAccentsAndLetterCase() {
        Food food = new Food(1, "Cơm tấm sườn", 45000, "Cơm", "Sườn nướng thơm");

        assertTrue(food.matchesQuery("COM TAM"));
        assertTrue(food.matchesQuery("nuong"));
    }

    @Test
    public void searchMatchesFormattedPrice() {
        Food food = new Food(1, "Phở bò", 50000, "Món nước", "Nước dùng đậm vị");

        assertTrue(food.matchesQuery("50.000"));
        assertFalse(food.matchesQuery("35000"));
    }

    @Test
    public void priceUsesVietnameseCurrencyDisplay() {
        String formattedPrice = PriceFormatter.format(45000);

        assertTrue(formattedPrice.contains("45"));
        assertTrue(formattedPrice.endsWith(" đ"));
    }
}
