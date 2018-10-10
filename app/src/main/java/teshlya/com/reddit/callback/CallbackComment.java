package teshlya.com.reddit.callback;

import java.util.ArrayList;

import teshlya.com.reddit.adapter.SwipePostAdapter;
import teshlya.com.reddit.model.CommentData;

public interface CallbackComment {
    void showDataOnScreen(ArrayList<CommentData> list);
}
