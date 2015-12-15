package xap.mw.coreitf.d;

@SuppressWarnings("rawtypes")
public final class FBoolean implements java.io.Serializable, Comparable {

	public static final FBoolean TRUE = new FBoolean(true);

	public static final FBoolean FALSE = new FBoolean(false);

	private static final long serialVersionUID = 1L;

	private boolean value = false;

	public boolean isValue() {
		return value;
	}

	public FBoolean(char ch) {
		super();
		value = (ch == 'Y' || ch == 'y');
	}

	public FBoolean(String val) {
		if (val != null
				&& val.length() > 0
				&& (val.equalsIgnoreCase("true") || val.charAt(0) == 'Y' || val
						.charAt(0) == 'y'))
			value = true;
		else
			value = false;
	}

	public FBoolean(boolean b) {
		super();
		value = b;
	}

	public boolean booleanValue() {
		return value;
	}

	public static FBoolean valueOf(boolean b) {
		return (b ? TRUE : FALSE);
	}

	public static FBoolean valueOf(String val) {
		if (val != null
				&& val.length() > 0
				&& (val.equalsIgnoreCase("true") || val.charAt(0) == 'Y' || val
						.charAt(0) == 'y'))
			return TRUE;
		else
			return FALSE;
	}

	public boolean equals(Object obj) {
		if ((obj != null) && (obj instanceof FBoolean)) {
			return value == ((FBoolean) obj).booleanValue();
		}
		return false;
	}

	public int hashCode() {
		return value ? 1231 : 1237;
	}

	public String toString() {
		return value ? "Y" : "N";
	}

	public int compareTo(Object o) {
		if (o == null)
			return 1;
		return toString().compareTo(o.toString());
	}
}