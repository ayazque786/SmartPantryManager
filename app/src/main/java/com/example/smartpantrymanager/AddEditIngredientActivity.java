package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextExpiryDate;
    private Spinner spinnerUnit;
    private Button buttonSaveIngredient;
    private TextView textFormTitle;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonSaveIngredient = findViewById(R.id.buttonSaveIngredient);
        textFormTitle = findViewById(R.id.textFormTitle);

        databaseHelper = new DatabaseHelper(this);

        setupUnitSpinner();

        editTextExpiryDate.setOnClickListener(v -> showDatePicker());

        buttonSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void setupUnitSpinner() {
        String[] units = {
                "items",
                "g",
                "kg",
                "ml",
                "L",
                "cups",
                "tbsp",
                "tsp"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" +
                            (selectedMonth + 1) + "/" +
                            selectedYear;

                    editTextExpiryDate.setText(date);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void saveIngredient() {

        String name = editTextName.getText().toString().trim();
        String quantityText = editTextQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Please enter an ingredient name");
            editTextName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            editTextQuantity.setError("Please enter a quantity");
            editTextQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Please enter a valid quantity");
            return;
        }

        if (quantity <= 0) {
            editTextQuantity.setError("Quantity must be greater than 0");
            return;
        }

        Ingredient ingredient = new Ingredient(
                0,
                name,
                quantity,
                unit,
                expiryDate
        );

        long result = databaseHelper.addIngredient(ingredient);

        if (result != -1) {
            Toast.makeText(
                    this,
                    "Ingredient added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "Unable to add ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}