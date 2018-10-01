package teshlya.com.reddit.screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.CommunityAdapter;
import teshlya.com.reddit.callback.CallbackArticle;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.parse.ParseCommunity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment implements CallbackArticle {

    RecyclerView recyclerView;
    CommunityAdapter adapter;

    public CommunityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.community_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new ParseCommunity(this, "https://www.reddit.com/r/android.json", getContext()).execute();

    }

    @Override
    public void addArticles(ArrayList<ArticleData> articles) {
        adapter = new CommunityAdapter();
        adapter.addArticle(articles);
        recyclerView.setAdapter(adapter);
    }
}
