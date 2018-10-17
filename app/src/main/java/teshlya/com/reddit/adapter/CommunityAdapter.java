package teshlya.com.reddit.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.screen.ArticleActivity;
import teshlya.com.reddit.screen.CommunityFragment;
import teshlya.com.reddit.screen.SwipePostFragment;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    Context context;
    private ArrayList<ArticleData> articles = new ArrayList<>();
    String community;

    public CommunityAdapter(RecyclerView rv, String community) {
        this.community = community;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    public void addArticle(List<ArticleData> articles) {
        this.articles.addAll(articles);
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.article_item, parent, false);
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
            score.setText(article.getScore());
            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openArticle(position);
                }
            });
            if (!article.getWithoutImage() && article.getUrlImage() != null && !article.getUrlImage().equals("")) {
                Picasso.with(context)
                        .load(article.getUrlImage3())
                        .into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(article.getUrlImage())
                                        .into(image);
                            }
                        });

            } else {
                image.setImageDrawable(null);
            }
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
            SwipePostFragment swipePostFragment = SwipePostFragment.newInstance(articles, position, community);


            FragmentTransaction ft = articleActivity.getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_article, swipePostFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();
            articles.remove(position);
            notifyDataSetChanged();

        }
    };

}
