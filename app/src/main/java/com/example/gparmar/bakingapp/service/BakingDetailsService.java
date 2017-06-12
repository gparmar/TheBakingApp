package com.example.gparmar.bakingapp.service;

import com.example.gparmar.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gparmar on 11/06/17.
 */

public interface BakingDetailsService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> listOfRecipes();
}
