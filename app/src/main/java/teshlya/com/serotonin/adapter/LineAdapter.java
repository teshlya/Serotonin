package teshlya.com.serotonin.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import teshlya.com.serotonin.R;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.LineHolder> {

    private int lineCount;
    private Context context;

    public LineAdapter(int lineCount) {
        this.lineCount = lineCount;
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.line_item, parent, false);
        return new LineAdapter.LineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return lineCount;
    }

    class LineHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;

        public LineHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.line);
        }

        public void bind(int position){
            int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
            linearLayout.setBackgroundColor(rainbow[position % 12]);
        }
    }
}
