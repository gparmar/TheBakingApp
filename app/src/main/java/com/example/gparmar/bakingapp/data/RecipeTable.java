package com.example.gparmar.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by gparmar on 11/06/17.
 */

public interface RecipeTable {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String SERVINGS = "servings";
    @DataType(DataType.Type.TEXT)
    public static final String IMAGE = "image";
}
