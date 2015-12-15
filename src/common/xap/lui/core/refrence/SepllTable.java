package xap.lui.core.refrence;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.BitSet;
public class SepllTable {
	private static final int COUNT = 21000;
	private static String[][] p = new String[COUNT][];
	static {
		init();
	}
	private static void init() {
		try {
			InputStream is = SepllTable.class.getClassLoader().getResourceAsStream("nc/vo/pub/pinyin/NCPY.txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String s = null;
			while ((s = r.readLine()) != null) {
				int index = s.charAt(0) - 'һ';
				p[index] = s.substring(1).split("\\s");
				for (int i = 0; i < p[index].length; i++) {
					p[index][i] = p[index][i].intern();
				}
			}
		} catch (Exception e) {
		}
	}
	public static String getPinyin(char ch) {
		int index = ch - 'һ';
		if (index >= 0 && index <= COUNT) {
			return p[index] != null ? p[index][0] : new String(new char[] { ch });
		}
		return new String(new char[] { ch });
	}
	public static String[] getPinyins(char ch) {
		int index = ch - 'һ';
		if (index >= 0 && index <= COUNT) {
			if (p[index] != null) {
				return p[index];
			}
		}
		return new String[] { new String(new char[] { ch }) };
	}
	public static char getFirstPinyin(char ch) {
		int index = ch - 'һ';
		if (index >= 0 && index <= COUNT) {
			return p[index] != null ? p[index][0].charAt(0) : ch;
		}
		return ch;
	}
	public static char[] getFirstPinyins(char ch) {
		int index = ch - 'һ';
		if (index >= 0 && index <= COUNT) {
			if (p[index].length == 1)
			{
				return new char[] { p[index][0].charAt(0) };
			} else {
				BitSet readed_flags = new BitSet();
				readed_flags.clear();
				int count = 0;
				char[] ss = new char[10];
				for (String pinyin : p[index]) {
					char initial = pinyin.charAt(0);
					int flagIndex = initial - 'a';
					if (readed_flags.get(flagIndex) == false) {
						ss[count++] = initial;
						readed_flags.set(flagIndex);
					}
				}
				char[] result = new char[count];
				System.arraycopy(ss, 0, result, 0, count);
				return result;
			}
		} else {
			return new char[] { ch };
		}
	}
	public static void main(String[] args) throws Exception {
		String ssss = "我是中国人";
		for (int i = 0; i < ssss.length(); i++) {
			int index = (int) ssss.charAt(i) - 'һ';
			if (index >= 0 && index < p.length) {
				String py = p[index][0];
				System.out.println(py);
			}
		}
	}
}
