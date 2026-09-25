package com.example.truanayangi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FoodFormActivity extends AppCompatActivity {
    public static final String EXTRA_FOOD_ID = "food_id";

    private TextInputLayout nameLayout;
    private TextInputLayout priceLayout;
    private TextInputLayout categoryLayout;
    private TextInputEditText nameInput;
    private TextInputEditText priceInput;
    private TextInputEditText categoryInput;
    private TextInputEditText descriptionInput;
    private long foodId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_form);
        ScreenInsets.apply(findViewById(R.id.formRoot));

        nameLayout = findViewById(R.id.nameLayout);
        priceLayout = findViewById(R.id.priceLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        categoryInput = findViewById(R.id.categoryInput);
        descriptionInput = findViewById(R.id.descriptionInput);

        MaterialToolbar toolbar = findViewById(R.id.formToolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        foodId = getIntent().getLongExtra(EXTRA_FOOD_ID, -1);
        if (foodId >= 0) {
            toolbar.setTitle(R.string.edit_food);
            loadFood();
        } else {
            toolbar.setTitle(R.string.add_food);
        }

        findViewById(R.id.saveButton).setOnClickListener(view -> saveFood());
    }

    private void loadFood() {
        Food food = FoodStore.getFoodById(foodId);
        if (food == null) {
            Toast.makeText(this, R.string.food_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        nameInput.setText(food.getName());
        priceInput.setText(String.valueOf(food.getPrice()));
        categoryInput.setText(food.getCategory());
        descriptionInput.setText(food.getDescription());
    }

    private void saveFood() {
        nameLayout.setError(null);
        priceLayout.setError(null);
        categoryLayout.setError(null);

        String name = readText(nameInput);
        String priceText = readText(priceInput).replace(".", "").replace(" ", "");
        String category = readText(categoryInput);
        String description = readText(descriptionInput);

        boolean valid = true;
        if (name.isEmpty()) {
            nameLayout.setError(getString(R.string.name_required));
            valid = false;
        }
        if (category.isEmpty()) {
            categoryLayout.setError(getString(R.string.category_required));
            valid = false;
        }

        long price = -1;
        try {
            price = Long.parseLong(priceText);
            if (price < 0) {
                valid = false;
                priceLayout.setError(getString(R.string.price_invalid));
            }
        } catch (NumberFormatException exception) {
            valid = false;
            priceLayout.setError(getString(R.string.price_invalid));
        }

        if (!valid) {
            return;
        }

        if (foodId >= 0) {
            if (!FoodStore.updateFood(new Food(foodId, name, price, category, description))) {
                Toast.makeText(this, R.string.save_failed, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            FoodStore.addFood(new Food(name, price, category, description));
        }

        Toast.makeText(this, R.string.food_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private String readText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
