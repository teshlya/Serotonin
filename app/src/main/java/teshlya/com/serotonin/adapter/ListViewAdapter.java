package teshlya.com.serotonin.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teshlya.com.serotonin.R;

public class ListViewAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> subreddits;
    int countLocalFound;
    private int[] rainbow;

    public ListViewAdapter(Context context, ArrayList<String> subreddits, int countLocalFound) {
        super(context, 0, subreddits);
        this.context = context;
        this.subreddits = subreddits;
        this.countLocalFound = countLocalFound;
        rainbow = context.getResources().getIntArray(R.array.rainbow);
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.item_main_menu, null);

        TextView tvSubreddits = (TextView) convertView.findViewById(R.id.tvRows);
        tvSubreddits.setText(subreddits.get(position));

        TextView circleDescription = (TextView) convertView.findViewById(R.id.circleDescription);
        if (position < countLocalFound)
            circleDescription.setText("r/");
        else
            circleDescription.setText("s/");

        Drawable background = circleDescription.getBackground();
        if (background instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setStroke(2, rainbow[position % 11]);
        }

        return convertView;
    }
}
