package teshlya.com.reddit.utils;

import android.content.res.Resources;

public class Constants {

    public static String DOMAIN = "https://www.reddit.com";

    public static final String POSITION = "position";
    public static final String URL = "url";
    public static final String DATA = "data";
    public static final String COMMUNITY = "community";

    public static final int TITLE = 1;
    public static final int MEDIA = 3;
    public static final int TEXT = 2;
    public static final int DETAIL = 4;

    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";

    public static final int SP_IN_PX_12 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 12;
    public static final int SP_IN_PX_14 = Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity) * 14;
    public static final String CALLBACK_FRONT_PAGE = "callbackFrontPage";
}
