package com.example.gparmar.bakingapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by gparmar on 11/06/17.
 */
@ContentProvider(authority = BakingProvider.AUTHORITY, database = BakingDatabase.class)
public class BakingProvider {
    public static final String AUTHORITY = "com.example.gparmar.bakingapp.data.BakingProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String INGREDIENT = "ingredient";
        String RECIPE = "recipe";
        String STEP = "step";
    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = BakingDatabase.INGREDIENT)
    public static class Ingredient {
        @ContentUri(
                path = Path.INGREDIENT,
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = IngredientTable._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENT);

        @InexactContentUri(
                name = "INGREDIENT_ID",
                path = Path.INGREDIENT + "/#",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = IngredientTable._ID,
                pathSegment = 1)
        public static Uri withId(String id){
            return buildUri(Path.INGREDIENT, id);
        }

        @InexactContentUri(
                name = "INGREDIENT_RECIPE_ID",
                path = Path.INGREDIENT + "/recipe/#",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = IngredientTable.RECIPE_ID,
                pathSegment = 1)
        public static Uri withRecipeId(String recipeId){
            return buildUri(Path.INGREDIENT, recipeId);
        }
    }

    @TableEndpoint(table = BakingDatabase.STEP)
    public static class Step {
        @ContentUri(
                path = Path.STEP,
                type = "vnd.android.cursor.dir/step",
                defaultSort = StepTable.STEP_NUMBER + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.STEP);

        @InexactContentUri(
                name = "STEP_ID",
                path = Path.STEP + "/#",
                type = "vnd.android.cursor.item/step",
                whereColumn = StepTable._ID,
                pathSegment = 1)
        public static Uri withId(String id){
            return buildUri(Path.STEP, id);
        }
        @InexactContentUri(
                name = "STEP_RECIPE_ID",
                path = Path.STEP + "/recipe/#",
                type = "vnd.android.cursor.item/step",
                whereColumn = StepTable.RECIPE_ID,
                pathSegment = 1)
        public static Uri withRecipeId(String recipeId){
            return buildUri(Path.STEP, recipeId);
        }
    }

    @TableEndpoint(table = BakingDatabase.RECIPE)
    public static class Recipe {
        @ContentUri(
                path = Path.RECIPE,
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeTable._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RECIPE);

        @InexactContentUri(
                name = "RECIPE_ID",
                path = Path.RECIPE + "/#",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeTable._ID,
                pathSegment = 1)
        public static Uri withId(String id){
            return buildUri(Path.RECIPE, id);
        }
    }
}
