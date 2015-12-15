package xap.lui.core.refrence;
import java.util.ArrayList;
public class SpellUtil {
	
	public static String getFullSpell(String cnStr) {
		if (null == cnStr || "".equals(cnStr.trim())) {
			return cnStr;
		}
		char[] chars = cnStr.toCharArray();
		StringBuffer retuBuf = new StringBuffer();
		for (int i = 0, Len = chars.length; i < Len; i++) {
			retuBuf.append(SepllTable.getPinyin(chars[i]));
		}
		return retuBuf.toString();
	}
	public static String[] getFullSpells(String cnStr) {
		if (null == cnStr || "".equals(cnStr.trim())) {
			return new String[] { cnStr };
		}
		char[] chars = cnStr.toCharArray();
		ArrayList<StringBuffer> bufs = new ArrayList<StringBuffer>();
		StringBuffer buf = new StringBuffer();
		bufs.add(buf);
		for (int i = 0, Len = chars.length; i < Len; i++) {
			String[] ccs = SepllTable.getPinyins(chars[i]);
			appendToBufs(ccs, bufs);
		}
		String[] results = new String[bufs.size()];
		for (int i = 0; i < results.length; i++) {
			results[i] = bufs.get(i).toString();
		}
		return results;
	}
	
	public static String getFirstSpell(String cnStr) {
		if (null == cnStr || "".equals(cnStr.trim())) {
			return cnStr;
		}
		char[] chars = cnStr.toCharArray();
		StringBuffer retuBuf = new StringBuffer();
		for (int i = 0, Len = chars.length; i < Len; i++) {
			retuBuf.append(SepllTable.getFirstPinyin(chars[i]));
		}
		return retuBuf.toString();
	}
	public static String[] getFirstSpells(String cnStr) {
		if (null == cnStr || "".equals(cnStr.trim())) {
			return new String[] { cnStr };
		}
		char[] chars = cnStr.toCharArray();
		ArrayList<StringBuffer> bufs = new ArrayList<StringBuffer>();
		StringBuffer buf = new StringBuffer();
		bufs.add(buf);
		for (int i = 0, Len = chars.length; i < Len; i++) {
			char[] ccs = SepllTable.getFirstPinyins(chars[i]);
			appendToBufs(ccs, bufs);
		}
		String[] results = new String[bufs.size()];
		for (int i = 0; i < results.length; i++) {
			results[i] = bufs.get(i).toString();
		}
		return results;
	}
	public static String[] getFullAndFirstSpells(String cnStr) {
		String[] s1 = getFirstSpells(cnStr);
		String[] s2 = getFullSpells(cnStr);
		String[] results = new String[s1.length + s2.length];
		System.arraycopy(s1, 0, results, 0, s1.length);
		System.arraycopy(s2, 0, results, s1.length, s2.length);
		return results;
	}
	private static void appendToBufs(char[] chars, ArrayList<StringBuffer> bufs) {
		int bufCount = bufs.size();
		duplicateBuf(bufs, chars.length);
		for (int i = 0; i < chars.length; i++) {
			for (int j = 0; j < bufCount; j++) {
				bufs.get(i * bufCount + j).append(chars[i]);
			}
		}
	}
	private static void appendToBufs(String[] pinyins, ArrayList<StringBuffer> bufs) {
		int bufCount = bufs.size();
		duplicateBuf(bufs, pinyins.length);
		for (int i = 0; i < pinyins.length; i++) {
			for (int j = 0; j < bufCount; j++) {
				bufs.get(i * bufCount + j).append(pinyins[i]);
			}
		}
	}
	private static void duplicateBuf(ArrayList<StringBuffer> bufs, int copys) {
		int originalSize = bufs.size();
		for (int i = 0; i < copys - 1; i++) {
			for (int j = 0; j < originalSize; j++) {
				bufs.add(new StringBuffer(bufs.get(j)));
			}
		}
	}
}
