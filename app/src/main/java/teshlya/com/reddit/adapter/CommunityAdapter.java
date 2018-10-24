package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.screen.ArticleActivity;
import teshlya.com.reddit.screen.SwipePostFragment;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.DrawableIcon;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    Context context;
    private ArrayList<ArticleData> articles = new ArrayList<>();
    InboxRecyclerView recyclerView;
    ExpandablePageLayout conteinerSwipePostFragment;

    public CommunityAdapter(InboxRecyclerView rv,
                            ExpandablePageLayout conteinerSwipePostFragment) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
        recyclerView = rv;
        this.conteinerSwipePostFragment = conteinerSwipePostFragment;
    }

    public void addArticle(List<ArticleData> articles) {
        this.articles.addAll(articles);
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.community_item, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        holder.bind(articles.get(position), position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView author;
        private TextView date;
        private TextView comment;
        private TextView score;
        private ImageView image;
        private LinearLayout onClickItem;

        public void bind(final ArticleData article, final int position) {
            title.setText(article.getTitle());
            date.setText(article.getDate());
            author.setText(article.getAuthor());
            comment.setText(article.getCommentCount());
            comment.setCompoundDrawablesWithIntrinsicBounds(DrawableIcon.comment, null, null, null);
            score.setText(article.getScore());
            score.setCompoundDrawablesWithIntrinsicBounds(DrawableIcon.score, null, null, null);
            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openArticle(position);
                }
            });
                Picasso.with(context)
                        .load(article.getUrlImage3())
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                image.setPadding(0, Calc.dpToPx(8), 0, 0);
                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(article.getUrlImage())
                                        .into(image, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                image.setPadding(0, Calc.dpToPx(8), 0, 0);
                                            }

                                            @Override
                                            public void onError() {
                                                image.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        });
            if ((article.getUrlImage3() == null || article.getUrlImage3().isEmpty()) &&
                    (article.getUrlImage() == null || article.getUrlImage().isEmpty()))
                image.setVisibility(View.GONE);

        }


        public CommunityViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comments);
            score = itemView.findViewById(R.id.score);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }


        private void openArticle(int position) {

            ArticleActivity articleActivity = (ArticleActivity) context;
            SwipePostFragment swipePostFragment = SwipePostFragment.newInstance(articles, position);

            FragmentManager fm = articleActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(conteinerSwipePostFragment.getId(), swipePostFragment)
                    .commitNowAllowingStateLoss();

            recyclerView.expandItem(position);
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
                articles.remove(position);
                notifyItemRemoved(position);
                //notifyDataSetChanged();
        }
    };

}
