package teshlya.com.reddit.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.model.ArticleData;

public class SwipePostFragment extends Fragment {


    private ArrayList<ArticleData> data;
    private int position;
    private RecyclerView recyclerView;
    private SwipePostAdapter adapter;
    private TextView communityTextView;
    private ImageView homeImageView;
    private String community;


    public static SwipePostFragment newInstance(ArrayList<ArticleData> list, int position, String community) {
        SwipePostFragment fragment = new SwipePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, list);
        args.putInt(Constants.POSITION, position);
        args.putString(Constants.COMMUNITY, community);
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
        communityTextView = view.findViewById(R.id.community);
        communityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        homeImageView = view.findViewById(R.id.home);
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        initArguments();
        communityTextView.setText(community);
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
    }

    private void initArguments() {
        data = (ArrayList<ArticleData>) getArguments().getSerializable(Constants.DATA);
        position = getArguments().getInt(Constants.POSITION);
        community = getArguments().getString(Constants.COMMUNITY);
    }


    public void goBack() {
        getFragmentManager().popBackStack();
    }
}
