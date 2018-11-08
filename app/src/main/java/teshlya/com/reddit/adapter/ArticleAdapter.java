package teshlya.com.reddit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.model.Media;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.utils.DrawableIcon;
import teshlya.com.reddit.utils.TrimHtml;

import static android.view.View.VISIBLE;

public class ArticleAdapter extends TreeViewAdapter {

    ArticleData articleData;
    private Context context;
    private Drawable drawableImage;
    private int widthScreen;

    public ArticleAdapter(List<CommentAdapter> commentAdapters, Context context) {
        super(commentAdapters);
        this.context = context;
        init();
    }

    private void init() {
        initDrawable();
        widthScreen = Calc.getWindowSizeInDp(context).x;
    }

    private void initDrawable() {
        drawableImage = context.getResources().getDrawable(R.drawable.placeholder);
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
                type = Constants.MEDIA;
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
        if (viewType == Constants.TITLE) {
            View view = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);
            return new TitleHolder(view);
        }
        if (viewType == Constants.TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            return new TextHolder(view);
        }
        if (viewType == Constants.MEDIA) {
            View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
            return new MediaHolder(view);
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
        if (holder instanceof MediaHolder) {
            ((MediaHolder) holder).bind();
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

    public class MediaHolder extends RecyclerView.ViewHolder {

        private FrameLayout conteinerMedia;


        public MediaHolder(View itemView) {
            super(itemView);
            conteinerMedia = itemView.findViewById(R.id.conteiner_media);
        }

        public void bind() {

            switch (articleData.getMediaType()) {
                case IMAGE:
                    initImage(articleData.getMedia());
                    break;
                case NONE: {
                    conteinerMedia.setVisibility(View.GONE);
                    break;
                }
            }
        }

        private void initImage(Media media) {
            ImageView image = new ImageView(context);
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(context)
                    .load(media.getUrl())
                    .placeholder(getPlaceholder(media.getWidth(), media.getHeight()))
                    .into(image);
            image.setVisibility(View.VISIBLE);
            image.setPadding(0, Calc.dpToPx(10), 0, 0);
            conteinerMedia.setVisibility(VISIBLE);
            conteinerMedia.removeAllViews();
            conteinerMedia.addView(image);
        }

        private Drawable getPlaceholder(int w, int h) {
            if (h == 0 || w == 0) return null;
            Drawable[] drawable = new Drawable[2];
            GradientDrawable drawableRectangle;
            drawableRectangle = new GradientDrawable();
            drawableRectangle.setShape(GradientDrawable.RECTANGLE);
            drawableRectangle.setColor(Color.LTGRAY);

            drawableRectangle.setSize(widthScreen, widthScreen * h / w);
            drawable[0] = drawableRectangle;
            drawable[1] = drawableImage;
            LayerDrawable placeholder = new LayerDrawable(drawable);
            return placeholder;
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

