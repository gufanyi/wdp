package xap.lui.core.decompres;

final class cab2 {
	static int A2(byte[] abyte0, int i, int j) {
		byte byte0 = (byte) (j & 0xFF);
		byte byte1 = (byte) (j >>> 8 & 0xFF);
		byte byte2 = (byte) (j >>> 16 & 0xFF);
		byte byte3 = (byte) (j >>> 24 & 0xFF);
		int l = 0;
		for (int k = i >>> 2; k-- > 0;) {
			byte0 = (byte) (byte0 ^ abyte0[(l++)]);
			byte1 = (byte) (byte1 ^ abyte0[(l++)]);
			byte2 = (byte) (byte2 ^ abyte0[(l++)]);
			byte3 = (byte) (byte3 ^ abyte0[(l++)]);
		}

		switch (i & 0x3) {
		case 3:
			byte2 = (byte) (byte2 ^ abyte0[(l++)]);
			break;
		case 2:
			byte1 = (byte) (byte1 ^ abyte0[(l++)]);
			break;
		case 1:
			byte0 = (byte) (byte0 ^ abyte0[(l++)]);
		}

		return byte0 & 0xFF | (byte1 & 0xFF) << 8 | (byte2 & 0xFF) << 16
				| (byte3 & 0xFF) << 24;
	}
}
