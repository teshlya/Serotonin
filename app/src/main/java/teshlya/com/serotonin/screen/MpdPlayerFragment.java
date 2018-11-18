package teshlya.com.serotonin.screen;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import androidx.fragment.app.Fragment;
import teshlya.com.serotonin.R;


public class MpdPlayerFragment extends Fragment {

    private String videoUrl = null;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Handler mainHandler;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private TrackSelector trackSelector;
    private LoadControl loadControl;
    private DataSource.Factory dataSourceFactory;
    private MediaSource videoSource;
    private Uri uri;
    private String userAgent;
    private FrameLayout holderBackground;
    private FrameLayout placeholder;
    private int width;
    private int height;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    public static MpdPlayerFragment newInstance(String url, int width, int height) {
        MpdPlayerFragment fragment = new MpdPlayerFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt("width", width);
        args.putInt("height", height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mpd_player, container, false);
        getArgs();
        if (videoUrl != null){
            init(view);
        }
        return view;
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            videoUrl = args.getString("url");
            width = args.getInt("width");
            height = args.getInt("height");
        }
    }

    private void init(View view) {
        initPlaceholder(view);
        simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        userAgent = Util.getUserAgent(getContext(), "Reddit");
        createPlayer();
        attachPlayerView();
        preparePlayer();
    }

    private void initPlaceholder(View view){
        placeholder = view.findViewById(R.id.placeholder);
        placeholder.setLayoutParams(new FrameLayout.LayoutParams(width,height));
        holderBackground = view.findViewById(R.id.holder_background);
    }

    public void createPlayer() {
        mainHandler = new Handler();
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playbackState == ExoPlayer.STATE_READY){
                    holderBackground.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }

    public void attachPlayerView() {
        simpleExoPlayerView.setPlayer(player);
    }

    public void preparePlayer() {
        uriParse();
        dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        videoSource = new DashMediaSource(uri, buildDataSourceFactory(null), new DefaultDashChunkSource.Factory(dataSourceFactory), mainHandler, null);
        player.prepare(videoSource);
    }

    public void uriParse() {
        uri = Uri.parse(videoUrl);
    }

    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(getContext(), bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    @Override
    public void onStop() {
        super.onStop();
        //player.release();
    }


}
