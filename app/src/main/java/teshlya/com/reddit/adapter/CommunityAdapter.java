package teshlya.com.reddit.adapter;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.screen.ArticleActivity;
import teshlya.com.reddit.screen.SwipePostFragment;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    Context context;
    private ArrayList<ArticleData> articles = new ArrayList<>();

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
                    openArticle(article, position);
                }
            });
            if (!article.getWithoutImage() && article.getUrlImage() != null && !article.getUrlImage().equals("")) {
                Picasso.with(context).load(article.getUrlImage()).into(image);
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


        private void openArticle(ArticleData article, int position) {
            ArticleActivity articleActivity = (ArticleActivity)context;
            FragmentTransaction ft = articleActivity.getSupportFragmentManager().beginTransaction();
            //ft.replace(R.id.fragment_article, ArticleFragment.newInstance(url));
            ft.add(R.id.fragment_article, SwipePostFragment.newInstance(articles, position));
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
