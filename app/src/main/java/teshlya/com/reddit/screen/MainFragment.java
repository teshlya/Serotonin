package teshlya.com.reddit.screen;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SubscriptionAdapter;
import teshlya.com.reddit.callback.CallbackCommunity;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.parse.ParseCommunityDetails;


public class MainFragment extends Fragment implements CallbackCommunity {

    public static final String API_KEY = "AIzaSyAZvE-JnqB5lF2IWb1DUhCYpR6GcZpQ0c4";


    EditText search;
    ListView redditFeedsList;
    RecyclerView subscription;
    SubscriptionAdapter adapter;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        initSearch(view);
        initRedditFeeds(view);
        initSubscription(view);
    }

    private void initRedditFeeds(View view) {
        redditFeedsList = view.findViewById(R.id.reddit_feeds);
        String[] strings;
        strings = getResources().getStringArray(R.array.reddit_feeds);
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.reddit_feeds_item, R.id.list_content, strings);
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
                        //url = "/r/explore";
                        community = "Explore";
                        break;
                }
                openCommunity(url, community);
            }
        });
    }

    private void initSubscription(View view) {
        subscription = view.findViewById(R.id.subscriptions);
        subscription.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SubscriptionAdapter();
        adapter.addSubscription(loadSubscriptionFromResource());
        subscription.setAdapter(adapter);
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

    private void initSearch(View view) {
        search = view.findViewById(R.id.search);
        search.setHint("\uD83D\uDD0D " + getResources().getString(R.string.search_reddit));
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

    private void openCommunity(String url, String community) {
        Intent myIntent = new Intent(getContext(), ArticleActivity.class);
        myIntent.putExtra(Constants.URL, url);
        myIntent.putExtra(Constants.COMMUNITY, community);
        getContext().startActivity(myIntent);
    }

}
