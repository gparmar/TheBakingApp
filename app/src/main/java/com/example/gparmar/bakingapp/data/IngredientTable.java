package com.example.gparmar.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by gparmar on 11/06/17.
 */

public interface IngredientTable {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String MEASURE = "measure";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String QUANTITY = "quantity";
    @DataType(DataType.Type.REAL) @NotNull
    public static final String INGREDIENT = "ingredient";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String RECIPE_ID = "recipe_id";

}
