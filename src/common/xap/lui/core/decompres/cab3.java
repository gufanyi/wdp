package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;

final class cab3 {
	private int A5;
	int B5;
	int BA;
	private int AA;

	cab3(int i) {
		this.AA = i;
	}

	void A3(InputStream inputstream, byte[] abyte0) throws IOException,
			CabException {
		this.A5 = cab8.A4(inputstream);
		this.B5 = cab8.A2(inputstream);
		this.BA = cab8.A2(inputstream);
		if (this.B5 > abyte0.length)
			throw new CabCorruptException("Corrupt cfdata record");
		if (this.AA != 0)
			inputstream.skip(this.AA);
		cab8.A3(inputstream, abyte0, 0, this.B5);
	}

	private int A2(byte[] abyte0) {
		byte[] abyte1 = new byte[4];
		abyte1[0] = (byte) (this.B5 & 0xFF);
		abyte1[1] = (byte) (this.B5 >>> 8 & 0xFF);
		abyte1[2] = (byte) (this.BA & 0xFF);
		abyte1[3] = (byte) (this.BA >>> 8 & 0xFF);
		return cab2.A2(abyte0, this.B5, cab2.A2(abyte1, 4, 0));
	}

	boolean A4(byte[] abyte0) {
		return A2(abyte0) == this.A5;
	}
}
