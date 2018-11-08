package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.reddit.R;
import teshlya.com.reddit.bean.CommentBean;
import teshlya.com.reddit.callback.CallbackComment;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.model.CommentData;
import teshlya.com.reddit.parse.ParseArticle;
import teshlya.com.reddit.screen.FrontPageActivity;


public class SwipePostAdapter extends RecyclerView.Adapter<SwipePostAdapter.SwipePostViewHolder> {

    private static String domain = "https://www.reddit.com";
    private Context context;
    private ArticleAdapter adapter;
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
    public SwipePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.swipe_post_item, parent, false);
        return new SwipePostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SwipePostViewHolder holder, int position) {
        holder.bind(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class SwipePostViewHolder extends RecyclerView.ViewHolder implements CallbackComment {
        private ArrayList<CommentData> commentData;
        private RecyclerView recyclerView;

        public void bind(final ArticleData article) {
            initRecycler();
            adapter.setData(article);
            ParseArticle parseArticle = new ParseArticle(this, domain + article.getUrl() + ".json", context, adapter);
            parseArticle.execute();

        }

        public SwipePostViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.rv);
        }


        public void bind(ArrayList<CommentData> list) {
            if (list == null) {
                Toast.makeText(context, "Error, load comment!", Toast.LENGTH_SHORT).show();
                return;
            }
            commentData = new ArrayList<>();
            commentData.addAll(list);
            initData();
        }

        protected void initRecycler() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new ArticleAdapter(Arrays.asList(new CommentAdapter()), context);
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
                    } else
                    if (dy < 0 && !FrontPageActivity.shownFab) {
                        FrontPageActivity.shownFab = true;
                        fab.show();
                    }

                }
            });

            recyclerView.setAdapter(adapter);
        }


        private void initData() {
            List<TreeNode> nodes = new ArrayList<>();
            if (commentData != null)
                for (CommentData comment : commentData) {
                    TreeNode<CommentBean> tempComment = new TreeNode<>(new CommentBean(comment.getComment()));
                    tempComment.setChildList(setData(comment.getReplies()));
                    tempComment.expandAll();
                    nodes.add(tempComment);
                }
            adapter.findDisplayNodes(nodes);
            adapter.notifyDataSetChanged();
        }

        private List<TreeNode> setData(ArrayList<CommentData> commentData) {
            List<TreeNode> nodes = new ArrayList<>();
            if (commentData != null)
                for (CommentData comment : commentData) {
                    TreeNode<CommentBean> tempComment = new TreeNode<>(new CommentBean(comment.getComment()));
                    tempComment.setChildList(setData(comment.getReplies()));
                    tempComment.expandAll();
                    nodes.add(tempComment);
                }
            return nodes;
        }

        @Override
        public void showDataOnScreen(ArrayList<CommentData> list) {
            bind(list);
        }
    }
}
