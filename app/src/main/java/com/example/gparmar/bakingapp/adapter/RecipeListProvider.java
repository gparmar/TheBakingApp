package com.example.gparmar.bakingapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.gparmar.bakingapp.R;
import com.example.gparmar.bakingapp.model.Ingredient;
import com.example.gparmar.bakingapp.model.Recipe;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;

/**
 * Created by gparmar on 19/06/17.
 */

public class RecipeListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "RecipeListProvider";
    private Cursor mCursor;
    private Context mContext;

    public RecipeListProvider(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCursor != null) {
            Log.d(TAG, "Returning count:"+mCursor.getCount());
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        Ingredient ingredient = CommonUtilities.getIngredientFromCursor(mCursor);
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                R.layout.ingredient_item);
        remoteView.setTextViewText(R.id.ing_name, ingredient.getIngredient());
        remoteView.setTextViewText(R.id.qty, ingredient.getQuantity()+"");
        remoteView.setTextViewText(R.id.measure, ingredient.getMeasure());
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
