package com.example.gparmar.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.gparmar.bakingapp.adapter.RecipeListAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecipeListActivity extends AppCompatActivity {
    private static final String TAG = "RecipeListActivity";
    private static final String STATE_INFO_RECIPES = "STATE_INFO_RECIPES";

    @BindView(R.id.card_list)
    RecyclerView mList;
    private ArrayList<Recipe> recipes;
    private RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle(getString(R.string.recipes));

        ButterKnife.bind(this);
        mAdapter = new RecipeListAdapter(this, null);
        mList.setAdapter(mAdapter);

        GridLayoutManager layoutManager = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 1);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }
        mList.setLayoutManager(layoutManager);

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
                                int count = 0;
                                for (Step step : recipe.getSteps()) {
                                    ContentValues stepCV = new ContentValues();
                                    stepCV.put(StepTable.DESCRIPTION, step.getDescription());
                                    stepCV.put(StepTable.RECIPE_ID, recipeId);
                                    stepCV.put(StepTable.SHORT_DESCRIPTION, step.getShortDescription());
                                    stepCV.put(StepTable.VIDEO_URL, step.getVideoURL());
                                    stepCV.put(StepTable.THUMBNAIL_URL, step.getThumbnailURL());
                                    stepCV.put(StepTable.STEP_NUMBER, count++);
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


    private void queryRecipes() {
        Cursor c = getContentResolver().query(BakingProvider.Recipe.CONTENT_URI,
                null, null, null, null);
        if (mAdapter == null) {
            mAdapter = new RecipeListAdapter(this, null);
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.setCursor(c);
        }
    }


}
