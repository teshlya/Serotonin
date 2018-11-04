package teshlya.com.reddit.screen;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.adapter.OnLoadMoreCallback;
import teshlya.com.reddit.adapter.ScrollListenerCommunity;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.callback.CallbackArticleLoaded;
import teshlya.com.reddit.model.CommunityData;
import teshlya.com.reddit.parse.ParseCommunity;
import teshlya.com.reddit.utils.Constants;

@SuppressLint("ValidFragment")
public class CommunityFragment extends Fragment implements CallbackArticleLoaded {

    private InboxRecyclerView recyclerView;
    private CommunityAdapter adapter;
    private String url;
    private ExpandablePageLayout conteinerSwipePostFragment;
    private FloatingActionButton fab;
    private ScrollListenerCommunity scrollListenerCommunity;
    private String after = null;
    private SmoothProgressBar smoothProgressBar;


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
        initFab();
        initProgressBar();
        initRecycler(view);
        new ParseCommunity(this, Constants.DOMAIN + url + ".json", getContext()).execute();
    }

    private void initFab(){
        fab = getActivity().findViewById(R.id.fab);
        SwipePostAdapter.setFab(fab);
    }

    private void initProgressBar() {
        smoothProgressBar = getActivity().findViewById(R.id.progress_bar_communuty);
    }

    private void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommunityAdapter(recyclerView, conteinerSwipePostFragment, url);
        adapter.setHasStableIds(true);
        conteinerSwipePostFragment = view.findViewById(R.id.conteinerSwipePostFragment);
        conteinerSwipePostFragment.setAnimationDurationMillis(800);
        conteinerSwipePostFragment.setPullToCollapseEnabled(false);
        recyclerView.setExpandablePage(conteinerSwipePostFragment);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));
        recyclerView.setAdapter(adapter);
        scrollListenerCommunity = new ScrollListenerCommunity(fab, new OnLoadMoreCallback() {
            @Override
            public void onLoadMore() {
                if (after != null)
                    new ParseCommunity(CommunityFragment.this, Constants.DOMAIN + url + ".json" + "?after=" + after, getContext()).execute();

            }
        });
        recyclerView.addOnScrollListener(scrollListenerCommunity);
    }

    @Override
    public void addArticles(CommunityData data) {
        if (data != null) {
            adapter.addArticle(data);
            after = data.getAfter();
        }
        scrollListenerCommunity.setLoaded();
        smoothProgressBar.setVisibility(View.GONE);
    }

    public boolean back() {
        if (conteinerSwipePostFragment.isExpandedOrExpanding()) {
            recyclerView.collapse();
            return false;
        }
        return true;
    }

}
