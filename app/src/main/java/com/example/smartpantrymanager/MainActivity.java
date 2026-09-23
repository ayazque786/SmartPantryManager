package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private Button buttonSettings;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        textEmpty = findViewById(R.id.textEmpty);
        buttonAddIngredient = findViewById(R.id.buttonAddIngredient);
        buttonSuggestedRecipes = findViewById(R.id.buttonSuggestedRecipes);
        buttonSettings = findViewById(R.id.buttonSettings);

        databaseHelper = new DatabaseHelper(this);

        ingredientList = new ArrayList<>();

        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(this)
        );

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

        loadIngredients();

        // Open Add Ingredient screen
        buttonAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // Open Suggested Recipes screen
        buttonSuggestedRecipes.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);
        });

        // Open Settings screen
        buttonSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    // Display the three-dot navigation menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    // Handle selections from the navigation menu.
    @Override
    public boolean onOptionsItemSelected(
            @NonNull MenuItem item
    ) {

        int itemId = item.getItemId();

        if (itemId == R.id.menuSuggestedRecipes) {

            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );

            startActivity(intent);

            return true;
        }

        if (itemId == R.id.menuSettings) {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadIngredients() {

        ingredientList.clear();

        ingredientList.addAll(
                databaseHelper.getAllIngredients()
        );

        ingredientAdapter.notifyDataSetChanged();

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

        if (databaseHelper != null &&
                ingredientAdapter != null) {

            loadIngredients();
        }
    }
}