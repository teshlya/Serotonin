package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.ArticleData;
import teshlya.com.reddit.model.CommunityData;
import teshlya.com.reddit.screen.SwipePostFragment;
import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.DrawableIcon;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    private Context context;
    private CommunityData data = new CommunityData();
    private InboxRecyclerView recyclerView;
    private ExpandablePageLayout conteinerSwipePostFragment;
    private String url;
    private Drawable drawableImage;
    private GradientDrawable drawableRectangle;
    private int widthScreen;


    public CommunityAdapter(InboxRecyclerView rv,
                            ExpandablePageLayout conteinerSwipePostFragment,
                            String url) {
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        //itemTouchHelper.attachToRecyclerView(rv);
        context = rv.getContext();
        recyclerView = rv;
        this.conteinerSwipePostFragment = conteinerSwipePostFragment;
        this.url = url;
        initDrawable();
        widthScreen = Calc.getWindowSizeInDp(context).x;
    }

    private void initDrawable() {
        drawableImage = context.getResources().getDrawable(R.drawable.placeholder);
        drawableRectangle = new GradientDrawable();
        drawableRectangle.setShape(GradientDrawable.RECTANGLE);
        drawableRectangle.setColor(Color.LTGRAY);

    }

    public void addArticle(CommunityData data) {
        int listSize = this.data.getArticles().size();
        int addCount = data.getArticles().size();
        this.data.getArticles().addAll(data.getArticles());
        this.data.setAfter(data.getAfter());
        notifyItemRangeChanged(listSize, addCount);
    }

    @Override
    public CommunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.community_item, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommunityViewHolder holder, int position) {
        holder.bind(data.getArticles().get(position), position);
    }

    @Override
    public int getItemCount() {
        return data.getArticles().size();
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
        //public YouTubePlayerView youTubePlayerView;
        private String idVideo;


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

            if (initVideo(article)) return;
            initImage(article);

        }

        private boolean initVideo(ArticleData article) {
            return false;
          /*  if (!article.getMediaType().equals("youtube.com")) {
                youTubePlayerView.setVisibility(View.GONE);
                return false;
            }
            idVideo = YouTubeURL.extractYTId(article.getVideoUrl());
            if (idVideo == null)
                idVideo = article.getVideoUrl().substring(article.getVideoUrl().length() - 11);


          /*  youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                @Override
                public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            initializedYouTubePlayer.cueVideo(idVideo, 0);

                        }
                    });
                }
            }, true);

            youTubePlayerView.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);*/
        }


        private void initImage(ArticleData article) {
            if (article.getUrlImage3() != null && !article.getUrlImage3().isEmpty())
                Picasso.with(context)
                        .load(article.getUrlImage3())
                        .placeholder(getPlaceholder(article.getWidth(), article.getHeight()))
                        .into(image);
            else if ((article.getUrlImage() != null && !article.getUrlImage().isEmpty() && !article.getUrlImage().equals("self")))
                Picasso.with(context)
                        .load(article.getUrlImage())
                        .placeholder(getPlaceholder(article.getWidth(), article.getHeight()))
                        .into(image);

            if ((article.getUrlImage3() == null || article.getUrlImage3().isEmpty()) &&
                    (article.getUrlImage() == null ||
                            article.getUrlImage().isEmpty() ||
                            article.getUrlImage().equals("self") ||
                            article.getUrlImage().equals("default"))) {
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
                image.setPadding(0, Calc.dpToPx(8), 0, 0);
            }
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

        public CommunityViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comments);
            score = itemView.findViewById(R.id.score);
            //youTubePlayerView = itemView.findViewById(R.id.youtube_view);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }


        private void openArticle(int position) {

            AppCompatActivity articleActivity = (AppCompatActivity) context;
            SwipePostFragment swipePostFragment = SwipePostFragment.newInstance(data,
                    url,
                    position);

            FragmentManager fm = articleActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(conteinerSwipePostFragment.getId(), swipePostFragment)
                    .commitNowAllowingStateLoss();
            recyclerView.expandItem(position);
        }

       /* public void onDisappear() {
            if (youTubePlayerView != null)
                youTubePlayerView.release();
        }*/
    }

    /*ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

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
    };*/

}
