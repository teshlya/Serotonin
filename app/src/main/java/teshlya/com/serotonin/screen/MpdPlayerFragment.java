package teshlya.com.serotonin.screen;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import androidx.fragment.app.Fragment;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.utils.Calc;
import teshlya.com.serotonin.utils.MpdPlayer;

import static teshlya.com.serotonin.model.PlayState.PAUSE;
import static teshlya.com.serotonin.model.PlayState.PLAY;

public class MpdPlayerFragment extends Fragment {

    private int width;
    private int height;
    private Media media;
    private MpdPlayer mpdPlayer;
    private ImageView play;
    public static ImageView pause;

    public static MpdPlayerFragment newInstance(Media media) {
        MpdPlayerFragment fragment = new MpdPlayerFragment();
        Bundle args = new Bundle();
        args.putSerializable("media", media);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mpd_player, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        getArgs();
        calcScreenSize(getScreenSize());
        initButtons(view);
        if (media != null)
            initPlayer(view);
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            media = (Media) args.getSerializable("media");
        }
    }

    private Point getScreenSize() {
        return Calc.getWindowSizeInPx(getContext());
    }

    private void calcScreenSize(Point point) {
        if ((double) point.x / (double) point.y < (double) media.getWidth() / (double) media.getHeight()) {
            width = point.x;
            height = point.x * media.getHeight() / media.getWidth();
        } else {
            height = point.y;
            width = point.y * media.getWidth() / media.getHeight();
        }
        if (point.y - height < 12)
            width -= 40;
    }

    private void initButtons(View view) {
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                MpdPlayer.playState = PLAY;
                if (mpdPlayer != null)
                    mpdPlayer.play();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                MpdPlayer.playState = PAUSE;
                if (mpdPlayer != null)
                    mpdPlayer.pause();
            }
        });
    }

    private void initPlayer(View view) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        SimpleExoPlayerView playerView = view.findViewById(R.id.player);
        playerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                switch (MpdPlayer.playState) {
                    case PAUSE:
                        play.setVisibility(visibility);
                        break;
                    case PLAY:
                        pause.setVisibility(visibility);
                        break;
                }
            }
        });
        mpdPlayer = MpdPlayer.newInstance();
        mpdPlayer.setPlayer(playerView);
        mpdPlayer.init(getContext(), media.getUrl());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mpdPlayer != null)
            mpdPlayer.stop();
    }
}
