package com.example.truanayangi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** RecyclerView adapter được dùng lại ở màn hình danh sách và tìm kiếm. */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    private final List<Food> foods = new ArrayList<>();
    private final OnFoodClickListener listener;

    public FoodAdapter(OnFoodClickListener listener) {
        this.listener = listener;
    }

    public void setFoods(List<Food> newFoods) {
        int previousSize = foods.size();
        foods.clear();
        notifyItemRangeRemoved(0, previousSize);
        foods.addAll(newFoods);
        notifyItemRangeInserted(0, foods.size());
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(foods.get(position));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodEmoji;
        private final TextView foodIndex;
        private final TextView foodName;
        private final TextView foodPrice;
        private final TextView foodCategory;
        private final TextView foodDescription;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodEmoji = itemView.findViewById(R.id.foodEmoji);
            foodIndex = itemView.findViewById(R.id.foodIndex);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodCategory = itemView.findViewById(R.id.foodCategory);
            foodDescription = itemView.findViewById(R.id.foodDescription);
        }

        void bind(Food food) {
            foodIndex.setText(String.format(java.util.Locale.ROOT, "%02d", getBindingAdapterPosition() + 1));
            foodEmoji.setText(food.getEmoji());
            foodName.setText(food.getName());
            foodPrice.setText(PriceFormatter.format(food.getPrice()));
            foodCategory.setText(food.getCategory());
            foodDescription.setText(food.getDescription());
            itemView.setContentDescription(itemView.getContext().getString(
                    R.string.food_item_content_description,
                    food.getName(),
                    PriceFormatter.format(food.getPrice())));
            itemView.setOnClickListener(view -> listener.onFoodClick(food));
        }
    }
}
