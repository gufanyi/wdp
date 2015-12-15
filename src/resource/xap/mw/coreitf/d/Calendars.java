package xap.mw.coreitf.d;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

public class Calendars {

    private static TimeZone defZone;

    public static TimeZone getGMTDefault() {
	if (defZone == null) {
	    TimeZone zone = getGMTTimeZone(TimeZone.getDefault());
	    TimeZone.setDefault(zone);
	    return zone;
	}
	return defZone;
    }

    public static void setGMTDefault(TimeZone zone) {
	defZone = getGMTTimeZone(zone);
    }

    public static TimeZone getGMTTimeZone(TimeZone zone) {
	if (zone == null) {
	    return getGMTDefault();
	}
	if (zone.getID().startsWith("GMT") || zone.getID().startsWith("UTC")) {
	    return zone;
	} else {
	    long rawOffset = zone.getRawOffset();
	    long absrawOffset = rawOffset > 0 ? rawOffset : -rawOffset;
	    int hourOffset = (int) absrawOffset / ICalendar.MILLIS_PER_HOUR;
	    int minueOffset = (int) ((absrawOffset % ICalendar.MILLIS_PER_HOUR) / ICalendar.MILLIS_PER_MINUTE);
	    return toGMTZone(rawOffset >= 0, hourOffset, minueOffset);
	}
    }

    public static TimeZone getGMTTimeZone(String id) {
	if (id == null)
	    return getGMTDefault();
	if (id.startsWith("UTC")) {
	    id = id.replace("UTC", "GMT");
	}
	TimeZone zone = TimeZone.getTimeZone(id);
	return getGMTTimeZone(zone);
    }

    public static FDateTime getUFDateTime(String date, DateFormat format) {
	try {
	    Date d = format.parse(date);
	    return new FDateTime(d);
	} catch (ParseException e) {
	    throw new IllegalArgumentException("Illegal date time: " + date);
	}
    }

    public static FDate getUFDate(String date, DateFormat format) {
	try {
	    Date d = format.parse(date);
	    return new FDate(d);
	} catch (ParseException e) {
	    throw new IllegalArgumentException("Illegal date time: " + date);
	}
    }

    public static String convertDate(String d, String tzFrom, String tzTo) {
	FDate date = new FDate(d, getGMTTimeZone(tzFrom));
	return date.toStdString(getGMTTimeZone(tzTo));
    }

    public static String convertDateTime(String dt, String tzFrom, String tzTo) {
	FDateTime dateTime = new FDateTime(dt, getGMTTimeZone(tzFrom));
	return dateTime.toStdString(getGMTTimeZone(tzTo));
    }

    private static TimeZone toGMTZone(boolean positive, int hourOffset,
	    int minueOffset) {
	StringBuffer sb = new StringBuffer("GMT");
	sb.append(positive ? '+' : '-');
	sb = hourOffset > 9 ? sb.append(hourOffset) : sb.append('0').append(
		hourOffset);
	sb.append(':');
	sb = minueOffset > 9 ? sb.append(minueOffset) : sb.append('0').append(
		minueOffset);
	return TimeZone.getTimeZone(sb.toString());
    }

    public static String getValidUFDateString(String str) {
	int[] v = FDate.internalParse(str);
	StringBuffer sb = new StringBuffer();
	append(sb, v[0], '-');
	append(sb, v[1], '-');
	if (v[2] < 10) {
	    sb.append('0');
	}
	sb.append(v[2]);
	return sb.toString();
    }

    public static String getValidUFDateTimeString(String str) {
	int[] v = FDateTime.internalParse(str);
	StringBuffer sb = new StringBuffer();
	append(sb, v[0], '-');
	append(sb, v[1], '-');
	append(sb, v[2], ' ');
	append(sb, v[3], ':');
	append(sb, v[4], ':');
	if (v[5] < 10) {
	    sb.append('0');
	}
	sb.append(v[5]);

	return sb.toString();
    }

    public static String getValidUFTimeString(String time) {
	int[] v = FTime.internalParse(time);
	StringBuffer sb = new StringBuffer();
	append(sb, v[0], ':');
	append(sb, v[1], ':');
	if (v[2] < 10) {
	    sb.append('0');
	}
	sb.append(v[2]);
	return sb.toString();
    }

    private static void append(StringBuffer sb, int v, char split) {
	if (v < 10) {
	    sb.append('0');
	}
	sb.append(v).append(split);
    }

    public static long getMillis(FDate date, FTime time, TimeZone zone) {
	zone = Calendars.getGMTTimeZone(zone);
	long mills = date.getMillis() + time.getMillis() + zone.getRawOffset();
	if (mills < date.getMillis()) {
	    mills += ICalendar.MILLIS_PER_DAY;
	} else if (mills > (date.getMillis() + ICalendar.MILLIS_PER_DAY)) {
	    mills -= ICalendar.MILLIS_PER_DAY;
	}
	return mills;
    }
}
