package teshlya.com.reddit.screen;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import teshlya.com.reddit.adapter.OnLoadMoreCallback;
import teshlya.com.reddit.adapter.ScrollListenerSwipePost;
import teshlya.com.reddit.callback.CallbackArticleLoaded;
import teshlya.com.reddit.model.CommunityData;
import teshlya.com.reddit.parse.ParseCommunity;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.model.ArticleData;

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
                        if (after != null)
                            new ParseCommunity(SwipePostFragment.this, Constants.DOMAIN + url + ".json" + "?after=" + after).execute();

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
        adapter.addArticle(data.getArticles());
        after = data.getAfter();
        scrollListenerSwipePost.setLoaded();
    }
}
