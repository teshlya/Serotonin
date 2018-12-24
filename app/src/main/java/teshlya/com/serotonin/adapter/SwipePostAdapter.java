package teshlya.com.serotonin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.holder.LoadingViewHolder;
import teshlya.com.serotonin.model.ArticleData;
import teshlya.com.serotonin.model.PlayState;
import teshlya.com.serotonin.parse.ParseArticle;
import teshlya.com.serotonin.screen.FrontPageActivity;
import teshlya.com.serotonin.screen.MpdPlayerFragment;
import teshlya.com.serotonin.utils.MpdPlayer;

import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_ITEM;
import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_LOADING;


public class SwipePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String domain = "https://www.reddit.com";
    private Context context;
    private ArrayList<ArticleData> articles = new ArrayList<>();
    private static FloatingActionButton fab;


    public static void setFab(FloatingActionButton fab) {
        SwipePostAdapter.fab = fab;
    }

    public void addArticle(List<ArticleData> articles) {
        int listSize = this.articles.size();
        int addCount = articles.size();
        this.articles.addAll(articles);
        notifyItemRangeChanged(listSize, addCount);
    }

    @Override
    public int getItemViewType(int position) {
        return articles.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        RecyclerView.ViewHolder holder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            holder = createSwipePostViewHolder(viewGroup);
        } else if (viewType == VIEW_TYPE_LOADING)
            holder = createLoadingViewHolder(viewGroup);
        return holder;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    private SwipePostViewHolder createSwipePostViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.swipe_post_item, viewGroup, false);
        return new SwipePostViewHolder(view);
    }

    private LoadingViewHolder createLoadingViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup
                .getContext()).inflate(R.layout.item_loading_full_screen, viewGroup, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SwipePostViewHolder) {
            ((SwipePostViewHolder) holder).bind(articles.get(position), position);
        } else if (holder instanceof LoadingViewHolder)
            ((LoadingViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void showProgress() {
        articles.add(null);
        notifyItemInserted(articles.size());
    }

    public void hideProgress() {
        articles.remove(articles.size() - 1);
        notifyItemRemoved(articles.size());
    }

    public class SwipePostViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private ArticleAdapter adapter;


        public void bind(final ArticleData article, int position) {
            initRecycler(position);
            adapter.setData(article);
            final ParseArticle parseArticle = new ParseArticle(domain + article.getUrl() + ".json", context, adapter);
           parseArticle.execute();
        }

        public SwipePostViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.rv);
        }

        protected void initRecycler(int position) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new ArticleAdapter(Arrays.asList(new CommentAdapter()), context, position);
            adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
                @Override
                public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                    if (!node.isLeaf()) {
                        onToggle(!node.isExpand(), holder);
                    }
                    return false;
                }

                @Override
                public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                    CommentAdapter.ViewHolder commentViewHolder = (CommentAdapter.ViewHolder) holder;
                }
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView2, int dx, int dy) {
                    super.onScrolled(recyclerView2, dx, dy);
                    if (dy > 0 && FrontPageActivity.shownFab) {
                        FrontPageActivity.shownFab = false;
                        fab.hide();
                    } else if (dy < 0 && !FrontPageActivity.shownFab) {
                        FrontPageActivity.shownFab = true;
                        fab.show();
                    }
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView2.getLayoutManager();
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    if (firstVisibleItem >1){
                        if (MpdPlayerFragment.pause != null)
                            MpdPlayerFragment.pause.performClick();
                        MpdPlayer.playState = PlayState.PAUSE;
                        if (ArticleAdapter.mpdPlayerFragment != null) {
                            FragmentTransaction transaction = ((FrontPageActivity) context).getSupportFragmentManager().beginTransaction();
                            transaction.remove(ArticleAdapter.mpdPlayerFragment);
                            transaction.commit();
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ArticleAdapter.mpdPlayerFragment = null;
                        }
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
