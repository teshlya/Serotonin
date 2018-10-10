package teshlya.com.reddit.viewbinder;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;
import teshlya.com.reddit.R;
import teshlya.com.reddit.bean.CommentBean;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class CommentNodeBinder extends TreeViewBinder<CommentNodeBinder.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        holder.ivArrow.setRotation(0);
        holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp);
        int rotateDegree = node.isExpand() ? 90 : 0;
        holder.ivArrow.setRotation(rotateDegree);
        CommentBean commentNode = (CommentBean) node.getContent();
        holder.body.setText(Html.fromHtml(commentNode.comment.getBody()));
        holder.body.setMovementMethod(LinkMovementMethod.getInstance());
        holder.author.setText(commentNode.comment.getAuthor());
        holder.score.setText(commentNode.comment.getScore());
        holder.date.setText(commentNode.comment.getDate());
        if (node.isLeaf())
            holder.ivArrow.setVisibility(View.INVISIBLE);
        else holder.ivArrow.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        private ImageView ivArrow;
        private TextView body;
        private TextView author;
        private TextView score;
        private TextView date;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            this.body = (TextView) rootView.findViewById(R.id.body);
            this.author = (TextView) rootView.findViewById(R.id.author);
            this.score = (TextView) rootView.findViewById(R.id.score);
            this.date = (TextView) rootView.findViewById(R.id.date);
        }

        public ImageView getIvArrow() { return ivArrow; }

        public TextView getBody() {
            return body;
        }
    }
}
