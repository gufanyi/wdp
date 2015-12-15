package xap.lui.core.decompres;

final class cab9 implements CabDecompressor {
	public void reset(int i) {
	}

	public int getMaxGrowth() {
		return 0;
	}

	public void decompress(byte[] abyte0, byte[] abyte1, int i, int j)
			throws CabException {
		if (i != j) {
			throw new CabCorruptException();
		}

		System.arraycopy(abyte0, 0, abyte1, 0, j);
	}

	public void init(int i) {
	}
}
