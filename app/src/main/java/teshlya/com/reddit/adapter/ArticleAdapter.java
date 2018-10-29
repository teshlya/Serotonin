package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.utils.DrawableIcon;
import teshlya.com.reddit.utils.TrimHtml;

public class ArticleAdapter extends TreeViewAdapter {

    ArticleData articleData;
    private Context context;

    public ArticleAdapter(List<CommentAdapter> commentAdapters) {
        super(commentAdapters);
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
                type = Constants.IMAGE;
                break;
            case 2:
                type = Constants.TEXT;
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
            if (articleData.getText() != null && !articleData.getText().isEmpty()) {

                text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                text.setText(TrimHtml.trim(Html.fromHtml(articleData.getText())));
                text.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind() {

            if (articleData.getUrlImage3() != null && !articleData.getUrlImage3().isEmpty())
                Picasso.with(context)
                        .load(articleData.getUrlImage3())
                        .into(imageView);

            if ((articleData.getUrlImage() != null && !articleData.getUrlImage().isEmpty() && !articleData.getUrlImage().equals("self")))

                Picasso.with(context)
                        .load(articleData.getUrlImage())
                        .into(imageView);

            if ((articleData.getUrlImage3() == null || articleData.getUrlImage3().isEmpty()) &&
                    (articleData.getUrlImage() == null || articleData.getUrlImage().isEmpty()))
                imageView.setVisibility(View.GONE);
            else
                imageView.setPadding(0, Calc.dpToPx(10), 0, 0);
        }
    }

    public class DetailHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView date;
        private TextView score;
        private TextView comments;

        public DetailHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);
            comments = itemView.findViewById(R.id.comments);
        }

        public void bind() {
            author.setText(articleData.getAuthor());
            date.setText(articleData.getDate());
            score.setText(articleData.getScore());
            score.setCompoundDrawablesWithIntrinsicBounds(DrawableIcon.score, null, null, null);
            comments.setText(articleData.getCommentCount());
            comments.setCompoundDrawablesWithIntrinsicBounds(DrawableIcon.comment, null, null, null);
        }
    }
}

