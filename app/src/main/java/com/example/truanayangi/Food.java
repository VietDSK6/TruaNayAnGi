package com.example.truanayangi;

import java.text.Normalizer;
import java.util.Locale;

/** Đối tượng dữ liệu đại diện cho một món ăn. */
public class Food {
    private final long id;
    private final String name;
    private final long price;
    private final String category;
    private final String description;
    private final String ingredients;
    private final String emoji;

    public Food(long id, String name, long price, String category, String description,
                String ingredients, String emoji) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.emoji = emoji;
    }

    // Giữ các hàm tạo này để những phần mở rộng cũ của project vẫn tương thích.
    public Food(long id, String name, long price, String category, String description) {
        this(id, name, price, category, description, "Đang cập nhật", "🍽️");
    }

    public Food(String name, long price, String category, String description) {
        this(0, name, price, category, description);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getEmoji() {
        return emoji;
    }

    public boolean matchesQuery(String query) {
        String keyword = normalize(query);
        if (keyword.isEmpty()) {
            return true;
        }
        String searchable = normalize(name + " " + category + " " + description + " " + ingredients);
        if (searchable.contains(keyword)) {
            return true;
        }
        String numericQuery = keyword.replace(".", "").replace(",", "").replace(" ", "");
        return numericQuery.matches("\\d+") && String.valueOf(price).contains(numericQuery);
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "")
                .replace('đ', 'd')
                .replace('Đ', 'D')
                .toLowerCase(Locale.ROOT)
                .trim();
    }
}
