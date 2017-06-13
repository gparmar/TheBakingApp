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

import com.example.gparmar.bakingapp.adapter.StepsAdapter;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 12/06/17.
 */

public class RecipeStepsFragment extends Fragment {
    private static final String TAG = "RecipeStepsFragment";
    private int mRecipeId = -1;
    private RecyclerView mStepsList;
    private StepsAdapter mAdapter;

    public RecipeStepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        Bundle args = getArguments();
        if (args != null) {
            mRecipeId = args.getInt(Constants.PROPERTY_RECIPES_ID, -1);
        }

        View fragmentView
                = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        mStepsList = (RecyclerView) fragmentView.findViewById(R.id.steps_list);

        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Step.CONTENT_URI,null,
                        StepTable.RECIPE_ID+"="+mRecipeId,null,null);
        mAdapter = new StepsAdapter(cursor);
        mStepsList.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStepsList.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration
                = new DividerItemDecoration(mStepsList.getContext(),
                layoutManager.getOrientation());
        mStepsList.addItemDecoration(dividerItemDecoration);

        Log.d(TAG, "returning fragmentView");
        return fragmentView;
    }

    public void setRecipeId(int mRecipeId) {
        Log.d(TAG, "Setting mRecipeId:"+mRecipeId);
        this.mRecipeId = mRecipeId;
        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Step.CONTENT_URI,null,
                        StepTable.RECIPE_ID+"="+mRecipeId,null,null);
        mAdapter.setCursor(cursor);
    }
}
