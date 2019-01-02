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
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.RequiresApi;
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
    private String period = "";
    private PopupMenu popupMenu;
    private String sort = "hot";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void init() {
        context = this;
        DrawableIcon.initAllIcons(this);
        Preference.getStarFromSharedPrefs(this);
        initFab();
        initTitle();
        initStar();
        initToolbar();
        initPopupMenu();
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

    private void initToolbar()
    {
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                if (FrontPageActivity.this.communityFragment != null) {
                    FrontPageActivity.this.communityFragment.scrollRecyclerViewUp();
                }
            }
        });
    }

    private void initTitle() {
        titleTextView = findViewById(R.id.community_title);
    }

    private void initMenu(String sort)
    {
        this.sort = sort;
        this.period = "";
        parseCommunity(this.url);
        popupMenu.getMenu().findItem(R.id.controversial_none).setChecked(true);
        popupMenu.getMenu().findItem(R.id.top_none).setChecked(true);
    }

    @RequiresApi(api=22)
    private void initPopupMenu()
    {
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupTheme);
        popupMenu = new PopupMenu(wrapper, findViewById(R.id.align_menu), Gravity.NO_GRAVITY, 0, R.style.MyPopupTheme);
        popupMenu.inflate(R.menu.popup_menu_sort);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    default:
                        return false;
                    case R.id.top_year:
                        initSubMenu("top", "year");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_year);
                        return true;
                    case R.id.top_week:
                        initSubMenu("top", "week");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_week);
                        return true;
                    case R.id.top_month:
                        initSubMenu("top", "month");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_month);
                        return true;
                    case R.id.top_hour:
                        initSubMenu("top", "hour");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_hour);
                        return true;
                    case R.id.top_day:
                        initSubMenu("top", "day");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_day);
                        return true;
                    case R.id.top_all:
                        initSubMenu("top", "all");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.top_all);
                        return true;
                    case R.id.rising:
                        initMenu("rising");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.rising);
                        return true;
                    case R.id.new_:
                        initMenu("new");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.new_);
                        return true;
                    case R.id.hot:
                        initMenu("hot");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.hot);
                        return true;
                    case R.id.controversial_year:
                        initSubMenu("controversial", "year");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_year);
                        return true;
                    case R.id.controversial_week:
                        initSubMenu("controversial", "week");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_week);
                        return true;
                    case R.id.controversial_month:
                        initSubMenu("controversial", "month");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_month);
                        return true;
                    case R.id.controversial_hour:
                        initSubMenu("controversial", "hour");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_hour);
                        return true;
                    case R.id.controversial_day:
                        initSubMenu("controversial", "day");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_day);
                        return true;
                    case R.id.controversial_all:
                        initSubMenu("controversial", "all");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.controversial_all);
                        return true;
                    case R.id.best:
                        initMenu("best");
                        menuItem.setChecked(true);
                        Preference.savePopupMenuToSharedPrefs(context, R.id.best);
                        return true;
                }
            }
        });
        findViewById(R.id.sort).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                FrontPageActivity.this.popupMenu.show();
            }
        });
        setCheckedPopupMenu();
    }

    private void setCheckedControversial()
    {
        this.sort = "controversial";
        this.popupMenu.getMenu().findItem(R.id.controversial).setChecked(true);
    }

    private void setCheckedPopupMenu()
    {
        int idCheckedButton = Preference.getPopupmenuFromSharedPrefs(this);
        this.popupMenu.getMenu().findItem(idCheckedButton).setChecked(true);
        switch (idCheckedButton)
        {
            case R.id.top_year:
                this.period = "?t=year";
                setCheckedTop();
                return;
            case R.id.top_week:
                this.period = "?t=week";
                setCheckedTop();
                return;
            case R.id.top_month:
                this.period = "?t=month";
                setCheckedTop();
                return;
            case R.id.top_hour:
                this.period = "?t=hour";
                setCheckedTop();
                return;
            case R.id.top_day:
                this.period = "?t=day";
                setCheckedTop();
                return;
            case R.id.top_all:
                this.period = "?t=all";
                setCheckedTop();
                return;
            case R.id.rising:
                this.sort = "rising";
                return;
            case R.id.new_:
                this.sort = "new";
                return;
            case R.id.hot:
                this.sort = "hot";
                return;
            case R.id.best:
                this.sort = "best";
                return;
            case R.id.controversial_year:
                this.period = "?t=year";
                setCheckedControversial();
                return;
            case R.id.controversial_week:
                this.period = "?t=week";
                setCheckedControversial();
                return;
            case R.id.controversial_month:
                this.period = "?t=month";
                setCheckedControversial();
                return;
            case R.id.controversial_hour:
                this.period = "?t=hour";
                setCheckedControversial();
                return;
            case R.id.controversial_day:
                this.period = "?t=day";
                setCheckedControversial();
                return;
            case R.id.controversial_all:
                this.period = "?t=all";
                setCheckedControversial();
                return;
        }
    }

    private void setCheckedTop()
    {
        this.sort = "top";
        this.popupMenu.getMenu().findItem(R.id.top).setChecked(true);
    }

    private void initSubMenu(String sort, String period)
    {
        this.sort = sort;
        this.period = "?t=" + period;
        parseCommunity(this.url);
        if (sort.equals("controversial"))
            popupMenu.getMenu().findItem(R.id.controversial).setChecked(true);
        if (sort.equals("top"))
            popupMenu.getMenu().findItem(R.id.top).setChecked(true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.sort).setVisibility(View.VISIBLE);
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
        new ParseCommunity(this, Constants.DOMAIN + url + sort+ ".json" + period).execute();
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
