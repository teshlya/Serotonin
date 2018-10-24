package teshlya.com.reddit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teshlya.com.reddit.utils.Calc;
import teshlya.com.reddit.utils.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.screen.ArticleActivity;


public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private List<Subscription> subscriptions = new ArrayList<>();
    private Context context;
    private LinearLayout onClickItem;
    RecyclerView recyclerView;

    public SubscriptionAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        notifyItemChanged(subscriptions.size() - 1);
    }

    public void addSubscription(List<Subscription> subscriptions) {
        this.subscriptions.addAll(subscriptions);
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.subscription_item, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubscriptionViewHolder holder, int position) {
        holder.bind(subscriptions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class SubscriptionViewHolder extends RecyclerView.ViewHolder {

        private ImageView subscriptionImageView;
        private TextView subscriptionTextView;

        public void bind(final Subscription subscription, final int position) {

            if (subscription.getIconResource() > 0)
                subscriptionImageView.setImageResource(subscription.getIconResource());
            else
                Picasso.with(context)
                        .load(subscription.getIconUrl())
                        .placeholder(R.drawable.default_icon)
                        .into(subscriptionImageView);

            subscriptionTextView.setText(subscription.getContent());

            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCommunity(subscription.getCommunityUrl(), subscription.getContent(), v);
                }
            });
        }


        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            subscriptionImageView = itemView.findViewById(R.id.icon);
            subscriptionTextView = itemView.findViewById(R.id.content);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }

        private void openCommunity(String url, String community, View view) {
            Rect rect = getPositionItem(view);
            Intent myIntent = new Intent(context, ArticleActivity.class);
            myIntent.putExtra(Constants.URL, url);
            myIntent.putExtra(Constants.COMMUNITY, community);
            myIntent.putExtra(Constants.LEFT, rect.left);
            myIntent.putExtra(Constants.RIGHT, rect.right);
            myIntent.putExtra(Constants.TOP, rect.top);
            myIntent.putExtra(Constants.BOTTOM, rect.bottom);
            context.startActivity(myIntent);
        }

        private Rect getPositionItem(View view) {
            int[] originalPos = new int[2];
            view.getLocationInWindow(originalPos);
            int x = originalPos[0];
            int y = originalPos[1];
            Rect rect = new Rect(x, y, x + view.getWidth(), y + view.getHeight());
            return rect;
        }
    }
}
