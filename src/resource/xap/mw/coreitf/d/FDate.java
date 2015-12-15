package xap.mw.coreitf.d;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class FDate implements java.io.Serializable, Comparable<FDate>,
	ICalendar {

    private static final long serialVersionUID = 1L;

    private static final int LRUSIZE = 500;

    private long utcTime;

    private final static Map<String, FDate> allUsedDate1 = new LRUMap<String, FDate>(
	    512);
    public static int num = 0;

    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    static {
	Calendars.getGMTDefault();
    }

    private static class LRUMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	public LRUMap(int initSize) {
	    super(initSize, 1f, true);
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
	    if (size() > LRUSIZE)
		return true;
	    else
		return false;
	}
    }

    public FDate() {
	this(System.currentTimeMillis());
    }

    public FDate(long m) {
	this.utcTime = m;
	utcTime = utcTime - utcTime % 1000;
    }
    

    public FDate(java.sql.Date date) {
	this(date.getTime());
    }

    public FDate(java.util.Date date) {
	this(date.getTime());
    }

    public FDate(boolean begin) {
	GregorianCalendar cal = new GregorianCalendar(Calendars.getGMTDefault());
	cal.setTimeInMillis(System.currentTimeMillis());
	if (begin) {
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	} else {
	    cal.set(Calendar.HOUR_OF_DAY, 23);
	    cal.set(Calendar.MINUTE, 59);
	    cal.set(Calendar.SECOND, 59);
	    cal.set(Calendar.MILLISECOND, 0);
	}
	this.utcTime = cal.getTimeInMillis();
    }

    public FDate(String date, boolean begin) {
	int[] v = internalParse(date);
	GregorianCalendar cal = null;
	if (begin) {
	    cal = new GregorianCalendar(v[0], v[1] - 1, v[2]);
	} else {
	    cal = new GregorianCalendar(v[0], v[1] - 1, v[2], 23, 59, 59);
	}
	utcTime = cal.getTimeInMillis();
    }

    public FDate(String date) {
	int[] v = internalParse(date);
	utcTime = new GregorianCalendar(v[0], v[1] - 1, v[2]).getTimeInMillis();
    }

    public FDate(String date, TimeZone zone, boolean begin) {
	zone = Calendars.getGMTTimeZone(zone);
	int[] v = internalParse(date);
	GregorianCalendar cal = new GregorianCalendar(zone);
	cal.set(Calendar.YEAR, v[0]);
	cal.set(Calendar.MONTH, v[1] - 1);
	cal.set(Calendar.DATE, v[2]);
	if (begin) {
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	} else {
	    cal.set(Calendar.HOUR_OF_DAY, 23);
	    cal.set(Calendar.MINUTE, 59);
	    cal.set(Calendar.SECOND, 59);
	    cal.set(Calendar.MILLISECOND, 0);
	}
	this.utcTime = cal.getTimeInMillis();
    }

    public FDate(String date, TimeZone zone) {
	zone = Calendars.getGMTTimeZone(zone);
	int[] v = internalParse(date);
	GregorianCalendar cal = new GregorianCalendar(zone);
	cal.set(Calendar.YEAR, v[0]);
	cal.set(Calendar.MONTH, v[1] - 1);
	cal.set(Calendar.DATE, v[2]);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	utcTime = cal.getTimeInMillis();
    }

    public boolean after(FDate when) {
	return this.compareTo(when) > 0;
    }

    public boolean afterDate(FDate when) {
	return compareTo(when) > 0 && !isSameDate(when);
    }

    public boolean before(FDate when) {
	return this.compareTo(when) < 0;
    }

    public boolean beforeDate(FDate when) {
	return compareTo(when) < 0 && !isSameDate(when);
    }

    public Object clone() {
	return new FDate(utcTime);
    }

    public int compareTo(FDate when) {
	long retl = this.utcTime - when.utcTime;
	if (retl == 0)
	    return 0;
	else
	    return retl > 0 ? 1 : -1;
    }

    public boolean equals(Object o) {
	if ((o != null) && (o instanceof FDate)) {
	    return this.utcTime == ((FDate) o).utcTime;
	}
	return false;
    }

    public boolean isSameDate(FDate o) {
	GregorianCalendar cal = new GregorianCalendar(BASE_TIMEZONE);
	cal.setTimeInMillis(o.getMillis());
	GregorianCalendar basezoneCalendar = basezoneCalendar();
	if (basezoneCalendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
		&& basezoneCalendar.get(Calendar.MONTH) == cal
			.get(Calendar.MONTH)
		&& basezoneCalendar.get(Calendar.DATE) == cal
			.get(Calendar.DATE)) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean isSameDate(FDate o, TimeZone zone) {
	zone = Calendars.getGMTTimeZone(zone);
	GregorianCalendar cal = new GregorianCalendar(zone);
	cal.setTimeInMillis(o.getMillis());
	GregorianCalendar cal1 = getCalendar(zone);
	if (cal1.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
		&& cal1.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
		&& cal1.get(Calendar.DATE) == cal.get(Calendar.DATE)) {
	    return true;
	} else {
	    return false;
	}
    }

    public static FDate getDate(long d) {
	d = d - d % 1000;
	return getDate(Long.valueOf(d));
    }

    public static FDate getDate(String strDate) {
	return new FDate(strDate);
    }

    public static FDate getDate(String strDate, TimeZone zone, boolean begin) {
	if (rwl.readLock().tryLock()) {
	    try {
		String key = strDate + zone.getID() + begin;
		FDate o = (FDate) allUsedDate1.get(key);
		if (o == null) {
		    FDate n = new FDate(strDate, zone, begin);
		    rwl.readLock().unlock();
		    rwl.writeLock().lock();
		    try {
			o = n;
			allUsedDate1.put(key, o);
		    } finally {
			rwl.readLock().lock();
			rwl.writeLock().unlock();
		    }
		}
		return o;
	    } finally {
		rwl.readLock().unlock();
	    }
	} else {
	    return new FDate(strDate, zone, begin);
	}

    }

    public static FDate getDate(Date date) {
	return new FDate(date);
    }

    public static FDate getDate(Long date) {
	return new FDate(date);
    }

    public FDate asBegin() {
	return asBegin(BASE_TIMEZONE);
    }

    public FDate asBegin(TimeZone zone) {
	GregorianCalendar calendar = getCalendar(zone);
	if (0 == calendar.get(Calendar.HOUR_OF_DAY)
		&& 0 == calendar.get(Calendar.MINUTE)
		&& 0 == calendar.get(Calendar.SECOND)) {
	    return this;
	}

	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	return new FDate(calendar.getTimeInMillis());
    }

    public FDate asLocalBegin() {
	return asBegin(Calendars.getGMTDefault());
    }

    public FDate asEnd() {
	return asEnd(BASE_TIMEZONE);
    }

    public FDate asEnd(TimeZone zone) {
	GregorianCalendar calendar = getCalendar(zone);
	if (23 == calendar.get(Calendar.HOUR_OF_DAY)
		&& 59 == calendar.get(Calendar.MINUTE)
		&& 59 == calendar.get(Calendar.SECOND)) {
	    return this;
	}
	calendar.set(Calendar.HOUR_OF_DAY, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	return new FDate(calendar.getTimeInMillis());
    }

    public FDate asLocalEnd() {
	return asEnd(Calendars.getGMTDefault());
    }

    public FDate getDateAfter(int days) {
	long l = utcTime + MILLIS_PER_DAY * days;
	return new FDate(l);
    }

    public FDate getDateBefore(int days) {
	return getDateAfter(-days);
    }

    public int getDay() {
	return basezoneCalendar().get(Calendar.DATE);
    }

    public int getLocalDay() {
	return localCalendar().get(Calendar.DATE);
    }

    public int getDay(TimeZone zone) {
	return getCalendar(zone).get(Calendar.DATE);
    }

    public int getYear() {
	return basezoneCalendar().get(Calendar.YEAR);
    }

    public int getLocalYear() {
	return localCalendar().get(Calendar.YEAR);
    }

    public int getYear(TimeZone zone) {
	return getCalendar(zone).get(Calendar.YEAR);
    }

    public int getDaysAfter(FDate when) {
	int days = 0;
	if (when != null) {
	    days = (int) ((this.utcTime - when.utcTime) / MILLIS_PER_DAY);
	}
	return days;
    }

    public static int getDaysBetween(FDate begin, FDate end) {
	if (begin != null && end != null) {
	    return (int) ((end.utcTime - begin.utcTime) / MILLIS_PER_DAY);
	} else {
	    throw new IllegalArgumentException("Dates to compare can't be null");
	}
    }

    public int getDaysMonth() {
	return getDaysMonth(getYear(), getMonth());
    }

    public String getEnMonth() {
	return MONTH_SYM[basezoneCalendar().get(Calendar.MONTH)];
    }

    public String getEnWeek() {
	return WEEK_SYM[getWeek()];
    }

    public int getMonth() {
	return basezoneCalendar().get(Calendar.MONTH) + 1;
    }

    public int getLocalMonth() {
	return localCalendar().get(Calendar.MONDAY) + 1;
    }

    public int getMonth(TimeZone zone) {
	return getCalendar(zone).get(Calendar.MONDAY) + 1;
    }

    public int getWeek() {
	int days = getDaysAfter(new FDate("1980-01-06"));
	int week = days % 7;
	if (week < 0)
	    week += 7;
	return week;
    }

    public String getStrMonth() {
	return toString().substring(5, 7);
    }

    public String getStrDay() {
	return toString().substring(8, 10);
    }

    public boolean isLeapYear() {
	return isLeapYear(getYear());
    }

    public int getWeekOfYear() {
	return basezoneCalendar().get(Calendar.WEEK_OF_YEAR);
    }

    public Date toDate() {
	return new Date(utcTime);
    }

    public FLiteralDate toUFLiteralDate(TimeZone zone) {
	zone = Calendars.getGMTTimeZone(zone);
	return new FLiteralDate(toStdString(zone));
    }

    public int hashCode() {
	return (int) (utcTime ^ (utcTime >>> 32));
    }

    public String toPersisted() {
	return toStdString();
	// GregorianCalendar cal = new GregorianCalendar(BASE_TIMEZONE);
	// cal.setTimeInMillis(utcTime);
	// return FDateTime.toDateTimeString(cal.get(Calendar.YEAR), cal
	// .get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal
	// .get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal
	// .get(Calendar.SECOND));
    }

    public static FDate fromPersisted(String s) {
	int[] t = internalParse(s);
	GregorianCalendar cal = new GregorianCalendar(ICalendar.BASE_TIMEZONE);
	cal.set(Calendar.YEAR, t[0]);
	cal.set(Calendar.MONTH, t[1] - 1);
	cal.set(Calendar.DATE, t[2]);
	long utcTime = cal.getTimeInMillis();
	return new FDate(utcTime);
    }

    public String toStdString() {
	return toStdString(BASE_TIMEZONE);
    }

    public String toStdString(TimeZone zone) {
	GregorianCalendar cal = new GregorianCalendar(zone);
	cal.setTimeInMillis(utcTime);
	return toDateString(cal.get(Calendar.YEAR),
		cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));

    }

    public String toString(TimeZone zone, DateFormat format) {
	zone = Calendars.getGMTTimeZone(zone);
	Date dt = new Date(utcTime);
	format.setTimeZone(zone);
	return format.format(dt);
    }

    public String toString() {
	return toPersisted();
    }

    public String toLocalString() {
	GregorianCalendar localCalendar = localCalendar();
	return toDateString(localCalendar.get(Calendar.YEAR),
		localCalendar.get(Calendar.MONTH) + 1,
		localCalendar.get(Calendar.DATE));
    }

    public long getMillis() {
	return utcTime;
    }

    static int[] internalParse(String str) {
	if (str == null)
	    throw new IllegalArgumentException("invalid date: " + str);

	str = str.trim();
	int spaceIndex = str.indexOf(' ');
	if (spaceIndex > -1) {
	    str = str.substring(0, spaceIndex);
	}

	String[] tokens = new String[3];
	StringTokenizer st = new StringTokenizer(str, "-/");
	if (st.countTokens() != 3) {
	    throw new IllegalArgumentException("invalid date: " + str);
	}

	int i = 0;
	while (st.hasMoreTokens()) {
	    tokens[i++] = st.nextToken();
	}

	try {
	    int year = Integer.parseInt(tokens[0]);
	    int month = Integer.parseInt(tokens[1]);
	    if (month < 1 || month > 12)
		throw new IllegalArgumentException("invalid date: " + str);
	    int day = Integer.parseInt(tokens[2]);

	    int daymax = isLeapYear(year) ? LEAP_MONTH_LENGTH[month - 1]
		    : MONTH_LENGTH[month - 1];

	    if (day < 1 || day > daymax)
		throw new IllegalArgumentException("invalid date: " + str);
	    return new int[] { year, month, day };
	} catch (Throwable thr) {
	    if (thr instanceof IllegalArgumentException) {
		throw (IllegalArgumentException) thr;
	    } else {
		throw new IllegalArgumentException("invalid date: " + str);
	    }
	}

    }

    public static int getDaysMonth(int year, int month) {
	if (isLeapYear(year)) {
	    return LEAP_MONTH_LENGTH[month - 1];
	} else {
	    return MONTH_LENGTH[month - 1];
	}
    }

    public static boolean isLeapYear(int year) {
	if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
	    return true;
	else
	    return false;
    }

    private GregorianCalendar localCalendar() {
	GregorianCalendar localCalendar = new GregorianCalendar(
		Calendars.getGMTDefault());
	localCalendar.setTimeInMillis(this.utcTime);
	return localCalendar;
    }

    private GregorianCalendar basezoneCalendar() {
	GregorianCalendar basezoneCalendar = new GregorianCalendar(
		BASE_TIMEZONE);
	basezoneCalendar.setTimeInMillis(this.utcTime);
	return basezoneCalendar;
    }

    private GregorianCalendar getCalendar(TimeZone zone) {
	zone = Calendars.getGMTTimeZone(zone);
	GregorianCalendar basezoneCalendar = new GregorianCalendar(zone);
	basezoneCalendar.setTimeInMillis(this.utcTime);
	return basezoneCalendar;
    }

    private static String toDateString(int year, int month, int day) {
	String strYear = String.valueOf(year);
	for (int j = strYear.length(); j < 4; j++)
	    strYear = "0" + strYear;
	String strMonth = String.valueOf(month);
	if (strMonth.length() < 2)
	    strMonth = "0" + strMonth;
	String strDay = String.valueOf(day);
	if (strDay.length() < 2)
	    strDay = "0" + strDay;
	return strYear + "-" + strMonth + "-" + strDay;
    }

}
