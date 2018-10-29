package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
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
import teshlya.com.reddit.utils.YouTubeURL;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder> {

    Context context;
    private ArrayList<ArticleData> articles = new ArrayList<>();
    InboxRecyclerView recyclerView;
    ExpandablePageLayout conteinerSwipePostFragment;

    public CommunityAdapter(InboxRecyclerView rv,
                            ExpandablePageLayout conteinerSwipePostFragment) {
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        //itemTouchHelper.attachToRecyclerView(rv);
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
                        .into(image);

            if ((article.getUrlImage() != null && !article.getUrlImage().isEmpty() && !article.getUrlImage().equals("self")))
                Picasso.with(context)
                        .load(article.getUrlImage())
                        .into(image);

            if ((article.getUrlImage3() == null || article.getUrlImage3().isEmpty()) &&
                    (article.getUrlImage() == null || article.getUrlImage().isEmpty() || article.getUrlImage().equals("self"))) {
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
                image.setPadding(0, Calc.dpToPx(8), 0, 0);
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
            //youTubePlayerView = itemView.findViewById(R.id.youtube_view);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }


        private void openArticle(int position) {

            AppCompatActivity articleActivity = (AppCompatActivity) context;
            SwipePostFragment swipePostFragment = SwipePostFragment.newInstance(articles, position);

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
