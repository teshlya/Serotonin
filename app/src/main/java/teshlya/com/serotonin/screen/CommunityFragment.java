package teshlya.com.serotonin.screen;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.dimming.CompleteListTintPainter;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.adapter.CommunityAdapter;
import teshlya.com.serotonin.adapter.ScrollListenerCallback;
import teshlya.com.serotonin.adapter.ScrollListenerCommunity;
import teshlya.com.serotonin.adapter.SwipePostAdapter;
import teshlya.com.serotonin.callback.CallbackArticleLoaded;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.parse.ParseCommunity;
import teshlya.com.serotonin.utils.Constants;

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
        scrollListenerCommunity = new ScrollListenerCommunity(fab, new ScrollListenerCallback() {
            @Override
            public void loadMore() {
                if (after != null) {
                    new ParseCommunity(CommunityFragment.this, Constants.DOMAIN + url + ".json" + "?after=" + after).execute();
                    adapter.showProgress();
                }
            }

            @Override
            public void releasePlayer() {

            }
        });
        recyclerView.addOnScrollListener(scrollListenerCommunity);
    }

    @Override
    public void addArticles(CommunityData data) {
        adapter.hideProgress();
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
