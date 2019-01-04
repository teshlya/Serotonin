package teshlya.com.serotonin.utils;

import android.content.res.Resources;

public class Constants {

    public static String DOMAIN = "https://www.reddit.com";

    public static final String POSITION = "position";
    public static final String URL = "url";
    public static final String DATA = "data";
    public static final String COMMUNITY = "community";
    public static final String STAR = "star";

    public static final int TITLE = 1;
    public static final int MEDIA = 3;
    public static final int TEXT = 2;
    public static final int DETAIL = 4;

    public static final int NORMAL_CLICK = 1;
    public static final int LONG_CLICK = 2;

    public static final int VIEW_TYPE_ITEM = 5;
    public static final int VIEW_TYPE_LOADING = 6;
    public static final int VIEW_TYPE_TWO_LINE_ITEM = 7;

    public static final int RESULT_SABREDDIT = 1;
    public static final int RESULT_QUESTION = 2;

    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";

    public static final int SP_IN_PX_12 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 12;
    public static final int SP_IN_PX_14 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 14;
    public static final int SP_IN_PX_16 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 16;
    public static final int SP_IN_PX_18 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 18;
    public static final int SP_IN_PX_20 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 20;
    public static final int SP_IN_PX_22 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 22;
}
