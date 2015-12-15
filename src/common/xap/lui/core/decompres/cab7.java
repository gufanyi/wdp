package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;

final class cab7 implements CabConstants {
	private int A4;
	private int A5;
	int C5;
	private int AA;
	private int B5;
	int C6;
	int C7;
	private int BA;
	private int C0;
	private int C1;
	private int C2;
	private int C3;
	int C8;
	private CabDecoderInterface C4;
	long C9;

	cab7(CabDecoderInterface cabdecoderinterface) {
		this.C2 = 0;
		this.C3 = 0;
		this.C8 = 0;
		this.C4 = cabdecoderinterface;
	}

	void A3(InputStream inputstream) throws IOException, CabException {
		int i = inputstream.read();
		int j = inputstream.read();
		int k = inputstream.read();
		int l = inputstream.read();
		if ((i != 77) || (j != 83) || (k != 67) || (l != 70))
			throw new CabCorruptException("Missing header signature");
		this.A4 = cab8.A4(inputstream);
		this.C9 = cab8.A5(cab8.A4(inputstream));
		this.A5 = cab8.A4(inputstream);
		this.C5 = cab8.A4(inputstream);
		this.AA = cab8.A4(inputstream);
		this.B5 = cab8.A2(inputstream);
		this.C6 = cab8.A2(inputstream);
		this.C7 = cab8.A2(inputstream);
		this.BA = cab8.A2(inputstream);
		this.C0 = cab8.A2(inputstream);
		this.C1 = cab8.A2(inputstream);
		if ((this.BA & 0x4) != 0)
			A2(inputstream);
		if ((this.BA & 0x3) != 0)
			throw new CabException("Spanned cabinets not supported");
		if (this.C2 != 0) {
			if (this.C4.reservedAreaData(1, null, this.C2, null, 0)) {
				byte[] abyte0 = new byte[this.C2];
				if (this.C2 != 0)
					cab8.A3(inputstream, abyte0, 0, this.C2);
				this.C4.reservedAreaData(1, abyte0, this.C2, null, 0);
				return;
			}
			inputstream.skip(this.C2);
		}
	}

	private void A2(InputStream inputstream) throws IOException {
		this.C2 = cab8.A2(inputstream);
		this.C3 = inputstream.read();
		this.C8 = inputstream.read();
	}
}
