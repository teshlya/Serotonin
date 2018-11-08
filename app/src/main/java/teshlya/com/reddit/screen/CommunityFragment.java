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

    public static CommunityFragment newInstance(String url, CommunityData data) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, data);
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        CommunityData data = getArgs();
        if (data != null) {
            initFab();
            initRecycler(view, data);
        }
        return view;
    }

    private CommunityData getArgs() {
        Bundle args = getArguments();
        CommunityData data;
        if (args != null) {
            data = (CommunityData) args.getSerializable(Constants.DATA);
            url = args.getString(Constants.URL);
            after = data.getAfter();
            return data;
        }
        return null;
    }

    private void initFab() {
        fab = getActivity().findViewById(R.id.fab);
        SwipePostAdapter.setFab(fab);
    }

    private void initRecycler(View view, CommunityData data) {
        recyclerView = view.findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conteinerSwipePostFragment = view.findViewById(R.id.conteinerSwipePostFragment);
        conteinerSwipePostFragment.setAnimationDurationMillis(800);
        conteinerSwipePostFragment.setPullToCollapseEnabled(false);
        adapter = new CommunityAdapter(recyclerView, conteinerSwipePostFragment, url);
        adapter.setHasStableIds(true);
        adapter.addArticle(data);
        recyclerView.setExpandablePage(conteinerSwipePostFragment);
        recyclerView.setTintPainter(new CompleteListTintPainter(Color.WHITE, 0.65F));
        recyclerView.setAdapter(adapter);
        scrollListenerCommunity = new ScrollListenerCommunity(fab, new OnLoadMoreCallback() {
            @Override
            public void onLoadMore() {
                if (after != null)
                    new ParseCommunity(CommunityFragment.this, Constants.DOMAIN + url + ".json" + "?after=" + after).execute();

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
    }

    public boolean back() {
        if (conteinerSwipePostFragment.isExpandedOrExpanding()) {
            recyclerView.collapse();
            return false;
        }
        return true;
    }

}
