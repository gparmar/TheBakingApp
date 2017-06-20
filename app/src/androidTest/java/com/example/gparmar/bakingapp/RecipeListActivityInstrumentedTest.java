package com.example.gparmar.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static com.example.gparmar.bakingapp.TestUtils.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gparmar on 20/06/17.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityInstrumentedTest {
    private static final String EXPECTED_STEPS_ACTIVITY_TITLE = "Nutella Pie Recipe";
    private static final String EXPECTED_INGREDIENT_NAME = "Graham Cracker crumbs";
    private static final String EXPECTED_STEP_DESCRIPTION = "Recipe Introduction";
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule =
            new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class);

    /**
     * This test case tests the following scenario:
     * Clicking on a recipe takes the user to the RecipeStepsActivity
     * where the title of the activity is EXPECTED_STEPS_ACTIVITY_TITLE
     */
    @Test
    public void clickRecipe(){
        onView(withId(R.id.card_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(EXPECTED_STEPS_ACTIVITY_TITLE)));
    }

    /**
     * This test case tests the navigation to the ingredients screen
     * and it checks if the ingredient at the position 0 is what it should be.
     */
    @Test
    public void clickIngredients(){
        onView(withId(R.id.card_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.steps_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withRecyclerView(R.id.ingredients_list).atPositionOnView(0, R.id.ing_name))
                .check(matches(withText(EXPECTED_INGREDIENT_NAME)));
    }

    /**
     * This test case tests the navigation to the Step Detail screen
     * and it checks if the description at the position 1 is what it should be.
     */
    @Test
    public void navigateToSteps(){
        onView(withId(R.id.card_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.steps_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.description))
                .check(matches(withText(EXPECTED_STEP_DESCRIPTION)));
    }
}
