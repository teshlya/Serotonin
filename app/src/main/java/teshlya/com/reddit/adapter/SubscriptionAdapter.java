package teshlya.com.reddit.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import teshlya.com.reddit.Constants;
import teshlya.com.reddit.R;
import teshlya.com.reddit.model.Subscription;
import teshlya.com.reddit.screen.ArticleActivity;


public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private List<Subscription> subscriptions = new ArrayList<>();
    private Context context;
    private LinearLayout onClickItem;

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        notifyItemChanged(subscriptions.size() - 1);
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

        holder.bind(subscriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    public class SubscriptionViewHolder extends RecyclerView.ViewHolder {

        private ImageView subscriptionImageView;
        private TextView subscriptionTextView;

        public void bind(final Subscription subscription) {
            Picasso.with(context)
                    .load(subscription.getIconUrl())
                    .placeholder(R.drawable.default_icon)
                    .into(subscriptionImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            subscriptionTextView.setText(subscription.getContent());
                        }
                        @Override
                        public void onError() {
                        }
                    });
            if (subscription.getIconUrl().equals("default"))
                subscriptionTextView.setText(subscription.getContent());

            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCommunity(subscription.getCommunityUrl());
                }
            });

        }


        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            subscriptionImageView = itemView.findViewById(R.id.icon);
            subscriptionTextView = itemView.findViewById(R.id.content);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }

        private void openCommunity(String url){
            Intent myIntent = new Intent(context, ArticleActivity.class);
            myIntent.putExtra(Constants.URL, url);
            context.startActivity(myIntent);
        }
    }

}
