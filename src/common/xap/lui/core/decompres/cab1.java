package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class cab1 implements CabConstants {
	private byte[] A4;
	private byte[] A5;
	private long AA;
	private int B5;
	private int BA;
	private InputStream C0;
	private CabDecompressor C1;
	private cab3 C2;

	void A2(long l, OutputStream outputstream) throws IOException, CabException {
		if (this.AA >= l) {
			outputstream.write(this.A5, this.B5, (int) l);
			this.AA -= l;
			this.B5 = (int) (this.B5 + l);
			return;
		}
		if (this.AA > 0L)
			outputstream.write(this.A5, this.B5, (int) this.AA);
		l -= this.AA;
		this.B5 = 0;
		this.AA = 0L;
		while (l > 0L) {
			this.C2.A3(this.C0, this.A4);
			if (!this.C2.A4(this.A4))
				throw new CabCorruptException("Invalid CFDATA checksum");
			this.C1.decompress(this.A4, this.A5, this.C2.B5, this.C2.BA);
			this.AA = this.C2.BA;
			this.B5 = 0;
			if (this.AA >= l) {
				outputstream.write(this.A5, this.B5, (int) l);
				this.B5 = (int) (this.B5 + l);
				this.AA -= l;
				l = 0L;
			} else {
				outputstream.write(this.A5, this.B5, (int) this.AA);
				l -= this.AA;
				this.B5 = 0;
				this.AA = 0L;
			}
		}
	}

	cab1(InputStream inputstream, int i) {
		this.C0 = inputstream;
		this.AA = 0L;
		this.B5 = 0;
		this.BA = -1;
		this.A5 = new byte[33028];
		this.C2 = new cab3(i);
	}

	void A3(int i) throws CabException {
		int j = i & 0xF;
		int k = (i & 0x1F00) >>> 8;
		if (i == this.BA) {
			this.C1.reset(k);
			return;
		}
		switch (j) {
		case 2:
		default:
			throw new CabException("Unknown compression type " + j);
		case 3:
			this.C1 = new cab13();
			break;
		case 1:
			this.C1 = new cab17();
			break;
		case 0:
			this.C1 = new cab9();
		}

		this.A4 = new byte[32768 + this.C1.getMaxGrowth()];
		this.C1.init(k);
		this.BA = i;
	}
}
