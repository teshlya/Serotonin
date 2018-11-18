package teshlya.com.serotonin.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollListenerSwipePost extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private int totalItemCount;
    private OnLoadMoreCallback callback;

    public ScrollListenerSwipePost(OnLoadMoreCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
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
