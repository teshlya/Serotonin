package teshlya.com.reddit.screen;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.callback.CallbackArticle;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.parse.ParseCommunity;
import teshlya.com.reddit.utils.Constants;

@SuppressLint("ValidFragment")
public class CommunityFragment extends Fragment implements CallbackArticle {

    private InboxRecyclerView recyclerView;
    private CommunityAdapter adapter;
    private static String url;
    private static String domain = "https://www.reddit.com";
    private ExpandablePageLayout conteinerSwipePostFragment;
    private FloatingActionButton fab;

    public CommunityFragment() {
    }

    public static CommunityFragment newInstance(String url) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args != null) {
            url = args.getString(Constants.URL, "");
        }
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        conteinerSwipePostFragment = view.findViewById(R.id.conteinerSwipePostFragment);
        fab = getActivity().findViewById(R.id.fab);
        SwipePostAdapter.setFab(fab);
        initRecycler(view);
        new ParseCommunity(this, domain + url + ".json", getContext()).execute();
    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && FrontPageActivity.shown) {
                    FrontPageActivity.shown = false;
                    fab.hide();
                } else if (dy < 0 && !FrontPageActivity.shown) {
                    FrontPageActivity.shown = true;
                    fab.show();
                }
            }
        });
    }

    @Override
    public void addArticles(ArrayList<ArticleData> articles) {
        adapter = new CommunityAdapter(recyclerView, conteinerSwipePostFragment);
        adapter.setHasStableIds(true);
        adapter.addArticle(articles);
        conteinerSwipePostFragment.setAnimationDurationMillis(800);
        conteinerSwipePostFragment.setPullToCollapseEnabled(false);
        recyclerView.setExpandablePage(conteinerSwipePostFragment);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));
        recyclerView.setAdapter(adapter);
    }

    public boolean back() {
        if (conteinerSwipePostFragment.isExpandedOrExpanding()) {
            recyclerView.collapse();
            return false;
        }
        return true;
    }

}
