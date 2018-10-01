package teshlya.com.reddit;

import android.util.Log;

import java.text.MessageFormat;
import java.util.Date;

public class TimeAgo {

    private String prefixAgo = null;

    private String prefixFromNow = null;

    private String suffixAgo = "ago"; //Messages.getString("TimeAgo.AGO"); //$NON-NLS-1$

    private String suffixFromNow = "from now"; //Messages.getString("TimeAgo.SUFFIX_FROM_NOW"); //$NON-NLS-1$

    private String second = " second"; //Messages.getString("TimeAgo.SECONDS"); //$NON-NLS-1$

    private String seconds = " seconds"; //Messages.getString("TimeAgo.SECONDS"); //$NON-NLS-1$

    private String minute = " minute"; //Messages.getString("TimeAgo.MINUTE"); //$NON-NLS-1$

    private String minutes = " minutes"; //Messages.getString("TimeAgo.MINUTES"); //$NON-NLS-1$

    private String hour = " hour"; //Messages.getString("TimeAgo.HOUR"); //$NON-NLS-1$

    private String hours = " hours"; //Messages.getString("TimeAgo.HOURS"); //$NON-NLS-1$

    private String day = " day"; //Messages.getString("TimeAgo.DAY"); //$NON-NLS-1$

    private String days = " days"; //Messages.getString("TimeAgo.DAYS"); //$NON-NLS-1$

    private String month = " month"; //Messages.getString("TimeAgo.MONTH"); //$NON-NLS-1$

    private String months = " months"; //Messages.getString("TimeAgo.MONTHS"); //$NON-NLS-1$

    private String year = " year"; //Messages.getString("TimeAgo.YEAR"); //$NON-NLS-1$

    private String years = " years"; //Messages.getString("TimeAgo.YEARS"); //$NON-NLS-1$

    /**
     * Get time until specified date
     *
     * @param date
     * @return time string
     */
    public String timeUntil(final Date date) {
        return timeUntil(date.getTime());
    }

    /**
     * Get time ago that date occurred
     *
     * @param date
     * @return time string
     */
    public String timeAgo(final Date date) {
        return timeAgo(date.getTime());
    }

    /**
     * Get time until specified milliseconds date
     *
     * @param millis
     * @return time string
     */
    public String timeUntil(final long millis) {
        return time(millis - System.currentTimeMillis(), true);
    }

    /**
     * Get time ago that milliseconds date occurred
     *
     * @param millis
     * @return time string
     */
    public String timeAgo(final long millis) {
        return time(System.currentTimeMillis() - millis, false);
    }

    /**
     * Get time string for milliseconds distance
     *
     * @param distanceMillis
     * @param allowFuture
     * @return time string
     */
    public String time(long distanceMillis, final boolean allowFuture) {
        final String prefix;
        final String suffix;
        //distanceMillis = Math.abs(distanceMillis);

        if (allowFuture) {
            prefix = prefixFromNow;
            suffix = suffixFromNow;
        } else {
            prefix = prefixAgo;
            suffix = suffixAgo;
        }

        final double seconds = distanceMillis / 1000;
        final double minutes = seconds / 60;
        final double hours = minutes / 60;
        final double days = hours / 24;
        final double months = days / 30.5;
        final double years = days / 365;

        String time = "";
        if (Math.round(seconds) == 1)
            time = Math.round(seconds) + this.second;
        else if (Math.round(seconds) < 60)
            time = Math.round(seconds) + this.seconds;
        else if (Math.round(minutes) == 1)
            time = Math.round(minutes) + this.minute;
        else if (Math.round(minutes) < 60)
            time = Math.round(minutes) + this.minutes;
        else if (Math.round(hours) == 1)
            time = Math.round(hours) + this.hour;
        else if (Math.round(hours) < 24)
            time = Math.round(hours) + this.hours;
        else if (Math.round(days) == 1)
            time = Math.round(days) + this.day;
        else if (Math.round(days) < 29)
            time = Math.round(days) + this.days;
        else if (Math.round(months) == 1)
            time = Math.round(months) + this.month;
        else if (Math.round(months) < 12)
            time = Math.round(months) + this.months;
        else if (Math.round(years) == 1)
            time = Math.round(years) + this.year;
        else
            time = Math.round(years) + this.years;


        return join(prefix, time, suffix);
    }

    /**
     * Join time string with prefix and suffix. The prefix and suffix are only
     * joined with the time if they are non-null and non-empty
     *
     * @param prefix
     * @param time
     * @param suffix
     * @return non-null joined string
     */
    public String join(final String prefix, final String time,
                       final String suffix) {
        StringBuilder joined = new StringBuilder();
        if (prefix != null && prefix.length() > 0)
            joined.append(prefix).append(' ');
        joined.append(time);
        if (suffix != null && suffix.length() > 0)
            joined.append(' ').append(suffix);
        return joined.toString();
    }

    /**
     * @return prefixAgo
     */
    public String getPrefixAgo() {
        return prefixAgo;
    }

    /**
     * @param prefixAgo prefixAgo value
     * @return this instance
     */
    public TimeAgo setPrefixAgo(final String prefixAgo) {
        this.prefixAgo = prefixAgo;
        return this;
    }

    /**
     * @return prefixFromNow
     */
    public String getPrefixFromNow() {
        return prefixFromNow;
    }

    /**
     * @param prefixFromNow prefixFromNow value
     * @return this instance
     */
    public TimeAgo setPrefixFromNow(final String prefixFromNow) {
        this.prefixFromNow = prefixFromNow;
        return this;
    }

    /**
     * @return suffixAgo
     */
    public String getSuffixAgo() {
        return suffixAgo;
    }

    /**
     * @param suffixAgo suffixAgo value
     * @return this instance
     */
    public TimeAgo setSuffixAgo(final String suffixAgo) {
        this.suffixAgo = suffixAgo;
        return this;
    }

    /**
     * @return suffixFromNow
     */
    public String getSuffixFromNow() {
        return suffixFromNow;
    }

    /**
     * @param suffixFromNow suffixFromNow value
     * @return this instance
     */
    public TimeAgo setSuffixFromNow(final String suffixFromNow) {
        this.suffixFromNow = suffixFromNow;
        return this;
    }

    /**
     * @return seconds
     */
    public String getSeconds() {
        return seconds;
    }

    /**
     * @param seconds seconds value
     * @return this instance
     */
    public TimeAgo setSeconds(final String seconds) {
        this.seconds = seconds;
        return this;
    }

    /**
     * @return minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     * @param minute minute value
     * @return this instance
     */
    public TimeAgo setMinute(final String minute) {
        this.minute = minute;
        return this;
    }

    /**
     * @return minutes
     */
    public String getMinutes() {
        return minutes;
    }

    /**
     * @param minutes minutes value
     * @return this instance
     */
    public TimeAgo setMinutes(final String minutes) {
        this.minutes = minutes;
        return this;
    }

    /**
     * @return hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * @param hour hour value
     * @return this instance
     */
    public TimeAgo setHour(final String hour) {
        this.hour = hour;
        return this;
    }

    /**
     * @return hours
     */
    public String getHours() {
        return hours;
    }

    /**
     * @param hours hours value
     * @return this instance
     */
    public TimeAgo setHours(final String hours) {
        this.hours = hours;
        return this;
    }

    /**
     * @return day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day day value
     * @return this instance
     */
    public TimeAgo setDay(final String day) {
        this.day = day;
        return this;
    }

    /**
     * @return days
     */
    public String getDays() {
        return days;
    }

    /**
     * @param days days value
     * @return this instance
     */
    public TimeAgo setDays(final String days) {
        this.days = days;
        return this;
    }

    /**
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month month value
     * @return this instance
     */
    public TimeAgo setMonth(final String month) {
        this.month = month;
        return this;
    }

    /**
     * @return months
     */
    public String getMonths() {
        return months;
    }

    /**
     * @param months months value
     * @return this instance
     */
    public TimeAgo setMonths(String months) {
        this.months = months;
        return this;
    }

    /**
     * @return year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year year value
     * @return this instance
     */
    public TimeAgo setYear(String year) {
        this.year = year;
        return this;
    }

    /**
     * @return years
     */
    public String getYears() {
        return years;
    }

    /**
     * @param years years value
     * @return this instance
     */
    public TimeAgo setYears(String years) {
        this.years = years;
        return this;
    }
}