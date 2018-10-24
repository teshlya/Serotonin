package teshlya.com.reddit.screen;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;

public class ArticleActivity extends AppCompatActivity {

    CommunityFragment communityFragment;
    int left;
    int right;
    int top;
    int bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        init();
        animateStartActivity();
    }

    private void animateStartActivity() {
        left = getIntent().getExtras().getInt(Constants.LEFT);
        right = getIntent().getExtras().getInt(Constants.RIGHT);
        top = getIntent().getExtras().getInt(Constants.TOP);
        bottom = getIntent().getExtras().getInt(Constants.BOTTOM);
        overridePendingTransition(0, 0);

        Point size = getWindowSize();
        int width = size.x;
        int height = size.y;
        float pivotXValue = 0.5f;
        float pivotYValue = (float) ((top + bottom) / 2) / height;
        float fromX = (float) (right - left) / width;
        float fromY = (float) (bottom - top) / height;
        AnimationSet animation = new AnimationSet(true);
        Animation anim = new ScaleAnimation(
                fromX, 1f,
                fromY, 1f,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);
        anim.setFillAfter(true);
        animation.addAnimation(anim);
        animation.addAnimation(new AlphaAnimation(0.3F, 1.0F));
        animation.setDuration(700);
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.startAnimation(animation);
    }

    private Point getWindowSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private void init() {
        String url = getIntent().getExtras().getString(Constants.URL, "");
        String community = getIntent().getExtras().getString(Constants.COMMUNITY, "");
        initTitle(community);
        initHomeButton();
        initFragmentCommunity(url, community);
    }

    private void initTitle(String community) {
        ((TextView) findViewById(R.id.community_title)).setText(community);
    }

    private void initHomeButton() {
        findViewById(R.id.home_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragmentCommunity(String url, String community) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        communityFragment = CommunityFragment.newInstance(url);
        ft.replace(R.id.fragment_article, communityFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (communityFragment.back()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        overridePendingTransition(0, 0);
        Point size = getWindowSize();
        int width = size.x;
        int height = size.y;
        float pivotXValue = 0.5f;
        float pivotYValue = (float) ((top + bottom) / 2) / height;
        float toX = (float) (right - left) / width;
        float toY = (float) (bottom - top) / height;
        AnimationSet animation = new AnimationSet(true);
        Animation anim = new ScaleAnimation(
                1f, toX,
                1f, toY,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);
        animation.addAnimation(anim);
        animation.addAnimation(new AlphaAnimation(1.0F, 0.3F));
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ArticleActivity.super.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(animation);
    }


    public void animationClose() {
        overridePendingTransition(0, 0);
        Point size = getWindowSize();
        int width = size.x;
        int height = size.y;
        float pivotXValue = 0.5f;
        float pivotYValue = (float) ((top + bottom) / 2) / height;
        float toX = (float) (right - left) / width;
        float toY = (float) (bottom - top) / height;
        AnimationSet animation = new AnimationSet(true);
        Animation anim = new ScaleAnimation(
                1f, toX,
                1f, toY,
                Animation.RELATIVE_TO_SELF, pivotXValue,
                Animation.RELATIVE_TO_SELF, pivotYValue);
        anim.setFillAfter(true);
        animation.addAnimation(anim);
        animation.addAnimation(new AlphaAnimation(1.0F, 0.0F));
        animation.setDuration(400);
        View rootView = findViewById(android.R.id.content);
        rootView.startAnimation(animation);
    }
}
