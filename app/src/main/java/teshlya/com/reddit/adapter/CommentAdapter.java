package teshlya.com.reddit.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;
import teshlya.com.reddit.R;
import teshlya.com.reddit.bean.CommentBean;
import teshlya.com.reddit.utils.TrimHtml;

public class CommentAdapter extends TreeViewBinder<CommentAdapter.ViewHolder> {

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(final ViewHolder holder, int position, final TreeNode node) {
        CommentBean commentNode = (CommentBean) node.getContent();
        holder.body.setText(TrimHtml.trim(Html.fromHtml(commentNode.comment.getBody())));
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
            this.body = rootView.findViewById(R.id.body);
            this.author = rootView.findViewById(R.id.author);
            this.score = rootView.findViewById(R.id.score);
            this.date = rootView.findViewById(R.id.date);
            this.lines = rootView.findViewById(R.id.lines);
            this.commentCount = rootView.findViewById(R.id.comment_count);
        }
    }
}
