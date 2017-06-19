package com.example.gparmar.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailFragment extends Fragment
    implements View.OnClickListener{
    private static final String TAG = "StepDetailFragment";
    private static final String ARG_STEP = "param_step";
    private static final String ARG_STEP_POSITION = "param_step_position";

    private Step mStep;
    private int mStepPosition;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.prev_button)
    Button prevStep;
    @BindView(R.id.next_button)
    Button nextStep;
    @BindView(R.id.simple_video)
    SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

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

        ButterKnife.bind(this, view);
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
        int maxSteps = Integer.parseInt(CommonUtilities.getSharedPref(getContext(),
                Constants.PROPERTY_MAX_STEPS, "-1"));
        if (mStepPosition == (maxSteps-1)) {
            nextStep.setVisibility(View.GONE);
        } else {
            nextStep.setVisibility(View.VISIBLE);
        }

        description.setText(mStep.getDescription());
    }

    private void initializePlayer() {
        Log.d(TAG, "initializePlayer");
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(mStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
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
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        Log.d(TAG, "releasePlayer");
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
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
        initializePlayer();
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
