package xap.lui.core.decompres;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class cab8 {
	static int A2(InputStream inputstream) throws IOException {
		byte[] abyte0 = new byte[2];
		if (inputstream.read(abyte0) != 2) {
			throw new EOFException();
		}
		return abyte0[0] & 0xFF | (abyte0[1] & 0xFF) << 8;
	}

	static void A3(InputStream inputstream, byte[] abyte0, int i, int j)
			throws IOException {
		int l;
		for (int k = 0; k < j; k += l) {
			l = inputstream.read(abyte0, i + k, j - k);
			if (l < 0)
				throw new EOFException();
		}
	}

	static int A4(InputStream inputstream) throws IOException {
		byte[] abyte0 = new byte[4];
		if (inputstream.read(abyte0) != 4) {
			throw new EOFException();
		}
		return abyte0[0] & 0xFF | (abyte0[1] & 0xFF) << 8
				| (abyte0[2] & 0xFF) << 16 | (abyte0[3] & 0xFF) << 24;
	}

	static long A5(int i) {
		return i & 0xFFFFFFFF;
	}

	static String AA(byte[] abyte0) {

		int j = 0;
		int i = 0;

		for (i = 0; abyte0[i] != 0; i++)
			;
		char[] ac = new char[i];

		int k = 0;
		for (k = 0; abyte0[j] != 0; k++) {
			char c = (char) (abyte0[(j++)] & 0xFF);
			if (c < '聙') {
				ac[k] = c;
			} else {
				if (c < '脌')
					return null;
				if (c < '脿') {
					ac[k] = (char) ((c & 0x1F) << '\006');
					c = (char) (abyte0[(j++)] & 0xFF);
					if ((c < '聙') || (c > '驴'))
						return null;
					int tmp121_119 = k;
					char[] tmp121_118 = ac;
					tmp121_118[tmp121_119] = (char) (tmp121_118[tmp121_119] | (char) (c & 0x3F));
				} else if (c < '冒') {
					ac[k] = (char) ((c & 0xF) << '\f');
					c = (char) (abyte0[(j++)] & 0xFF);
					if ((c < '聙') || (c > '驴'))
						return null;
					int tmp190_188 = k;
					char[] tmp190_187 = ac;
					tmp190_187[tmp190_188] = (char) (tmp190_187[tmp190_188] | (char) ((c & 0x3F) << '\006'));
					c = (char) (abyte0[(j++)] & 0xFF);
					if ((c < '聙') || (c > '驴'))
						return null;
					int tmp238_236 = k;
					char[] tmp238_235 = ac;
					tmp238_235[tmp238_236] = (char) (tmp238_235[tmp238_236] | (char) (c & 0x3F));
				} else {
					return null;
				}
			}

		}
		return new String(ac, 0, k);

	}

}
