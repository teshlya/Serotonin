package teshlya.com.reddit.screen;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.callback.CallbackArticle;
import teshlya.com.reddit.callback.CallbackBack;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.parse.ParseCommunity;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CommunityFragment extends Fragment implements CallbackArticle, CallbackBack {

    private InboxRecyclerView recyclerView;
    private CommunityAdapter adapter;
    private static String url;
    private static String domain = "https://www.reddit.com";
    private String comunnity;
    ExpandablePageLayout conteinerSwipePostFragment;

    public CommunityFragment() {
    }

    public static CommunityFragment newInstance(String url, String comunnity) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        args.putString(Constants.COMMUNITY, comunnity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            url = args.getString(Constants.URL, "");
            comunnity = args.getString(Constants.COMMUNITY, "");
        }

        View view = inflater.inflate(R.layout.fragment_community, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conteinerSwipePostFragment = view.findViewById(R.id.conteinerSwipePostFragment);
        new ParseCommunity(this, domain + url + ".json", getContext()).execute();

    }

    @Override
    public void addArticles(ArrayList<ArticleData> articles) {
        adapter = new CommunityAdapter(recyclerView, comunnity, conteinerSwipePostFragment, this);
        adapter.setHasStableIds(true);
        adapter.addArticle(articles);
        conteinerSwipePostFragment.setAnimationDurationMillis(800);
        conteinerSwipePostFragment.setPullToCollapseEnabled(false);
        recyclerView.setExpandablePage(conteinerSwipePostFragment);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean back() {
        if (conteinerSwipePostFragment.isExpandedOrExpanding()) {
            recyclerView.collapse();
            return false;
        }
        return true;
    }
}
