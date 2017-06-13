package com.example.gparmar.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gparmar.bakingapp.data.BakingDatabase;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.IngredientTable;
import com.example.gparmar.bakingapp.data.RecipeTable;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Ingredient;
import com.example.gparmar.bakingapp.model.Recipe;
import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.service.BakingDetailsService;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;
import com.example.gparmar.bakingapp.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {
    private static final String TAG = "RecipeListActivity";
    private static final String STATE_INFO_RECIPES = "STATE_INFO_RECIPES";

    private LinearLayout cardContainer;
    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle(getString(R.string.recipes));

        cardContainer = (LinearLayout) findViewById(R.id.card_container);

        if (savedInstanceState != null
                && savedInstanceState.getParcelableArrayList(STATE_INFO_RECIPES) != null) {
            recipes = savedInstanceState.getParcelableArrayList(STATE_INFO_RECIPES);
            renderRecipes(recipes);
        } else {

            boolean recipesDownloaded =
                    Boolean.parseBoolean(CommonUtilities.getSharedPref(this,
                            Constants.PROPERTY_RECIPES_DOWNLOADED, "false"));
            if (!recipesDownloaded) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.RECIPES_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                BakingDetailsService service = retrofit.create(BakingDetailsService.class);
                service.listOfRecipes().enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> recipes) {
                        if (recipes != null && recipes.body() != null) {
                            for (Recipe recipe : recipes.body()) {
                                Log.d(TAG, "Recipe:" + recipe);
                                ContentValues recipeCV = new ContentValues();
                                recipeCV.put(RecipeTable.NAME, recipe.getName());
                                recipeCV.put(RecipeTable.IMAGE, recipe.getImage());
                                recipeCV.put(RecipeTable.SERVINGS, recipe.getServings());
                                Uri dbUri =
                                        getContentResolver().insert(BakingProvider
                                                .Recipe.CONTENT_URI, recipeCV);
                                int recipeId = Integer.parseInt(dbUri.getLastPathSegment());
                                Log.d(TAG, "dbUri:" + dbUri.toString() + ", recipeId:" + recipeId);

                                if (recipe.getIngredients() != null) {
                                    for (Ingredient ingt : recipe.getIngredients()) {
                                        ContentValues ingCV = new ContentValues();
                                        ingCV.put(IngredientTable.INGREDIENT, ingt.getIngredient());
                                        ingCV.put(IngredientTable.MEASURE, ingt.getMeasure());
                                        ingCV.put(IngredientTable.QUANTITY, ingt.getQuantity());
                                        ingCV.put(IngredientTable.RECIPE_ID, recipeId);
                                        dbUri = RecipeListActivity.this
                                                .getContentResolver()
                                                .insert(BakingProvider.Ingredient.CONTENT_URI, ingCV);
                                        int ingId = Integer.parseInt(dbUri.getLastPathSegment());
                                        Log.d(TAG, "dbUri:" + dbUri.toString() + ", ingId:" + ingId);
                                    }
                                }
                                if (recipe.getSteps() != null) {
                                    for (Step step : recipe.getSteps()) {
                                        ContentValues stepCV = new ContentValues();
                                        stepCV.put(StepTable.DESCRIPTION, step.getDescription());
                                        stepCV.put(StepTable.RECIPE_ID, recipeId);
                                        stepCV.put(StepTable.SHORT_DESCRIPTION, step.getShortDescription());
                                        stepCV.put(StepTable.VIDEO_URL, step.getVideoURL());
                                        stepCV.put(StepTable.THUMBNAIL_URL, step.getThumbnailURL());
                                        stepCV.put(StepTable.STEP_NUMBER, step.getId());
                                        dbUri =
                                                getContentResolver().insert(BakingProvider.Step.CONTENT_URI, stepCV);
                                        int stepId = Integer.parseInt(dbUri.getLastPathSegment());
                                        Log.d(TAG, "dbUri:" + dbUri.toString() + ", stepId:" + stepId);
                                    }
                                }
                            }
                            CommonUtilities.putSharedPref(RecipeListActivity.this,
                                    Constants.PROPERTY_RECIPES_DOWNLOADED, "true");
                            queryRecipes();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Log.e(TAG, "Exception while querying the Baking Url", t);
                    }
                });
            } else {
                queryRecipes();
            }
        }
    }

    private void queryRecipes() {
        Cursor c = getContentResolver().query(BakingProvider.Recipe.CONTENT_URI,
                null, null, null, null);
        if (c != null && c.getCount() > 0) {
            recipes = new ArrayList<>();
            c.moveToFirst();
            do {
                final int recipeId = c.getInt(c.getColumnIndex(RecipeTable._ID));
                final String name = c.getString(c.getColumnIndex(RecipeTable.NAME));
                final String recipeImage = c.getString(c.getColumnIndex(RecipeTable.IMAGE));
                final int servings = c.getInt(c.getColumnIndex(RecipeTable.SERVINGS));

                recipes.add(new Recipe(recipeId, name, servings, recipeImage));

                c.moveToNext();
            } while (!c.isAfterLast());
            renderRecipes(recipes);
        }
    }

    private void renderRecipes(List<Recipe> rcps) {
        if (rcps != null && rcps.size() > 0) {
            cardContainer.removeAllViews();
            for (Recipe rcp : rcps) {
                View cardView = getLayoutInflater().inflate(R.layout.recipe_card, cardContainer, false);
                TextView recipeName = (TextView) cardView.findViewById(R.id.recipe_name);
                recipeName.setText(rcp.getName());
                cardContainer.addView(cardView);
                final Recipe recipe = rcp;
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent stepsIntent = new Intent(RecipeListActivity.this, RecipeStepsActivity.class);
                        stepsIntent.putExtra(Constants.PROPERTY_RECIPES_ID, recipe.getId());
                        startActivity(stepsIntent);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_INFO_RECIPES, recipes);
    }
}
