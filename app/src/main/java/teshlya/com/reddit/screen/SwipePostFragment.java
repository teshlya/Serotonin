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

import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.model.ArticleData;

public class SwipePostFragment extends Fragment {


    private ArrayList<ArticleData> data;
    private int position;
    private RecyclerView recyclerView;
    private SwipePostAdapter adapter;


    public static SwipePostFragment newInstance(ArrayList<ArticleData> list,
                                                int position) {
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void initArguments() {
        data = (ArrayList<ArticleData>) getArguments().getSerializable(Constants.DATA);
        position = getArguments().getInt(Constants.POSITION);
    }
}
