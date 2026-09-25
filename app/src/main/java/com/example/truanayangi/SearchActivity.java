package com.example.truanayangi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/** Màn hình 2: tìm kiếm món ăn theo tên, không phân biệt dấu và chữ hoa/thường. */
public class SearchActivity extends AppCompatActivity {
    private FoodAdapter adapter;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ScreenInsets.apply(findViewById(R.id.searchRoot));

        MaterialToolbar toolbar = findViewById(R.id.searchToolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        TextInputEditText searchInput = findViewById(R.id.searchInput);
        emptyView = findViewById(R.id.emptyView);

        RecyclerView resultList = findViewById(R.id.searchResultList);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(food ->
                startActivity(FoodDetailActivity.createIntent(this, food)));
        resultList.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                filterFoods(text == null ? "" : text.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        filterFoods("");
        searchInput.requestFocus();
    }

    private void filterFoods(String query) {
        List<Food> results = new ArrayList<>();
        for (Food food : FoodStore.getFoods()) {
            if (food.matchesQuery(query)) {
                results.add(food);
            }
        }
        adapter.setFoods(results);
        emptyView.setVisibility(results.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
