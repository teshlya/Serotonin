package teshlya.com.serotonin.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

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

public class MpdPlayer {

    private Context context;
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
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    public static MpdPlayer newInstance() {
        return new MpdPlayer();
    }

    public void setPlayer(SimpleExoPlayerView simpleExoPlayerView) {
        this.simpleExoPlayerView = simpleExoPlayerView;
    }

    public void init(Context context, String url) {
        this.context = context;
        this.videoUrl = url;
        userAgent = Util.getUserAgent(context, "Reddit");
        createPlayer();
        attachPlayerView();
        preparePlayer();
    }

    public void createPlayer() {
        mainHandler = new Handler();
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        player.setPlayWhenReady(true);
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
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    player.seekTo(0);
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
        return new DefaultDataSourceFactory(context, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public void play(){
        if (player != null)
            player.setPlayWhenReady(true);
    }

    public void pause(){
        if (player != null)
            player.setPlayWhenReady(false);
    }

    public void stop() {
        if (player != null)
            player.release();
    }
}
