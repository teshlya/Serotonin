package teshlya.com.reddit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;
import tellh.com.recyclertreeview_lib.TreeViewBinder;
import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.viewbinder.CommentNodeBinder;

public class ArticleAdapter extends TreeViewAdapter {
    public ArticleAdapter(List<TreeNode> nodes, List<? extends TreeViewBinder> viewBinders) {
        super(nodes, viewBinders);
    }

    private String title;
    private String text;
    private String image;
    private Context context;

    public ArticleAdapter(List<CommentNodeBinder> commentNodeBinders) {
        super(commentNodeBinders);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        switch (position) {
            case 0:
                type = Constants.TITLE;
                break;
            case 1:
                type = Constants.TEXT;
                break;
            case 2:
                type = Constants.IMAGE;
                break;
            default:
                type = super.getItemViewType(position);
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return displayNodes == null ? 3 : displayNodes.size()+3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == Constants.TITLE) {
            View view = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);
            return new TitleHolder(view);
        }
        if (viewType == Constants.TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.text_item, parent, false);
            return new TextHolder(view);
        }
        if (viewType == Constants.IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new ImageHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            ((TitleHolder) holder).bind(title);
            return;
        }
        if (holder instanceof TextHolder) {
            ((TextHolder) holder).bind(text);
            return;
        }
        if (holder instanceof ImageHolder) {
            if (image != null)
            ((ImageHolder) holder).bind(image);
            return;
        }
        super.onBindViewHolder(holder, position);
    }


    public class TitleHolder extends RecyclerView.ViewHolder {

        TextView title;

        public TitleHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        public void bind(String str) {
            title.setText(str);
        }
    }

    public class TextHolder extends RecyclerView.ViewHolder {

        TextView text;

        public TextHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        public void bind(String str) {
            text.setText(Html.fromHtml(str));
        }
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind(String image) {
                Picasso.with(context).load(image).into(imageView);
            }
        }


    }

