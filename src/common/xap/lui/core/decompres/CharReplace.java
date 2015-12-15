package xap.lui.core.decompres;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CharReplace {
	private static List<byte[]> fErrCharList = new ArrayList();

	private static boolean initflage = false;
	private static List<TprvReplaceItem> fEnCodeList = new ArrayList();
	private static List<TprvReplaceItem> fDeCodeList = new ArrayList();
	private static String obj = "";

	private static final byte[] C_b27 = { 38, 35, 50, 55, 59 };
	private static final byte[] C_b28 = { 38, 35, 50, 56, 59 };

	private static final byte[] C_b1B = { 38, 35, 120, 49, 98, 59 };
	private static final byte[] C_b1C = { 38, 35, 120, 49, 99, 59 };

	// private static final TLogUtils log = new TLogUtils(CharReplace.class);

	private static final TprvReplaceItem getCheckit(byte[] aByteArray, int pos,
			List<TprvReplaceItem> aList) {
		for (TprvReplaceItem findbyte : aList) {
			if (pos + findbyte.source.length >= aByteArray.length)
				break;
			boolean findit = true;
			for (int i = 0; i < findbyte.source.length; i++) {
				if (aByteArray[(pos + i)] != findbyte.source[i]) {
					findit = false;
					break;
				}
			}
			if (findit)
				return findbyte;
		}
		return null;
	}

	private static final byte[] byteArrayReplace(byte[] aSource,
			List<TprvReplaceItem> aList) {
		boolean inReplace = false;
		int i = 0;
		int startPos = 0;
		if (aSource == null)
			return null;
		ByteArrayOutputStream fBuffier = new ByteArrayOutputStream(
				aSource.length + aSource.length / 10);

		while (i < aSource.length) {
			if (!inReplace) {
				if (aSource[i] == 62) {
					inReplace = true;
					i++;
				} else {
					i++;
				}
			} else {
				TprvReplaceItem fitem = getCheckit(aSource, i, aList);
				if ((inReplace) && (aSource[i] == 60)) {
					inReplace = false;
				}

				if (inReplace) {
					fBuffier.write(aSource, startPos, i - startPos);
					if (fitem != null) {
						fitem.doReplace(fBuffier);
						i += fitem.source.length;
					} else {
						fBuffier.write(aSource[i]);
						i++;
					}
					startPos = i;
				}
			}
		}
		if (i > startPos)
			try {
				fBuffier.write(aSource, startPos, i - startPos);
			} catch (Exception localException) {
			}
		return fBuffier.toByteArray();
	}

	private static void init() {
		if (!initflage)
			synchronized (obj) {
				if (initflage)
					return;
				TprvReplaceItem ftemp = new TprvReplaceItem();
				ftemp.source = new byte[] { -62, -96 };
				ftemp.desc = C_b27;
				fEnCodeList.add(ftemp);

				ftemp = new TprvReplaceItem();
				ftemp.source = new byte[] { -29, -128, -128 };
				ftemp.desc = C_b28;
				fEnCodeList.add(ftemp);

				ftemp = new TprvReplaceItem();
				ftemp.source = C_b27;
				ftemp.desc = new byte[] { -62, -96 };
				fDeCodeList.add(ftemp);

				ftemp = new TprvReplaceItem();
				ftemp.source = C_b28;
				ftemp.desc = new byte[] { -29, -128, -128 };
				fDeCodeList.add(ftemp);
				initflage = true;
			}
	}

	public static byte[] doReplace_Decode(byte[] aByteArray) {
		init();
		return byteArrayReplace(aByteArray, fDeCodeList);
	}

	public static byte[] doReplace_Encode(byte[] aByteArray) {
		init();
		return byteArrayReplace(aByteArray, fEnCodeList);
	}

	public static void main(String[] aaa) {
		try {
			FileInputStream fin = new FileInputStream("C:/dhd/view1.xsl");
			try {
				ByteArrayOutputStream fbuffer = new ByteArrayOutputStream();
				byte[] ftemp = new byte[8000];
				int len;
				do {
					len = fin.read(ftemp);
					if (len >= 0)
						fbuffer.write(ftemp, 0, len);
				} while (len >= 0);
				ftemp = doReplace_Encode(fbuffer.toByteArray());
				FileOutputStream fout = new FileOutputStream("c:/dhd/ok.xsl");
				try {
					fout.write(ftemp);
				} finally {
					fout.close();
				}
			} finally {
				fin.close();
			}
			System.out.println("ok1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileInputStream fin = new FileInputStream("C:/dhd/ok.xsl");
			try {
				ByteArrayOutputStream fbuffer = new ByteArrayOutputStream();
				byte[] ftemp = new byte[8000];
				int len;
				do {
					len = fin.read(ftemp);
					if (len >= 0)
						fbuffer.write(ftemp, 0, len);
				} while (len >= 0);
				ftemp = doReplace_Decode(fbuffer.toByteArray());
				FileOutputStream fout = new FileOutputStream("c:/dhd/ok2.xsl");
				try {
					fout.write(ftemp);
				} finally {
					fout.close();
				}
			} finally {
				fin.close();
			}
			System.out.println("ok2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class TprvReplaceItem {
		private byte[] source;
		private byte[] desc;

		private void doReplace(ByteArrayOutputStream aBuffier) {
			try {
				aBuffier.write(this.desc);
			} catch (Exception localException) {
			}
		}
	}
}
