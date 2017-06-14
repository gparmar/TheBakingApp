package com.example.gparmar.bakingapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.Constants;

public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Step step = getIntent().getParcelableExtra(Constants.PROPERTY_STEP);
        int position = getIntent().getIntExtra(Constants.PROPERTY_STEP_POSITION, -1);
        Fragment fragment = StepDetailFragment.newInstance(step, position);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.step_detail_fragment, fragment)
                .commit();
        setTitle(step.getShortDescription());
    }


    @Override
    public void setNewTitle(String title) {
        setTitle(title);
    }
}
