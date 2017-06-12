package com.example.gparmar.bakingapp;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gparmar.bakingapp.adapter.StepsAdapter;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.utilities.Constants;

/**
 * Created by gparmar on 12/06/17.
 */

public class RecipeStepsFragment extends Fragment {
    private int mRecipeId;
    private RecyclerView mStepsList;
    private StepsAdapter mAdapter;

    public RecipeStepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        mRecipeId = args.getInt(Constants.PROPERTY_RECIPES_ID, -1);

        View fragmentView
                = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        if (mRecipeId == -1){
            return fragmentView;
        }
        mStepsList = (RecyclerView) fragmentView.findViewById(R.id.steps_list);
        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Step.withRecipeId(mRecipeId+""),null,null,null,null);
        mAdapter = new StepsAdapter(cursor);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStepsList.setLayoutManager(layoutManager);

        return fragmentView;
    }
}
