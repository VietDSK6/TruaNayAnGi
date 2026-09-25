package com.example.truanayangi;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {
    private static final String STATE_SELECTED_FOOD_ID = "selected_food_id";

    private List<Food> foods;
    private WheelView wheelView;
    private View resultCard;
    private TextView resultName;
    private TextView resultPrice;
    private TextView resultCategory;
    private TextView resultDescription;
    private MaterialButton spinButton;
    private long selectedFoodId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        ScreenInsets.apply(findViewById(R.id.spinRoot));

        foods = FoodStore.getFoods();
        wheelView = findViewById(R.id.wheelView);
        resultCard = findViewById(R.id.resultCard);
        resultName = findViewById(R.id.resultName);
        resultPrice = findViewById(R.id.resultPrice);
        resultCategory = findViewById(R.id.resultCategory);
        resultDescription = findViewById(R.id.resultDescription);
        spinButton = findViewById(R.id.startSpinButton);
        resultCard.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);

        MaterialToolbar toolbar = findViewById(R.id.spinToolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        wheelView.setFoods(foods);

        View emptyView = findViewById(R.id.spinEmptyView);
        emptyView.setVisibility(foods.isEmpty() ? View.VISIBLE : View.GONE);
        wheelView.setVisibility(foods.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        spinButton.setEnabled(!foods.isEmpty());
        spinButton.setOnClickListener(view -> spin());

        if (savedInstanceState != null) {
            selectedFoodId = savedInstanceState.getLong(STATE_SELECTED_FOOD_ID, -1);
            restoreResult();
        }
    }

    private void spin() {
        int selectedIndex = new Random().nextInt(foods.size());
        Food selectedFood = foods.get(selectedIndex);
        resultCard.setVisibility(View.GONE);
        spinButton.setEnabled(false);
        spinButton.setText(R.string.spinning);

        wheelView.spinTo(selectedIndex, () -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            selectedFoodId = selectedFood.getId();
            showResult(selectedFood);
            spinButton.setEnabled(true);
            spinButton.setText(R.string.spin_again);
        });
    }

    private void restoreResult() {
        for (int index = 0; index < foods.size(); index++) {
            Food food = foods.get(index);
            if (food.getId() == selectedFoodId) {
                wheelView.pointTo(index);
                showResult(food);
                spinButton.setText(R.string.spin_again);
                return;
            }
        }
    }

    private void showResult(Food food) {
        resultName.setText(food.getName());
        resultPrice.setText(PriceFormatter.format(food.getPrice()));
        resultCategory.setText(food.getCategory());
        resultDescription.setText(food.getDescription().isEmpty()
                ? getString(R.string.no_description)
                : food.getDescription());
        resultCard.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_SELECTED_FOOD_ID, selectedFoodId);
    }
}
