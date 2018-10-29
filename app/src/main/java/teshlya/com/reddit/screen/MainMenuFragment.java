package teshlya.com.reddit.screen;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SubscriptionAdapter;
import teshlya.com.reddit.callback.CallbackCommunity;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.parse.ParseArticle;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.utils.DrawableIcon;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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


public class MainMenuFragment extends Fragment implements CallbackCommunity {


    public MainMenuFragment() {
    }

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    private EditText search;
    private ListView redditFeedsList;
    private RecyclerView recyclerView;
    private SubscriptionAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        context = view.getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        initSearch(view);
        initRedditFeeds(view);
        initSubscription(view);
    }


    private void initSearch(View view) {
        search = view.findViewById(R.id.search);
        final RelativeLayout hint = view.findViewById(R.id.hint);
        ImageView icon = view.findViewById(R.id.icon_search);
        icon.setImageDrawable(DrawableIcon.hintSearch);
        search.addTextChangedListener(new TextWatcher() {
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

    private void initRedditFeeds(View view) {

        redditFeedsList = view.findViewById(R.id.reddit_feeds);

        String[] strings;
        strings = getResources().getStringArray(R.array.reddit_feeds);
        ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.reddit_feeds_item, R.id.reddir_feeds_imet, strings);
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

    private void initSubscription(View view) {
        recyclerView = view.findViewById(R.id.subscriptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

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
        /*Intent myIntent = new Intent(context, ArticleActivity.class);
        myIntent.putExtra(Constants.URL, url);
        Rect rect = getPositionItem(view);
        myIntent.putExtra(Constants.LEFT, rect.left);
        myIntent.putExtra(Constants.RIGHT, rect.right);
        myIntent.putExtra(Constants.TOP, rect.top);
        myIntent.putExtra(Constants.BOTTOM, rect.bottom);

        myIntent.putExtra(Constants.COMMUNITY, community);
        startActivity(myIntent);*/
        ParseArticle.hmap.clear();
        Intent data = new Intent();
        data.putExtra(Constants.URL, url);
        data.putExtra(Constants.COMMUNITY, community);
        getActivity().setResult(getActivity().RESULT_OK, data);
        getActivity().finish();
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
