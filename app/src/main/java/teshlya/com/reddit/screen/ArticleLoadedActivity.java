package teshlya.com.reddit.screen;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.recyclerview.widget.LinearLayoutManager;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.callback.CallbackArticleLoaded;
import teshlya.com.reddit.model.CommunityData;
import teshlya.com.reddit.parse.ParseCommunity;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;

public class ArticleLoadedActivity extends AppCompatActivity implements CallbackArticleLoaded {

    CommunityFragment communityFragment;
    int left;
    int right;
    int top;
    int bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        init2();
        //animateStartActivity();
    }

    private void animateStartActivity() {
        left = getIntent().getExtras().getInt(Constants.LEFT);
        right = getIntent().getExtras().getInt(Constants.RIGHT);
        top = getIntent().getExtras().getInt(Constants.TOP);
        bottom = getIntent().getExtras().getInt(Constants.BOTTOM);
        overridePendingTransition(0, 0);

        Point size = Calc.getWindowSizeInPx(this);
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

    private void init() {
        //String url = getIntent().getExtras().getString(Constants.URL, "");
        //String community = getIntent().getExtras().getString(Constants.COMMUNITY, "");
        initTitle("Front");
        //initFragmentCommunity("/");
    }

    private void initTitle(String community) {
       // ((TextView) findViewById(R.id.community_title)).setText(community);
    }

    private void initFragmentCommunity(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //communityFragment = CommunityFragment.newInstance(url);
        //ft.replace(R.id.fragment_article, communityFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
//        if (communityFragment.back())
        {
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
        Point size = Calc.getWindowSizeInPx(this);
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
                ArticleLoadedActivity.super.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(animation);
    }





    private InboxRecyclerView recyclerView;
    private CommunityAdapter adapter;
    private static String url;
    private static String domain = "https://www.reddit.com";
    private ExpandablePageLayout conteinerSwipePostFragment;

    private void init2() {
        conteinerSwipePostFragment = findViewById(R.id.conteinerSwipePostFragment);
        //fab = getActivity().findViewById(R.id.fab);
        SwipePostAdapter.setFab(null);
        initRecycler();
        new ParseCommunity(this, domain + "/" + ".json").execute();
    }

    private void initRecycler(){
        recyclerView = findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView2, int dx, int dy){
                if (dy > 0 && fab.isShown())
                    fab.hide();
                else
                    fab.show();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/

      /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               if (dy > 0 && fab.isShown())
                   fab.hide();
               else
                   fab.show();
           }
       });*/
    }

    @Override
    public void addArticles(CommunityData data) {
        adapter = new CommunityAdapter(recyclerView, conteinerSwipePostFragment, url);
        adapter.setHasStableIds(true);
        adapter.addArticle(data);
        conteinerSwipePostFragment.setAnimationDurationMillis(800);
        conteinerSwipePostFragment.setPullToCollapseEnabled(false);
        SwipePostFragment swipePostFragment = SwipePostFragment.newInstance(data, url, 4);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(conteinerSwipePostFragment.getId(), swipePostFragment)
                .commitNowAllowingStateLoss();

        recyclerView.setExpandablePage(conteinerSwipePostFragment);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));
        recyclerView.setAdapter(adapter);
    }

}
