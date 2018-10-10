package teshlya.com.reddit.screen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.callback.CallbackComment;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.model.CommentData;
import teshlya.com.reddit.parse.ParseArticle;

public class SwipePostFragment extends Fragment {


    private ArrayList<ArticleData> data;
    private int position;
    private RecyclerView recyclerView;
    SwipePostAdapter adapter;
    Context context;

    public SwipePostFragment() {
    }

    public static SwipePostFragment newInstance(ArrayList<ArticleData> list, int position) {
        SwipePostFragment fragment = new SwipePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, list);
        args.putInt(Constants.POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
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
        adapter.addArticle(data);
        recyclerView.setAdapter(adapter);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(position);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).setItemPrefetchEnabled(true);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).setInitialPrefetchItemCount(20);
    }

    private void initArguments() {
        data = (ArrayList<ArticleData>) getArguments().getSerializable(Constants.DATA);
        position = getArguments().getInt(Constants.POSITION);
    }

}
