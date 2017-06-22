package com.example.gparmar.bakingapp;

import android.content.Context;
import android.content.Intent;
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
import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;
import com.example.gparmar.bakingapp.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gparmar on 12/06/17.
 */

public class RecipeStepsFragment extends Fragment {
    private static final String TAG = "RecipeStepsFragment";
    private int mRecipeId = -1;
    private int mStepPosition = -1;
    @BindView(R.id.steps_list)
    RecyclerView mStepsList;
    private StepsAdapter mAdapter;
    private StepsClickListener mListener;

    public RecipeStepsFragment() {
    }

    public static RecipeStepsFragment newInstance(int recipeId, int stepPosition) {
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PROPERTY_RECIPE_ID, recipeId);
        if (stepPosition != -1) {
            args.putInt(Constants.PROPERTY_STEP_POSITION, stepPosition);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static RecipeStepsFragment newInstance(int recipeId) {
        return newInstance(recipeId, -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        Bundle args = getArguments();
        if (args != null) {
            mRecipeId = args.getInt(Constants.PROPERTY_RECIPE_ID, -1);
            mStepPosition = args.getInt(Constants.PROPERTY_STEP_POSITION, -1);
        }

        View fragmentView
                = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        ButterKnife.bind(this, fragmentView);

        Cursor cursor = getActivity().getContentResolver()
                .query(BakingProvider.Step.CONTENT_URI,null,
                        StepTable.RECIPE_ID+"="+mRecipeId,null,null);
        mAdapter = new StepsAdapter(cursor, mListener, mRecipeId);
        mStepsList.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStepsList.setLayoutManager(layoutManager);

        if (mStepPosition != -1) {
            mAdapter.setSelectedPos(mStepPosition);
        }

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
        CommonUtilities.putSharedPref(getActivity(),
                Constants.PROPERTY_MAX_STEPS, cursor.getCount()+"");
        mAdapter.setRecipeId(mRecipeId);
        mAdapter.setCursor(cursor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepsClickListener) {
            mListener = (StepsClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepsClickListener");
        }
    }


    public void setPositionClicked(int position){
        mAdapter.setSelectedPos(position);
    }
}
