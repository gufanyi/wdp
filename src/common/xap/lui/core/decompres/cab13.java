package xap.lui.core.decompres;

final class cab13 implements CabDecompressor, LZXConstants {
	private int[] CC;
	private int[] CD;
	private cab14 CE;
	private cab14 CF;
	private cab14 D0;
	private cab14 D1;
	private int D2;
	private int D3;
	private int D4;
	private int D5;
	private int D6;
	private int D8;
	private int D9;
	private int DA;
	private int DB;
	private int DC;
	private int DD;
	private byte[] DE;
	private int DF;
	private boolean E0;
	private int E1;
	private int E2;
	private int E3;
	private byte[] E4;
	private boolean E5;
	int EC;
	private int E6;
	private int E7;
	private int E8;
	private byte[] E9;
	private boolean EA;
	private int EB;

	private void A2(int i) throws CabException {
		this.DA &= this.D3;
		if (this.DA + i > this.D2)
			throw new CabException();
		switch (this.D6) {
		default:
			throw new CabCorruptException();
		case 1:
			BA(i);
			return;
		case 2:
			C2(i);
			return;
		case 3:
		}
		C6(i);
	}

	public void reset(int i) throws CabException {
		if (this.DF == i) {
			this.CE.A2();
			this.CF.A2();
			this.D0.A2();
		} else {
			B5();
			int j = 256 + this.D4 * 8;
			this.DE = new byte[this.D2 + 261];
			this.D1 = new cab14(20, 8, this, null);
			this.CE = new cab14(j, 9, this, this.D1);
			this.CF = new cab14(249, 6, this, this.D1);
			this.D0 = new cab14(8, 7, this, this.D1);
		}
		this.DF = i;
		this.DB = (this.DC = this.DD = 1);
		this.D5 = 0;
		this.DA = 0;
		this.E8 = 0;
		this.E0 = true;
		this.EA = false;
		this.EB = 0;
		this.D6 = 4;
	}

	private void A3(byte[] abyte0) {
		this.E5 = false;
		this.E4 = abyte0;
		this.E2 = 0;
		this.E3 = abyte0.length;
		AA();
	}

	public int getMaxGrowth() {
		return 6144;
	}

	cab13() {
		this.CC = new int[51];
		this.CD = new int[51];
		this.E9 = new byte[6];
		C5();
		this.DF = -1;
	}

	void CA(int i) {
		this.EC <<= i;
		this.E6 -= i;
		if (this.E6 <= 0) {
			this.EC |= C4() << -this.E6;
			this.E6 += 16;
		}
	}

	private int A4(int i) {
		int j = this.EC >>> 32 - i;
		C9(i);
		return j;
	}

	public void decompress(byte[] abyte0, byte[] abyte1, int i, int j)
			throws CabException {
		A3(abyte0);
		int k = C0(j);
		System.arraycopy(this.DE, this.E1, abyte1, 0, k);
		if ((this.E7 != 0) && (this.EB < 32768))
			C3(abyte1, 0, k);
		this.EB += 1;
	}

	private void A5() throws CabCorruptException {
		this.E2 -= 2;
		if ((this.E2 < 0) || (this.E2 + 12 >= this.E3)) {
			throw new CabCorruptException();
		}

		this.DB = C7();
		this.DC = C7();
		this.DD = C7();
	}

	private void AA() {
		if (this.D6 != 3) {
			this.EC = (C4() << 16 | C4());
			this.E6 = 16;
		}
	}

	private void B5() {
		this.D4 = 4;
		int i = 4;
		do {
			i += (1 << this.CC[this.D4]);
			this.D4 += 1;
		} while (i < this.D2);
	}

	private void BA(int i) throws CabCorruptException {
		int j = this.DA;
		int k = this.D3;
		byte[] abyte0 = this.DE;
		int l = this.DB;
		int i1 = this.DC;
		int j1 = this.DD;
		int[] ai = this.CC;
		int[] ai1 = this.CD;
		cab14 cab14_1 = this.CE;
		cab14 cab14_2 = this.CF;
		do {
			int k1 = cab14_1.B5();
			if (k1 < 256) {
				abyte0[j] = (byte) k1;
				j++;
				i--;
			} else {
				k1 -= 256;
				int i2 = k1 & 0x7;
				if (i2 == 7)
					i2 += cab14_2.B5();
				int j2 = k1 >>> 3;
				int l1;
				if (j2 > 2) {
					if (j2 > 3)
						l1 = A4(ai[j2]) + ai1[j2];
					else
						l1 = 1;
					j1 = i1;
					i1 = l;
					l = l1;
				} else {
					if (j2 == 0) {
						l1 = l;
					} else if (j2 == 1) {
						l1 = i1;
						i1 = l;
						l = l1;
					} else {
						l1 = j1;
						j1 = l;
						l = l1;
					}
				}
				i2 += 2;
				i -= i2;
				do {
					abyte0[j] = abyte0[(j - l1 & k)];
					j++;
					i2--;
				} while (i2 > 0);
			}
		} while (i > 0);
		if (i != 0) {
			throw new CabCorruptException();
		}

		this.DB = l;
		this.DC = i1;
		this.DD = j1;
		this.DA = j;
	}

	private int C0(int i) throws CabException {
		int j = i;
		int k = 0;
		if (this.E0) {
			if (CB(1) == 1) {
				int l = CB(16);
				int k1 = CB(16);
				this.E7 = (l << 16 | k1);
			} else {
				this.E7 = 0;
			}
			this.E0 = false;
		}
		k = 0;
		while (i > 0) {
			if (this.D5 == 0) {
				if (this.D6 == 3) {
					if (((this.D9 & 0x1) != 0) && (this.E2 < this.E3))
						this.E2 += 1;
					this.D6 = 4;
					AA();
				}
				this.D6 = CB(3);
				int i1 = CB(8);
				int l1 = CB(8);
				int i2 = CB(8);
				if (this.E5)
					break;
				this.D8 = (this.D9 = (i1 << 16) + (l1 << 8) + i2);
				if (this.D6 == 2)
					C8();
				if ((this.D6 == 1) || (this.D6 == 2)) {
					this.CE.AA();
					this.CF.AA();
					C1();
				} else if (this.D6 == 3) {
					A5();
				} else {
					throw new CabCorruptException();
				}
			}
			this.D5 = 1;
			while ((this.D8 > 0) && (i > 0)) {
				int j1;
				if (this.D8 < i)
					j1 = this.D8;
				else
					j1 = i;
				A2(j1);
				this.D8 -= j1;
				i -= j1;
				k += j1;
			}
			if (this.D8 == 0)
				this.D5 = 0;
			if ((i == 0) && (this.E6 != 16))
				CA(this.E6);
		}
		if (k != j)
			throw new CabCorruptException();
		if (this.DA == 0)
			this.E1 = (this.D2 - k);
		else
			this.E1 = (this.DA - k);
		return k;
	}

	private void C1() throws CabException {
		this.CE.A4(0, 256);
		this.CE.A4(256, 256 + this.D4 * 8);
		this.CF.A4(0, 249);
		if (this.CE.CE['è'] != 0)
			this.EA = true;
		this.CE.A5();
		this.CF.A5();
	}

	private void C2(int i) throws CabCorruptException {
		int j = this.DA;
		int k = this.D3;
		byte[] abyte0 = this.DE;
		int l = this.DB;
		int i1 = this.DC;
		int j1 = this.DD;
		do {
			int k1 = this.CE.B5();
			if (k1 < 256) {
				abyte0[j] = (byte) k1;
				j = j + 1 & k;
				i--;
			} else {
				k1 -= 256;
				int i2 = k1 & 0x7;
				if (i2 == 7)
					i2 += this.CF.B5();
				int j2 = k1 >>> 3;
				int l1;
				if (j2 > 2) {
					int k2 = this.CC[j2];
					l1 = this.CD[j2];
					if (k2 > 3) {
						l1 += (CB(k2 - 3) << 3) + this.D0.B5();
					} else if (k2 == 3) {
						l1 += this.D0.B5();
					} else if (k2 != 0)
						l1 += CB(k2);
					else
						l1 = 1;
					j1 = i1;
					i1 = l;
					l = l1;
				} else {
					if (j2 == 0) {
						l1 = l;
					} else if (j2 == 1) {
						l1 = i1;
						i1 = l;
						l = l1;
					} else {
						l1 = j1;
						j1 = l;
						l = l1;
					}
				}
				i2 += 2;
				i -= i2;
				do {
					abyte0[j] = abyte0[(j - l1 & k)];
					j = j + 1 & k;
					i2--;
				} while (i2 > 0);
			}
		} while (i > 0);
		if (i != 0) {
			throw new CabCorruptException();
		}

		this.DB = l;
		this.DC = i1;
		this.DD = j1;
		this.DA = j;
	}

	private void C3(byte[] abyte0, int i, int j) {
		if ((j <= 6) || (!this.EA)) {
			this.E8 += j;
			return;
		}
		int i1 = this.E8;
		int j1 = this.E7;
		int k1 = 0;
		do {
			this.E9[k1] = abyte0[(i + j - 6 + k1)];
			abyte0[(i + j - 6 + k1)] = -24;
			k1++;
		} while (k1 < 6);
		int l = i;
		int k = i1 + j - 6;
		while (true) {
			while (abyte0[(l++)] != -24)
				i1++;
			if (i1 >= k)
				break;
			k1 = abyte0[l] & 0xFF | (abyte0[(l + 1)] & 0xFF) << 8
					| (abyte0[(l + 2)] & 0xFF) << 16
					| (abyte0[(l + 3)] & 0xFF) << 24;
			if (k1 >= 0) {
				if (k1 < j1) {
					int l1 = k1 - i1;
					abyte0[l] = (byte) (l1 & 0xFF);
					abyte0[(l + 1)] = (byte) (l1 >>> 8 & 0xFF);
					abyte0[(l + 2)] = (byte) (l1 >>> 16 & 0xFF);
					abyte0[(l + 3)] = (byte) (l1 >>> 24);
				}
			} else if (k1 >= -i1) {
				int i2 = k1 + this.E7;
				abyte0[l] = (byte) (i2 & 0xFF);
				abyte0[(l + 1)] = (byte) (i2 >>> 8 & 0xFF);
				abyte0[(l + 2)] = (byte) (i2 >>> 16 & 0xFF);
				abyte0[(l + 3)] = (byte) (i2 >>> 24);
			}
			l += 4;
			i1 += 5;
		}
		this.E8 = (k + 6);
		k1 = 0;
		do {
			abyte0[(i + j - 6 + k1)] = this.E9[k1];
			k1++;
		} while (k1 < 6);
	}

	private int C4() {
		if (this.E2 < this.E3) {
			int i = this.E4[this.E2] & 0xFF
					| (this.E4[(this.E2 + 1)] & 0xFF) << 8;
			this.E2 += 2;
			return i;
		}

		this.E5 = true;
		this.E2 = 0;
		return 0;
	}

	int CB(int i) {
		int j = this.EC >>> 32 - i;
		CA(i);
		return j;
	}

	private void C5() {
		int i = 4;
		int j = 1;
		do {
			this.CC[i] = j;
			this.CC[(i + 1)] = j;
			i += 2;
			j++;
		} while (j <= 16);
		do
			this.CC[(i++)] = 17;
		while (i < 51);
		i = -2;
		for (int k = 0; k < this.CC.length; k++) {
			this.CD[k] = i;
			i += (1 << this.CC[k]);
		}
	}

	private void C6(int i) throws CabCorruptException {
		if ((this.E2 + i > this.E3) || (this.DA + i > this.D2)) {
			throw new CabCorruptException();
		}

		this.EA = true;
		System.arraycopy(this.E4, this.E2, this.DE, this.DA, i);
		this.E2 += i;
		this.DA += i;
	}

	public void init(int i) throws CabException {
		this.D2 = (1 << i);
		this.D3 = (this.D2 - 1);
		reset(i);
	}

	private int C7() {
		int i = this.E4[this.E2] & 0xFF | (this.E4[(this.E2 + 1)] & 0xFF) << 8
				| (this.E4[(this.E2 + 2)] & 0xFF) << 16
				| (this.E4[(this.E2 + 3)] & 0xFF) << 24;
		this.E2 += 4;
		return i;
	}

	private void C8() throws CabException {
		this.D0.A3();
		this.D0.A5();
	}

	private void C9(int i) {
		this.EC <<= i;
		this.E6 -= i;
		if (this.E6 <= 0) {
			this.EC |= C4() << -this.E6;
			this.E6 += 16;
			if (this.E6 <= 0) {
				this.EC |= C4() << -this.E6;
				this.E6 += 16;
			}
		}
	}
}
