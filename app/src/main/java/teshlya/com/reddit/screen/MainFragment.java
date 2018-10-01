package teshlya.com.reddit.screen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SubscriptionAdapter;
import teshlya.com.reddit.callback.CallbackCommunity;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.parse.ParseCommunityDetails;


public class MainFragment extends Fragment implements CallbackCommunity {

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
        view.findViewById(R.id.reddit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApticlePage();
            }
        });

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
    }

    private void initSubscription(View view) {
        subscription = view.findViewById(R.id.subscriptions);
        subscription.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SubscriptionAdapter();
        subscription.setAdapter(adapter);
        String[] strings;
        strings = getResources().getStringArray(R.array.subscriptions);
        for (String str : strings) {
            new ParseCommunityDetails(str, getContext(), this).execute();
        }
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

    public void openApticlePage() {
        Intent myIntent = new Intent(getActivity(), ArticleActivity.class);
        startActivity(myIntent);

    }

}
