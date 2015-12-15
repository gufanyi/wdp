package xap.mw.coreitf.d;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class FDouble extends java.lang.Number implements java.io.Serializable,
		Comparable {
	static final long serialVersionUID = 1L;

	private int power = DEFAULT_POWER;

	public final static int ROUND_UP = 0;

	public final static int ROUND_DOWN = 1;

	public final static int ROUND_CEILING = 2;

	public final static int ROUND_FLOOR = 3;

	public final static int ROUND_HALF_UP = 4;

	public final static int ROUND_HALF_DOWN = 5;

	public final static int ROUND_HALF_EVEN = 6;

	public final static int ROUND_UNNECESSARY = 7;

	private final static int ARRAY_LENGTH = 5;

	private final static int EFFICIENCY_SEATE = 16;

	private final static long MAX_ONELONG_VALUE = (long) 1E16;

	private final static long POWER_ARRAY[];

	public final static int ROUND_TO_ZERO_AND_HALF = 8;

	private byte si = 1;

	private long v[] = new long[ARRAY_LENGTH];
	static {
		POWER_ARRAY = new long[EFFICIENCY_SEATE + 2];
		for (int i = 0; i < POWER_ARRAY.length - 1; i++) {
			POWER_ARRAY[i] = (long) Math.pow(10, EFFICIENCY_SEATE - i);
		}
		POWER_ARRAY[POWER_ARRAY.length - 1] = 0;
	}

	public static FDouble ONE_DBL = new FDouble(1f);

	public static FDouble ZERO_DBL = new FDouble(0f);

	public FDouble() {
		super();
	}

	public FDouble(double d) throws NumberFormatException {
		this(d, DEFAULT_POWER);
	}

	public FDouble(double d, int newPower) throws NumberFormatException {
		setValue(d, newPower);
	}

	public FDouble(int d) {
		this((long) d);
	}

	public FDouble(int d, int pow) {
		this((long) d, pow);
	}

	public FDouble(long d) {
		this(d, DEFAULT_POWER);
	}

	public FDouble(long d, int pow) throws NumberFormatException {
		this(d + 0.0, pow);
	}

	public FDouble(long[] dv, byte si, int pow) throws NumberFormatException {
		if (dv == null || dv.length != ARRAY_LENGTH) {
			throw new NumberFormatException("array length must be 5");
		}
		this.v = dv;
		this.si = si;
		this.power = pow;
	}

	public FDouble(Double d) throws NumberFormatException {
		this(d.doubleValue(), DEFAULT_POWER);
	}

	public FDouble(String str) throws NumberFormatException {
		String s = "";
		int npower = DEFAULT_POWER;
		if (str == null || str.trim().length() == 0) {
			s = "0";
		} else {
			java.util.StringTokenizer token = new java.util.StringTokenizer(
					str, ",");
			while (token.hasMoreElements()) {
				s += token.nextElement().toString();
			}

			int pos = s.indexOf('e');
			pos = pos < 0 ? s.indexOf('E') : pos;
			if (pos >= 0) {
				try {
					npower = Integer.parseInt(s.substring(pos + 1));
				} catch (Throwable t) {
					npower = DEFAULT_POWER;
				}
				setValue(Double.parseDouble(s), npower);
				return;
			}
			if (s.charAt(0) == '-') {
				si = -1;
				s = s.substring(1);
			} else if (s.charAt(0) == '+')
				s = s.substring(1);
		}

		int loc = s.indexOf('.');

		if (loc >= 0) {
			npower = s.length() - (loc + 1);
		} else {
			npower = 0;
		}

		fromString(npower, s);
	}

	public FDouble(String str, int newPower) {
		String s = "";
		if (str == null || str.trim().length() == 0) {
			s = "0";
		} else {
			java.util.StringTokenizer token = new java.util.StringTokenizer(
					str, ",");
			while (token.hasMoreElements()) {
				s += token.nextElement().toString();
			}
			if (s.indexOf('e') >= 0 || s.indexOf('E') >= 0) {
				setValue(Double.parseDouble(s), getValidPower(newPower));
				return;
			}
			if (s.charAt(0) == '-') {
				si = -1;
				s = s.substring(1);
			} else if (s.charAt(0) == '+')
				s = s.substring(1);
		}
		fromString(newPower, s);
	}

	private void fromString(int newPower, String s) {
		newPower = getValidPower(newPower);
		int loc = s.indexOf('.');
		if (loc >= 0) {
			String s1 = s.substring(loc + 1);
			if (s1.length() > -newPower) {
				if (-newPower >= EFFICIENCY_SEATE)
					s1 = s1.substring(0, EFFICIENCY_SEATE);
				else
					s1 = s1.substring(0, 1 - newPower);
			}

			power = newPower;
			for (int i = s1.length(); i < EFFICIENCY_SEATE; i++)
				s1 += "0";
			v[0] = Long.parseLong(s1);
			s = s.substring(0, loc);
		} else {
			power = newPower;
			v[0] = 0;
		}

		int len = s.length();
		int sitLoc = 1;
		while (len > 0) {
			String s1 = "";
			if (len > EFFICIENCY_SEATE) {
				s1 = s.substring(len - EFFICIENCY_SEATE);
				s = s.substring(0, len - EFFICIENCY_SEATE);
			} else {
				s1 = s;
				s = "";
			}
			len = s.length();
			v[sitLoc++] = Long.parseLong(s1);
		}
		for (int i = sitLoc; i < v.length; i++)
			v[i] = 0;
		round(ROUND_HALF_UP);
	}

	public FDouble(java.math.BigDecimal value) {
		this(value.toString(), value.scale());
	}

	public FDouble(FDouble fd) {
		si = fd.si;
		for (int i = 0; i < v.length; i++) {
			v[i] = fd.v[i];
		}
		power = fd.power;
	}

	public FDouble add(double d1) {
		return add(new FDouble(d1));
	}

	public FDouble add(FDouble ufd) {
		int power = Math.abs(ufd.getPower()) > Math.abs(getPower()) ? ufd
				.getPower() : getPower();

		return add(ufd, power, ROUND_HALF_UP);
	}

	public FDouble add(FDouble ufd, int newPower) {
		return add(ufd, newPower, ROUND_HALF_UP);
	}

	public FDouble add(FDouble ufd, int newPower, int roundingMode) {
		newPower = getValidPower(newPower);

		FDouble fdnew = new FDouble(this);

		fdnew.power = newPower;
		fdnew.addUp0(ufd, newPower, roundingMode);
		return fdnew;
	}

	private void addUp0(double ufd) {
		addUp0(new FDouble(ufd), power, ROUND_HALF_UP);
	}

	private void addUp0(FDouble ufd, int newPower, int roundingMode) {
		toPlus();
		ufd.toPlus();
		for (int i = 0; i < v.length; i++) {
			v[i] += ufd.v[i];
		}
		judgNegative();
		adjustIncluedFs();

		ufd.judgNegative();
		round(roundingMode);
	}

	private void adjustIncluedFs() {
		for (int i = 1; i < v.length; i++) {
			if (v[i - 1] < 0) {
				v[i]--;
				v[i - 1] += MAX_ONELONG_VALUE;
			} else {
				v[i] = v[i] + v[i - 1] / MAX_ONELONG_VALUE;
				v[i - 1] = v[i - 1] % MAX_ONELONG_VALUE;
			}
		}
	}

	private void adjustNotIncluedFs() {
		for (int i = 1; i < v.length; i++) {
			v[i] = v[i] + v[i - 1] / MAX_ONELONG_VALUE;
			v[i - 1] = v[i - 1] % MAX_ONELONG_VALUE;
		}
	}

	public int compareTo(Object o) {
		return toBigDecimal().compareTo(((FDouble) o).toBigDecimal());
	}

	private void cutdown() {
		int p = -power;
		v[0] = v[0] / POWER_ARRAY[p] * POWER_ARRAY[p];
	}

	public FDouble div(double d1) {
		FDouble ufd = new FDouble(d1);
		return div(ufd);
	}

	public FDouble div(FDouble ufd) {
		return div(ufd, DEFAULT_POWER);
	}

	public FDouble div(FDouble ufd, int power) {
		return div(ufd, power, ROUND_HALF_UP);
	}

	public FDouble div(FDouble ufd, int power, int roundingMode) {
		int newPower = getValidPower(power);
		BigDecimal bd = toBigDecimal();
		BigDecimal divisor = ufd.toBigDecimal();
		int maxScale = divisor.scale() > bd.scale() ? divisor.scale() : bd
				.scale();
		int nPower = Math.abs(power);
		maxScale = maxScale > nPower ? maxScale : nPower;
		BigDecimal newbd = bd.divide(divisor, maxScale, RoundingMode.HALF_UP);
		FDouble ufdNew = new FDouble(newbd);
		return maxScale == nPower ? ufdNew : ufdNew.setScale(newPower,
				roundingMode);
	}

	public double doubleValue() {
		double d = 0;
		for (int i = v.length - 1; i >= 0; i--) {
			d *= MAX_ONELONG_VALUE;
			d += v[i];
		}
		d /= MAX_ONELONG_VALUE;
		return d * si;
	}

	public float floatValue() {
		return (float) getDouble();
	}

	public double getDouble() {
		return this.doubleValue();
	}

	public long[] getDV() {
		return this.v;
	}

	public byte getSIValue() {
		return this.si;
	}

	public int intValue() {
		return (int) getDouble();
	}

	private void judgNegative() {

		boolean isFs = false;
		for (int i = v.length - 1; i >= 0; i--) {
			if (v[i] < 0) {

				isFs = true;
				break;
			}
			if (v[i] > 0)
				break;
		}
		if (isFs) {
			for (int i = 0; i < v.length; i++)
				v[i] = -v[i];
			si = -1;
		}
	}

	public long longValue() {
		long d = 0;

		for (int i = v.length - 1; i > 0; i--) {
			d *= MAX_ONELONG_VALUE;
			d += v[i];
		}
		return d * si;
	}

	public FDouble multiply(double d1) {

		FDouble ufD1 = new FDouble(d1);
		return multiply(ufD1, DEFAULT_POWER, ROUND_HALF_UP);
	}

	public FDouble multiply(FDouble ufd) {
		return multiply(ufd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	public FDouble multiply(FDouble ufd, int newPower) {
		return multiply(ufd, newPower, ROUND_HALF_UP);
	}

	public FDouble multiply(FDouble ufd, int newPower, int roundingMode) {
		newPower = getValidPower(newPower);

		BigDecimal bd = toBigDecimal();
		BigDecimal divisor = ufd.toBigDecimal();
		// int maxPrecious = divisor.precision() > bd.precision() ? divisor
		// .precision() : bd.precision();

		BigDecimal bdn = bd.multiply(divisor);
		bdn = bdn.setScale(-newPower, roundingMode);

		FDouble ufdNew = new FDouble(bdn);

		// ufdNew = ufdNew.setScale(newPower, roundingMode);
		return ufdNew;

	}

	private void round(int roundingMode) {
		boolean increment = true;
		switch (roundingMode) {
		case ROUND_UP:
			increment = true;
			break;
		case ROUND_CEILING:
			increment = si == 1;
			break;
		case ROUND_FLOOR:
			increment = si == -1;
			break;
		case ROUND_DOWN:
			increment = false;
			// si == -1;
			break;
		case ROUND_TO_ZERO_AND_HALF:
			// 一种特殊的舍位机制：1、2舍位；3~7取5；8、9进位。只处理正数：
			/*
			 * long l = (long)(d / Math.pow(10, newPower)); double fraction = d
			 * - l; if (fraction < 0.3) { fraction = 0; } else if (fraction <
			 * 0.8) { fraction = 0.5; } else { fraction = 1; } return new
			 * FDouble(l + fraction, newPower);
			 */
		}
		int p = -power;
		long vxs = POWER_ARRAY[p + 1];

		if (increment) {
			v[0] += vxs * 5;
			adjustNotIncluedFs();
		}
		cutdown();
		// 为0时去掉负号
		boolean isZero = true;
		for (int i = 0; i < v.length; i++) {
			if (v[i] != 0) {
				isZero = false;
				break;
			}
		}
		if (si == -1 && isZero)
			si = 1;
		//
	}

	public FDouble setScale(int power, int roundingMode) {
		return multiply(ONE_DBL, power, roundingMode);
	}

	private void setValue(double d, int newPower) throws NumberFormatException {
		double dd, ld;

		if (d < 0) {
			d = -d;
			si = -1;
		}
		dd = d;
		power = getValidPower(newPower);

		double dxs = d % 1;
		d -= dxs;
		ld = d;
		for (int i = 1; i < v.length; i++) {
			v[i] = (long) (d % MAX_ONELONG_VALUE);
			d = d / MAX_ONELONG_VALUE;
		}
		long v2 = 0;
		if (dxs == 0.0)
			v2 = (long) (dxs * MAX_ONELONG_VALUE);
		else {
			if (dd / ld == 1.0) {
				dxs = 0.0;
				v2 = (long) (dxs * MAX_ONELONG_VALUE);
			} else {
				if (power <= -8) {
					int iv = (int) v[2];
					if (iv != 0) {
						if (iv >= 1000000)
							power = -0;
						else if (iv >= 100000)
							power = -1;
						else if (iv >= 10000)
							power = -2;
						else if (iv >= 1000)
							power = -3;
						else if (iv >= 100)
							power = -4;
						else if (iv >= 10)
							power = -5;
						else if (iv >= 1)
							power = -6;
					} else {
						iv = (int) v[1];
						if (iv >= 100000000)
							power = -7;
					}
					if (power < 0) {
						int ii = -power;
						double d1;
						int i2 = 1;
						double dxs1;
						for (int i = 1; i < ii; i++) {
							i2 *= 10;
							dxs1 = ((double) Math.round(dxs * i2)) / i2;
							d1 = ld + dxs1;
							if (dd / d1 == 1.0) {
								dxs = dxs1;
								break;
							}
						}
					}
				}
				v2 = (long) ((dxs + 0.00000000001) * MAX_ONELONG_VALUE);
			}
		}
		v[0] = v2;
		round(ROUND_HALF_UP);
	}

	public FDouble sub(double d1) {
		FDouble ufd = new FDouble(d1);
		return sub(ufd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	public FDouble sub(FDouble ufd) {
		int power = Math.abs(ufd.getPower()) > Math.abs(getPower()) ? ufd
				.getPower() : getPower();
		return sub(ufd, power, ROUND_HALF_UP);
	}

	public FDouble sub(FDouble ufd, int newPower) {
		return sub(ufd, newPower, ROUND_HALF_UP);
	}

	public FDouble sub(FDouble ufd, int newPower, int roundingMode) {
		// newPower = newPower > 0 ? -newPower : ((newPower > -9) ? newPower :
		// -9);
		newPower = getValidPower(newPower);

		FDouble ufdnew = new FDouble(ufd);
		ufdnew.si = (byte) -ufdnew.si;
		return add(ufdnew, newPower, roundingMode);
	}

	public static FDouble sum(double[] dArray) {
		return sum(dArray, DEFAULT_POWER);
	}

	public static FDouble sum(double[] dArray, int newPower) {
		// newPower = newPower > 0 ? -newPower : ((newPower > -9) ? newPower :
		// -9);
		newPower = getValidPower(newPower);

		FDouble ufd = new FDouble(0, newPower);
		for (int i = 0; i < dArray.length; i++) {
			ufd.addUp0(dArray[i]);
		}
		return ufd;
	}

	public static FDouble sum(double[] dArray, int newPower, int roundingMode) {
		// newPower = newPower > 0 ? -newPower : ((newPower > -9) ? newPower :
		// -9);
		newPower = getValidPower(newPower);

		FDouble ufd = new FDouble(0, newPower);
		for (int i = 0; i < dArray.length; i++) {
			FDouble ufdNew = new FDouble(dArray[i], newPower);
			ufd.addUp0(ufdNew, newPower, roundingMode);
		}
		return ufd;
	}

	public BigDecimal toBigDecimal() {
		return new BigDecimal(toString());
	}

	public BigDecimal toBigDecimal(int precious, RoundingMode mode) {
		return new BigDecimal(toString(), new MathContext(precious, mode));
	}

	public Double toDouble() {
		return new Double(getDouble());
	}

	private void toPlus() {
		if (si == 1)
			return;
		si = 1;
		for (int i = 0; i < v.length; i++) {
			v[i] = -v[i];
		}
	}

	public String toString() {
		boolean addZero = false;
		StringBuffer sb = new StringBuffer();
		if (si == -1)
			sb.append("-");
		for (int i = v.length - 1; i > 0; i--) {
			if (v[i] == 0 && !addZero)
				continue;
			String temp = String.valueOf(v[i]);
			if (addZero) {
				int len = temp.length();
				int addZeroNo = EFFICIENCY_SEATE - len;
				for (int j = 0; j < addZeroNo; j++) {
					sb.append('0');
				}
			}
			sb.append(temp);
			addZero = true;
		}
		if (!addZero)
			sb.append('0');

		if (power < 0) {
			sb.append('.');
			for (int j = 0; j < EFFICIENCY_SEATE && j < -power; j++) {
				sb.append((v[0] / POWER_ARRAY[j + 1]) % 10);
			}
		}
		// 压缩小数点后尾部0
		int index = -1;
		if (isTrimZero()) {
			if (power < 0) {
				String sTemp = sb.toString();
				for (int i = sb.length() - 1; i >= 0; i--) {
					if (sTemp.substring(i, i + 1).equals("0"))
						index = i;
					else {
						if (sTemp.substring(i, i + 1).equals(".")) {
							index = i;
						}
						break;
					}
				}
			}
		}
		if (index >= 0)
			sb = sb.delete(index, sb.length());
		return sb.toString();
	}

	public final static int DEFAULT_POWER = -8;

	private boolean trimZero = false;

	public FDouble abs() {
		FDouble fdnew = new FDouble();
		fdnew.power = this.power;
		fdnew.si = 1;
		for (int i = 0; i < v.length; i++) {
			fdnew.v[i] = v[i];
		}
		return fdnew;
	}

	public int getPower() {
		return power;
	}

	public boolean isTrimZero() {
		return trimZero;
	}

	public FDouble mod(FDouble ufd) {
		return mod(ufd, DEFAULT_POWER, ROUND_HALF_UP);
	}

	public FDouble mod(FDouble ufd, int newPower) {
		return mod(ufd, newPower, ROUND_HALF_UP);
	}

	public FDouble mod(FDouble ufd, int newPower, int roundingMode) {
		FDouble ufdDiv = div(ufd, 0, ROUND_DOWN);
		FDouble ufdnew = sub(ufdDiv.multiply(ufd));
		if (ufd.si != si)
			ufdnew = ufdnew.sub(ufd);
		ufdnew.power = newPower;
		ufdnew.round(roundingMode);
		return ufdnew;
	}

	public void setTrimZero(boolean newTrimZero) {
		trimZero = newTrimZero;
	}

	private static int getValidPower(int newPower) {

		int power = newPower > 0 ? -newPower : newPower;
		if (power < -EFFICIENCY_SEATE)
			power = -EFFICIENCY_SEATE;
		return power;

	}

	@Override
	public int hashCode() {
		int v = 0;
		for (int i = 0; i < this.v.length; i++) {
			v += this.v[i];
		}
		return v * this.si;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FDouble) {
			FDouble ud = (FDouble) o;
			return si == ud.si && Arrays.equals(v, ud.v);
		}
		return false;

	}

	public Object clone() {
		return new FDouble(this);
	}
}