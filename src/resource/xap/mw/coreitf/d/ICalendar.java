package xap.mw.coreitf.d;

import java.text.DateFormat;
import java.util.TimeZone;

public interface ICalendar {
    public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

    public static final int MILLIS_PER_HOUR = 60 * 60 * 1000;

    public static final int MILLIS_PER_MINUTE = 60 * 1000;

    public static final int MILLIS_PER_SECOND = 1000;

    public static final String STD_DATE_FORMAT = "yyyy-MM-dd";

    public static final String STD_TIME_FORMAT = "HH:mm:ss";

    public static final String STD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String[] MONTH_SYM = new String[] { "Jan", "Feb",
	    "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
	    "Dec" };

    public static final String[] WEEK_SYM = new String[] { "Sun", "Mon", "Tue",
	    "Wed", "Thu", "Fri", "Sat" };

    public static int MONTH_LENGTH[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
	    31, 30, 31 };
    public static int LEAP_MONTH_LENGTH[] = { 31, 29, 31, 30, 31, 30, 31, 31,
	    30, 31, 30, 31 };

    public static final TimeZone BASE_TIMEZONE = TimeZone
	    .getTimeZone("GMT+08:00");

    public String toStdString();

    public String toStdString(TimeZone zone);

    public String toString(TimeZone zone, DateFormat format);

    long getMillis();

    public String toString();

    public String toPersisted();

}
