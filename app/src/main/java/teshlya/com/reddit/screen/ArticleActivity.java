package teshlya.com.reddit.screen;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;

public class ArticleActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init() {
        url = getIntent().getExtras().getString(Constants.URL);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_article, CommunityFragment.newInstance(url));
        ft.commit();
    }
}
