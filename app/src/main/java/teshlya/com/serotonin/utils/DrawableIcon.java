package teshlya.com.serotonin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import teshlya.com.serotonin.R;

public class DrawableIcon {

    public static Drawable comment;
    public static Drawable score;
    public static Drawable hintSearch;
    public static Drawable clearMenuSearch;

    public static void initAllIcons(Context context){
        initCommentIcon(context);
        initScoreIcon(context);
        initHintSearchIcon(context);
        initClearMenuSearch(context);

    }

    private static void initCommentIcon(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.chat);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        comment = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_12, Constants.SP_IN_PX_12, true));
    }


    private static void initScoreIcon(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.score);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        score = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_12, Constants.SP_IN_PX_12, true));
    }

    private static void initHintSearchIcon(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.search);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        hintSearch = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_14, Constants.SP_IN_PX_14, true));
    }

    private static void initClearMenuSearch(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.clear_menu);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        clearMenuSearch = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_16, Constants.SP_IN_PX_16, true));
    }
}
