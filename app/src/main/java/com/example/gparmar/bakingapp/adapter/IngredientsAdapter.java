package com.example.gparmar.bakingapp.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gparmar.bakingapp.R;
import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.IngredientTable;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Ingredient;
import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gparmar on 12/06/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private Cursor mCursor;

    public IngredientsAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ingredientItemView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ingredient_item, parent, false);

        return new IngredientsViewHolder(ingredientItemView);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        Ingredient ingredient = CommonUtilities.getIngredientFromCursor(mCursor);
        holder.mIngName.setText(ingredient.getIngredient());
        holder.mQty.setText(ingredient.getQuantity()+"");
        holder.mMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ing_name)
        TextView mIngName;
        @BindView(R.id.qty)
        TextView mQty;
        @BindView(R.id.measure)
        TextView mMeasure;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
