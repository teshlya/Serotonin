package teshlya.com.serotonin.screen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.callback.CallbackArticleLoaded;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.parse.ParseCommunity;
import teshlya.com.serotonin.parse.ParseJsonSubscription;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.utils.DrawableIcon;
import teshlya.com.serotonin.utils.Preference;

public class FrontPageActivity extends AppCompatActivity implements CallbackArticleLoaded {

    private CommunityFragment communityFragment;
    private String url = "/";
    private Boolean star = false;
    private FloatingActionButton fab;
    private Context context;
    private TextView titleTextView;
    private String title = "Front page";
    public static boolean shownFab = true;
    private ImageView star_enabled;
    private ImageView star_disabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        init();
    }

  /*  private void getHeshKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "teshlya.com.reddit",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }*/

    private void init() {
        context = this;

        //MainMenuFragment.fillSubscriptionsList(this);

        DrawableIcon.initAllIcons(this);

        Preference.getStarFromSharedPrefs(this);
        initFab();
        initTitle();
        initStar();
        parseCommunity(url);
    }

    private void initFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("click", Constants.NORMAL_CLICK);
                ((FrontPageActivity) context).startActivityForResult(intent, 1);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, MainMenuActivity.class);
                intent.putExtra("click", Constants.LONG_CLICK);
                ((FrontPageActivity) context).startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    private void initTitle() {
        titleTextView = findViewById(R.id.community_title);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra(Constants.URL);
                String community = data.getStringExtra(Constants.COMMUNITY);
                Boolean star = data.getBooleanExtra(Constants.STAR, false);
                if (url != null && community != null) {
                    this.url = url;
                    this.title = community;
                    this.star = star;
                    parseCommunity(url);
                }
            }
        }
    }

    private void parseCommunity(String url) {
        showProgressBar();
        setTitle();
        setStar();
        new ParseCommunity(this, Constants.DOMAIN + url + ".json").execute();
    }

    private void showProgressBar() {
        FrameLayout conteiner = findViewById(R.id.conteiner);
        conteiner.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout progress = (LinearLayout) inflater.inflate(R.layout.item_loading, null);
        ProgressBar progressBar = progress.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF1976D2, android.graphics.PorterDuff.Mode.MULTIPLY);
        conteiner.addView(progress);

    }

    private void initStar() {
        star_enabled = findViewById(R.id.star_enabled);
        star_disabled = findViewById(R.id.star_disabled);
        setOnClickStarEnabled();
        setOnClickStarDisabled();
    }

    private void setOnClickStarEnabled() {
        star_enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_enabled.setVisibility(View.GONE);
                star_disabled.setVisibility(View.VISIBLE);
                if (Preference.starList.contains(title))
                    Preference.starList.remove(title);
                Preference.saveStarToSharedPrefs(context);
            }
        });
    }

    private void setOnClickStarDisabled() {
        star_disabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_disabled.setVisibility(View.GONE);
                star_enabled.setVisibility(View.VISIBLE);
                Preference.starList.add(title);
                Preference.saveStarToSharedPrefs(context);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (communityFragment == null || communityFragment.back()) {
            super.onBackPressed();
        }
    }

    @Override
    public void addArticles(CommunityData data) {
        if (data != null) {
            openCommunityFragment(url, data);
            if (title.equals("Random")) {
                title = data.getSubreddit();
                star = true;
                setTitle();
                setStar();
            }
        }
        ((FrameLayout) findViewById(R.id.conteiner)).removeAllViews();
    }

    private void openCommunityFragment(String url, CommunityData data) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        communityFragment = CommunityFragment.newInstance(url, data);
        ft.replace(R.id.conteiner, communityFragment);
        ft.commit();
    }

    private void setTitle() {
        titleTextView.setText(title);
    }

    private void setStar() {
        star_enabled.setVisibility(View.GONE);
        star_disabled.setVisibility(View.GONE);
        if (star) {
            if (Preference.starList.contains(title))
                star_enabled.setVisibility(View.VISIBLE);
            else
                star_disabled.setVisibility(View.VISIBLE);
        }
    }
}
