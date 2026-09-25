package com.example.truanayangi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Nguồn dữ liệu mẫu trong bộ nhớ, không cần cơ sở dữ liệu hay API. */
public final class FoodStore {
    private static final ArrayList<Food> foods = new ArrayList<>();
    private static long nextId = 1;

    static {
        seed("Hamburger bò", 55000, "Đồ ăn nhanh",
                "Bánh mì mềm kẹp bò nướng mọng nước và rau tươi.",
                "Bánh mì, thịt bò, phô mai, xà lách, cà chua, sốt đặc biệt", "🍔");
        seed("Pizza hải sản", 120000, "Pizza",
                "Pizza đế mỏng phủ hải sản tươi và phô mai béo ngậy.",
                "Tôm, mực, phô mai mozzarella, ớt chuông, sốt cà chua", "🍕");
        seed("Pizza bò", 105000, "Pizza",
                "Pizza bò bằm thơm lừng, đậm đà và nhiều phô mai.",
                "Bò bằm, hành tây, phô mai mozzarella, sốt cà chua", "🍕");
        seed("Pizza phô mai", 90000, "Pizza",
                "Vị phô mai kéo sợi hấp dẫn trên lớp đế nướng giòn.",
                "Phô mai mozzarella, parmesan, sốt cà chua, lá oregano", "🍕");
        seed("Mì cay hải sản", 65000, "Món Hàn",
                "Mì dai cùng nước dùng cay nồng, có thể chọn cấp độ cay.",
                "Mì, tôm, mực, xúc xích, nấm kim châm, cải thảo", "🍜");
        seed("Gà rán giòn", 75000, "Đồ ăn nhanh",
                "Gà rán vàng giòn bên ngoài, mềm mọng bên trong.",
                "Đùi gà, bột chiên giòn, gia vị, tương ớt", "🍗");
        seed("Cơm tấm sườn", 50000, "Món Việt",
                "Cơm tấm nóng ăn cùng sườn nướng thơm và mỡ hành.",
                "Cơm tấm, sườn heo, bì, chả trứng, đồ chua, nước mắm", "🍚");
        seed("Phở bò", 60000, "Món Việt",
                "Phở truyền thống với nước dùng trong, ngọt vị xương.",
                "Bánh phở, thịt bò, hành lá, rau thơm, nước dùng xương", "🍲");
        seed("Trà sữa trân châu", 35000, "Đồ uống",
                "Trà sữa thơm béo cùng trân châu đường đen dai mềm.",
                "Trà đen, sữa, đường đen, trân châu", "🧋");
    }

    private FoodStore() {
    }

    private static void seed(String name, long price, String category, String description,
                             String ingredients, String emoji) {
        foods.add(new Food(nextId++, name, price, category, description, ingredients, emoji));
    }

    public static List<Food> getFoods() {
        return Collections.unmodifiableList(new ArrayList<>(foods));
    }

    public static Food getFoodById(long id) {
        for (Food food : foods) {
            if (food.getId() == id) {
                return food;
            }
        }
        return null;
    }

    // Các hàm dưới đây giữ tương thích với phần mở rộng thêm/sửa món có sẵn.
    public static void addFood(Food food) {
        foods.add(new Food(nextId++, food.getName(), food.getPrice(), food.getCategory(),
                food.getDescription(), food.getIngredients(), food.getEmoji()));
    }

    public static boolean updateFood(Food updatedFood) {
        for (int index = 0; index < foods.size(); index++) {
            if (foods.get(index).getId() == updatedFood.getId()) {
                foods.set(index, updatedFood);
                return true;
            }
        }
        return false;
    }

    public static void deleteFood(long id) {
        foods.removeIf(food -> food.getId() == id);
    }
}
