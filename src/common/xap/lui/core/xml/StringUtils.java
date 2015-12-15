package xap.lui.core.xml;

import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	private static final String FOLDER_SEPARATOR = "/";
	private static final String WINDOWS_FOLDER_SEPARATOR = "\\\\";
	private static final String TOP_PATH = "..";
	private static final String CURRENT_PATH = ".";
	public static final String HYPHEN = " ";

	public static String substringBetween(String source, String strBegin, String strEnd) {
		if (null == source)
			return null;
		int index = source.indexOf(strBegin);
		int indexEnd = source.indexOf(strEnd);
		if (index < 0)
			index = 0 - strBegin.length();
		if (indexEnd < 0)
			indexEnd = source.length();
		return source.substring(index + strBegin.length(), indexEnd);
	}

	public static String removeStringBetween(String source, String strBegin, String strEnd) {
		int index = source.indexOf(strBegin);
		int indexEnd = source.indexOf(strEnd);
		return source.substring(0, index) + source.substring(indexEnd + strEnd.length());
	}

	public static String replaceAllString(String source, String strReplaced, String strReplace) {
		if (isBlank(source) || isBlank(strReplaced) || strReplace == null)
			return source;
		StringBuffer buf = new StringBuffer(source.length());
		int start = 0, end = 0;
		while ((end = source.indexOf(strReplaced, start)) != -1) {
			buf.append(source.substring(start, end)).append(strReplace);
			start = end + strReplaced.length();
		}
		buf.append(source.substring(start));
		return buf.toString();
	}

	public static String replaceIgnoreCase(String source, String strBeReplace, String strReplaced) {
		if (isBlank(source) || isBlank(strBeReplace) || strReplaced == null)
			return source;
		StringBuffer buf = new StringBuffer(source.length());
		int start = 0, end = 0;
		String strReplacedCopy = strBeReplace.toUpperCase();
		String sourceCopy = source.toUpperCase();
		while ((end = sourceCopy.indexOf(strReplacedCopy, start)) != -1) {
			buf.append(source.substring(start, end)).append(strReplaced);
			start = end + strReplacedCopy.length();
		}
		buf.append(source.substring(start));
		return buf.toString();
	}

	public static String replaceFromTo(String source, String strBegin, String strEnd, String replaced) {
		if (null == source)
			return null;
		int index = source.indexOf(strBegin);
		int index1 = source.indexOf(strEnd);
		return source.substring(0, index) + replaced + source.substring(index1 + strEnd.length());
	}

	public static String[] gb2Unicode(String[] srcAry) {
		String[] strOut = new String[srcAry.length];
		for (int i = 0; i < srcAry.length; i++) {
			strOut[i] = gb2Unicode(srcAry[i]);
		}
		return strOut;
	}

	public static String gb2Unicode(String src) {
		src = spaceToNull(src);
		if (src == null) {
			return null;
		}
		char[] c = src.toCharArray();
		int n = c.length;
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) c[i];
		return new String(b);
	}

	public static String[] unicode2Gb(String[] srcAry) {
		String[] strOut = new String[srcAry.length];
		for (int i = 0; i < srcAry.length; i++) {
			strOut[i] = uniCode2Gb(srcAry[i]);
		}
		return strOut;
	}

	public static String uniCode2Gb(String src) {
		src = spaceToNull(src);
		if (src == null) {
			return null;
		}
		byte[] b = src.getBytes();
		int n = b.length;
		char[] c = new char[n];
		for (int i = 0; i < n; i++)
			c[i] = (char) ((short) b[i] & 0xff);
		return new String(c);
	}

	// public static boolean match(String reg, String str) {
	// return WildcardMatcher.match(reg, str);
	// }
	//
	// public static boolean matchIgnoreCase(String reg, String str) {
	// return WildcardMatcher.match(reg, str);
	// }

	public static List<String> toList(String inputstring, String splitstr) {
		StringTokenizer st = new StringTokenizer(inputstring, splitstr, false);
		List<String> reslist = new ArrayList<String>(st.countTokens());
		while (st.hasMoreTokens()) {
			reslist.add(st.nextToken().trim());
		}
		return reslist;
	}

	public static String getUnionStr(String[] strAry, String unionChar, String appendChar) {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < strAry.length; i++) {
			if (i != 0)
				ret.append(unionChar);
			ret.append(appendChar + strAry[i] + appendChar);
		}
		return ret.toString();
	}

	public static String getPYIndexStr(String strChinese, boolean bUpCase) {
		try {
			StringBuffer buffer = new StringBuffer();
			byte b[] = strChinese.getBytes("GBK");
			int i = 0;
			while (i < b.length) {
				if ((int) (b[i] & 0xff) > 0x80) {
					int char1 = b[i++] & 0xff;
					char1 = char1 << 8;
					int chart = char1 + (int) (b[i] & 0xff);
					buffer.append(getPYIndexChar((char) chart, bUpCase));
				} else {
					char c = (char) b[i];
					if (!Character.isJavaIdentifierPart(c))
						c = 'A';
					buffer.append(c);
				}
				i++;
			}
			return buffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	private static char getPYIndexChar(char strChinese, boolean bUpCase) {
		int charGBK = (int) strChinese;
		char result;
		if ((charGBK >= 0xB0A1) && (charGBK <= 0xB0C4))
			result = 'A';
		else if ((charGBK >= 0xB0C5) && (charGBK <= 0xB2C0))
			result = 'B';
		else if ((charGBK >= 0xB2C1) && (charGBK <= 0xB4ED))
			result = 'C';
		else if ((charGBK >= 0xB4EE) && (charGBK <= 0xB6E9))
			result = 'D';
		else if ((charGBK >= 0xB6EA) && (charGBK <= 0xB7A1))
			result = 'E';
		else if ((charGBK >= 0xB7A2) && (charGBK <= 0xB8C0))
			result = 'F';
		else if ((charGBK >= 0xB8C1) && (charGBK <= 0xB9FD))
			result = 'G';
		else if ((charGBK >= 0xB9FE) && (charGBK <= 0xBBF6))
			result = 'H';
		else if ((charGBK >= 0xBBF7) && (charGBK <= 0xBFA5))
			result = 'J';
		else if ((charGBK >= 0xBFA6) && (charGBK <= 0xC0AB))
			result = 'K';
		else if ((charGBK >= 0xC0AC) && (charGBK <= 0xC2E7))
			result = 'L';
		else if ((charGBK >= 0xC2E8) && (charGBK <= 0xC4C2))
			result = 'M';
		else if ((charGBK >= 0xC4C3) && (charGBK <= 0xC5B5))
			result = 'N';
		else if ((charGBK >= 0xC5B6) && (charGBK <= 0xC5BD))
			result = 'O';
		else if ((charGBK >= 0xC5BE) && (charGBK <= 0xC6D9))
			result = 'P';
		else if ((charGBK >= 0xC6DA) && (charGBK <= 0xC8BA))
			result = 'Q';
		else if ((charGBK >= 0xC8BB) && (charGBK <= 0xC8F5))
			result = 'R';
		else if ((charGBK >= 0xC8F6) && (charGBK <= 0xCBF9))
			result = 'S';
		else if ((charGBK >= 0xCBFA) && (charGBK <= 0xCDD9))
			result = 'T';
		else if ((charGBK >= 0xCDDA) && (charGBK <= 0xCEF3))
			result = 'W';
		else if ((charGBK >= 0xCEF4) && (charGBK <= 0xD1B8))
			result = 'X';
		else if ((charGBK >= 0xD1B9) && (charGBK <= 0xD4D0))
			result = 'Y';
		else if ((charGBK >= 0xD4D1) && (charGBK <= 0xD7F9))
			result = 'Z';
		else
			result = (char) ('A' + new Random().nextInt(25));
		if (!bUpCase)
			result = Character.toLowerCase(result);
		return result;
	}

	public static String[] toArray(String str, String delim) {
		return toArray(str, delim, false, false);
	}

	public static String[] toArray(String s) {
		return toArray(s, ",", false, false);
	}

	public static String[] split(String str, String token) {
		return toArray(str, token);
	}

	public static String replaceQuotMark(String strValue) {
		String oldMark = "'";
		String strResult = strValue;
		if (strValue != null && strValue.length() > 0 && strValue.indexOf(oldMark) >= 0) {
			boolean hasOneQuoMard = true;
			int pos = 0;
			while (hasOneQuoMard) {
				pos = strResult.indexOf(oldMark, pos);
				if (pos < 0)
					break;
				if (pos >= strResult.length() - 1) {
					strResult = strResult.substring(0, strResult.length()) + oldMark;
					hasOneQuoMard = false;
				} else {
					if (strResult.substring(pos + 1, pos + 2).equals(oldMark)) {
						pos += 2;
					} else {
						strResult = strResult.substring(0, pos + 1) + oldMark + strResult.substring(pos + 1);
						pos += 2;
					}
				}
			}
		}
		return strResult;
	}

	public static String spaceToNull(String str) {
		if (str == null || str.trim().length() == 0)
			return null;
		return str.trim();
	}

	public static String removeCharFromString(String value, char removeChar) {
		if (value == null)
			return null;
		String regular = value.trim();
		String removestr = String.valueOf(removeChar);
		int index = regular.indexOf(removestr);
		while (index > 0) {
			String temp = regular.substring(0, index);
			regular = temp + regular.substring(index + 1);
			index = regular.indexOf(removestr);
		}
		return regular;
	}

	public static String addCharToString(String value, char addChar) {
		if (value == null)
			return null;
		String regular = value;
		String sign = "";
		if (regular.substring(0, 1).equals("-")) {
			sign = "-";
			regular = regular.substring(1, regular.length());
		}
		int index = regular.indexOf(".");
		String fracTemp = "";
		if (index > 0) {
			fracTemp = "." + regular.substring(index + 1);
			regular = regular.substring(0, index);
		}
		String after = null;
		String strAdd = String.valueOf(addChar);
		while (regular.length() > 3) {
			String temp = regular.substring(regular.length() - 3, regular.length());
			regular = regular.substring(0, regular.length() - 3);
			if (after == null)
				after = temp;
			else
				after = temp + strAdd + after;
		}
		if (after == null)
			regular = sign + regular + fracTemp;
		else
			regular = sign + regular + strAdd + after + fracTemp;
		return regular;
	}

	public static int computeStringWidth(FontMetrics fontMetrics, String str) {
		if (str == null || str.length() <= 0)
			return 0;
		int width = 10 + javax.swing.SwingUtilities.computeStringWidth(fontMetrics, str);
		int bytesLen = str.getBytes().length;
		if (bytesLen >= 10)
			width += (bytesLen - 10) * 2 + 5;
		return width;
	}

	public static String convExpoToRegular(String value) {
		if (value == null)
			return "0";
		String regular = value.toUpperCase();
		String sign = "";
		if (regular.substring(0, 1).equals("-")) {
			sign = "-";
			regular = regular.substring(1, regular.length());
		}
		int index1 = regular.indexOf("E");
		if (index1 > 0) {
			String temp = regular.substring(0, index1);
			String strExep = regular.substring(index1 + 1);
			int exep = Integer.parseInt(strExep);
			int index2 = temp.indexOf(".");
			if (exep >= 0) {
				if (index2 > 0) {
					String inteTemp = temp.substring(0, index2);
					String fracTemp = temp.substring(index2 + 1);
					if (fracTemp.length() > exep) {
						regular = inteTemp + fracTemp.substring(0, exep) + "." + fracTemp.substring(exep);
					} else {
						int diff = exep - fracTemp.length();
						for (int l = 0; l < diff; l++)
							fracTemp += "0";
						regular = inteTemp + fracTemp + ".0";
					}
				} else {
					for (int l = 0; l < exep; l++)
						temp += "0";
					regular = temp;
				}
			} else {
				String inteTemp = temp;
				String fracTemp = "";
				exep = -exep;
				if (index2 > 0) {
					inteTemp = temp.substring(0, index2);
					fracTemp = temp.substring(index2 + 1);
				}
				if (inteTemp.length() > exep) {
					int diff = inteTemp.length() - exep;
					regular = inteTemp.substring(0, diff) + "." + inteTemp.substring(diff) + fracTemp;
				} else {
					int diff = exep - inteTemp.length();
					for (int l = 0; l < diff; l++)
						inteTemp = "0" + inteTemp;
					regular = "0." + inteTemp + fracTemp;
				}
			}
		}
		return sign + regular;
	}

	public static String formatFloat(String str, int precision) {
		if (str == null)
			return "0";
		if (str.indexOf("E") > -1)
			str = convExpoToRegular(str);
		String preStr = "";
		String numStr = "";
		if (precision < 0)
			precision = 0;
		try {
			int index = str.indexOf(".");
			if (index > -1) {
				if (index == 0) {
					preStr = "0";
				} else {
					preStr = str.substring(0, index);
					if (preStr.equals("-"))
						preStr += "0";
				}
				numStr = str.substring(index + 1);
			} else {
				preStr = str;
			}
			if (precision > 0) {
				preStr += ".";
				int len = numStr.length();
				if (len < precision) {
					for (int i = 0; i < precision - len; i++)
						numStr += "0";
					preStr += numStr;
				} else if (len > precision) {
					String s = numStr.substring(precision, precision + 1);
					String temp = numStr.substring(0, precision);
					if (Integer.parseInt(s) >= 5) {
						preStr = addOne(preStr, temp, "");
					} else {
						preStr += temp;
					}
				} else
					preStr += numStr;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return preStr;
	}

	public static boolean stringToBoolean(String str) {
		if (str == null)
			return false;
		if (str.equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}

	public static String getObjectCode(String obj) {
		String code = null;
		if (obj != null) {
			int index = obj.indexOf(HYPHEN);
			if (index > -1) {
				code = obj.substring(0, index);
			}
		}
		return code;
	}

	public static String getObjectName(String obj) {
		String name = null;
		if (obj != null) {
			int index = obj.indexOf(HYPHEN);
			if (index > -1) {
				name = obj.substring(index + 1);
			}
		}
		return name;
	}

	public static String[] getReservedWords() {
		return new String[] { " ", "`", "#", "&", "*", "\"", "'", "?", "+", "-", "!" };
	}

	private static String addOne(String preStr, String numStr, String afterStr) {
		String result = "";
		if (numStr.length() > 0) {
			String s = numStr.substring(numStr.length() - 1);
			numStr = numStr.substring(0, numStr.length() - 1);
			int value = Integer.parseInt(s);
			if (value == 9) {
				afterStr = "0" + afterStr;
				result = addOne(preStr, numStr, afterStr);
			} else {
				result = preStr + numStr + Integer.toString(value + 1) + afterStr;
			}
		} else if (preStr.length() > 0) {
			String s = preStr.substring(preStr.length() - 1);
			preStr = preStr.substring(0, preStr.length() - 1);
			if (s.equals(".")) {
				afterStr = s + afterStr;
				result = addOne(preStr, numStr, afterStr);
			} else {
				int value = Integer.parseInt(s);
				if (value == 9) {
					afterStr = "0" + afterStr;
					result = addOne(preStr, numStr, afterStr);
				} else {
					result = preStr + numStr + Integer.toString(value + 1) + afterStr;
				}
			}
		} else {
			result = preStr + "1" + numStr + afterStr;
		}
		return result;
	}

	public static int compareByByte(Object o1, Object o2) {
		if (null == o1) {
			return (null == o2) ? 0 : -1;
		}
		if (null == o2) {
			return 1;
		}
		boolean isBytes = o1 instanceof byte[];
		byte[] bAry1 = isBytes ? (byte[]) o1 : o1.toString().getBytes();
		byte[] bAry2 = isBytes ? (byte[]) o2 : o2.toString().getBytes();
		int len1 = bAry1.length;
		int len2 = bAry2.length;
		int n = Math.min(len1, len2);
		int i = 0;
		int j = 0;
		int r = 0;
		while (n-- != 0) {
			if ((r = bAry1[i++] - bAry2[j++]) != 0)
				break;
		}
		if (r == 0)
			r = len1 - len2;
		if (r == 0)
			return 0;
		if (r > 0)
			return 1;
		return -1;
	}

	public static boolean equals(String str1, String str2) {
		return str1 != null && str2 != null && str1.equals(str2);
	}

	/**
	 * 后续优化掉，建议使用isBlank
	 */
	public static boolean isEmpty(String str) {
		return isBlank(str);
	}

	/**
	 * 后续优化掉，建议使用isBlankWithTrim
	 */
	public static boolean isEmptyWithTrim(String str) {
		return isBlankWithTrim(str);
	}

	/**
	 * 后续优化掉，建议使用isNotBlank
	 */
	public static boolean isNotEmpty(String str) {
		return isNotBlank(str);
	}

	/**
	 * 判断字符串为空
	 */
	public static final boolean isNull(String str) {
		return str == null || str.trim().length() <= 0 || str.toLowerCase().equals("null");
	}

	/**
	 * 判断字符串不为空
	 */
	public static boolean isNotBlank(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断字符串为空
	 */
	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字符串去空格后为空
	 */
	public static boolean isBlankWithTrim(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNull(Object[] os) {
		return os == null || os.length == 0;
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static final boolean isNull(Collection<?> cs) {
		return cs == null || cs.isEmpty();
	}

	public static boolean isContainChinese(String str) {
		if (isBlank(str))
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
				return true;
		}
		return false;
	}

	public static int lenOfChinesString(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '\u4e00' && c <= '\u9fa5') {
				len += 2;
			} else {
				len++;
			}
		}
		return len;
	}

	public static String subChineseString(String str, int from, int len) {
		if (str == null || from < 0 || len <= 0) {
			throw new IllegalArgumentException();
		}
		int splitedLen = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = from; i < str.length() && (i < from + len); i++) {
			char c = str.charAt(i);
			if (c >= '\u4e00' && c <= '\u9fa5') {
				splitedLen += 2;
			} else {
				splitedLen++;
			}
			if (splitedLen > len) {
				break;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String recoverWrapLineChar(String msg) {
		if (msg == null)
			return "";
		StringBuffer dest = new StringBuffer();
		for (int i = 0; i < msg.length(); i++) {
			char aChar = msg.charAt(i);
			if (aChar == '\\' && i < msg.length() - 1) {
				char aCharNext = msg.charAt(++i);
				if (aCharNext == 't')
					aCharNext = '\t';
				else if (aCharNext == 'r')
					aCharNext = '\r';
				else if (aCharNext == 'n')
					aCharNext = '\n';
				else if (aCharNext == 'f')
					aCharNext = '\f';
				else
					dest.append(aChar);
				dest.append(aCharNext);
			} else
				dest.append(aChar);
		}
		return dest.toString();
	}

	public static char[] toHexChar(byte[] bArray) {
		char[] digitChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] charDigest = new char[bArray.length * 2];
		for (int i = 0; i < bArray.length; i++) {
			charDigest[i * 2] = digitChars[(bArray[i] >>> 4) & 0X0F];
			charDigest[i * 2 + 1] = digitChars[bArray[i] & 0X0F];
		}
		return charDigest;
	}

	public static int compare(String str1, String str2) {
		String t1 = "";
		String t2 = "";
		try {
			if (str1 != null)
				t1 = new String(str1.getBytes(), "ISO-8859-1");
			if (str2 != null)
				t2 = new String(str2.getBytes(), "ISO-8859-1");
		} catch (Exception e) {
		}
		return t1.compareTo(t2);
	}

	public static String[] toArray(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
		if (s == null) {
			return new String[0];
		}
		StringTokenizer st = new StringTokenizer(s, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!(ignoreEmptyTokens && token.length() == 0)) {
				tokens.add(token);
			}
		}
		return (String[]) tokens.toArray(new String[tokens.size()]);
	}

	public static boolean nstartsWith(String str1, String str2) {
		if (str2.length() > str1.length())
			return true;
		for (int i = 0; i < str2.length(); i++) {
			if (str2.charAt(i) != str1.charAt(i))
				return true;
		}
		return false;
	}

	public static boolean nendsWith(String str1, String str2) {
		if (str2.length() > str1.length())
			return true;
		int indexSrc = str1.length() - 1;
		for (int i = str2.length() - 1; i > 0; i--) {
			if (str2.charAt(i) != str1.charAt(indexSrc--))
				return true;
		}
		return false;
	}

	public static boolean nequals(String str1, String str2) {
		if (str2.length() != str1.length())
			return true;
		for (int i = 0; i < str2.length(); i++) {
			if (str2.charAt(i) != str1.charAt(i))
				return true;
		}
		return false;
	}

	public static boolean hasText(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static String digest(String strSource) {
		StringBuffer digest = new StringBuffer();
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA");
			byte[] sourBytes = strSource.getBytes();
			byte[] digestBytes = md.digest(sourBytes);
			if (digestBytes != null) {
				for (int i = 0, n = digestBytes.length; i < n; i++) {
					digest.append(digestBytes[i]);
				}
			}
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return digest.toString();
	}

	public static String digest(URL[] urls) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < urls.length; i++) {
			long lastModified = -1;
			if (i > 1)
				buffer.append(";");
			buffer.append(urls[i]);
			if ("file".equals(urls[i].getProtocol())) {
				File file = new File(urls[i].getPath().replace('/', File.separatorChar).replace('|', ':'));
				lastModified = file.lastModified();
			} else {
				try {
					URLConnection connection;
					connection = urls[i].openConnection();
					lastModified = connection.getLastModified();
				} catch (java.io.IOException e) {
				}
			}
			buffer.append('!').append(lastModified);
		}
		return digest(buffer.toString());
	}

	public static String toString(Object[] arr) {
		return toString(arr, ",");
	}

	public static String toString(Object value, String delim) {
		if (value instanceof String) {
			return (String) value;
		} else {
			if (value.getClass().isArray()) {
				return toString((Object[]) value, delim);
			} else if (value instanceof Iterable) {
				return toString((Iterable) value, delim);
			} else {
				return value.toString();
			}
		}
	}

	public static String toString(Iterable c, String delim, String prefix, String suffix) {
		if (c == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = c.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (i++ > 0) {
				sb.append(delim);
			}
			sb.append(prefix + it.next() + suffix);
		}
		return sb.toString();
	}

	public static String toString(Iterable c, String delim) {
		return toString(c, delim, "", "");
	}

	public static String toString(Object value) {
		return toString(value, ",");
	}

	public static String toString(Object[] arr, String delim) {
		if (arr == null) {
			return "null";
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arr.length; i++) {
				if (i > 0)
					sb.append(delim);
				sb.append(arr[i]);
			}
			return sb.toString();
		}
	}

	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	public static String capitalize(String str) {
		return changeFirstCharacterCase(true, str);
	}

	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(false, str);
	}

	private static String changeFirstCharacterCase(boolean capitalize, String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(strLen);
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	public static String getFilename(String path) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	public static String normalizePath(String path) {
		String p = path.replaceAll(WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
		String[] pArray = toArray(p, FOLDER_SEPARATOR);
		List<String> pList = new LinkedList<String>();
		int tops = 0;
		for (int i = pArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pArray[i])) {
			} else if (TOP_PATH.equals(pArray[i])) {
				tops++;
			} else {
				if (tops > 0) {
					tops--;
				} else {
					pList.add(0, pArray[i]);
				}
			}
		}
		return toString(pList, FOLDER_SEPARATOR);
	}

	public static boolean pathEquals(String path1, String path2) {
		return normalizePath(path1).equals(normalizePath(path2));
	}

	public static URL[] pathToURLs(String path) {
		StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
		URL[] urls = new URL[st.countTokens()];
		int count = 0;
		while (st.hasMoreTokens()) {
			File file = new File(st.nextToken());
			URL url = null;
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
			}
			if (url != null) {
				urls[count++] = url;
			}
		}
		if (urls.length != count) {
			URL[] tmp = new URL[count];
			System.arraycopy(urls, 0, tmp, 0, count);
			urls = tmp;
		}
		return urls;
	}

	public static String removeLastFileSeperator(String path) {
		if (path == null)
			return null;
		while (path.endsWith("\\") || path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return StringUtils.spaceToNull(path.trim());
	}

	public static String getTimeStampString(long l) {
		java.util.Calendar cl = java.util.Calendar.getInstance();
		cl.setTimeInMillis(l);
		int ia[] = new int[5];
		int year = cl.get(Calendar.YEAR);
		ia[0] = cl.get(Calendar.MONTH) + 1;
		ia[1] = cl.get(Calendar.DAY_OF_MONTH);
		ia[2] = cl.get(Calendar.HOUR_OF_DAY);
		ia[3] = cl.get(Calendar.MINUTE);
		ia[4] = cl.get(Calendar.SECOND);
		byte ba[] = new byte[19];
		ba[4] = ba[7] = (byte) '-';
		ba[10] = (byte) ' ';
		ba[13] = ba[16] = (byte) ':';
		ba[0] = (byte) (year / 1000 + '0');
		ba[1] = (byte) ((year / 100) % 10 + '0');
		ba[2] = (byte) ((year / 10) % 10 + '0');
		ba[3] = (byte) (year % 10 + '0');
		for (int i = 0; i < 5; i++) {
			ba[i * 3 + 5] = (byte) (ia[i] / 10 + '0');
			ba[i * 3 + 6] = (byte) (ia[i] % 10 + '0');
		}
		return new String(ba);
	}

	public static String replace(String s, Properties p) {
		String regex = "\\$\\w+\\W|\\$\\{[^}]+\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(s);
		while (m.find()) {
			String temp = m.group();
			String key = null;
			if (temp.indexOf("{") != -1) {
				key = temp.substring(temp.indexOf("{") + 1, temp.length() - 1);
			} else {
				key = temp.substring(1, temp.length() - 1);
			}
			String value = p.getProperty(key);
			if (value != null) {
				s = s.replace(temp, value);
				m = pattern.matcher(s);
			}
		}
		return s;
	}

	public static boolean contains(String s, String text, String delimiter) {
		if ((s == null) || (text == null) || (delimiter == null)) {
			return false;
		}
		if (!s.endsWith(delimiter)) {
			s += delimiter;
		}
		int pos = s.indexOf(delimiter + text + delimiter);
		if (pos == -1) {
			if (s.startsWith(text + delimiter)) {
				return true;
			}
			return false;
		}
		return true;
	}

	public static int count(String s, String text) {
		if ((s == null) || (text == null)) {
			return 0;
		}
		int count = 0;
		int pos = s.indexOf(text);
		while (pos != -1) {
			pos = s.indexOf(text, pos + text.length());
			count++;
		}
		return count;
	}

	public static boolean endsWith(String s, char end) {
		return startsWith(s, (Character.valueOf(end)).toString());
	}

	public static boolean endsWith(String s, String end) {
		if ((s == null) || (end == null)) {
			return false;
		}
		if (end.length() > s.length()) {
			return false;
		}
		String temp = s.substring(s.length() - end.length(), s.length());
		if (temp.equalsIgnoreCase(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static String merge(List<String> list, String delimiter) {
		return merge((String[]) list.toArray(new String[0]), delimiter);
	}

	public static String merge(Set<String> set, String delimiter) {
		return merge((String[]) set.toArray(new String[0]), delimiter);
	}

	public static String merge(String[] array, String delimiter) {
		if (array == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].trim());
			if ((i + 1) != array.length) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	// public static String randomize(String s) {
	// Randomizer r = new Randomizer();
	//
	// return r.randomize(s);
	// }
	public static String read(ClassLoader classLoader, String name) throws IOException {
		return read(classLoader.getResourceAsStream(name));
	}

	public static String read(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line).append('\n');
		}
		br.close();
		return sb.toString().trim();
	}

	public static String replace(String s, char oldSub, char newSub) {
		return replace(s, oldSub, Character.valueOf(newSub).toString());
	}

	public static String replace(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}
		char[] c = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == oldSub) {
				sb.append(newSub);
			} else {
				sb.append(c[i]);
			}
		}
		return sb.toString();
	}

	public static String replace(String s, String oldSub, String newSub) {
		if ((s == null) || (oldSub == null) || (newSub == null)) {
			return null;
		}
		int y = s.indexOf(oldSub);
		if (y >= 0) {
			StringBuffer sb = new StringBuffer();
			int length = oldSub.length();
			int x = 0;
			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);
				x = y + length;
				y = s.indexOf(oldSub, x);
			}
			sb.append(s.substring(x));
			return sb.toString();
		} else {
			return s;
		}
	}

	public static String replace(String s, String[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}
		if (oldSubs.length != newSubs.length) {
			return s;
		}
		for (int i = 0; i < oldSubs.length; i++) {
			s = replace(s, oldSubs[i], newSubs[i]);
		}
		return s;
	}

	public static String reverse(String s) {
		if (s == null) {
			return null;
		}
		char[] c = s.toCharArray();
		char[] reverse = new char[c.length];
		for (int i = 0; i < c.length; i++) {
			reverse[i] = c[c.length - i - 1];
		}
		return new String(reverse);
	}

	public static String shorten(String s) {
		return shorten(s, 20);
	}

	public static String shorten(String s, int length) {
		return shorten(s, length, "..");
	}

	public static String shorten(String s, String suffix) {
		return shorten(s, 20, suffix);
	}

	public static String shorten(String s, int length, String suffix) {
		if (s == null || suffix == null) {
			return null;
		}
		if (s.length() > length) {
			s = s.substring(0, length) + suffix;
		}
		return s;
	}

	public static final String stackTrace(Throwable t) {
		String s = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			t.printStackTrace(new PrintWriter(baos, true));
			s = baos.toString();
		} catch (Exception e) {
		}
		return s;
	}

	public static boolean startsWith(String s, char begin) {
		return startsWith(s, (Character.valueOf(begin)).toString());
	}

	public static boolean startsWith(String s, String start) {
		if ((s == null) || (start == null)) {
			return false;
		}
		if (start.length() > s.length()) {
			return false;
		}
		String temp = s.substring(0, start.length());
		if (temp.equalsIgnoreCase(start)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 合并成逗号分隔的形式，即"a", "b"
	 * 
	 * @param strArr
	 * @return
	 */
	public static String mergeScriptArray(String[] strArr) {
		if (strArr == null)
			return null;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if (i != strArr.length - 1)
				buf.append(",");
		}
		return buf.toString();
	}

	/**
	 * 合并成javascript的数组样式，即["a", "b"]
	 * 
	 * @param str
	 *            以逗号分隔的字符串
	 * @return
	 */
	public static String mergeScriptArrayStr(String[] strArr) {
		if (strArr == null)
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		for (int i = 0; i < strArr.length; i++) {
			buf.append("\"");
			buf.append(strArr[i]);
			buf.append("\"");
			if (i != strArr.length - 1)
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * �ϲ���javascript��������ʽ����["a", "b"]
	 * 
	 * @param strList
	 * @return
	 */
	public static String mergeScriptArray(Collection<String> strList) {
		if (strList == null)
			return null;
		StringBuffer buf = new StringBuffer();
		buf.append("[");
		Iterator<String> it = strList.iterator();
		while (it.hasNext()) {
			String str = it.next();
			buf.append("\"");
			buf.append(str);
			buf.append("\"");
			if (it.hasNext())
				buf.append(",");
		}
		buf.append("]");
		return buf.toString();
	}

	/**
	 * 拼接in语句
	 * 
	 * @param values
	 * @return
	 */
	public static String getInSql(String[] array) {
		if (isNull(array)) {
			return null;
		}
		StringBuilder insql = new StringBuilder();
		insql.append("('");
		insql.append(org.apache.commons.lang.StringUtils.join(array, "','"));
		insql.append("')");
		return insql.toString();
	}
}
