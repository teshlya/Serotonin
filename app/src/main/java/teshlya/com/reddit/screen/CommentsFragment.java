package teshlya.com.reddit.screen;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import teshlya.com.reddit.R;
import teshlya.com.reddit.bean.CommentBean;
import teshlya.com.reddit.model.CommentData;
import teshlya.com.reddit.viewbinder.CommentNodeBinder;


@SuppressLint("ValidFragment")
public class CommentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TreeViewAdapter adapter;
    private ArrayList<CommentData> commentData;

    public CommentsFragment(ArrayList<CommentData> commentData) {
        this.commentData = new ArrayList<>();
        this.commentData.addAll(commentData);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
    }

    private void initData() {
        List<TreeNode> nodes = new ArrayList<>();
        nodes.addAll(setData(commentData));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TreeViewAdapter(nodes, Arrays.asList(new CommentNodeBinder()));
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (!node.isLeaf()) {
                    onToggle(!node.isExpand(), holder);
                }
                return false;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                CommentNodeBinder.ViewHolder dirViewHolder = (CommentNodeBinder.ViewHolder) holder;
                ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = isExpand ? 90 : -90;

                TextView body = dirViewHolder.getBody();
                body.setVisibility(isExpand ? View.VISIBLE : View.GONE);
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private List<TreeNode> setData(ArrayList<CommentData> commentData) {
        List<TreeNode> nodes = new ArrayList<>();
        if (commentData != null)
            for (CommentData comment : commentData) {
                TreeNode<CommentBean> tempComment = new TreeNode<>(new CommentBean(comment.getComment()));
                tempComment.setChildList(setData(comment.getReplies()));
                tempComment.expandAll();
                nodes.add(tempComment);
            }
        return nodes;
    }
}
