package xap.mw.coreitf.d;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class FDateTime implements java.io.Serializable,
		Comparable<FDateTime>, ICalendar {
	static final long serialVersionUID = 1L;

	static {
		Calendars.getGMTDefault();
	}

	private long utcTime;

	public FDateTime() {
		this(System.currentTimeMillis());
	}

	public FDateTime(long m) {
		this.utcTime = m;
		utcTime = utcTime - utcTime % 1000;
	}

	public FDateTime(String date) {
		int[] t = internalParse(date);
		this.utcTime = new GregorianCalendar(t[0], t[1] - 1, t[2], t[3], t[4],
				t[5]).getTimeInMillis();
	}

	public FDateTime(String date, TimeZone zone) {
		zone = Calendars.getGMTTimeZone(zone);
		int[] t = internalParse(date);
		GregorianCalendar cal = new GregorianCalendar(zone);
		cal.set(Calendar.YEAR, t[0]);
		cal.set(Calendar.MONTH, t[1] - 1);
		cal.set(Calendar.DATE, t[2]);
		cal.set(Calendar.HOUR_OF_DAY, t[3]);
		cal.set(Calendar.MINUTE, t[4]);
		cal.set(Calendar.SECOND, t[5]);
		cal.set(Calendar.MILLISECOND, 0);
		utcTime = cal.getTimeInMillis();
	}

	public FDateTime(java.sql.Date date) {
		this((java.util.Date) date);
	}

	public FDateTime(java.util.Date date) {
		this(date.getTime());
	}

	public FDateTime(FDate date, FTime time) {
		if (null == date) {
			date = new FDate(true);
		}

		if (null == time) {
			time = new FTime();
		}
		this.utcTime = Calendars.getMillis(date, time,
				Calendars.getGMTDefault());
	}

	public boolean after(FDateTime dateTime) {
		return this.utcTime - dateTime.utcTime > 0;
	}

	public boolean before(FDateTime dateTime) {
		return this.utcTime - dateTime.utcTime < 0;
	}

	public Object clone() {
		return new FDateTime(utcTime);
	}

	public int compareTo(FDateTime dateTime) {
		long ret = this.utcTime - dateTime.utcTime;
		if (ret == 0)
			return 0;
		return ret > 0 ? 1 : -1;
	}

	public FDate getDate() {
		return new FDate(this.utcTime);
	}

	public FDate getBeginDate() {
		return new FDate(this.utcTime).asBegin();
	}

	public FDate getEndDate() {
		return new FDate(this.utcTime).asEnd();
	}

	public boolean after(FDate when) {
		return getDate().compareTo(when) > 0;
	}

	public boolean afterDate(FDate when) {
		return getDate().afterDate(when);
	}

	public boolean before(FDate when) {
		return getDate().compareTo(when) < 0;
	}

	public boolean beforeDate(FDate when) {
		return getDate().beforeDate(when);
	}

	public int dateCompare(FDate when) {
		return getDate().compareTo(when);
	}

	public boolean isSameDate(FDate when) {
		return getDate().isSameDate(when);
	}

	public boolean isSameDate(FDate when, TimeZone zone) {
		return getDate().isSameDate(when, zone);
	}

	public int getDaysAfter(FDate when) {
		if (when != null) {
			return (int) ((utcTime - when.getMillis()) / MILLIS_PER_DAY);
		}
		return 0;
	}

	public FDateTime getDateTimeAfter(int days) {
		return new FDateTime(this.utcTime + MILLIS_PER_DAY * days);
	}

	public FDateTime getDateTimeBefore(int days) {
		return getDateTimeAfter(-days);
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

	public int getDaysAfter(FDateTime when) {
		int days = 0;
		if (when != null) {
			days = (int) ((utcTime - when.getMillis()) / MILLIS_PER_DAY);
		}
		return days;
	}

	public static int getDaysBetween(FDate begin, FDate end) {
		if (begin != null && end != null) {
			return (int) ((end.getMillis() - begin.getMillis()) / MILLIS_PER_DAY);
		}
		return 0;
	}

	public static int getHoursBetween(FDateTime begin, FDateTime end) {
		return (int) (getMilisBetween(begin, end) / MILLIS_PER_HOUR);
	}

	public static int getMinutesBetween(FDateTime begin, FDateTime end) {
		return (int) (getMilisBetween(begin, end) / MILLIS_PER_MINUTE);
	}

	public static int getSecondsBetween(FDateTime begin, FDateTime end) {
		return (int) (getMilisBetween(begin, end) / MILLIS_PER_SECOND);
	}

	private static long getMilisBetween(FDateTime begin, FDateTime end) {
		return end.utcTime - begin.utcTime;
	}

	public static int getDaysBetween(FDate begin, FDateTime end) {
		return getDaysBetween(begin, end.getDate());
	}

	public static int getDaysBetween(FDateTime begin, FDate end) {
		return getDaysBetween(begin.getDate(), end);
	}

	public static int getDaysBetween(FDateTime begin, FDateTime end) {
		return getDaysBetween(begin.getDate(), end.getDate());
	}

	public int getDaysMonth() {
		GregorianCalendar baseCal = basezoneCalendar();
		return getDaysMonth(baseCal.get(Calendar.YEAR),
				baseCal.get(Calendar.MONTH) + 1);
	}

	public static int getDaysMonth(int year, int month) {
		if (isLeapYear(year)) {
			return LEAP_MONTH_LENGTH[month - 1];
		} else {
			return MONTH_LENGTH[month - 1];
		}
	}

	public String getEnMonth() {
		return MONTH_SYM[basezoneCalendar().get(Calendar.MONTH)];
	}

	public String getEnWeek() {
		return WEEK_SYM[getWeek()];
	}

	public int getWeek() {
		return basezoneCalendar().get(Calendar.DAY_OF_WEEK) - 1;
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

	public int getMonth() {
		return basezoneCalendar().get(Calendar.MONTH) + 1;
	}

	public int getLocalMonth() {
		return localCalendar().get(Calendar.MONDAY) + 1;
	}

	public int getMonth(TimeZone zone) {
		return getCalendar(zone).get(Calendar.MONDAY) + 1;
	}

	public int getHour() {
		return basezoneCalendar().get(Calendar.HOUR_OF_DAY);
	}

	public int getLocalHour() {
		return localCalendar().get(Calendar.HOUR_OF_DAY);
	}

	public int getHout(TimeZone zone) {
		return getCalendar(zone).get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return basezoneCalendar().get(Calendar.MINUTE);
	}

	public int getLocalMinute() {
		return localCalendar().get(Calendar.MINUTE);
	}

	public int getMinute(TimeZone zone) {
		return getCalendar(zone).get(Calendar.MINUTE);
	}

	public int getSecond() {
		return basezoneCalendar().get(Calendar.SECOND);
	}

	public long getMillis() {
		return utcTime;
	}

	public long getMillisAfter(FDateTime dateTime) {
		if (dateTime != null) {
			return this.utcTime - dateTime.utcTime;
		} else {
			throw new IllegalArgumentException("date time can't be null");
		}
	}

	public String getStrMonth() {
		int month = getMonth();
		if (month > 0 && month < 10)
			return "0" + Integer.toString(month);
		else if (month >= 10 && month < 13)
			return Integer.toString(month);
		else
			return null;
	}

	public String getStrDay() {
		int day = getDay();
		if (day > 0 && day < 10)
			return "0" + Integer.toString(day);
		else if (day >= 10 && day < 32)
			return Integer.toString(day);
		else
			return null;
	}

	public String getTime() {
		return toString().substring(11, 19);
	}

	public FTime getUFTime() {
		return new FTime(utcTime);
	}

	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
			return true;
		else
			return false;
	}

	public boolean isLeapYear() {
		return isLeapYear(getYear());
	}

	public int getWeekOfYear() {
		return basezoneCalendar().get(Calendar.WEEK_OF_YEAR);
	}

	public String toString() {
		GregorianCalendar baseCalendar = basezoneCalendar();
		return toDateTimeString(baseCalendar.get(Calendar.YEAR),
				baseCalendar.get(Calendar.MONTH) + 1,
				baseCalendar.get(Calendar.DATE),
				baseCalendar.get(Calendar.HOUR_OF_DAY),
				baseCalendar.get(Calendar.MINUTE),
				baseCalendar.get(Calendar.SECOND));
	}

	public String toLocalString() {
		GregorianCalendar localCalendar = localCalendar();
		return toDateTimeString(localCalendar.get(Calendar.YEAR),
				localCalendar.get(Calendar.MONTH) + 1,
				localCalendar.get(Calendar.DATE),
				localCalendar.get(Calendar.HOUR_OF_DAY),
				localCalendar.get(Calendar.MINUTE),
				localCalendar.get(Calendar.SECOND));
	}

	public String toString(TimeZone zone, DateFormat format) {
		zone = Calendars.getGMTTimeZone(zone);
		Date dt = new Date(utcTime);
		format.setTimeZone(zone);
		return format.format(dt);
	}

	public String toStdString() {
		return toStdString(BASE_TIMEZONE);
	}

	public String toStdString(TimeZone zone) {
		zone = Calendars.getGMTTimeZone(zone);
		if (zone.equals(Calendars.getGMTDefault())) {
			return toString();
		}
		GregorianCalendar cal = new GregorianCalendar(zone);
		cal.setTimeInMillis(utcTime);
		return toDateTimeString(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
	}

	public boolean equals(Object o) {
		if ((o != null) && (o instanceof FDateTime)) {
			return this.utcTime == ((FDateTime) o).utcTime;
		}
		return false;
	}

	public int hashCode() {
		return (int) (utcTime ^ (utcTime >>> 32));
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
		GregorianCalendar localCalendar = new GregorianCalendar(zone);
		localCalendar.setTimeInMillis(this.utcTime);
		return localCalendar;
	}

	static String toDateTimeString(int year, int month, int day, int hour,
			int minute, int second) {
		StringBuffer sb = new StringBuffer();
		String strYear = String.valueOf(year);
		for (int j = strYear.length(); j < 4; j++)
			sb.append('0');
		sb.append(strYear).append('-');

		append(sb, month, '-');
		append(sb, day, ' ');
		append(sb, hour, ':');
		append(sb, minute, ':');
		if (second < 10) {
			sb.append('0');
		}
		sb.append(second);
		return sb.toString();
	}

	private static void append(StringBuffer sb, int v, char split) {
		if (v < 10) {
			sb.append('0');
		}
		sb.append(v).append(split);
	}

	static int[] internalParse(String str) {
		if (str == null) {
			throw new IllegalArgumentException("invalid FDateTime: " + str);
		}
		str = str.trim();
		int index = str.indexOf(' ');
		if (index < 0 || index > (str.length() - 1)) {
			throw new IllegalArgumentException("invalid FDateTime: " + str);
		}
		int[] d = FDate.internalParse(str);

		int[] t = FTime.internalParse(str, index + 1);

		int[] a = new int[6];

		System.arraycopy(d, 0, a, 0, d.length);

		System.arraycopy(t, 0, a, d.length, t.length);

		return a;

	}

	public String toPersisted() {
		return toStdString();
	}
}