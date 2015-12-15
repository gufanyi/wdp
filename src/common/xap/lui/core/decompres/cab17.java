package xap.lui.core.decompres;

final class cab17 implements CabDecompressor {
	private static final int[] C6 = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17,
			19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195,
			227, 258 };

	private static final int[] C7 = { 1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49,
			65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097,
			6145, 8193, 12289, 16385, 24577 };

	private static final int[] C8 = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4,
			12, 3, 13, 2, 14, 1, 15 };
	private byte[] C9;
	private byte[] CA;
	private byte[] CB;
	private int CC;
	private int CD;
	int D4;
	private int CE;
	private int CF;
	private int D0;
	private cab18 D1;
	private cab18 D2;
	private cab18 D3;

	private void A2() throws CabException {
		this.D1.A3();
		this.D2.A3();
	}

	private void A3() {
		int i = 0;
		do {
			this.D1.C2[i] = 8;
			i++;
		} while (i <= 143);
		i = 144;
		do {
			this.D1.C2[i] = 9;
			i++;
		} while (i <= 255);
		i = 256;
		do {
			this.D1.C2[i] = 7;
			i++;
		} while (i <= 279);
		i = 280;
		do {
			this.D1.C2[i] = 8;
			i++;
		} while (i <= 287);
		i = 0;
		do {
			this.D2.C2[i] = 5;
			i++;
		} while (i < 32);
	}

	public void reset(int i) {
	}

	void C5(int i) {
		this.D4 >>>= i;
		this.CE -= i;
		if (this.CE <= 0) {
			this.CE += 16;
			this.D4 |= (this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8) << this.CE;
			this.CC += 2;
		}
	}

	public int getMaxGrowth() {
		return 28;
	}

	cab17() {
		this.C9 = new byte[320];
	}

	public void decompress(byte[] abyte0, byte[] abyte1, int i, int j)
			throws CabException {
		this.CA = abyte0;
		this.CB = abyte1;
		if ((this.CA[0] != 67) || (this.CA[1] != 75))
			throw new CabCorruptException();
		if (abyte1.length < 33027)
			throw new CabException();
		if (abyte0.length < 28)
			throw new CabException();
		this.CC = 2;
		this.CD = (i + 4);
		this.D0 = j;
		this.CF = 0;
		A4();
		while (this.CF < this.D0)
			C2();
	}

	private void A4() throws CabException {
		if (this.CC + 4 > this.CD) {
			throw new CabCorruptException();
		}

		this.D4 = (C0() | C0() << 16);
		this.CE = 16;
	}

	private void A5() throws CabException {
		if (this.CC > this.CD)
			throw new CabCorruptException();
	}

	private void AA() throws CabException {
		int i = this.CF;
		int j = this.D0;
		byte[] abyte0 = this.CB;
		int[] ai = this.D1.C3;
		int[] ai1 = this.D1.C4;
		int[] ai2 = this.D1.C5;
		byte[] abyte1 = this.D1.C2;
		int[] ai3 = this.D2.C3;
		int[] ai4 = this.D2.C4;
		int[] ai5 = this.D2.C5;
		byte[] abyte2 = this.D2.C2;
		int k = this.D4;
		int l = this.CE;
		do {
			if (this.CC > this.CD) {
				break;
			}
			int i1 = 0;
			for (i1 = ai[(k & 0x1FF)]; i1 < 0;) {
				int j1 = 512;
				do {
					i1 = -i1;
					if ((k & j1) == 0)
						i1 = ai1[i1];
					else
						i1 = ai2[i1];
					j1 <<= 1;
				} while (i1 < 0);
			}

			byte byte0 = abyte1[i1];
			k >>>= byte0;
			l -= byte0;
			if (l <= 0) {
				l += 16;
				k |= (this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8) << l;
				this.CC += 2;
			}
			if (i1 < 256) {
				abyte0[(i++)] = (byte) i1;
			} else {
				i1 -= 257;
				if (i1 < 0)
					break;
				if (i1 < 8) {
					i1 += 3;
				} else if (i1 != 28) {
					int i2 = i1 - 4 >>> 2;
					i1 = C6[i1] + (k & (1 << i2) - 1);
					k >>>= i2;
					l -= i2;
					if (l <= 0) {
						l += 16;
						k |= (this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8) << l;
						this.CC += 2;
					}
				} else {
					i1 = 258;
				}
				int k1 = 0;
				for (k1 = ai3[(k & 0x7F)]; k1 < 0;) {
					int k2 = 128;
					do {
						k1 = -k1;
						if ((k & k2) == 0)
							k1 = ai4[k1];
						else
							k1 = ai5[k1];
						k2 <<= 1;
					} while (k1 < 0);
				}

				byte0 = abyte2[k1];
				k >>>= byte0;
				l -= byte0;
				if (l <= 0) {
					l += 16;
					k |= (this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8) << l;
					this.CC += 2;
				}
				int j2 = k1 - 2 >> 1;
				int l1;
				if (j2 > 0) {
					l1 = C7[k1] + (k & (1 << j2) - 1);
					k >>>= j2;
					l -= j2;
					if (l <= 0) {
						l += 16;
						k |= (this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8) << l;
						this.CC += 2;
					}
				} else {
					l1 = k1 + 1;
				}
				do {
					abyte0[i] = abyte0[(i - l1 & 0x7FFF)];
					i++;
					i1--;
				} while (i1 != 0);
			}
		} while (i <= j);
		this.CF = i;
		this.D4 = k;
		this.CE = l;
		A5();
	}

	private int B5(byte byte0, byte byte1) {
		return byte0 & 0xFF | (byte1 & 0xFF) << 8;
	}

	private void BA() throws CabException {
		A5();
		int i = C1(5) + 257;
		int j = C1(5) + 1;
		int k = C1(4) + 4;
		for (int i1 = 0; i1 < k; i1++) {
			this.D3.C2[C8[i1]] = (byte) C1(3);
			A5();
		}

		for (int j1 = k; j1 < C8.length; j1++) {
			this.D3.C2[C8[j1]] = 0;
		}
		this.D3.A3();
		int l = i + j;
		for (int k1 = 0; k1 < l;) {
			A5();
			byte byte0 = (byte) this.D3.A4();
			if (byte0 <= 15) {
				this.C9[(k1++)] = byte0;
			} else if (byte0 == 16) {
				if (k1 == 0)
					throw new CabCorruptException();
				byte byte1 = this.C9[(k1 - 1)];
				int k2 = C1(2) + 3;
				if (k1 + k2 > l)
					throw new CabCorruptException();
				for (int l3 = 0; l3 < k2; l3++) {
					this.C9[(k1++)] = byte1;
				}
			} else if (byte0 == 17) {
				int l2 = C1(3) + 3;
				if (k1 + l2 > l)
					throw new CabCorruptException();
				for (int j3 = 0; j3 < l2; j3++)
					this.C9[(k1++)] = 0;
			} else {
				int i3 = C1(7) + 11;
				if (k1 + i3 > l)
					throw new CabCorruptException();
				for (int k3 = 0; k3 < i3; k3++) {
					this.C9[(k1++)] = 0;
				}
			}
		}

		System.arraycopy(this.C9, 0, this.D1.C2, 0, i);
		for (int l1 = i; l1 < 288; l1++) {
			this.D1.C2[l1] = 0;
		}
		for (int i2 = 0; i2 < j; i2++) {
			this.D2.C2[i2] = this.C9[(i2 + i)];
		}
		for (int j2 = j; j2 < 32; j2++) {
			this.D2.C2[j2] = 0;
		}
		if (this.D1.C2[256] == 0)
			throw new CabCorruptException();
	}

	private int C0() {
		int i = this.CA[this.CC] & 0xFF | (this.CA[(this.CC + 1)] & 0xFF) << 8;
		this.CC += 2;
		return i;
	}

	private int C1(int i) {
		int j = this.D4 & (1 << i) - 1;
		C5(i);
		return j;
	}

	private void C2() throws CabException {
		int i = C1(1);
		int j = C1(2);
		if (j == 2) {
			BA();
			A2();
			AA();
			return;
		}
		if (j == 1) {
			A3();
			A2();
			AA();
			return;
		}
		if (j == 0) {
			C3();
			return;
		}

		throw new CabException();
	}

	public void init(int i) {
		this.D1 = new cab18(288, 9, this);
		this.D2 = new cab18(32, 7, this);
		this.D3 = new cab18(19, 7, this);
	}

	private void C3() throws CabException {
		C4();
		if (this.CC >= this.CD)
			throw new CabCorruptException();
		int i = B5(this.CA[this.CC], this.CA[(this.CC + 1)]);
		int j = B5(this.CA[(this.CC + 2)], this.CA[(this.CC + 3)]);
		if ((short) i != (short) (j ^ 0xFFFFFFFF))
			throw new CabCorruptException();
		if ((this.CC + i > this.CD) || (this.CF + i > this.D0))
			throw new CabCorruptException();
		A4();
		System.arraycopy(this.CA, this.CC, this.CB, this.CF, i);
		this.CF += i;
		if (this.CF < this.D0)
			A4();
	}

	private void C4() {
		if (this.CE == 16) {
			this.CC -= 4;
		} else if (this.CE >= 8)
			this.CC -= 3;
		else
			this.CC -= 2;
		if (this.CC < 0)
			this.CC = 0;
		this.CE = 0;
	}
}
