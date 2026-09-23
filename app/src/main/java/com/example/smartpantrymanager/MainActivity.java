package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIngredients;
    private TextView textEmpty;
    private Button buttonAddIngredient;
    private Button buttonSuggestedRecipes;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect views from activity_main.xml
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        textEmpty = findViewById(R.id.textEmpty);
        buttonAddIngredient = findViewById(R.id.buttonAddIngredient);
        buttonSuggestedRecipes = findViewById(R.id.buttonSuggestedRecipes);

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // Create ingredient list
        ingredientList = new ArrayList<>();

        // Set up RecyclerView
        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Set up ingredient adapter
        ingredientAdapter = new IngredientAdapter(
                ingredientList,
                new IngredientAdapter.OnIngredientActionListener() {

                    @Override
                    public void onEdit(Ingredient ingredient) {

                        Intent intent = new Intent(
                                MainActivity.this,
                                AddEditIngredientActivity.class
                        );

                        intent.putExtra(
                                "ingredient_id",
                                ingredient.getId()
                        );

                        intent.putExtra(
                                "ingredient_name",
                                ingredient.getName()
                        );

                        intent.putExtra(
                                "ingredient_quantity",
                                ingredient.getQuantity()
                        );

                        intent.putExtra(
                                "ingredient_unit",
                                ingredient.getUnit()
                        );

                        intent.putExtra(
                                "ingredient_expiry",
                                ingredient.getExpiryDate()
                        );

                        startActivity(intent);
                    }

                    @Override
                    public void onDelete(Ingredient ingredient) {

                        databaseHelper.deleteIngredient(
                                ingredient.getId()
                        );

                        loadIngredients();
                    }
                }
        );

        recyclerViewIngredients.setAdapter(ingredientAdapter);

        // Load pantry ingredients
        loadIngredients();

        // Add Ingredient button
        buttonAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // Suggested Recipes button
        buttonSuggestedRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });
    }

    private void loadIngredients() {

        ingredientList.clear();

        ingredientList.addAll(
                databaseHelper.getAllIngredients()
        );

        ingredientAdapter.notifyDataSetChanged();

        // Show empty message if pantry has no ingredients
        if (ingredientList.isEmpty()) {

            textEmpty.setVisibility(View.VISIBLE);
            recyclerViewIngredients.setVisibility(View.GONE);

        } else {

            textEmpty.setVisibility(View.GONE);
            recyclerViewIngredients.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh pantry whenever returning to this screen
        if (databaseHelper != null && ingredientAdapter != null) {
            loadIngredients();
        }
    }
}