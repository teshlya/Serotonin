package teshlya.com.reddit.viewbinder;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;
import teshlya.com.reddit.R;
import teshlya.com.reddit.adapter.LineAdapter;
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
    public void bindView(final ViewHolder holder, int position, final TreeNode node) {
        //holder.ivArrow.setRotation(0);
        //holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp);
        //int rotateDegree = node.isExpand() ? 90 : 0;
        //holder.ivArrow.setRotation(rotateDegree);
        CommentBean commentNode = (CommentBean) node.getContent();
        holder.body.setText(Html.fromHtml(commentNode.comment.getBody()));
        holder.body.setMovementMethod(LinkMovementMethod.getInstance());
        holder.author.setText(commentNode.comment.getAuthor());
        holder.score.setText(commentNode.comment.getScore());
        holder.date.setText(commentNode.comment.getDate());
        LineAdapter adapter = new LineAdapter(node.getHeight());
        holder.lines.setLayoutManager(new LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false));
        holder.lines.setAdapter(adapter);
        if (node.getChildList() != null)
            holder.commentCount.setText("+" + String.valueOf(calcComment(node)));

        if (!node.isLeaf())
            if (!node.isExpand())
                holder.commentCount.setVisibility(View.VISIBLE);
            else
                holder.commentCount.setVisibility(View.INVISIBLE);

        if (node.isLeaf())
            holder.commentCount.setVisibility(View.INVISIBLE);

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });
       /* if (node.isLeaf())
            holder.ivArrow.setVisibility(View.INVISIBLE);
        else holder.ivArrow.setVisibility(View.VISIBLE);*/
    }

    public int calcComment(TreeNode node) {
        int sum = 0;
        if (node.getChildList() != null) {
            sum = node.getChildList().size();
            List<TreeNode> nodes = node.getChildList();
            for (TreeNode tempNode : nodes) {
                sum = sum + calcComment(tempNode);
            }
        }
        return sum;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_comment;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        //private ImageView ivArrow;
        private TextView body;
        private TextView author;
        private TextView score;
        public TextView date;
        public TextView commentCount;
        private RecyclerView lines;
        private Context context;

        public ViewHolder(View rootView) {
            super(rootView);
            context = rootView.getContext();
            this.body = (TextView) rootView.findViewById(R.id.body);
            this.author = (TextView) rootView.findViewById(R.id.author);
            this.score = (TextView) rootView.findViewById(R.id.score);
            this.date = (TextView) rootView.findViewById(R.id.date);
            this.lines = (RecyclerView) rootView.findViewById(R.id.lines);
            this.commentCount = (TextView) rootView.findViewById(R.id.comment_count);
        }

        //public ImageView getIvArrow() { return ivArrow; }

        public TextView getCommentCount() {
            return commentCount;
        }
    }
}
