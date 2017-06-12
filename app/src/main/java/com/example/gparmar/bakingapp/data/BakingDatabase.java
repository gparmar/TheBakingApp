package com.example.gparmar.bakingapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by gparmar on 11/06/17.
 */
@Database(version = BakingDatabase.VERSION)
public final class BakingDatabase {
    public static final int VERSION = 1;

    public BakingDatabase() {
    }

    @Table(RecipeTable.class)
    public static final String RECIPE = "recipe";
    @Table(IngredientTable.class)
    public static final String INGREDIENT = "ingredient";
    @Table(StepTable.class)
    public static final String STEP = "step";
}
