package teshlya.com.serotonin.screen;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teshlya.com.serotonin.adapter.OnLoadMoreCallback;
import teshlya.com.serotonin.adapter.ScrollListenerSwipePost;
import teshlya.com.serotonin.callback.CallbackArticleLoaded;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.parse.ParseCommunity;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.adapter.SwipePostAdapter;

public class SwipePostFragment extends Fragment implements CallbackArticleLoaded {

    private String url;
    private CommunityData data;
    private int position;
    private RecyclerView recyclerView;
    private SwipePostAdapter adapter;
    private ScrollListenerSwipePost scrollListenerSwipePost;
    private String after;


    public static SwipePostFragment newInstance(CommunityData data,
                                                String url,
                                                int position) {
        SwipePostFragment fragment = new SwipePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, data);
        args.putInt(Constants.POSITION, position);
        args.putString(Constants.URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_post, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.rw);
        initArguments();
        initRecycler();
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new SwipePostAdapter();
        adapter.addArticle(data.getArticles());
        data = null;
        recyclerView.setAdapter(adapter);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(position);
        scrollListenerSwipePost =
                new ScrollListenerSwipePost(new OnLoadMoreCallback() {
                    @Override
                    public void onLoadMore() {
                        if (after != null) {
                            new ParseCommunity(SwipePostFragment.this, Constants.DOMAIN + url + ".json" + "?after=" + after).execute();
                            adapter.showProgress();
                        }

                    }
                });
        recyclerView.addOnScrollListener(scrollListenerSwipePost);

    }

    private void initArguments() {
        data = (CommunityData) getArguments().getSerializable(Constants.DATA);
        position = getArguments().getInt(Constants.POSITION);
        url = getArguments().getString(Constants.URL);
        after = data.getAfter();
    }

    @Override
    public void addArticles(CommunityData data) {
        adapter.hideProgress();
        if (data != null) {
            adapter.addArticle(data.getArticles());
            after = data.getAfter();
        }
        scrollListenerSwipePost.setLoaded();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerView.setAdapter(null);
    }
}
