package teshlya.com.serotonin.holder;


import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.serotonin.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;

    public LoadingViewHolder(View view) {
        super(view);
        progressBar = view.findViewById(R.id.progressBar);
    }

    public void bind() {
        progressBar.setIndeterminate(true);
    }
}
