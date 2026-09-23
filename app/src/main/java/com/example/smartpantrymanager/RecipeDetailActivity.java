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
                        .append(formatQuantity(ingredient.getQuantity()))
                        .append(" ")
                        .append(ingredient.getUnit())
                        .append(" ")
                        .append(ingredient.getIngredientName())
                        .append("\n");
            }

            textRecipeIngredients.setText(ingredientText.toString());
        }

        buttonBackToRecipes.setOnClickListener(v -> finish());
    }

    private String formatQuantity(double quantity) {

        if (quantity == (long) quantity) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }
}