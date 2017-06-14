package com.example.gparmar.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 13/06/17.
 */

public class RecipeIngredientsActivity extends AppCompatActivity {
    private static final String TAG = "RecipeIngsActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_ingredients);

        int recipeId = getIntent().getIntExtra(Constants.PROPERTY_RECIPE_ID, -1);

        RecipeIngredientsFragment fragment
                = (RecipeIngredientsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ingredients_fragment);

        Log.d(TAG, "Setting recipe id");
        fragment.setRecipeId(recipeId);

        setTitle(getString(R.string.rcp_ingredients_title));
    }
}
