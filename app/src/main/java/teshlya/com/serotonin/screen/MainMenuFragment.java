package teshlya.com.serotonin.screen;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.callback.ExistsUrlCallback;
import teshlya.com.serotonin.component.StickyNestedScrollView;
import teshlya.com.serotonin.model.SubscriptionsGroup;
import teshlya.com.serotonin.parse.ParseArticle;
import teshlya.com.serotonin.parse.ParseJsonSubscription;
import teshlya.com.serotonin.utils.Calc;
import teshlya.com.serotonin.utils.CheckUrlExists;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.utils.DrawableIcon;
import teshlya.com.serotonin.utils.Preference;


public class MainMenuFragment extends Fragment {

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    private Context context;
    private FrameLayout conteinerMainMenu;
    private View fullMainMenu;
    private View searchMainMenu;
    private EditText search;
    private ArrayList<SubscriptionsGroup> subscriptionsGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        context = view.getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        initView(view);
        initSearch(view);
        initRedditFeeds();
        initStarred();
        initSubscription();
    }

    private void initView(View view) {
        conteinerMainMenu = view.findViewById(R.id.conteiner_main_menu);
        fullMainMenu = LayoutInflater.from(context).inflate(R.layout.main_menu_full, null, false);
        searchMainMenu = LayoutInflater.from(context).inflate(R.layout.main_menu_search, null, false);
        conteinerMainMenu.addView(fullMainMenu);
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
                    searchCommunity(s.toString());
                } else {
                    conteinerMainMenu.removeAllViews();
                    conteinerMainMenu.addView(fullMainMenu);
                    ((StickyNestedScrollView) fullMainMenu.findViewById(R.id.scroll_view)).
                            fullScroll(NestedScrollView.FOCUS_UP);
                    search.requestFocus();
                    hint.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void searchCommunity(final String str) {
        if (conteinerMainMenu.findViewById(R.id.searchMenu) == null) {
            conteinerMainMenu.removeAllViews();
            conteinerMainMenu.addView(searchMainMenu);
            ((StickyNestedScrollView) searchMainMenu.findViewById(R.id.scroll_view)).
                    fullScroll(NestedScrollView.FOCUS_UP);
            search.requestFocus();
        }
        SortedSet<String> sortedSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

        for (SubscriptionsGroup group : subscriptionsGroups) {
            sortedSet.addAll(group.subscriptions);
        }
        sortedSet.addAll(Preference.starList);

        boolean listCloneContainsSubscruption = false;
        final ArrayList<String> listClone = new ArrayList<String>();
        for (String string : sortedSet) {
            if (string.toLowerCase().contains(str.toLowerCase())) {
                listClone.add(string);
                if (string.toLowerCase().equals(str.toLowerCase()))
                    listCloneContainsSubscruption = true;
            }
        }
        addGotoToMainMenu(listCloneContainsSubscruption, str);
        addSubredditsToSearchMenu(listClone);
    }

    private void addGotoToMainMenu(Boolean listContainsSubscruption, final String str) {
        TextView gotoTextView = searchMainMenu.findViewById(R.id.go_to);
        ListView gotoListView = searchMainMenu.findViewById(R.id.goto_list);

        if (!listContainsSubscruption) {
            gotoTextView.setVisibility(View.VISIBLE);
            gotoListView.setVisibility(View.VISIBLE);
            final ArrayList<String> gotoList = new ArrayList<String>();
            gotoList.add(str);

            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.main_menu_item, R.id.community_item, gotoList);
            gotoListView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(gotoListView);

            gotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    new CheckUrlExists(existsUrlCallback, str).execute();
                }
            });
        } else {
            gotoTextView.setVisibility(View.GONE);
            gotoListView.setVisibility(View.GONE);
        }
    }

    private void addSubredditsToSearchMenu(final ArrayList<String> subreddits) {
        TextView subredditsTextView = searchMainMenu.findViewById(R.id.subreddits);
        ListView subredditsListView = searchMainMenu.findViewById(R.id.search_list);

        if (subreddits.size() > 0) {
            subredditsTextView.setVisibility(View.VISIBLE);
            subredditsListView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.main_menu_item, R.id.community_item, subreddits);
            subredditsListView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(subredditsListView);

            subredditsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openCommunity("/r/" + subreddits.get(position) + "/", subreddits.get(position), true);
                }
            });
        } else {
            subredditsTextView.setVisibility(View.GONE);
            subredditsListView.setVisibility(View.GONE);
        }
    }

    private void initRedditFeeds() {

        ListView redditFeedsList = fullMainMenu.findViewById(R.id.reddit_feeds_list);

        String[] strings;
        strings = getResources().getStringArray(R.array.reddit_feeds);
        ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.main_menu_item, R.id.community_item, strings);
        redditFeedsList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(redditFeedsList);

        redditFeedsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "/";
                String community = "";
                Boolean star = false;
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
                        star = true;
                        break;
                }
                openCommunity(url, community, star);
            }
        });
    }

    private void initStarred() {
        if (Preference.starList != null && Preference.starList.size() > 0) {
            TextView textView = fullMainMenu.findViewById(R.id.starred);
            textView.setVisibility(View.VISIBLE);
            ListView listView = fullMainMenu.findViewById(R.id.starred_list);
            listView.setVisibility(View.VISIBLE);
            final ArrayList<String> starList = Preference.starList;
            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.main_menu_item, R.id.community_item, starList);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openCommunity("/r/" + starList.get(position) + "/", starList.get(position), true);
                }
            });
        }
    }

    private void initSubscription() {
        LinearLayout fullMainMenuConteiner = fullMainMenu.findViewById(R.id.full_main_menu_conteiner);
        subscriptionsGroups = new ParseJsonSubscription(getContext()).getSubscription();
        for (SubscriptionsGroup group : subscriptionsGroups) {
            fullMainMenuConteiner.addView(createTextView(group.title));
            fullMainMenuConteiner.addView(createListView(group.subscriptions));
        }
    }

    private View createTextView(String title) {
        TextView titleTextView = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, Calc.dpToPx(5), 0, 0);
        titleTextView.setLayoutParams(lp);
        titleTextView.setBackgroundColor(Color.WHITE);
        titleTextView.setText(title.toUpperCase());
        titleTextView.setTag("sticky");
        titleTextView.setTextAppearance(context, R.style.reddit_feeds);
        return titleTextView;
    }


    private View createListView(final ArrayList<String> subscriptions) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.main_menu_item, R.id.community_item, subscriptions);
        ListView listView = new ListView(context);
        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        listView.setDivider(null);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCommunity("/r/" + subscriptions.get(position) + "/", subscriptions.get(position), true);
            }
        });
        return listView;
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
                view.setLayoutParams(new LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private void openCommunity(String url, String community, Boolean star) {
        ParseArticle.hmap.clear();
        Intent data = new Intent();
        data.putExtra(Constants.URL, url);
        data.putExtra(Constants.COMMUNITY, community);
        data.putExtra(Constants.STAR, star);
        getActivity().setResult(getActivity().RESULT_OK, data);
        getActivity().finish();
    }

    private ExistsUrlCallback existsUrlCallback = new ExistsUrlCallback() {
        @Override
        public void sendResult(Boolean isExists, String subreddit) {
            if (isExists)
                openCommunity("/r/" + subreddit + "/", subreddit, true);
            else
                Toast.makeText(getActivity(), "Subreddit does not exist!", Toast.LENGTH_SHORT).show();
        }
    };
}
