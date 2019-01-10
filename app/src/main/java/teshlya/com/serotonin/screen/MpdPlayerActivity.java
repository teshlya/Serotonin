package teshlya.com.serotonin.screen;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import androidx.appcompat.app.AppCompatActivity;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.model.PlayState;
import teshlya.com.serotonin.utils.Calc;
import teshlya.com.serotonin.utils.MpdPlayer;

import static teshlya.com.serotonin.model.PlayState.PAUSE;
import static teshlya.com.serotonin.model.PlayState.PLAY;

public class MpdPlayerActivity extends AppCompatActivity {

    private int width;
    private int height;
    private Media media;
    private MpdPlayer mpdPlayer;
    private ImageView play;
    private ImageView pause;
    private PlayState playState = PAUSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpd_player);
        init();
    }

    private void init() {
        getArguments();
        calcScreenSize(getScreenSize());
        initButtons();
        if (media != null)
            initPlayer();
        findViewById(R.id.background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Point getScreenSize() {
        return Calc.getWindowSizeInPx(this);
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

    private void getArguments() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            media = (Media) extras.getSerializable("media");
        }
    }

    private void initButtons(){
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                playState = PAUSE;
                if (mpdPlayer != null)
                    mpdPlayer.play();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                playState = PLAY;
                if (mpdPlayer != null)
                    mpdPlayer.pause();
            }
        });

    }

    private void initPlayer() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        findViewById(R.id.conteinerPlayer).setLayoutParams(layoutParams);
        SimpleExoPlayerView playerView = findViewById(R.id.player);
        playerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                switch (playState) {
                    case PLAY:
                        play.setVisibility(visibility);
                        break;
                    case PAUSE:
                        pause.setVisibility(visibility);
                        break;
                }
            }
        });
        mpdPlayer = MpdPlayer.newInstance();
        mpdPlayer.setPlayer(playerView);
        mpdPlayer.init(this, media.getUrl());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mpdPlayer != null)
            mpdPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mpdPlayer != null)
            pause.performClick();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mpdPlayer != null)
            pause.performClick();
    }
}
