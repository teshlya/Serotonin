package teshlya.com.serotonin.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import teshlya.com.serotonin.R;
import teshlya.com.serotonin.model.DataMainMenu;
import teshlya.com.serotonin.parse.ParseArticle;
import teshlya.com.serotonin.utils.Constants;

public class MainMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickHeaderItemDecoration.StickyHeaderInterface {
    private ArrayList<DataMainMenu> list;
    private Context context;

    public MainMenuAdapter(ArrayList<DataMainMenu> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HeaderItem.HEADER:
                return new MainMenuAdapter.HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_main_menu, parent, false));
            case HeaderItem.ITEM:
                return new MainMenuAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_menu, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bindData(position);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isHeader();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) {
        if (list.get(headerPosition).isHeader() == 1)
            return R.layout.header_main_menu;
        else {
            return 0;
        }
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        ((TextView) header.findViewById(R.id.tvHeader)).setText(list.get(headerPosition).getTitle());
        ((ImageView) header.findViewById(R.id.icon)).setImageDrawable(getDrawable(list.get(headerPosition).getDetail()));
    }

    @Override
    public boolean isHeader(int itemPosition) {
        if (list.get(itemPosition).isHeader() == 1)
            return true;
        else
            return false;
    }

    private Drawable getDrawable(String icon) {
        int id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(id)).getBitmap();
        Drawable drawable = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_16, Constants.SP_IN_PX_16, true));
        drawable.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(R.color.colorMainMenuIcon), PorterDuff.Mode.SRC_ATOP));
        return drawable;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        ImageView iconHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
            iconHeader = itemView.findViewById(R.id.icon);
        }

        void bindData(int position) {
            tvHeader.setText(list.get(position).getTitle());
            iconHeader.setImageDrawable(getDrawable(list.get(position).getDetail()));
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRows;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            tvRows = itemView.findViewById(R.id.tvRows);
            view = itemView.findViewById(R.id.onClickItem);
        }

        void bindData(final int position) {
            tvRows.setText(list.get(position).getTitle());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCommunity(list.get(position).getDetail(),
                            list.get(position).getTitle(),
                            (position < 5 ? false : true));
                }
            });
        }

        private void openCommunity(String url, String community, Boolean star) {
            ParseArticle.hmap.clear();
            Intent data = new Intent();
            data.putExtra(Constants.URL, url);
            data.putExtra(Constants.COMMUNITY, community);
            data.putExtra(Constants.STAR, star);
            ((Activity) context).setResult(((Activity) context).RESULT_OK, data);
            ((Activity) context).finish();
        }
    }

}

