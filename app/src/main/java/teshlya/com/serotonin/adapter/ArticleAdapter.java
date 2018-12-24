package teshlya.com.serotonin.adapter;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.holder.LoadingViewHolder;
import teshlya.com.serotonin.model.ArticleData;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.model.PlayState;
import teshlya.com.serotonin.screen.FrontPageActivity;
import teshlya.com.serotonin.screen.MpdPlayerFragment;
import teshlya.com.serotonin.utils.Calc;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.utils.DrawableIcon;
import teshlya.com.serotonin.utils.MpdPlayer;
import teshlya.com.serotonin.utils.TrimHtml;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_LOADING;

public class ArticleAdapter extends TreeViewAdapter {

    ArticleData articleData;
    private Context context;
    private Drawable drawableImage;
    private int widthScreen;
    private boolean loaded = false;
    private int position;
    public static int positionPlayingVideo = -1;
    public static MpdPlayerFragment mpdPlayerFragment = null;
    public static boolean playWhenOpen = false;

    public ArticleAdapter(List<CommentAdapter> commentAdapters, Context context, int position) {
        super(commentAdapters);
        this.context = context;
        this.position = position;
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
                if (!loaded) {
                    type = VIEW_TYPE_LOADING;
                } else
                    type = super.getItemViewType(position);
        }
        return type;
    }

    @Override
    public int getItemCount() {
        int count = (!loaded) ? 5 : displayNodes.size() + 4;
        return count;
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

        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
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
        if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).bind();
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    public void hideProgress() {
        loaded = true;
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

        private ImageView image;
        private RelativeLayout video;
        private Button play;
        private ImageView videoPreview;


        public MediaHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            video = itemView.findViewById(R.id.video);
            videoPreview = itemView.findViewById(R.id.videoPreview);
            play = itemView.findViewById(R.id.play);
        }

        public void bind() {
            switch (articleData.getMediaType()) {
                case IMAGE:
                    initImage(articleData.getMediaImage());
                    break;
                case MPD:
                case GIF:
                    initMpd(articleData.getMediaImage(), articleData.getMediaVideo());
                    break;
                case NONE: {
                    image.setVisibility(View.GONE);
                    video.setVisibility(GONE);
                    break;
                }
            }
        }

        private void initImage(Media media) {
            Picasso.with(context)
                    .load(media.getUrl())
                    .placeholder(getPlaceholder(media.getWidth(), media.getHeight()))
                    .into(image);
            image.setVisibility(View.VISIBLE);
            video.setVisibility(GONE);
            image.setPadding(0, Calc.dpToPx(10), 0, 0);
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

        private void initMpd(final Media mediaImage, final Media mediaVideo) {
            Picasso.with(context)
                    .load(mediaImage.getUrl())
                    .placeholder(getPlaceholder(mediaImage.getWidth(), mediaImage.getHeight()))
                    .into(videoPreview);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MpdPlayer.playState = PlayState.PLAY;
                    FragmentTransaction ft = ((FrontPageActivity)context).getSupportFragmentManager().beginTransaction();
                    mpdPlayerFragment = MpdPlayerFragment.newInstance(mediaVideo);
                    ft.add(R.id.conteiner_media, mpdPlayerFragment);
                    ft.commit();
                    positionPlayingVideo = position;
                }
            });
            image.setVisibility(View.GONE);
            video.setVisibility(VISIBLE);
            video.setPadding(0, Calc.dpToPx(8), 0, 0);
            if (playWhenOpen) {
                playWhenOpen = false;
                play.performClick();
            }
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

