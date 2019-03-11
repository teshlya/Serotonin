package teshlya.com.serotonin.screen;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.adapter.ArticleAdapter;
import teshlya.com.serotonin.adapter.ScrollListenerCallback;
import teshlya.com.serotonin.adapter.ScrollListenerSwipePost;
import teshlya.com.serotonin.adapter.SwipePostAdapter;
import teshlya.com.serotonin.callback.CallbackArticleLoaded;
import teshlya.com.serotonin.component.NestedScrollingParentRecyclerView;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.parse.ParseCommunity;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.utils.Preference;

public class SwipePostFragment extends Fragment implements CallbackArticleLoaded {

    private CommunityData data;
    private int position;
    private String title;
    private NestedScrollingParentRecyclerView recyclerView;
    private SwipePostAdapter adapter;
    private ScrollListenerSwipePost scrollListenerSwipePost;
    private String after;
    private Context context;
    private ImageView star_enabled;
    private ImageView star_disabled;

    public static SwipePostFragment newInstance(CommunityData data,
                                                int position) {
        SwipePostFragment fragment = new SwipePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.DATA, data);
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
        context = getContext();
        recyclerView = view.findViewById(R.id.rw);
        initArguments();
        initRecycler();
        if (FrontPageActivity.searchMode) {
            initTitle();
            initStar();
        }
        hidePopupMenuSort();
    }

    private void initStar() {
        star_enabled = getActivity().findViewById(R.id.star_enabled);
        star_disabled = getActivity().findViewById(R.id.star_disabled);
        setStar();
    }

    private void setStar() {
        star_enabled.setVisibility(View.GONE);
        star_disabled.setVisibility(View.GONE);
        if (Preference.starList.contains(title))
            star_enabled.setVisibility(View.VISIBLE);
        else
            star_disabled.setVisibility(View.VISIBLE);
    }

    private void hidePopupMenuSort()
    {
        getActivity().findViewById(R.id.sort).setVisibility(View.GONE);
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
                new ScrollListenerSwipePost(new ScrollListenerCallback() {
                    @Override
                    public void loadMore() {
                        if (after != null) {
                            new ParseCommunity(SwipePostFragment.this, buildUri()).execute();
                            adapter.showProgress();
                        }
                    }

                    Uri buildUri(){
                        Uri.Builder builderUri = Uri.parse(FrontPageActivity.builderUri.build().toString()).buildUpon();
                        builderUri.appendQueryParameter("after",after);
                        return builderUri.build();
                    }

                    @Override
                    public void releasePlayer() {
                        if (ArticleAdapter.mpdPlayerFragment != null) {
                            FragmentTransaction transaction = ((FrontPageActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.remove(ArticleAdapter.mpdPlayerFragment);
                            transaction.commit();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ArticleAdapter.mpdPlayerFragment = null;
                        }
                    }

                    @Override
                    public void setTitleInSearchMode(int curentPositionItem) {
                        if (FrontPageActivity.searchMode) {
                            title = adapter.getArticles().get(curentPositionItem).getSubreddit();
                            ((TextView) getActivity().findViewById(R.id.community_title)).
                                    setText(title);
                            setStar();
                        }
                    }
                });
        recyclerView.addOnScrollListener(scrollListenerSwipePost);
    }

    private void initTitle() {
        title = adapter.getArticles().get(position).getSubreddit();

        ((TextView) getActivity().findViewById(R.id.community_title)).
                setText(title);
    }

    private void initArguments() {
        data = (CommunityData) getArguments().getSerializable(Constants.DATA);
        position = getArguments().getInt(Constants.POSITION);
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

    public void scrollRecyclerViewUp()
    {
        if (this.recyclerView != null)
        {
            int i = ((LinearLayoutManager)this.recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            SwipePostAdapter.SwipePostViewHolder localSwipePostViewHolder = (SwipePostAdapter.SwipePostViewHolder)this.recyclerView.findViewHolderForLayoutPosition(i);
            if (localSwipePostViewHolder != null) {
                localSwipePostViewHolder.scrollRecyclerViewUp();
            }
        }
    }


}
