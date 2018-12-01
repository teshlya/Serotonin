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
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF1976D2, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void bind() {
        progressBar.setIndeterminate(true);
    }
}
