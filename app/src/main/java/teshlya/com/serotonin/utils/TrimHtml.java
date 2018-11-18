package teshlya.com.serotonin.utils;

public class TrimHtml {

    public static CharSequence trim(CharSequence source) {
        if (source == null)
            return "";
        int i = source.length();
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }
        return source.subSequence(0, i + 1);
    }
}
