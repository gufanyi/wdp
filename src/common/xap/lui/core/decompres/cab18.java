package xap.lui.core.decompres;

final class cab18 {
	private int A5;
	private int[] AA;
	private int B5;
	private int BA;
	private int C0;
	private cab17 C1;
	byte[] C2;
	int[] C3;
	int[] C4;
	int[] C5;

	cab18(int i, int j, cab17 cab17_1) {
		this.A5 = i;
		this.C1 = cab17_1;
		this.C2 = new byte[i];
		this.AA = new int[i];
		this.B5 = j;
		this.BA = (1 << this.B5);
		this.C0 = (this.BA - 1);
		this.C3 = new int[1 << this.B5];
		this.C4 = new int[this.A5 * 2];
		this.C5 = new int[this.A5 * 2];
	}

	void A3() throws CabCorruptException {
		int[] ai = new int[17];
		int[] ai1 = new int[17];
		int k = 0;
		do {
			ai[k] = 0;
			k++;
		} while (k <= 16);
		for (k = 0; k < this.A5; k++) {
			ai[this.C2[k]] += 1;
		}
		for (k = this.B5; k <= 16; k++) {
			if (ai[k] <= 0)
				continue;
			for (int j1 = 0; j1 < this.BA; j1++) {
				this.C3[j1] = 0;
			}
			break;
		}

		int i = 0;
		ai[0] = 0;
		k = 1;
		do {
			i = i + ai[(k - 1)] << 1;
			ai1[k] = i;
			k++;
		} while (k <= 16);
		for (int l = 0; l < this.A5; l++) {
			byte byte0 = this.C2[l];
			if (byte0 <= 0)
				continue;
			this.AA[l] = A2(ai1[byte0], byte0);
			ai1[byte0] += 1;
		}

		int j = this.A5;
		for (int i1 = 0; i1 < this.A5; i1++) {
			byte byte1 = this.C2[i1];
			int k1 = this.AA[i1];
			if (byte1 > 0)
				if (byte1 <= this.B5) {
					int l1 = 1 << this.B5 - byte1;
					int j2 = 1 << byte1;
					if (k1 >= j2)
						throw new CabCorruptException();
					for (int l2 = 0; l2 < l1; l2++) {
						this.C3[k1] = i1;
						k1 += j2;
					}
				} else {
					int i2 = byte1 - this.B5;
					int k2 = 1 << this.B5;
					int i3 = k1 & this.C0;
					byte byte2 = 2;
					do {
						int j3;
						if (byte2 == 2) {
							j3 = this.C3[i3];
						} else {
							if (byte2 == 1)
								j3 = this.C5[i3];
							else
								j3 = this.C4[i3];
						}
						if (j3 == 0) {
							this.C4[j] = 0;
							this.C5[j] = 0;
							if (byte2 == 2) {
								this.C3[i3] = (-j);
							} else if (byte2 == 1)
								this.C5[i3] = (-j);
							else
								this.C4[i3] = (-j);
							j3 = -j;
							j++;
						}
						i3 = -j3;
						if ((k1 & k2) == 0)
							byte2 = 0;
						else
							byte2 = 1;
						k2 <<= 1;
						i2--;
					} while (i2 != 0);
					if (byte2 == 0)
						this.C4[i3] = i1;
					else
						this.C5[i3] = i1;
				}
		}
	}

	private int A2(int i, int j) {
		int k = 0;
		do {
			k |= i & 0x1;
			k <<= 1;
			i >>>= 1;
			j--;
		} while (j > 0);
		return k >>> 1;
	}

	int A4() {
		int i = 0;
		for (i = this.C3[(this.C1.D4 & this.C0)]; i < 0;) {
			int j = 1 << this.B5;
			do {
				i = -i;
				if ((this.C1.D4 & j) == 0)
					i = this.C4[i];
				else
					i = this.C5[i];
				j <<= 1;
			} while (i < 0);
		}

		this.C1.C5(this.C2[i]);
		return i;
	}
}
