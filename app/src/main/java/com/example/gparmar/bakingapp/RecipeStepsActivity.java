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
        implements StepsClickListener,
        StepDetailFragment.OnFragmentInteractionListener,
        VideoPositionListener {
    private static final String TAG = "RecipeStepsActivity";
    private static final String ARG_POSITION = "position";
    private static final String ARG_STEP = "step";
    private static final String ARG_VIDEO_POSITION = "video_position";
    private int mRecipeId = -1;
    private int mStepClicked = -1;
    private long mVideoPosition = -1;
    private Step mSelectedStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_steps);

        mRecipeId = getIntent().getIntExtra(Constants.PROPERTY_RECIPE_ID, -1);
        String recipeName =
                getIntent().getStringExtra(Constants.PROPERTY_RECIPE_NAME);

        Fragment stepsFragment = RecipeStepsFragment.newInstance(mRecipeId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.steps_fragment, stepsFragment)
                .commit();

        setTitle(recipeName + getString(R.string.recipe_suffix));

        //Make ingredients fragment visible when it is landscape on tablet
        View view = findViewById(R.id.step_detail_fragment);
        if (view != null) {
            Fragment ingredientsFragment = RecipeIngredientsFragment.newInstance(mRecipeId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.step_detail_fragment, ingredientsFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        View view = findViewById(R.id.step_detail_fragment);
        //If view is not null then it is a tablet in landscape mode.
        //Else it is in portrait mode or on a phone
        if (view == null) {
            //Now find out if the step detail fragment is displayed.
            //If so, then replace it with the steps fragment.
            //Otherwise, do the regular super.onBackPressed
            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.steps_fragment);
            if (fragment instanceof StepDetailFragment ||
                    fragment instanceof RecipeIngredientsFragment) {
                RecipeStepsFragment recipeStepsFragment =
                        RecipeStepsFragment.newInstance(mRecipeId, mStepClicked);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.steps_fragment, recipeStepsFragment)
                        .commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStepClicked(int position, Step step, boolean updateClickedPositionInFragment) {
        mStepClicked = position;
        mSelectedStep = step;
        View view = findViewById(R.id.step_detail_fragment);
        //If view is not null then it is a tablet in landscape mode.
        if (view == null) {
            if (position == 0) {
                Fragment fragment = RecipeIngredientsFragment.newInstance(step.getRecipeId());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.steps_fragment, fragment)
                        .commit();
            } else {
                Fragment fragment = StepDetailFragment.newInstance(step, position - 1, mVideoPosition);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.steps_fragment, fragment)
                        .commit();
            }
        } else {
            if (updateClickedPositionInFragment) {
                setClickedPosition(position);
            }
            if (position == 0) {
                Fragment fragment = RecipeIngredientsFragment.newInstance(step.getRecipeId());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_detail_fragment, fragment)
                        .commit();
            } else {
                Fragment fragment = StepDetailFragment.newInstance(step, position - 1, mVideoPosition);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_detail_fragment, fragment)
                        .commit();
            }
        }
    }

    private void setClickedPosition(int position) {
        RecipeStepsFragment recipeStepsFragment
                = (RecipeStepsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.steps_fragment);
        recipeStepsFragment.setPositionClicked(position);
    }

    @Override
    public void setNewTitle(String title) {
        //This can be ignored as in a master-detail view we don't want the title to change.
        //The title change method is only for phone where master-detail view is not present.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mStepClicked != -1) {
            outState.putInt(ARG_POSITION, mStepClicked);
            outState.putParcelable(ARG_STEP, mSelectedStep);
            outState.putLong(ARG_VIDEO_POSITION, mVideoPosition);
            //Find out if the steps fragment is currently shown
            //or the step details fragment.
            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.steps_fragment);
            boolean detailShowing = false;
            if (fragment instanceof RecipeStepsFragment) {
                outState.putBoolean(Constants.STEP_FRAGMENT_SHOWING, true);
            } else {
                outState.putBoolean(Constants.STEP_FRAGMENT_SHOWING, false);
                outState.putBoolean(Constants.STEP_DETAIL_FRAGMENT_SHOWING, true);
                detailShowing = true;
            }
            if (!detailShowing) {
                fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.step_detail_fragment);
                if (fragment instanceof StepDetailFragment ||
                        fragment instanceof RecipeIngredientsFragment) {
                    outState.putBoolean(Constants.STEP_DETAIL_FRAGMENT_SHOWING, true);
                } else {
                    outState.putBoolean(Constants.STEP_DETAIL_FRAGMENT_SHOWING, false);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepClicked = savedInstanceState.getInt(ARG_POSITION);
            mSelectedStep = (Step) savedInstanceState.getParcelable(ARG_STEP);
            mVideoPosition = savedInstanceState.getLong(ARG_VIDEO_POSITION);
            boolean stepDetailFragmentWasShowing = savedInstanceState.getBoolean(Constants.STEP_DETAIL_FRAGMENT_SHOWING);
            boolean stepFragmentWasShowing = savedInstanceState.getBoolean(Constants.STEP_FRAGMENT_SHOWING);;
            boolean stepDetailFragmentShowing = false;
            boolean stepFragmentShowing = false;
            //Check if step detail fragment is present
            Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.step_detail_fragment);
            if (fragment instanceof StepDetailFragment ||
                    fragment instanceof RecipeIngredientsFragment) {
                stepDetailFragmentShowing = true;
            }

            //Check if steps fragment is present
            fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.steps_fragment);
            if (fragment instanceof RecipeStepsFragment) {
                stepFragmentShowing = true;
            }

            boolean showCorrectStep = false;
            boolean showDetail = false;
            if (stepFragmentWasShowing && !stepDetailFragmentWasShowing) {
                if (stepFragmentShowing && stepDetailFragmentShowing) {
                    showCorrectStep = true;
                    showDetail = true;
                } else {
                    showCorrectStep = true;
                }
            }
            if (!stepFragmentWasShowing && stepDetailFragmentWasShowing) {
                if (stepFragmentShowing && stepDetailFragmentShowing) {
                    showCorrectStep = true;
                    showDetail = true;
                } else {
                    showDetail = true;
                }
            }
            if (stepFragmentWasShowing && stepDetailFragmentWasShowing) {
                if (stepFragmentShowing && !stepDetailFragmentShowing) {
                    showCorrectStep = true;
                } else if (!stepFragmentShowing && stepDetailFragmentShowing) {
                    showDetail = true;
                } else {
                    showCorrectStep = true;
                }
            }
            if (showDetail) {
                onStepClicked(mStepClicked, mSelectedStep, true);
            }
            if (showCorrectStep) {
                //Restore the clicked position
                RecipeStepsFragment recipeStepsFragment = (RecipeStepsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.steps_fragment);
                recipeStepsFragment.setPositionClicked(mStepClicked);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mStepClicked != -1) {
//            setClickedPosition(mStepClicked);
//        }
    }

    @Override
    public void setVideoPosition(long videoPosition) {
        mVideoPosition = videoPosition;
    }
}
