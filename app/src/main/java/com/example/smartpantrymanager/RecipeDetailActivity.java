package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView textRecipeDetailName;
    private TextView textRecipeDetailDescription;
    private TextView textRecipeIngredients;
    private TextView textRecipeInstructions;
    private Button buttonBackToRecipes;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        textRecipeDetailName = findViewById(R.id.textRecipeDetailName);
        textRecipeDetailDescription = findViewById(R.id.textRecipeDetailDescription);
        textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        textRecipeInstructions = findViewById(R.id.textRecipeInstructions);
        buttonBackToRecipes = findViewById(R.id.buttonBackToRecipes);

        databaseHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeDescription = getIntent().getStringExtra("recipe_description");
        String recipeInstructions = getIntent().getStringExtra("recipe_instructions");

        if (recipeName != null) {
            textRecipeDetailName.setText(recipeName);
        }

        if (recipeDescription != null) {
            textRecipeDetailDescription.setText(recipeDescription);
        }

        if (recipeInstructions != null) {
            textRecipeInstructions.setText(recipeInstructions);
        }

        if (recipeId != -1) {

            List<RecipeIngredient> ingredients =
                    databaseHelper.getRecipeIngredients(recipeId);

            StringBuilder ingredientText = new StringBuilder();

            for (RecipeIngredient ingredient : ingredients) {

                ingredientText
                        .append("• ")
                        .append(formatIngredient(ingredient))
                        .append("\n");
            }

            textRecipeIngredients.setText(ingredientText.toString().trim());
        }

        buttonBackToRecipes.setOnClickListener(v -> finish());
    }

    private String formatIngredient(RecipeIngredient ingredient) {

        String quantity = formatQuantity(ingredient.getQuantity());
        String unit = ingredient.getUnit();
        String name = ingredient.getIngredientName();

        if (unit == null) {
            unit = "";
        }

        String normalisedUnit = unit.trim().toLowerCase();

        // Item/piece units do not need to be displayed.
        if (normalisedUnit.equals("items")
                || normalisedUnit.equals("item")
                || normalisedUnit.equals("pcs")
                || normalisedUnit.equals("pc")
                || normalisedUnit.equals("piece")
                || normalisedUnit.equals("pieces")) {

            return quantity + " " + name;
        }

        // Other units such as g, kg, ml, cups and slices are displayed.
        if (!normalisedUnit.isEmpty()) {
            return quantity + " " + unit + " " + name;
        }

        return quantity + " " + name;
    }

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }
}