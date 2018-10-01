package teshlya.com.reddit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;


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
        holder.bind(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {

        private TextView communityTitle;
        private TextView communityAuthor;
        private TextView communityDate;
        private ImageView communityImage;

        public void bind(ArticleData article) {
            communityTitle.setText(article.getTitle());
            communityDate.setText(article.getDate());
            communityAuthor.setText("Posted by " + article.getAuthor());
            if (!(article.getUrlImage().equals("self") || article.getUrlImage().equals("default"))) {
                Picasso.with(context).load(article.getUrlImage()).into(communityImage);
            } else {
                communityImage.setImageDrawable(null);
            }
        }


        public CommunityViewHolder(View itemView) {
            super(itemView);
            communityTitle = itemView.findViewById(R.id.title);
            communityDate = itemView.findViewById(R.id.date);
            communityImage = itemView.findViewById(R.id.image);
            communityAuthor = itemView.findViewById(R.id.author);
        }
    }
}
