package com.example.gparmar.bakingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gparmar.bakingapp.adapter.IngredientsAdapter;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 12/06/17.
 */

public class RecipeIngredientsFragment extends Fragment {
    private static final String TAG = "RecipeIngsFragment";
    private static final String ARG_RECIPE_ID = "recipe_id";
    private int mRecipeId = -1;
    private RecyclerView mIngredientsList;
    private IngredientsAdapter mAdapter;

    public RecipeIngredientsFragment() {
    }

    public static RecipeIngredientsFragment newInstance(int recipeId){
        RecipeIngredientsFragment fragment =
                new RecipeIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        Bundle args = getArguments();
        if (args != null) {
            mRecipeId = args.getInt(ARG_RECIPE_ID, -1);
        }

        View fragmentView
                = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        mIngredientsList = (RecyclerView) fragmentView.findViewById(R.id.ingredients_list);

        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Ingredient.CONTENT_URI,null,
                        StepTable.RECIPE_ID+"="+mRecipeId,null,null);
        mAdapter = new IngredientsAdapter(cursor);
        mIngredientsList.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mIngredientsList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(mIngredientsList.getContext(),
                layoutManager.getOrientation());
        mIngredientsList.addItemDecoration(dividerItemDecoration);

        Log.d(TAG, "returning fragmentView");
        return fragmentView;
    }

    public void setRecipeId(int mRecipeId) {
        Log.d(TAG, "Setting mRecipeId:"+mRecipeId);
        this.mRecipeId = mRecipeId;
        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Ingredient.CONTENT_URI,null,
                        StepTable.RECIPE_ID+"="+mRecipeId,null,null);
        mAdapter.setCursor(cursor);
    }
}
