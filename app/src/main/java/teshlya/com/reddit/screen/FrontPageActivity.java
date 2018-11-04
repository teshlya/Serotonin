package teshlya.com.reddit.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import teshlya.com.reddit.R;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.utils.DrawableIcon;

public class FrontPageActivity extends AppCompatActivity {

    private CommunityFragment communityFragment;
    private String Url = "/";
    private FloatingActionButton fab;
    private Context context;
    private TextView titleTextView;
    public static boolean shownFab = true;
    private SmoothProgressBar smoothProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        init();
    }

    private void init() {
        context = this;
        DrawableIcon.initAllIcons(this);
        openCommunityFragment(Url);
        initFab();
        initTitle();
    }

    private void openCommunityFragment(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        communityFragment = CommunityFragment.newInstance(url);
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

    private void openCommunity(String url, String title) {
        openCommunityFragment(url);
        titleTextView.setText(title);
    }

    private void initTitle() {
        titleTextView = findViewById(R.id.community_title);
    }

    private void initProgressBar() {
        smoothProgressBar = findViewById(R.id.progress_bar_communuty);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String ulr = data.getStringExtra(Constants.URL);
                String community = data.getStringExtra(Constants.COMMUNITY);
                if (ulr != null && community != null) {
                }
                openCommunity(ulr, community);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (communityFragment.back()) {
            super.onBackPressed();
        }
    }
}
