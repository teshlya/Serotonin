package teshlya.com.serotonin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import teshlya.com.serotonin.R;

public class DrawableIcon {

    public static Drawable clearMenuSearch;

    public static void initAllIcons(Context context){
        initClearMenuSearch(context);

    }

    private static void initClearMenuSearch(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.clear_menu);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        clearMenuSearch = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, Constants.SP_IN_PX_16, Constants.SP_IN_PX_16, true));
    }
}
