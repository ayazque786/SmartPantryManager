package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private OnIngredientActionListener listener;

    public interface OnIngredientActionListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    public IngredientAdapter(List<Ingredient> ingredientList,
                             OnIngredientActionListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.textIngredientName.setText(ingredient.getName());

        String quantityText =
                "Quantity: " + ingredient.getQuantity() + " " + ingredient.getUnit();

        holder.textQuantity.setText(quantityText);

        String expiry = ingredient.getExpiryDate();

        if (expiry == null || expiry.trim().isEmpty()) {
            holder.textExpiryDate.setText("Expiry: Not specified");
        } else {
            holder.textExpiryDate.setText("Expiry: " + expiry);
        }

        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(ingredient);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void updateData(List<Ingredient> newList) {
        ingredientList = newList;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView textIngredientName;
        TextView textQuantity;
        TextView textExpiryDate;
        Button buttonEdit;
        Button buttonDelete;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            textIngredientName =
                    itemView.findViewById(R.id.textIngredientName);

            textQuantity =
                    itemView.findViewById(R.id.textQuantity);

            textExpiryDate =
                    itemView.findViewById(R.id.textExpiryDate);

            buttonEdit =
                    itemView.findViewById(R.id.buttonEdit);

            buttonDelete =
                    itemView.findViewById(R.id.buttonDelete);
        }
    }
}