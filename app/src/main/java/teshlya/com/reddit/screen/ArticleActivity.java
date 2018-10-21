package teshlya.com.reddit.screen;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;

public class ArticleActivity extends AppCompatActivity {

    CommunityFragment communityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        init();
    }

    private void init() {
        String url = getIntent().getExtras().getString(Constants.URL, "");
        String community = getIntent().getExtras().getString(Constants.COMMUNITY, "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        communityFragment = CommunityFragment.newInstance(url, community);
        ft.replace(R.id.fragment_article, communityFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (communityFragment.onBack())
        super.onBackPressed();
    }
}
