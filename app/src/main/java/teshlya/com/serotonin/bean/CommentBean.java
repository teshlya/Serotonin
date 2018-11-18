package teshlya.com.serotonin.bean;

import tellh.com.recyclertreeview_lib.LayoutItemType;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.model.Comment;

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