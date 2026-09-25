package com.example.truanayangi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Màn hình 1: hiển thị toàn bộ thực đơn. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenInsets.apply(findViewById(R.id.main));

        RecyclerView foodList = findViewById(R.id.foodList);
        foodList.setLayoutManager(new LinearLayoutManager(this));
        foodList.setHasFixedSize(true);

        FoodAdapter adapter = new FoodAdapter(food ->
                startActivity(FoodDetailActivity.createIntent(this, food)));
        foodList.setAdapter(adapter);
        adapter.setFoods(FoodStore.getFoods());

        findViewById(R.id.openSearchButton).setOnClickListener(view ->
                startActivity(new Intent(this, SearchActivity.class)));
    }
}
