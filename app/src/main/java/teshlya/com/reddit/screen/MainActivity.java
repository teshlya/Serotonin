package teshlya.com.reddit.screen;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import teshlya.com.reddit.R;

public class MainActivity extends AppCompatActivity  {

    DrawerLayout mainDrawerL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        initToolbar();
        initMainFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mainDrawerL = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawerL, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainDrawerL.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initMainFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_fragment, new MainFragment());
        ft.commit();
    }

}
