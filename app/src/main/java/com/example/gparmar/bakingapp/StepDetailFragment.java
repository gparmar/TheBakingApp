package com.example.gparmar.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gparmar.bakingapp.data.BakingProvider;
import com.example.gparmar.bakingapp.data.StepTable;
import com.example.gparmar.bakingapp.model.Step;
import com.example.gparmar.bakingapp.utilities.CommonUtilities;
import com.example.gparmar.bakingapp.utilities.Constants;


public class StepDetailFragment extends Fragment
    implements View.OnClickListener{
    private static final String ARG_STEP = "param_step";
    private static final String ARG_STEP_POSITION = "param_step_position";

    private Step mStep;
    private int mStepPosition;
    private TextView description;
    private Button prevStep;
    private Button nextStep;

    private OnFragmentInteractionListener mListener;

    public StepDetailFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step The step which this fragment will show.
     * @return A new instance of fragment StepDetailFragment.
     */
    public static StepDetailFragment newInstance(Step step, int position) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        args.putInt(ARG_STEP_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(ARG_STEP);
            mStepPosition = getArguments().getInt(ARG_STEP_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        description = (TextView) view.findViewById(R.id.description);
        prevStep = (Button) view.findViewById(R.id.prev_button);
        nextStep = (Button) view.findViewById(R.id.next_button);
        prevStep.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        setupView();

        return view;
    }

    private void setupView(){
        if (mStepPosition == 0) {
            prevStep.setVisibility(View.GONE);
        } else {
            prevStep.setVisibility(View.VISIBLE);
        }
        int maxSteps = Integer.parseInt(CommonUtilities.getSharedPref(getActivity(),
                Constants.PROPERTY_MAX_STEPS, "-1"));
        if (mStepPosition == (maxSteps-1)) {
            nextStep.setVisibility(View.GONE);
        } else {
            nextStep.setVisibility(View.VISIBLE);
        }

        description.setText(mStep.getDescription());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int newPosition = mStepPosition;
        //Step newStep = null;
        if (v.getId() == R.id.prev_button) {
            newPosition = mStepPosition - 1;
        } else if (v.getId() == R.id.next_button) {
            newPosition = mStepPosition + 1;
        }
        Cursor cursor = getContext().getContentResolver().query(BakingProvider.Step.CONTENT_URI,
                null,
                StepTable.STEP_NUMBER+"="+newPosition+" and "+StepTable.RECIPE_ID+"="+mStep.getRecipeId(),
                null,null);
        cursor.moveToFirst();
        mStep = CommonUtilities.getStepFromCursor(cursor);
        mStepPosition = newPosition;
        mListener.setNewTitle(mStep.getShortDescription());
        setupView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void setNewTitle(String title);
    }
}
