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

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView textNoRecipes;
    private Button buttonBackToPantry;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        textNoRecipes = findViewById(R.id.textNoRecipes);
        buttonBackToPantry = findViewById(R.id.buttonBackToPantry);

        databaseHelper = new DatabaseHelper(this);

        recipeList = new ArrayList<>();

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recipeAdapter = new RecipeAdapter(
                recipeList,
                new RecipeAdapter.OnRecipeClickListener() {

                    @Override
                    public void onRecipeClick(Recipe recipe) {

                        Intent intent = new Intent(
                                SuggestedRecipesActivity.this,
                                RecipeDetailActivity.class
                        );

                        intent.putExtra(
                                "recipe_id",
                                recipe.getId()
                        );

                        intent.putExtra(
                                "recipe_name",
                                recipe.getName()
                        );

                        intent.putExtra(
                                "recipe_description",
                                recipe.getDescription()
                        );

                        intent.putExtra(
                                "recipe_instructions",
                                recipe.getInstructions()
                        );

                        startActivity(intent);
                    }
                }
        );

        recyclerViewRecipes.setAdapter(recipeAdapter);

        loadSuggestedRecipes();

        buttonBackToPantry.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void loadSuggestedRecipes() {

        recipeList.clear();

        List<Recipe> suggestedRecipes =
                databaseHelper.getSuggestedRecipes();

        recipeList.addAll(suggestedRecipes);

        recipeAdapter.notifyDataSetChanged();

        if (recipeList.isEmpty()) {

            textNoRecipes.setVisibility(View.VISIBLE);
            recyclerViewRecipes.setVisibility(View.GONE);

        } else {

            textNoRecipes.setVisibility(View.GONE);
            recyclerViewRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && recipeAdapter != null) {
            loadSuggestedRecipes();
        }
    }
}