package teshlya.com.reddit.screen;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import teshlya.com.reddit.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.add(R.id.fragment_article, new ArticleFragment());
        ft.add(R.id.fragment_article, new CommunityFragment());
        ft.commit();
    }
}
