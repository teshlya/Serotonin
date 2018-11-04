package teshlya.com.reddit.adapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.reddit.screen.FrontPageActivity;

public class ScrollListenerCommunity extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private int totalItemCount;
    private OnLoadMoreCallback callback;
    private FloatingActionButton fab;

    public ScrollListenerCommunity(FloatingActionButton fab, OnLoadMoreCallback callback) {
        this.fab = fab;
        this.callback = callback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0 && FrontPageActivity.shownFab) {
            FrontPageActivity.shownFab = false;
            fab.hide();
        } else if (dy < 0 && !FrontPageActivity.shownFab) {
            FrontPageActivity.shownFab = true;
            fab.show();
        }


        initVariables(recyclerView);
        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            if (callback != null) {
                callback.onLoadMore();
            }
            isLoading = true;
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
