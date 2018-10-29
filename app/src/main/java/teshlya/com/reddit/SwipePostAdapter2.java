package teshlya.com.reddit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class SwipePostAdapter2 extends RecyclerView.Adapter<SwipePostAdapter2.SwipePostViewHolder> {
    private static String domain = "https://www.reddit.com";

    private Context context;



    @Override
    public SwipePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false);
        return new SwipePostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SwipePostViewHolder holder, int position) {
        //holder.bind(articles.get(position));
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 20;//articles.size();
    }

    public class SwipePostViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;


        public SwipePostViewHolder(View view) {
            super(view);
            //recyclerView = view.findViewById(R.id.rv);
            textView = view.findViewById(R.id.button);
        }


        public void bind(int pos) {
            textView.setText("garwfwav" + pos);
           /* if (list == null) {
                Toast.makeText(context, "Error, load comment!", Toast.LENGTH_SHORT).show();
                return;
            }
            commentData = new ArrayList<>();
            commentData.addAll(list);
            initData();*/
        }
    }
}
