package teshlya.com.reddit.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SubscriptionAdapter;
import teshlya.com.reddit.callback.CallbackCommunity;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.utils.DrawableIcon;

public class MainActivity extends AppCompatActivity implements CallbackCommunity {

    EditText search;
    ListView redditFeedsList;
    RecyclerView recyclerView;
    SubscriptionAdapter adapter;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        DrawableIcon.initAllIcons(this);
        initSearch();
        initRedditFeeds();
        initSubscription();
    }


    private void initSearch() {
        search = findViewById(R.id.search);
        final RelativeLayout hint = findViewById(R.id.hint);
        icon = findViewById(R.id.icon_search);
        icon.setImageDrawable(DrawableIcon.hintSearch);
        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    hint.setVisibility(View.INVISIBLE);
                } else {
                    hint.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRedditFeeds() {

        redditFeedsList = findViewById(R.id.reddit_feeds);

        String[] strings;
        strings = getResources().getStringArray(R.array.reddit_feeds);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.reddit_feeds_item, R.id.reddir_feeds_imet, strings);
        redditFeedsList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(redditFeedsList);

        redditFeedsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "/";
                String community = "";
                switch (position) {
                    case 0:
                        url = "/r/all";
                        community = "All";
                        break;
                    case 1:
                        url = "/";
                        community = "Front Page";
                        break;
                    case 2:
                        url = "/r/popular";
                        community = "Popular";
                        break;
                    case 3:
                        url = "/r/random";
                        community = "Random";
                        break;
                }
                openCommunity(url, community, view);
            }
        });
    }

    private void initSubscription() {
        recyclerView = findViewById(R.id.subscriptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SubscriptionAdapter(recyclerView);
        adapter.addSubscription(loadSubscriptionFromResource());
        recyclerView.setAdapter(adapter);
        /*String[] strings;
        strings = getResources().getStringArray(R.array.subscriptions_name);
        for (String str : strings) {
            new ParseCommunityDetails(str, getContext(), this).execute();
        }*/
    }

    private List<Subscription> loadSubscriptionFromResource() {
        List<Subscription> subscriptions = new ArrayList<>();
        String[] strings = getResources().getStringArray(R.array.subscriptions_name);
        TypedArray icons = getResources().obtainTypedArray(R.array.subscriptions_icon);
        for (int i = 0; i < strings.length; i++) {
            subscriptions.add(new Subscription(strings[i],
                    "/r/" + strings[i] + "/",
                    icons.getResourceId(i, -1)));
        }
        return subscriptions;
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void addCommunity(Subscription subscription) {
        adapter.addSubscription(subscription);
    }

    private void openCommunity(String url, String community, View view) {
        Intent myIntent = new Intent(this, ArticleActivity.class);
        myIntent.putExtra(Constants.URL, url);
        Rect rect = getPositionItem(view);
        myIntent.putExtra(Constants.LEFT, rect.left);
        myIntent.putExtra(Constants.RIGHT, rect.right);
        myIntent.putExtra(Constants.TOP, rect.top);
        myIntent.putExtra(Constants.BOTTOM, rect.bottom);

        myIntent.putExtra(Constants.COMMUNITY, community);
        startActivity(myIntent);
    }

    private Rect getPositionItem(View view) {
        int[] originalPos = new int[2];
        view.getLocationInWindow(originalPos);
        int x = originalPos[0];
        int y = originalPos[1];
        Rect rect = new Rect(x, y, x + view.getWidth(), y + view.getHeight());
        return rect;
    }

}
