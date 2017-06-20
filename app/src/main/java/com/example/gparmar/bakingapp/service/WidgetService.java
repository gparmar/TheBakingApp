package com.example.gparmar.bakingapp.service;

import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViewsService;

import com.example.gparmar.bakingapp.adapter.RecipeListProvider;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.IngredientTable;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;
import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 19/06/17.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int recipeId = Integer.parseInt(CommonUtilities.getSharedPref(this,
                Constants.PROPERTY_CURRENT_SELECTED_RECIPE_ID, "-1"));

        if (recipeId != -1) {
            Cursor cursor = getContentResolver().query(BakingProvider.Ingredient.CONTENT_URI,
                    null, IngredientTable.RECIPE_ID + "=" + recipeId, null, null);
            return new RecipeListProvider(this, cursor);
        }
        return null;
    }
}
