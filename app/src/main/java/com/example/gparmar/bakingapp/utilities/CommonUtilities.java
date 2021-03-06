package com.example.gparmar.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.example.gparmar.bakingapp.data.IngredientTable;
import com.example.gparmar.bakingapp.data.RecipeTable;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Ingredient;
import com.example.gparmar.bakingapp.model.Recipe;
import com.example.gparmar.bakingapp.model.Step;

/**
 * Created by gparmar on 11/06/17.
 */

public class CommonUtilities {
    public static String TAG = "CommonUtilities";
    public static boolean isEmpty(String s){
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }

    public static void putSharedPref(Context context, String name, Object object) {
        SharedPreferences prefs = context.getSharedPreferences("TheBakingApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, object.toString());
        editor.apply();
    }

    public static String getSharedPref(Context context, String name, String defaultVal) {
        SharedPreferences prefs = context.getSharedPreferences("TheBakingApp", Context.MODE_PRIVATE);
        return prefs.getString(name, defaultVal);
    }

    public static Step getStepFromCursor(Cursor cursor){
        Step step = new Step();
        step.setDescription(cursor.getString(cursor.getColumnIndex(StepTable.DESCRIPTION)));
        step.setShortDescription(cursor.getString(cursor.getColumnIndex(StepTable.SHORT_DESCRIPTION)));
        step.setThumbnailURL(cursor.getString(cursor.getColumnIndex(StepTable.THUMBNAIL_URL)));
        step.setVideoURL(cursor.getString(cursor.getColumnIndex(StepTable.VIDEO_URL)));
        step.setId(cursor.getInt(cursor.getColumnIndex(StepTable._ID)));
        step.setRecipeId(cursor.getInt(cursor.getColumnIndex(StepTable.RECIPE_ID)));
        step.setStepNumber(cursor.getInt(cursor.getColumnIndex(StepTable.STEP_NUMBER)));
        return step;
    }

    public static Recipe getRecipeFromCursor(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getInt(cursor.getColumnIndex(RecipeTable._ID)));
        recipe.setImage(cursor.getString(cursor.getColumnIndex(RecipeTable.IMAGE)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(RecipeTable.NAME)));
        recipe.setServings(cursor.getInt(cursor.getColumnIndex(RecipeTable.SERVINGS)));
        Log.d(TAG, "Returning recipe:"+recipe);
        return recipe;
    }

    public static Ingredient getIngredientFromCursor(Cursor cursor) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(IngredientTable.INGREDIENT)));
        ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(IngredientTable.MEASURE)));
        ingredient.setQuantity(cursor.getInt(cursor.getColumnIndex(IngredientTable.QUANTITY)));

        return ingredient;
    }
}
