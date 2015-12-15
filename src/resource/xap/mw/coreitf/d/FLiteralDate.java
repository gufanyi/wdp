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

public final class FLiteralDate implements java.io.Serializable,
	Comparable<FLiteralDate>, ICalendar {

    private static final long serialVersionUID = 1L;

    private static final int LRUSIZE = 500;

    private long utcTime;

    private transient GregorianCalendar basezoneCalendar;

    private final static Map<String, FLiteralDate> allUsedDate1 = new LRUMap<String, FLiteralDate>(
	    512);

    private final static Map<Long, FLiteralDate> allUsedDate2 = new LRUMap<Long, FLiteralDate>(
	    512);

    public static int num = 0;

    private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

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

    public FLiteralDate() {
	this(System.currentTimeMillis());
    }

    public FLiteralDate(long m) {
	this.utcTime = m;
	utcTime = utcTime - utcTime % 1000;
    }

    public FLiteralDate(String date) {
	int[] v = internalParse(date);
	GregorianCalendar cal = new GregorianCalendar(BASE_TIMEZONE);
	cal.set(Calendar.YEAR, v[0]);
	cal.set(Calendar.MONTH, v[1] - 1);
	cal.set(Calendar.DATE, v[2]);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	utcTime = cal.getTimeInMillis();
	basezoneCalendar = cal;
    }

    public boolean after(FLiteralDate when) {
	return this.compareTo(when) > 0;
    }

    public boolean afterDate(FLiteralDate when) {
	return compareTo(when) > 0 && !isSameDate(when);
    }

    public boolean before(FLiteralDate when) {
	return this.compareTo(when) < 0;
    }

    public boolean beforeDate(FLiteralDate when) {
	return compareTo(when) < 0 && !isSameDate(when);
    }

    public Object clone() {
	return new FLiteralDate(utcTime);
    }

    public int compareTo(FLiteralDate when) {
	long retl = this.utcTime - when.utcTime;
	if (retl == 0)
	    return 0;
	else
	    return retl > 0 ? 1 : -1;
    }

    public boolean equals(Object o) {
	if (o == null) {
	    return false;
	}
	return toString().equals(o.toString());
    }

    public boolean isSameDate(FLiteralDate o) {
	return equals(o);
    }

    public FLiteralDate getDateAfter(int days) {
	long l = utcTime + MILLIS_PER_DAY * days;
	return new FLiteralDate(l);
    }

    public FLiteralDate getDateBefore(int days) {
	return getDateAfter(-days);
    }

    public int getDay() {
	return basezoneCalendar().get(Calendar.DATE);
    }

    public int getYear() {
	return basezoneCalendar().get(Calendar.YEAR);
    }

    public int getDaysAfter(FLiteralDate when) {
	int days = 0;
	if (when != null) {
	    days = (int) ((this.utcTime - when.utcTime) / MILLIS_PER_DAY);
	}
	return days;
    }

    public static int getDaysBetween(FLiteralDate begin, FLiteralDate end) {
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

    public int getWeek() {
	int days = getDaysAfter(new FLiteralDate("1980-01-06"));
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

    public String toPersisted() {
	return toDateString(basezoneCalendar().get(Calendar.YEAR),
		basezoneCalendar.get(Calendar.MONTH) + 1,
		basezoneCalendar.get(Calendar.DATE));
    }

    public String toString() {
	return toPersisted();
    }

    public long getMillis() {
	return utcTime;
    }

    private GregorianCalendar basezoneCalendar() {
	if (basezoneCalendar == null) {
	    basezoneCalendar = new GregorianCalendar(BASE_TIMEZONE);
	    basezoneCalendar.setTimeInMillis(this.utcTime);
	}
	return basezoneCalendar;
    }

    public static FLiteralDate fromPersisted(String s) {
	return getDate(s);
    }

    public static FLiteralDate getDate(long d) {
	if (rwl.readLock().tryLock()) {
	    try {
		long longDate = d - d % 1000;
		FLiteralDate o = (FLiteralDate) allUsedDate2.get(longDate);
		if (o == null) {
		    FLiteralDate n = new FLiteralDate(longDate);
		    rwl.readLock().unlock();
		    rwl.writeLock().lock();
		    try {
			o = n;
			allUsedDate2.put(longDate, o);
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
	    return new FLiteralDate(d);
	}
    }

    public static FLiteralDate getDate(String strDate) {
	if (rwl.readLock().tryLock()) {
	    try {
		FLiteralDate o = (FLiteralDate) allUsedDate1.get(strDate);
		if (o == null) {
		    FLiteralDate n = new FLiteralDate(strDate);
		    rwl.readLock().unlock();
		    rwl.writeLock().lock();
		    try {
			o = n;
			allUsedDate1.put(strDate, o);
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
	    return new FLiteralDate(strDate);
	}
    }

    public static FLiteralDate getDate(Date date) {
	return new FLiteralDate(date);
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
	    tokens[i++] = st.nextToken().trim();
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

    @Override
    public int hashCode() {
	return (int) (utcTime ^ (utcTime >>> 32));
    }

    // 以下API为实现接口所必须， 不建议使用

    public FLiteralDate(java.sql.Date date) {
	this(date.getTime());
    }

    public FLiteralDate(java.util.Date date) {
	this(date.getTime());
    }

    public String toStdString(TimeZone zone) {
	return toPersisted();
    }

    public String toString(TimeZone zone, DateFormat format) {
	zone = Calendars.getGMTTimeZone(BASE_TIMEZONE);
	Date dt = new Date(utcTime);
	format.setTimeZone(zone);
	return format.format(dt);
    }

    public String toStdString() {
	return toPersisted();
    }

}
