package teshlya.com.serotonin.screen;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.adapter.MainMenuAdapter;
import teshlya.com.serotonin.adapter.StickHeaderItemDecoration;
import teshlya.com.serotonin.callback.ExistsUrlCallback;
import teshlya.com.serotonin.component.StickyNestedScrollView;
import teshlya.com.serotonin.model.DataMainMenu;
import teshlya.com.serotonin.model.SubscriptionsGroup;
import teshlya.com.serotonin.parse.ParseArticle;
import teshlya.com.serotonin.parse.ParseJsonSubscription;
import teshlya.com.serotonin.utils.CheckUrlExists;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.utils.DrawableIcon;
import teshlya.com.serotonin.utils.Preference;


public class MainMenuFragment extends Fragment {

    private Context context;
    private FrameLayout conteinerMainMenu;
    private View fullMainMenu;
    private View searchMainMenu;
    private EditText search;
    private ArrayList<SubscriptionsGroup> subscriptionsGroups;
    private int clickState;
    private ImageView searchButton;
    private ArrayList<DataMainMenu> list;
    private RecyclerView recyclerView;
    private static int positionScroll = 0;

    public static MainMenuFragment newInstance(int clickState) {
        Bundle bundle = new Bundle();
        bundle.putInt("click", clickState);
        MainMenuFragment fragment = new MainMenuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        context = view.getContext();
        init(view);
        return view;
    }

    private void init(View view) {
        getArgs();
        initView(view);
        initSearch(view);
        fillList();
        initListMenu();
        initQuestion(view);
    }


    private void initQuestion(View view) {
        view.findViewById(R.id.question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseArticle.hmap.clear();
                getActivity().setResult(Constants.RESULT_QUESTION, null);
                getActivity().finish();
            }
        });
    }

    public void fillList() {
        list = new ArrayList<>();
        initRedditFeeds();
        initStarred();
        initSubscription();
    }

    private void initRedditFeeds() {

        list.add(new DataMainMenu("REDDIT FEEDS", "feeds", 1));
        list.add(new DataMainMenu("All", "/r/all/", 0));
        list.add(new DataMainMenu("Front page", "/", 0));
        list.add(new DataMainMenu("Popular", "/r/popular/", 0));
        list.add(new DataMainMenu("Random", "/r/random/", 0));
    }

    private void initStarred() {
        if (Preference.starList != null && Preference.starList.size() > 0) {
            list.add(new DataMainMenu("STARRED", "star", 1));
            for (String community : Preference.starList)
                list.add(new DataMainMenu(community, "/r/" + community + "/", 0));
        }
    }

    private void initSubscription() {
        subscriptionsGroups = new ParseJsonSubscription(context).getSubscription();
        for (SubscriptionsGroup group : subscriptionsGroups) {
            list.add(new DataMainMenu(group.title, group.icon, 1));
            for (String community : group.subscriptions)
                list.add(new DataMainMenu(community, "/r/" + community + "/", 0));
        }
    }

    private void getArgs() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            clickState = bundle.getInt("click", 0);
        } else{
            clickState = 0;
        }
    }

    private void initView(View view) {
        conteinerMainMenu = view.findViewById(R.id.conteiner_main_menu);
        fullMainMenu = LayoutInflater.from(context).inflate(R.layout.main_menu_full, null, false);
        searchMainMenu = LayoutInflater.from(context).inflate(R.layout.main_menu_search, null, false);
        conteinerMainMenu.addView(fullMainMenu);
    }

    private void initSearch(View view) {
        initSearchView(view);
        initAnimateSearch(view);
    }

    private void initSearchView(View view) {
        search = view.findViewById(R.id.search);
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
                    searchCommunity(s.toString());
                } else {
                    conteinerMainMenu.removeAllViews();
                    conteinerMainMenu.addView(fullMainMenu);
                    //((StickyNestedScrollView) fullMainMenu.findViewById(R.id.scroll_view)).
                    //        fullScroll(NestedScrollView.FOCUS_UP);
                    search.requestFocus();
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

    private void initAnimateSearch(View view) {
        setPositionSearch(view);
        setShowAnimateSearch(view);
        setHideAnimateSearch(view);
    }

    private void setPositionSearch(View view) {
        final RelativeLayout conteinerSearch = view.findViewById(R.id.conteiner_search);
        ((ImageView) view.findViewById(R.id.arrow_hide_search)).setImageDrawable(DrawableIcon.clearMenuSearch);

        conteinerSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                conteinerSearch.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(conteinerSearch.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(conteinerSearch.getWidth(), 0, 0, 0);
                conteinerSearch.setLayoutParams(params);
                if (clickState == Constants.LONG_CLICK){
                    searchButton.performClick();
                }
            }
        });
    }

    private void setShowAnimateSearch(View view) {
        final RelativeLayout conteinerSearch = view.findViewById(R.id.conteiner_search);
        searchButton = view.findViewById(R.id.search_button);
        final LinearLayout conteinerTitle = view.findViewById(R.id.conteiner_title);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conteinerTitle.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                search.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
                View bottom_sheet = getActivity().findViewById(R.id.bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                ObjectAnimator animX = ObjectAnimator.ofFloat(conteinerSearch,
                        View.TRANSLATION_X, 0, -conteinerSearch.getWidth());
                animX.setDuration(300);
                animX.start();
            }
        });
    }

    private void setHideAnimateSearch(View view) {
        final RelativeLayout conteinerSearch = view.findViewById(R.id.conteiner_search);
        final ImageView searchButton = view.findViewById(R.id.search_button);
        final LinearLayout conteinerTitle = view.findViewById(R.id.conteiner_title);
        view.findViewById(R.id.arrow_hide_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.getText().clear();
                conteinerTitle.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                ObjectAnimator animX = ObjectAnimator.ofFloat(conteinerSearch,
                        View.TRANSLATION_X, -conteinerSearch.getWidth(), 0);
                animX.setDuration(300);
                animX.start();
            }
        });
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

    private void initListMenu() {
        recyclerView = fullMainMenu.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainMenuAdapter adapter = new MainMenuAdapter(list, context);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new StickHeaderItemDecoration(adapter));
        recyclerView.scrollToPosition(positionScroll);
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
        getActivity().setResult(Constants.RESULT_SABREDDIT, data);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            positionScroll = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }
    }
}
