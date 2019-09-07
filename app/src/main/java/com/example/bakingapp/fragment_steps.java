package com.example.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_steps extends Fragment {

    @BindView(R.id.ingredient_video)
    PlayerView playerView;

    @BindView(R.id.txt_steps_description)
    TextView steps_description;

    @BindView(R.id.ingredient_img)
    ImageView IngredientImg;

    @Nullable
    @BindView(R.id.btn_next)
    ImageButton btn_next;

    @Nullable
    @BindView(R.id.btn_previous)
    ImageButton btn_previous;

    private SimpleExoPlayer player;
    private List<Recipes.StepsBean> stepsList;

    public static int getIndex() {
        return index;
    }
    private static int index;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stepsList = getArguments().getParcelableArrayList(getString(R.string.Ingredient_KEY));
        index=  getArguments().getInt(getString(R.string.STEPS_INDEX_KEY),0);
        if(btn_next!=null) {
            btn_next.setOnClickListener(v -> {
                index++;
                SetUI();
            });

            btn_previous.setOnClickListener(v -> {
                index--;
                SetUI();
            });
        }
        SetUI();
    }

    private void SetUI(){
       if(btn_next!=null ){
           getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
           ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            if (index == 0)
                btn_previous.setVisibility(View.GONE);
            else
                btn_previous.setVisibility(View.VISIBLE);

            if (index == stepsList.size() - 1)
                btn_next.setVisibility(View.GONE);
            else
                btn_next.setVisibility(View.VISIBLE);
        }else if(!getResources().getBoolean(R.bool.Tablet)){
           getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
           ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
       }
        steps_description.setText(stepsList.get(index).getDescription());
        setMediaPlayer();
    }

    private void setMediaPlayer() {
        if (!stepsList.get(index).getVideoURL().isEmpty()) {
            if (player == null) initialiseExoPlayer();
            setExoPlayer();
        } else if (!stepsList.get(index).getThumbnailURL().isEmpty()) {
            playerView.setVisibility(View.GONE);
            IngredientImg.setVisibility(View.VISIBLE);
            Picasso.get().load(stepsList.get(index).getThumbnailURL()).error(R.drawable.problem).into(IngredientImg);
        } else {
            playerView.setVisibility(View.GONE);
            IngredientImg.setVisibility(View.VISIBLE);
        }
    }

    private void setExoPlayer() {
        playerView.setVisibility(View.VISIBLE);
        IngredientImg.setVisibility(View.GONE);

        Uri mediaUri = Uri.parse(stepsList.get(index).getVideoURL());
        playerView.setKeepScreenOn(true);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "BakesRecipes2"));
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, extractorsFactory, null, null, null);
        player.prepare(videoSource);
        playerView.requestFocus();
        player.setPlayWhenReady(true);
    }

    private void initialiseExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        playerView.setUseController(true);
        playerView.requestFocus();
        playerView.setPlayer(player);
    }

    @Override
    public void onStop() {
        super.onStop();
        playerView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(player!=null)
            player.stop();
        onDetach();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(player!=null)
          player.release();

    }
}
