package com.example.gparmar.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gparmar.bakingapp.R;
import com.example.gparmar.bakingapp.StepsClickListener;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gparmar on 12/06/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private Cursor mCursor;
    private StepsClickListener mStepsClickListener;
    private int mRecipeId;
    private int selectedPos = 0;

    public StepsAdapter(Cursor cursor,
                        StepsClickListener stepsClickListener,
                        int recipeId){
        mCursor = cursor;
        mStepsClickListener = stepsClickListener;
        mRecipeId = recipeId;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View stepItemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.step_item, parent, false);

        return new StepsViewHolder(stepItemView);
    }

    @Override
    public void onBindViewHolder(final StepsViewHolder holder, final int position) {
        Step step = null;
        if (position == 0) {
            step = new Step();
            step.setRecipeId(mRecipeId);
            holder.mPosition = 0;
            holder.mStepNumber.setText("0");
            holder.mStepDescription.setText("Recipe Ingredients");
        } else {
            mCursor.moveToPosition(position-1);
            step = CommonUtilities.getStepFromCursor(mCursor);
            holder.mPosition = position;
            holder.mStepNumber.setText((step.getStepNumber()+1)+"");
            holder.mStepDescription.setText(step.getShortDescription());
            holder.mRecipeId = step.getRecipeId();
            holder.mStepId = step.getId();
        }
        final Step finalStep = step;
        holder.itemView.setSelected(selectedPos == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStepsClickListener.onStepClicked(position, finalStep);
                notifyItemChanged(selectedPos);
                selectedPos = position;
                notifyItemChanged(selectedPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount()+1;
        }
        return 0;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public void setRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        int mPosition;
        int mStepId;
        int mRecipeId;
        @BindView(R.id.step_description)
        TextView mStepDescription;
        @BindView(R.id.step_number)
        TextView mStepNumber;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
