package teshlya.com.serotonin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import teshlya.com.serotonin.parse.ParseArticle;
import teshlya.com.serotonin.utils.Constants;
import teshlya.com.serotonin.R;


public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {

    private ArrayList<String> subscriptions = new ArrayList<>();
    private Context context;
    private LinearLayout onClickItem;


    public void addSubscription(ArrayList<String> subscriptions) {
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
        holder.bind(subscriptions.get(position));
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

        private TextView subscriptionTextView;

        public void bind(final String subscription) {
            subscriptionTextView.setText(subscription);

            onClickItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCommunity("/r/" + subscription + "/", subscription);
                }
            });
        }

        public SubscriptionViewHolder(View itemView) {
            super(itemView);
            subscriptionTextView = itemView.findViewById(R.id.content);
            onClickItem = itemView.findViewById(R.id.onClickItem);
        }

        private void openCommunity(String url, String community) {
            ParseArticle.hmap.clear();
            Intent data = new Intent();
            data.putExtra(Constants.URL, url);
            data.putExtra(Constants.COMMUNITY, community);
            ((Activity) context).setResult(((Activity) context).RESULT_OK, data);
            ((Activity) context).finish();
        }
    }
}
