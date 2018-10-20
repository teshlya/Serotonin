package teshlya.com.reddit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import tellh.com.recyclertreeview_lib.TreeViewBinder;
import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.viewbinder.CommentNodeBinder;

public class ArticleAdapter extends TreeViewAdapter {

    ArticleData articleData;
    private Context context;

    public ArticleAdapter(List<CommentNodeBinder> commentNodeBinders) {
        super(commentNodeBinders);
    }

    public void setData(ArticleData articleData) {
        this.articleData = articleData;
    }
    @Override
    public int getItemViewType(int position) {
        int type;
        switch (position) {
            case 0:
                type = Constants.TITLE;
                break;
            case 1:
                type = Constants.TEXT;
                break;
            case 2:
                type = Constants.IMAGE;
                break;
            case 3:
                type = Constants.DETAIL;
                break;
            default:
                type = super.getItemViewType(position);
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return displayNodes == null ? 4 : displayNodes.size() + 4;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == Constants.TITLE) {
            View view = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);
            return new TitleHolder(view);
        }
        if (viewType == Constants.TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            return new TextHolder(view);
        }
        if (viewType == Constants.IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new ImageHolder(view);
        }
        if (viewType == Constants.DETAIL) {
            View view = LayoutInflater.from(context).inflate(R.layout.article_detail_item, parent, false);
            return new DetailHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            ((TitleHolder) holder).bind();
            return;
        }
        if (holder instanceof TextHolder) {
            ((TextHolder) holder).bind();
            return;
        }
        if (holder instanceof ImageHolder) {
                ((ImageHolder) holder).bind();
            return;
        }
        if (holder instanceof DetailHolder) {
                ((DetailHolder) holder).bind();
            return;
        }
        super.onBindViewHolder(holder, position);
    }


    public class TitleHolder extends RecyclerView.ViewHolder {

        TextView title;

        public TitleHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        public void bind() {
            title.setText(articleData.getTitle());
        }
    }

    public class TextHolder extends RecyclerView.ViewHolder {

        TextView text;

        public TextHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        public void bind() {
            text.setText(Html.fromHtml(articleData.getText()));
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind() {

            Picasso.with(context)
                    .load(articleData.getUrlImage3())
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(articleData.getUrlImage())
                                    .into(imageView);
                        }
                    });
        }
    }

    public class DetailHolder extends RecyclerView.ViewHolder {

        TextView community;
        TextView author;
        TextView date;
        TextView score;
        TextView comments;

        public DetailHolder(View itemView) {
            super(itemView);
            community = itemView.findViewById(R.id.community);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);
            comments = itemView.findViewById(R.id.comments);
        }

        public void bind() {
            community.setText(articleData.getSubredditName());
            author.setText(articleData.getAuthor());
            date.setText(articleData.getDate());
            score.setText(articleData.getScore());
            comments.setText(articleData.getCommentCount());
        }
    }
}

