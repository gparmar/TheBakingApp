package com.example.gparmar.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 13/06/17.
 */

public class RecipeStepsActivity extends AppCompatActivity {
    private static final String TAG = "RecipeStepsActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_steps);

        int recipeId = getIntent().getIntExtra(Constants.PROPERTY_RECIPES_ID, -1);

        RecipeStepsFragment fragment
                = (RecipeStepsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.steps_fragment);

        Log.d(TAG, "Setting recipe id");
        fragment.setRecipeId(recipeId);
    }
}
