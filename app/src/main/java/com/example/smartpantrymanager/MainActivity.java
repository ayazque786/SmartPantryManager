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
                        // Edit functionality will be connected next.
                    }

                    @Override
                    public void onDelete(Ingredient ingredient) {
                        databaseHelper.deleteIngredient(ingredient.getId());
                        loadIngredients();
                    }
                }
        );

        recyclerViewIngredients.setAdapter(ingredientAdapter);

        loadIngredients();

        buttonAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });
    }

    private void loadIngredients() {

        ingredientList.clear();
        ingredientList.addAll(databaseHelper.getAllIngredients());

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

        if (databaseHelper != null && ingredientAdapter != null) {
            loadIngredients();
        }
    }
}