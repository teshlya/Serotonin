package teshlya.com.serotonin.adapter;

public interface ScrollListenerCallback {
    void loadMore();
    void releasePlayer();
    void setTitleInSearchMode(int lastVisibleItem);
}
