package xap.lui.core.decompres;

final class cab14 implements LZXConstants {
	private int BA;
	private int[] C0;
	int[] CE;
	private int[] C1;
	private int[] C2;
	private int[] C3;
	private int C4;
	private int C5;
	private int C6;
	private int C7;
	private cab13 C8;
	private cab14 C9;
	private int[] CA;
	private int[] CB;
	private int[] CC;
	private static final byte[] CD = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16 };

	void A2() {
		for (int i = 0; i < this.BA; i++) {
			int tmp16_15 = 0;
			this.CE[i] = tmp16_15;
			this.C0[i] = tmp16_15;
		}
	}

	cab14(int i, int j, cab13 cab13_1, cab14 cab14_1) {
		this.CA = new int[17];
		this.CB = new int[17];
		this.CC = new int[18];
		this.BA = i;
		this.C4 = j;
		this.C8 = cab13_1;
		this.C9 = cab14_1;
		this.C5 = (1 << this.C4);
		this.C6 = (this.C5 - 1);
		this.C7 = (32 - this.C4);
		this.C1 = new int[this.BA * 2];
		this.C2 = new int[this.BA * 2];
		this.C3 = new int[this.C5];
		this.C0 = new int[this.BA];
		this.CE = new int[this.BA];
	}

	void A3() throws CabCorruptException {
		for (int i = 0; i < this.BA; i++)
			this.CE[i] = this.C8.CB(3);
	}

	void A4(int i, int j) throws CabCorruptException {
		int k = 0;
		do {
			this.C9.CE[k] = (byte) this.C8.CB(4);
			k++;
		} while (k < 20);
		this.C9.A5();
		for (int l = i; l < j; l++) {
			int l1 = this.C9.B5();
			if (l1 == 17) {
				int i1 = this.C8.CB(4) + 4;
				if (l + i1 >= j)
					i1 = j - l;
				while (i1-- > 0)
					this.CE[(l++)] = 0;
				l--;
			} else if (l1 == 18) {
				int j1 = this.C8.CB(5) + 20;
				if (l + j1 >= j)
					j1 = j - l;
				while (j1-- > 0)
					this.CE[(l++)] = 0;
				l--;
			} else if (l1 == 19) {
				int k1 = this.C8.CB(1) + 4;
				if (l + k1 >= j)
					k1 = j - l;
				l1 = this.C9.B5();
				byte byte0 = CD[(this.C0[l] - l1 + 17)];
				while (k1-- > 0)
					this.CE[(l++)] = byte0;
				l--;
			} else {
				this.CE[l] = CD[(this.C0[l] - l1 + 17)];
			}
		}
	}

	void A5() throws CabCorruptException {
		int[] ai = this.C3;
		int[] ai1 = this.CC;
		int j2 = this.C4;
		int i = 1;
		do {
			this.CA[i] = 0;
			i++;
		} while (i <= 16);
		for (i = 0; i < this.BA; i++) {
			this.CA[this.CE[i]] += 1;
		}
		ai1[1] = 0;
		i = 1;
		do {
			ai1[(i + 1)] = (ai1[i] + (this.CA[i] << 16 - i));
			i++;
		} while (i <= 16);
		if (ai1[17] != 65536) {
			if (ai1[17] == 0) {
				for (i = 0; i < this.C5; i++) {
					ai[i] = 0;
				}
				return;
			}

			throw new CabCorruptException();
		}
		int i2 = 16 - j2;
		for (i = 1; i <= j2; i++) {
			ai1[i] >>>= i2;
			this.CB[i] = (1 << j2 - i);
		}

		for (; i <= 16; i++) {
			this.CB[i] = (1 << 16 - i);
		}
		i = ai1[(j2 + 1)] >>> i2;
		if (i != 65536) {
			for (; i < this.C5; i++)
				ai[i] = 0;
		}
		int i1 = this.BA;
		for (int l = 0; l < this.BA; l++) {
			int l1 = this.CE[l];
			if (l1 == 0)
				continue;
			int j1 = ai1[l1] + this.CB[l1];
			if (l1 <= j2) {
				if (j1 > this.C5)
					throw new CabCorruptException();
				for (int j = ai1[l1]; j < j1; j++) {
					ai[j] = l;
				}
				ai1[l1] = j1;
			} else {
				int k1 = ai1[l1];
				ai1[l1] = j1;
				int l2 = k1 >>> i2;
				byte byte0 = 2;
				int k = l1 - j2;
				k1 <<= j2;
				do {
					int k2;
					if (byte0 == 2) {
						k2 = ai[l2];
					} else {
						if (byte0 == 0)
							k2 = this.C1[l2];
						else
							k2 = this.C2[l2];
					}
					if (k2 == 0) {
						this.C1[i1] = 0;
						this.C2[i1] = 0;
						if (byte0 == 2) {
							ai[l2] = (-i1);
						} else if (byte0 == 0)
							this.C1[l2] = (-i1);
						else
							this.C2[l2] = (-i1);
						k2 = -i1;
						i1++;
					}
					l2 = -k2;
					if ((k1 & 0x8000) == 0)
						byte0 = 0;
					else
						byte0 = 1;
					k1 <<= 1;
					k--;
				} while (k != 0);
				if (byte0 == 0)
					this.C1[l2] = l;
				else
					this.C2[l2] = l;
			}
		}
	}

	void AA() {
		System.arraycopy(this.CE, 0, this.C0, 0, this.BA);
	}

	int B5() {
		int i = 0;
		for (i = this.C3[(this.C8.EC >>> this.C7 & this.C6)]; i < 0;) {
			int j = 1 << this.C7 - 1;
			do {
				i = -i;
				if ((this.C8.EC & j) == 0)
					i = this.C1[i];
				else
					i = this.C2[i];
				j >>>= 1;
			} while (i < 0);
		}

		this.C8.CA(this.CE[i]);
		return i;
	}
}
