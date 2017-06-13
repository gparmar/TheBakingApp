package com.example.gparmar.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gparmar.bakingapp.R;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Step;

import java.util.List;

/**
 * Created by gparmar on 12/06/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private Cursor mCursor;

    public StepsAdapter(Cursor cursor){
        mCursor = cursor;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View stepItemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.step_item, parent, false);

        return new StepsViewHolder(stepItemView);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        if (position == 0) {
            holder.mPosition = 0;
            holder.mStepDescription.setText("Recipe Ingredients");
        } else {
            mCursor.moveToPosition(position-1);
            Step step = getStepFromCursor();
            holder.mPosition = position;
            holder.mStepDescription.setText(step.getShortDescription());
            holder.mRecipeId = step.getRecipeId();
            holder.mStepId = step.getId();
        }
    }

    private Step getStepFromCursor(){
        Step step = new Step();
        step.setDescription(mCursor.getString(mCursor.getColumnIndex(StepTable.DESCRIPTION)));
        step.setShortDescription(mCursor.getString(mCursor.getColumnIndex(StepTable.SHORT_DESCRIPTION)));
        step.setThumbnailURL(mCursor.getString(mCursor.getColumnIndex(StepTable.THUMBNAIL_URL)));
        step.setVideoURL(mCursor.getString(mCursor.getColumnIndex(StepTable.VIDEO_URL)));
        step.setId(mCursor.getInt(mCursor.getColumnIndex(StepTable._ID)));
        step.setRecipeId(mCursor.getInt(mCursor.getColumnIndex(StepTable.RECIPE_ID)));
        return step;
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount()+1;
        }
        return 0;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder {
        int mPosition;
        int mStepId;
        int mRecipeId;
        TextView mStepDescription;

        public StepsViewHolder(View itemView) {
            super(itemView);
            mStepDescription = (TextView) itemView.findViewById(R.id.step_description);
        }
    }
}
