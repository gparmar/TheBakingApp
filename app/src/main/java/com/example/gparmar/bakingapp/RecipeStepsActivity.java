package com.example.gparmar.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.Constants;

import butterknife.ButterKnife;

/**
 * Created by gparmar on 13/06/17.
 */

public class RecipeStepsActivity extends AppCompatActivity
        implements StepsClickListener, StepDetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "RecipeStepsActivity";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_steps);

        int recipeId = getIntent().getIntExtra(Constants.PROPERTY_RECIPE_ID, -1);
        String recipeName =
                getIntent().getStringExtra(Constants.PROPERTY_RECIPE_NAME);

        RecipeStepsFragment fragment
                = (RecipeStepsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.steps_fragment);

        Log.d(TAG, "Setting recipe id");
        fragment.setRecipeId(recipeId);

        setTitle(recipeName + getString(R.string.recipe_suffix));

        //Make ingredients fragment visible when it is landscape on tablet
        View view = findViewById(R.id.step_detail_fragment);
        if (view != null) {
            Fragment ingredientsFragment = RecipeIngredientsFragment.newInstance(recipeId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.step_detail_fragment, ingredientsFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStepClicked(int position, Step step) {
        View view = findViewById(R.id.step_detail_fragment);
        //If view is not null then it is a tablet in landscape mode.
        if (view == null) {
            if (position == 0) {
                Intent intent = new Intent(RecipeStepsActivity.this, RecipeIngredientsActivity.class);
                intent.putExtra(Constants.PROPERTY_RECIPE_ID, step.getRecipeId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(RecipeStepsActivity.this, StepDetailActivity.class);
                intent.putExtra(Constants.PROPERTY_STEP, step);
                intent.putExtra(Constants.PROPERTY_STEP_POSITION, position - 1);
                startActivity(intent);
            }
        } else {
            if (position == 0) {
                Fragment fragment = RecipeIngredientsFragment.newInstance(step.getRecipeId());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_detail_fragment, fragment)
                        .commit();
            } else {
                Fragment fragment = StepDetailFragment.newInstance(step, position - 1);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_detail_fragment, fragment)
                        .commit();
            }
        }
    }

    @Override
    public void setNewTitle(String title) {
        //This can be ignored as in a master-detail view we don't want the title to change.
        //The title change method is only for phone where master-detail view is not present.
    }
}
