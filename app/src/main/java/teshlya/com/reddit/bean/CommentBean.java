package teshlya.com.reddit.bean;

import tellh.com.recyclertreeview_lib.LayoutItemType;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.Comment;

public class CommentBean implements LayoutItemType {
    public Comment comment;

    public CommentBean(Comment comment) {

        this.comment = comment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }
}