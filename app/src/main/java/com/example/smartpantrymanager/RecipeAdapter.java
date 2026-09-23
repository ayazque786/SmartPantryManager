package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(
            List<Recipe> recipeList,
            OnRecipeClickListener listener
    ) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {

        Recipe recipe = recipeList.get(position);

        holder.textRecipeName.setText(recipe.getName());
        holder.textRecipeDescription.setText(recipe.getDescription());

        holder.buttonViewRecipe.setOnClickListener(
                v -> listener.onRecipeClick(recipe)
        );

        holder.itemView.setOnClickListener(
                v -> listener.onRecipeClick(recipe)
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView textRecipeName;
        TextView textRecipeDescription;
        Button buttonViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textRecipeName =
                    itemView.findViewById(R.id.textRecipeName);

            textRecipeDescription =
                    itemView.findViewById(R.id.textRecipeDescription);

            buttonViewRecipe =
                    itemView.findViewById(R.id.buttonViewRecipe);
        }
    }
}