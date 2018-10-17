package teshlya.com.reddit.screen;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.callback.CallbackArticle;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.parse.ParseCommunity;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CommunityFragment extends Fragment implements CallbackArticle {

    private RecyclerView recyclerView;
    private CommunityAdapter adapter;
    private static String url;
    private static String domain = "https://www.reddit.com";
    private String comunnity;

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
        new ParseCommunity(this, domain + url + ".json", getContext()).execute();

    }

    @Override
    public void addArticles(ArrayList<ArticleData> articles) {
        adapter = new CommunityAdapter(recyclerView, comunnity);
        adapter.addArticle(articles);
        recyclerView.setAdapter(adapter);
    }

}
