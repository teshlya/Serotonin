package teshlya.com.reddit.screen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import teshlya.com.reddit.R;
import teshlya.com.reddit.callback.CallbackArticleLoaded;
import teshlya.com.reddit.model.CommunityData;
import teshlya.com.reddit.parse.ParseCommunity;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.utils.DrawableIcon;

public class FrontPageActivity extends AppCompatActivity implements CallbackArticleLoaded {

    private CommunityFragment communityFragment;
    private String url = "/";
    private FloatingActionButton fab;
    private Context context;
    private TextView titleTextView;
    String title = "Front page";
    public static boolean shownFab = true;
    private SmoothProgressBar smoothProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        init();
        getHeshKey();
    }

    private void getHeshKey(){
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
    }

    private void init() {
        context = this;
        DrawableIcon.initAllIcons(this);
        //openCommunityFragment(url);
        initProgressBar();
        initFab();
        initTitle();
        parseCommunity(url);
    }

    private void openCommunityFragment(String url, CommunityData data) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        communityFragment = CommunityFragment.newInstance(url, data);
        ft.replace(R.id.conteiner, communityFragment);
        ft.commit();
    }

    private void initFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainMenuActivity.class);
                ((FrontPageActivity) context).startActivityForResult(intent, 1);
            }
        });
    }

   /* private void openCommunity(String url, String title) {
        openCommunityFragment(url);
        titleTextView.setText(title);
    }*/

    private void initTitle() {
        titleTextView = findViewById(R.id.community_title);
    }

    private void initProgressBar() {
        smoothProgressBar = findViewById(R.id.progress_bar_communuty);
    }

    private void parseCommunity(String url) {
        smoothProgressBar.setVisibility(View.VISIBLE);
        new ParseCommunity(this, Constants.DOMAIN + url + ".json").execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra(Constants.URL);
                String community = data.getStringExtra(Constants.COMMUNITY);
                if (url != null && community != null) {
                }
                parseCommunity(url);
                this.url = url;
                this.title = community;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (communityFragment.back()) {
            super.onBackPressed();
        }
    }

    @Override
    public void addArticles(CommunityData data) {
        if (data != null) {
            openCommunityFragment(url, data);
            titleTextView.setText(title);
        }
        smoothProgressBar.setVisibility(View.GONE);
    }
}
