package teshlya.com.serotonin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import me.saket.inboxrecyclerview.page.ExpandablePageLayout;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.holder.LoadingViewHolder;
import teshlya.com.serotonin.model.ArticleData;
import teshlya.com.serotonin.model.CommunityData;
import teshlya.com.serotonin.model.Media;
import teshlya.com.serotonin.screen.FrontPageActivity;
import teshlya.com.serotonin.screen.SwipePostFragment;
import teshlya.com.serotonin.utils.Calc;

import static android.view.ViewGroup.GONE;
import static android.view.ViewGroup.OnClickListener;
import static android.view.ViewGroup.VISIBLE;
import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_ITEM;
import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_LOADING;
import static teshlya.com.serotonin.utils.Constants.VIEW_TYPE_TWO_LINE_ITEM;


public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private CommunityData data = new CommunityData();
    private RecyclerView recyclerView;
    private FrameLayout conteinerSwipePostFragment;
    private String url;
    private Drawable drawableImage;
    private int widthScreen;
    private SwipePostFragment swipePostFragment;
    private String subreddit;
    public CommunityAdapter(RecyclerView rv,
                            FrameLayout conteinerSwipePostFragment,
                            String url) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
        context = rv.getContext();
        recyclerView = rv;
        this.conteinerSwipePostFragment = conteinerSwipePostFragment;
        this.url = url;
        initDrawable();
        widthScreen = Calc.getWindowSizeInDp(context).x;
        getSubreddit();
    }

    private void initDrawable() {
        drawableImage = context.getResources().getDrawable(R.drawable.placeholder);
    }

    public void addArticle(CommunityData data) {
        int listSize = this.data.getArticles().size();
        int addCount = data.getArticles().size();
        this.data.getArticles().addAll(data.getArticles());
        this.data.setAfter(data.getAfter());
        notifyItemRangeChanged(listSize, addCount);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.getArticles().get(position) == null) return VIEW_TYPE_LOADING;
        if (position == 0) return VIEW_TYPE_TWO_LINE_ITEM;
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == VIEW_TYPE_ITEM)
            holder = createCommunityViewHolder(viewGroup);
        if (viewType == VIEW_TYPE_TWO_LINE_ITEM)
            holder = createCommunityViewHolderTwoLine(viewGroup);
        if (viewType == VIEW_TYPE_LOADING)
            holder = createLoadingViewHolder(viewGroup);
        return holder;
    }

    private CommunityViewHolder createCommunityViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.community_item, viewGroup, false);
        return new CommunityViewHolder(view);
    }

    private CommunityViewHolder createCommunityViewHolderTwoLine(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.community_item_two_line, viewGroup, false);
        return new CommunityViewHolder(view);
    }

    private LoadingViewHolder createLoadingViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup
                .getContext()).inflate(R.layout.item_loading, viewGroup, false);
        return new LoadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommunityViewHolder) {
            ((CommunityViewHolder) holder).bind(data.getArticles().get(position), position);
        } else if (holder instanceof LoadingViewHolder)
            ((LoadingViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return data.getArticles().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void showProgress() {
        data.getArticles().add(null);
        notifyItemInserted(data.getArticles().size());
    }

    public void hideProgress() {
        data.getArticles().remove(data.getArticles().size() - 1);
        notifyItemRemoved(data.getArticles().size());
    }

    private void getSubreddit() {
        String str = ((TextView) ((FrontPageActivity) this.context).findViewById(R.id.community_title)).getText().toString();
        if ((!str.equals("All")) && (!str.equals("Front page")) && (!str.equals("Popular"))) {
            this.subreddit = "";
            return;
        }
        this.subreddit = str;
    }

    public void scrollRecyclerViewUp()
    {
        if (this.swipePostFragment != null) {
            this.swipePostFragment.scrollRecyclerViewUp();
        }
    }

    public class CommunityViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView author;
        private TextView date;
        private TextView comment;
        private TextView score;
        private LinearLayout onClickItem;
        //public YouTubePlayerView youTubePlayerView;
        private ImageView image;
        private RelativeLayout video;
        private ImageView videoPreview;
        private Button play;
        private TextView subredditTV;

        public CommunityViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
            comment = itemView.findViewById(R.id.comments);
            score = itemView.findViewById(R.id.score);
            //youTubePlayerView = itemView.findViewById(R.id.youtube_view);
            onClickItem = itemView.findViewById(R.id.onClickItem);
            image = itemView.findViewById(R.id.image);
            video = itemView.findViewById(R.id.video);
            videoPreview = itemView.findViewById(R.id.videoPreview);
            play = itemView.findViewById(R.id.play);
            this.subredditTV = itemView.findViewById(R.id.subreddit);
        }

        public void bind(final ArticleData article, final int position) {
            title.setText(article.getTitle());
            date.setText(article.getDate());
            author.setText(article.getAuthor());
            comment.setText(article.getCommentCount());
            score.setText(article.getScore());
            if (CommunityAdapter.this.subreddit.equals("")) {
                this.subredditTV.setVisibility(GONE);
            } else {
                this.subredditTV.setText(article.getSubreddit());
                this.subredditTV.setVisibility(VISIBLE);
            }
            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openArticle(position);
                }
            });

            switch (article.getMediaType()) {
                case IMAGE:
                    initImage(article.getMediaImage());
                    break;
                case MPD: case GIF:
                    initMpd(article.getMediaImage(), article.getMediaVideo());
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
            image.setPadding(0, Calc.dpToPx(8), 0, 0);
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
            play.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Intent myIntent = new Intent(context, MpdPlayerActivity.class);
                    myIntent.putExtra("media", mediaVideo); //Optional parameters
                    context.startActivity(myIntent);*/
                    ArticleAdapter.playWhenOpen = true;
                    onClickItem.performClick();
                }
            });
            image.setVisibility(View.GONE);
            video.setVisibility(VISIBLE);
            video.setPadding(0, Calc.dpToPx(8), 0, 0);
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


        private void openArticle(int position) {
            AppCompatActivity articleActivity = (AppCompatActivity) context;
            swipePostFragment = SwipePostFragment.newInstance(data,
                    url,
                    position);

            FragmentManager fm = articleActivity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(conteinerSwipePostFragment.getId(), swipePostFragment);
            //ft.addToBackStack(null);
            ft.commit();
            ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);

            //recyclerView.expandItem(position);
        }

       /* public void onDisappear() {
            if (youTubePlayerView != null)
                youTubePlayerView.release();
        }*/

    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            data.getArticles().remove(position);
            notifyItemRemoved(position);
            //notifyDataSetChanged();
        }
    };

}
