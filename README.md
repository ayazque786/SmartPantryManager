# Smart Pantry Manager

## About the App

Smart Pantry Manager is an Android application designed to help users keep track of ingredients they currently have at home.

The application allows users to add, edit and delete pantry ingredients, including their quantity, unit of measurement and expiry date. It also suggests recipes that can be made using the ingredients currently available in the pantry.

The app includes a settings screen where users can manage their pantry notification preference.

## Main Features

- Add new pantry ingredients
- Edit existing ingredients
- Delete ingredients
- Store ingredient quantities and units
- Store optional expiry dates
- Prevent selection of past expiry dates
- View all saved pantry ingredients
- Suggest recipes based on available pantry ingredients
- View recipe ingredients and cooking instructions
- Settings screen for expiry notification preference
- Toolbar navigation menu for Suggested Recipes and Settings
- Input validation for ingredient information

## Database

The application uses SQLite for local data storage.

SQLite was chosen because it provides a lightweight relational database that works directly on Android devices without requiring an internet connection or external database server.

The database is used to store pantry ingredients, recipes and recipe ingredients. It also supports the CRUD operations required by the application:

- Create - Add new pantry ingredients
- Read - Display stored ingredients and recipe information
- Update - Edit existing pantry ingredients
- Delete - Remove pantry ingredients

## Technologies Used

- Java
- Android Studio
- XML
- SQLite
- RecyclerView
- Git
- GitHub

## How to Run the Application

1. Clone or download the project from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the SmartPantryManager project folder.
4. Allow Android Studio to complete the Gradle sync.
5. Start an Android emulator or connect an Android device with USB debugging enabled.
6. Select the device in Android Studio.
7. Click the **Run** button.
8. The Smart Pantry Manager application will launch on the selected device.

## Using the Application

### Adding an Ingredient

1. Open the Smart Pantry Manager application.
2. Tap **Add Ingredient**.
3. Enter the ingredient name.
4. Enter the quantity.
5. Select the appropriate unit.
6. Select an expiry date if required.
7. Tap the save button.

### Editing an Ingredient

1. Find the ingredient on the My Pantry screen.
2. Tap **Edit**.
3. Change the required information.
4. Tap **Update Ingredient**.

### Deleting an Ingredient

Tap **Delete** next to the ingredient you want to remove.

### Suggested Recipes

Tap **Suggested Recipes** or use the toolbar menu.

The application compares the ingredients in the pantry with the ingredients required by the stored recipes. Recipes that can be made using the available pantry ingredients are displayed.

Tap **View Recipe** to see the required ingredients and cooking instructions.

### Settings

Open **Settings** from the My Pantry screen or from the toolbar menu.

The expiry notification preference can be switched on or off and the selected preference is saved by the application.

## Project Structure

The project separates the application into model classes, activities, adapters, XML layouts and database functionality.

Important components include:

- `MainActivity` - Displays pantry ingredients and main navigation
- `AddEditIngredientActivity` - Adds and edits ingredients
- `SuggestedRecipesActivity` - Displays available recipe suggestions
- `RecipeDetailActivity` - Displays recipe details
- `SettingsActivity` - Manages application preferences
- `DatabaseHelper` - Handles the SQLite database and database operations
- `IngredientAdapter` - Displays pantry ingredients using RecyclerView
- `RecipeAdapter` - Displays suggested recipes using RecyclerView
- `Ingredient` - Pantry ingredient model
- `Recipe` - Recipe model
- `RecipeIngredient` - Recipe ingredient model

## Version Control

Git and GitHub were used throughout the development of the project.

Development was completed incrementally with separate commits for major features such as database functionality, ingredient management, recipe suggestions, settings, navigation and validation.

## Developer

Mohamed Ayaz Qureshi