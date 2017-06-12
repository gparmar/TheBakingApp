package com.example.gparmar.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by gparmar on 11/06/17.
 */

public interface StepTable {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID =
            "_id";
    @DataType(DataType.Type.INTEGER)
    public static final String STEP_NUMBER = "step_number";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SHORT_DESCRIPTION = "shortDescription";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DESCRIPTION = "description";
    @DataType(DataType.Type.TEXT)
    public static final String VIDEO_URL = "videoURL";
    @DataType(DataType.Type.TEXT)
    public static final String THUMBNAIL_URL = "thumbnailURL";
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String RECIPE_ID = "recipe_id";
}
