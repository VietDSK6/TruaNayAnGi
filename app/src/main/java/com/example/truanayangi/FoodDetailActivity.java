package com.example.truanayangi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

/** Màn hình 3: thông tin món, chọn số lượng và đặt món. */
public class FoodDetailActivity extends AppCompatActivity {
    private static final String EXTRA_NAME = "food_name";
    private static final String EXTRA_PRICE = "food_price";
    private static final String EXTRA_CATEGORY = "food_category";
    private static final String EXTRA_DESCRIPTION = "food_description";
    private static final String EXTRA_INGREDIENTS = "food_ingredients";
    private static final String EXTRA_EMOJI = "food_emoji";
    private static final String STATE_QUANTITY = "quantity";

    private long price;
    private int quantity = 1;
    private TextView quantityText;
    private TextView totalPrice;
    private MaterialButton decreaseButton;

    public static Intent createIntent(Context context, Food food) {
        Intent intent = new Intent(context, FoodDetailActivity.class);
        intent.putExtra(EXTRA_NAME, food.getName());
        intent.putExtra(EXTRA_PRICE, food.getPrice());
        intent.putExtra(EXTRA_CATEGORY, food.getCategory());
        intent.putExtra(EXTRA_DESCRIPTION, food.getDescription());
        intent.putExtra(EXTRA_INGREDIENTS, food.getIngredients());
        intent.putExtra(EXTRA_EMOJI, food.getEmoji());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ScreenInsets.apply(findViewById(R.id.detailRoot));

        MaterialToolbar toolbar = findViewById(R.id.detailToolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        Intent intent = getIntent();
        String foodName = intent.getStringExtra(EXTRA_NAME);
        if (foodName == null) {
            Toast.makeText(this, R.string.food_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        price = intent.getLongExtra(EXTRA_PRICE, 0);
        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt(STATE_QUANTITY, 1);
        }

        ((TextView) findViewById(R.id.detailEmoji)).setText(intent.getStringExtra(EXTRA_EMOJI));
        ((TextView) findViewById(R.id.detailName)).setText(foodName);
        ((TextView) findViewById(R.id.detailCategory)).setText(intent.getStringExtra(EXTRA_CATEGORY));
        ((TextView) findViewById(R.id.detailPrice)).setText(PriceFormatter.format(price));
        ((TextView) findViewById(R.id.detailDescription)).setText(
                intent.getStringExtra(EXTRA_DESCRIPTION));
        ((TextView) findViewById(R.id.detailIngredients)).setText(
                intent.getStringExtra(EXTRA_INGREDIENTS));

        quantityText = findViewById(R.id.quantityText);
        totalPrice = findViewById(R.id.totalPrice);
        decreaseButton = findViewById(R.id.decreaseButton);

        decreaseButton.setOnClickListener(view -> {
            if (quantity > 1) {
                quantity--;
                updateOrderSummary();
            }
        });
        findViewById(R.id.increaseButton).setOnClickListener(view -> {
            if (quantity < 99) {
                quantity++;
                updateOrderSummary();
            }
        });
        findViewById(R.id.orderButton).setOnClickListener(view ->
                Toast.makeText(this, R.string.order_success, Toast.LENGTH_LONG).show());

        updateOrderSummary();
    }

    private void updateOrderSummary() {
        quantityText.setText(String.valueOf(quantity));
        totalPrice.setText(PriceFormatter.format(price * quantity));
        decreaseButton.setEnabled(quantity > 1);
        quantityText.setContentDescription(getString(R.string.quantity_value, quantity));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_QUANTITY, quantity);
    }
}
