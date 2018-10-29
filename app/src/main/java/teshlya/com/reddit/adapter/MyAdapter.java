package teshlya.com.reddit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import me.saket.inboxrecyclerview.InboxRecyclerView;
import teshlya.com.reddit.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
    Context context;
    InboxRecyclerView recyclerView;
    ArrayList<Integer> list = new ArrayList<>();



    public MyAdapter(InboxRecyclerView recyclerView) {
        setHasStableIds(true);
        this.recyclerView = recyclerView;
        for (int i=0; i<15;i++)
            list.add(i);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new Holder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Log.d("qwerty", "bind - " + i);

        holder.bind(list.get(i), i);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        FrameLayout frameLayout;
        TextView textView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.click);
            textView = itemView.findViewById(R.id.button);
        }

        public void bind(int value, final int pos) {
            textView.setText(String.valueOf(value));
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    recyclerView.expandItem(pos);

                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
