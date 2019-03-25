package teshlya.com.serotonin.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollListenerSwipePost extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;
    private int visibleThreshold = 5;
    public static int lastVisibleItem;
    private int totalItemCount;
    private ScrollListenerCallback callback;

    public  ScrollListenerSwipePost(ScrollListenerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        initVariables(recyclerView);
        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (callback != null) {
                callback.loadMore();
            }
            isLoading = true;
        }

        if (ArticleAdapter.positionPlayingVideo != -1 &&
                ArticleAdapter.positionPlayingVideo != lastVisibleItem) {
            callback.releasePlayer();
            ArticleAdapter.positionPlayingVideo = -1;
        }

    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            int currentPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (callback != null) {
                callback.setTitleInSearchMode(currentPosition);
            }
        }
    }

    private void initVariables(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
