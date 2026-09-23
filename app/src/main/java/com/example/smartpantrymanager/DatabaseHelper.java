package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiry_date";

    // Recipe table
    private static final String TABLE_RECIPES = "recipes";

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_DESCRIPTION = "description";
    private static final String RECIPE_INSTRUCTIONS = "instructions";

    // Recipe ingredient table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    private static final String RI_ID = "id";
    private static final String RI_RECIPE_ID = "recipe_id";
    private static final String RI_INGREDIENT_NAME = "ingredient_name";
    private static final String RI_QUANTITY = "quantity";
    private static final String RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);
        createRecipeTables(db);
        seedRecipes(db);
    }

    private void createPantryTable(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY + " TEXT)";

        db.execSQL(createPantryTable);
    }

    private void createRecipeTables(SQLiteDatabase db) {

        String createRecipeTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RECIPE_NAME + " TEXT NOT NULL, " +
                        RECIPE_DESCRIPTION + " TEXT NOT NULL, " +
                        RECIPE_INSTRUCTIONS + " TEXT NOT NULL)";

        db.execSQL(createRecipeTable);

        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RI_RECIPE_ID + " INTEGER NOT NULL, " +
                        RI_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        RI_QUANTITY + " REAL NOT NULL, " +
                        RI_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + RI_RECIPE_ID + ") REFERENCES " +
                        TABLE_RECIPES + "(" + RECIPE_ID + "))";

        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            createRecipeTables(db);
            seedRecipes(db);
        }
    }

    // ---------------------------------------------------------
    // PANTRY CRUD
    // ---------------------------------------------------------

    // CREATE
    public long addIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ingredient.getName());
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY, ingredient.getExpiryDate());

        return db.insert(TABLE_PANTRY, null, values);
    }

    // READ
    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY)
                );

                Ingredient ingredient = new Ingredient(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    // UPDATE
    public int updateIngredient(Ingredient ingredient) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, ingredient.getName());
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY, ingredient.getExpiryDate());

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(ingredient.getId())}
        );
    }

    // DELETE
    public int deleteIngredient(int id) {

        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // ---------------------------------------------------------
    // RECIPE METHODS
    // ---------------------------------------------------------

    private long addRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String instructions) {

        ContentValues values = new ContentValues();

        values.put(RECIPE_NAME, name);
        values.put(RECIPE_DESCRIPTION, description);
        values.put(RECIPE_INSTRUCTIONS, instructions);

        return db.insert(TABLE_RECIPES, null, values);
    }

    private void addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit) {

        ContentValues values = new ContentValues();

        values.put(RI_RECIPE_ID, recipeId);
        values.put(RI_INGREDIENT_NAME, ingredientName);
        values.put(RI_QUANTITY, quantity);
        values.put(RI_UNIT, unit);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    // Get all recipes
    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                RECIPE_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(RECIPE_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(RECIPE_NAME)
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(RECIPE_DESCRIPTION)
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(RECIPE_INSTRUCTIONS)
                );

                Recipe recipe = new Recipe(
                        id,
                        name,
                        description,
                        instructions
                );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return recipes;
    }

    // Get ingredients required by a recipe
    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {

        List<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(RI_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(RI_INGREDIENT_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(RI_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(RI_UNIT)
                );

                RecipeIngredient ingredient =
                        new RecipeIngredient(
                                id,
                                recipeId,
                                name,
                                quantity,
                                unit
                        );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    // ---------------------------------------------------------
    // PRELOADED RECIPES
    // ---------------------------------------------------------

    private void seedRecipes(SQLiteDatabase db) {

        // 1. Scrambled Eggs
        long recipe1 = addRecipe(
                db,
                "Scrambled Eggs",
                "Quick and simple scrambled eggs.",
                "Beat the eggs with milk. Heat a pan and cook until softly set."
        );

        addRecipeIngredient(db, recipe1, "egg", 2, "pcs");
        addRecipeIngredient(db, recipe1, "milk", 50, "ml");

        // 2. Cheese Omelette
        long recipe2 = addRecipe(
                db,
                "Cheese Omelette",
                "A simple cheese-filled omelette.",
                "Beat the eggs, cook in a pan, add cheese and fold the omelette."
        );

        addRecipeIngredient(db, recipe2, "egg", 2, "pcs");
        addRecipeIngredient(db, recipe2, "cheese", 50, "g");

        // 3. Tomato Toast
        long recipe3 = addRecipe(
                db,
                "Tomato Toast",
                "Fresh tomato served on toasted bread.",
                "Toast the bread, slice the tomato and place it on top."
        );

        addRecipeIngredient(db, recipe3, "bread", 2, "slices");
        addRecipeIngredient(db, recipe3, "tomato", 1, "pcs");

        // 4. Cheese Toast
        long recipe4 = addRecipe(
                db,
                "Cheese Toast",
                "Warm toasted bread topped with cheese.",
                "Place cheese on bread and toast until the cheese melts."
        );

        addRecipeIngredient(db, recipe4, "bread", 2, "slices");
        addRecipeIngredient(db, recipe4, "cheese", 50, "g");

        // 5. Banana Milkshake
        long recipe5 = addRecipe(
                db,
                "Banana Milkshake",
                "A quick banana and milk drink.",
                "Add banana and milk to a blender and blend until smooth."
        );

        addRecipeIngredient(db, recipe5, "banana", 1, "pcs");
        addRecipeIngredient(db, recipe5, "milk", 250, "ml");

        // 6. Tomato and Egg Scramble
        long recipe6 = addRecipe(
                db,
                "Tomato and Egg Scramble",
                "Eggs scrambled with fresh tomato.",
                "Chop the tomato, cook briefly, add beaten eggs and scramble."
        );

        addRecipeIngredient(db, recipe6, "egg", 2, "pcs");
        addRecipeIngredient(db, recipe6, "tomato", 1, "pcs");

        // 7. Chicken and Rice
        long recipe7 = addRecipe(
                db,
                "Chicken and Rice",
                "A simple chicken and rice meal.",
                "Cook the rice. Cook the chicken thoroughly and serve together."
        );

        addRecipeIngredient(db, recipe7, "chicken", 200, "g");
        addRecipeIngredient(db, recipe7, "rice", 100, "g");

        // 8. Tuna Sandwich
        long recipe8 = addRecipe(
                db,
                "Tuna Sandwich",
                "A quick tuna sandwich.",
                "Place tuna between the bread slices and serve."
        );

        addRecipeIngredient(db, recipe8, "tuna", 100, "g");
        addRecipeIngredient(db, recipe8, "bread", 2, "slices");

        // 9. Cheese Sandwich
        long recipe9 = addRecipe(
                db,
                "Cheese Sandwich",
                "A basic cheese sandwich.",
                "Place the cheese between two slices of bread."
        );

        addRecipeIngredient(db, recipe9, "cheese", 50, "g");
        addRecipeIngredient(db, recipe9, "bread", 2, "slices");

        // 10. Banana Toast
        long recipe10 = addRecipe(
                db,
                "Banana Toast",
                "Toast topped with sliced banana.",
                "Toast the bread, slice the banana and place it on top."
        );

        addRecipeIngredient(db, recipe10, "bread", 2, "slices");
        addRecipeIngredient(db, recipe10, "banana", 1, "pcs");

        // 11. Egg Sandwich
        long recipe11 = addRecipe(
                db,
                "Egg Sandwich",
                "A simple cooked egg sandwich.",
                "Cook the eggs and place them between the bread slices."
        );

        addRecipeIngredient(db, recipe11, "egg", 2, "pcs");
        addRecipeIngredient(db, recipe11, "bread", 2, "slices");

        // 12. Tomato Rice
        long recipe12 = addRecipe(
                db,
                "Tomato Rice",
                "Rice cooked with tomato.",
                "Cook the rice. Add chopped tomato and cook together briefly."
        );

        addRecipeIngredient(db, recipe12, "rice", 100, "g");
        addRecipeIngredient(db, recipe12, "tomato", 1, "pcs");

        // 13. Chicken Sandwich
        long recipe13 = addRecipe(
                db,
                "Chicken Sandwich",
                "A simple chicken sandwich.",
                "Cook the chicken thoroughly, slice it and place it between bread."
        );

        addRecipeIngredient(db, recipe13, "chicken", 150, "g");
        addRecipeIngredient(db, recipe13, "bread", 2, "slices");

        // 14. Cheesy Rice
        long recipe14 = addRecipe(
                db,
                "Cheesy Rice",
                "Warm rice mixed with cheese.",
                "Cook the rice and stir in cheese while the rice is still hot."
        );

        addRecipeIngredient(db, recipe14, "rice", 100, "g");
        addRecipeIngredient(db, recipe14, "cheese", 50, "g");

        // 15. Banana Yoghurt Bowl
        long recipe15 = addRecipe(
                db,
                "Banana Yoghurt Bowl",
                "A simple banana and yoghurt snack.",
                "Slice the banana and mix it with the yoghurt."
        );

        addRecipeIngredient(db, recipe15, "banana", 1, "pcs");
        addRecipeIngredient(db, recipe15, "yoghurt", 150, "g");

        // 16. Tomato Cheese Sandwich
        long recipe16 = addRecipe(
                db,
                "Tomato Cheese Sandwich",
                "A sandwich with tomato and cheese.",
                "Slice the tomato and place it with cheese between the bread."
        );

        addRecipeIngredient(db, recipe16, "bread", 2, "slices");
        addRecipeIngredient(db, recipe16, "tomato", 1, "pcs");
        addRecipeIngredient(db, recipe16, "cheese", 50, "g");

        // 17. Chicken Tomato Rice
        long recipe17 = addRecipe(
                db,
                "Chicken Tomato Rice",
                "Chicken and rice served with tomato.",
                "Cook the rice and chicken. Add chopped tomato and combine."
        );

        addRecipeIngredient(db, recipe17, "chicken", 200, "g");
        addRecipeIngredient(db, recipe17, "rice", 100, "g");
        addRecipeIngredient(db, recipe17, "tomato", 1, "pcs");

        // 18. Egg and Cheese Toast
        long recipe18 = addRecipe(
                db,
                "Egg and Cheese Toast",
                "Toast topped with egg and cheese.",
                "Toast the bread, cook the egg and add egg and cheese on top."
        );

        addRecipeIngredient(db, recipe18, "bread", 2, "slices");
        addRecipeIngredient(db, recipe18, "egg", 1, "pcs");
        addRecipeIngredient(db, recipe18, "cheese", 30, "g");
    }

    // ---------------------------------------------------------
    // STRICT RECIPE MATCHING
    // ---------------------------------------------------------

    // Returns only recipes where ALL required ingredients
    // are available in sufficient quantities.
    public List<Recipe> getSuggestedRecipes() {

        List<Recipe> suggestedRecipes = new ArrayList<>();
        List<Recipe> allRecipes = getAllRecipes();
        List<Ingredient> pantryIngredients = getAllIngredients();

        for (Recipe recipe : allRecipes) {

            List<RecipeIngredient> requiredIngredients =
                    getRecipeIngredients(recipe.getId());

            boolean canMakeRecipe = true;

            for (RecipeIngredient required : requiredIngredients) {

                boolean ingredientFound = false;

                for (Ingredient pantry : pantryIngredients) {

                    if (normaliseIngredientName(pantry.getName())
                            .equals(normaliseIngredientName(
                                    required.getIngredientName()))) {

                        double pantryQuantity =
                                convertToBaseUnit(
                                        pantry.getQuantity(),
                                        pantry.getUnit()
                                );

                        double requiredQuantity =
                                convertToBaseUnit(
                                        required.getQuantity(),
                                        required.getUnit()
                                );

                        if (unitsCompatible(
                                pantry.getUnit(),
                                required.getUnit())
                                && pantryQuantity >= requiredQuantity) {

                            ingredientFound = true;
                            break;
                        }
                    }
                }

                // If even one required ingredient is missing,
                // do not suggest the recipe.
                if (!ingredientFound) {
                    canMakeRecipe = false;
                    break;
                }
            }

            if (canMakeRecipe && !requiredIngredients.isEmpty()) {
                suggestedRecipes.add(recipe);
            }
        }

        return suggestedRecipes;
    }

    // Makes simple ingredient names more consistent.
    // Example: "Tomatoes" and "tomato" can match.
    private String normaliseIngredientName(String name) {

        if (name == null) {
            return "";
        }

        String result = name
                .trim()
                .toLowerCase();

        if (result.equals("tomatoes")) {
            return "tomato";
        }

        if (result.equals("potatoes")) {
            return "potato";
        }

        if (result.equals("eggs")) {
            return "egg";
        }

        if (result.endsWith("s")
                && !result.endsWith("ss")
                && result.length() > 3) {

            result = result.substring(
                    0,
                    result.length() - 1
            );
        }

        return result;
    }

    // Checks whether the pantry unit and recipe unit
    // represent the same type of measurement.
    private boolean unitsCompatible(
            String pantryUnit,
            String requiredUnit
    ) {

        String first = normaliseUnit(pantryUnit);
        String second = normaliseUnit(requiredUnit);

        if (first.equals(second)) {
            return true;
        }

        boolean firstWeight =
                first.equals("g") || first.equals("kg");

        boolean secondWeight =
                second.equals("g") || second.equals("kg");

        if (firstWeight && secondWeight) {
            return true;
        }

        boolean firstVolume =
                first.equals("ml") || first.equals("l");

        boolean secondVolume =
                second.equals("ml") || second.equals("l");

        return firstVolume && secondVolume;
    }

    // Converts compatible measurements to a common base unit.
    // kg -> g
    // L -> ml
    private double convertToBaseUnit(
            double quantity,
            String unit
    ) {

        String normalisedUnit = normaliseUnit(unit);

        if (normalisedUnit.equals("kg")) {
            return quantity * 1000;
        }

        if (normalisedUnit.equals("l")) {
            return quantity * 1000;
        }

        return quantity;
    }

    // Normalises units used by pantry items and recipes.
    // Pantry "items" can match recipe "pcs" or "slices".
    private String normaliseUnit(String unit) {

        if (unit == null) {
            return "";
        }

        String result = unit
                .trim()
                .toLowerCase();

        if (result.equals("item")
                || result.equals("items")
                || result.equals("piece")
                || result.equals("pieces")
                || result.equals("pc")
                || result.equals("pcs")
                || result.equals("slice")
                || result.equals("slices")) {

            return "items";
        }

        return result;
    }
}