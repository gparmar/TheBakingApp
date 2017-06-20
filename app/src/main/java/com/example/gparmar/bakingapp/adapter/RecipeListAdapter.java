package com.example.gparmar.bakingapp.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gparmar.bakingapp.IngredientsWidget;
import com.example.gparmar.bakingapp.R;
import com.example.gparmar.bakingapp.RecipeListActivity;
import com.example.gparmar.bakingapp.RecipeStepsActivity;
import com.example.gparmar.bakingapp.model.Recipe;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;
import com.example.gparmar.bakingapp.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gparmar on 15/06/17.
 */

public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    public RecipeListAdapter(Context context, Cursor mCursor) {
        this.mContext = context;
        this.mCursor = mCursor;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeCard = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new RecipeListViewHolder(recipeCard);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        final Recipe recipe = CommonUtilities.getRecipeFromCursor(mCursor);
        holder.recipeName.setText(recipe.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtilities.putSharedPref(mContext,
                        Constants.PROPERTY_CURRENT_SELECTED_RECIPE_ID, recipe.getId());
                AppWidgetManager man = AppWidgetManager.getInstance(mContext);
                int[] ids = man.getAppWidgetIds(
                        new ComponentName(mContext,IngredientsWidget.class));
                Intent updateIntent = new Intent();
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(IngredientsWidget.WIDGET_IDS_KEY, ids);

                mContext.sendBroadcast(updateIntent);

                Intent stepsIntent = new Intent(mContext, RecipeStepsActivity.class);
                stepsIntent.putExtra(Constants.PROPERTY_RECIPE_ID, recipe.getId());
                stepsIntent.putExtra(Constants.PROPERTY_RECIPE_NAME, recipe.getName());
                mContext.startActivity(stepsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipeName;

        public RecipeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }
}
