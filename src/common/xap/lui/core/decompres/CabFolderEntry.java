package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;

public final class CabFolderEntry implements CabConstants {
	long A3;
	int A4;
	int A5;

	public int getCompressionMethod() {
		return this.A5 & 0xF;
	}

	public CabFolderEntry() {
		this.A5 = 0;
	}

	void A2(InputStream inputstream) throws IOException {
		this.A3 = cab8.A4(inputstream);
		this.A4 = cab8.A2(inputstream);
		this.A5 = cab8.A2(inputstream);
	}

	public int getCompressionWindowSize() {
		if (this.A5 == 0)
			return 0;
		if (this.A5 == 1) {
			return 16;
		}
		return (this.A5 & 0x1F00) >>> 8;
	}

	public String compressionToString() {
		switch (getCompressionMethod()) {
		default:
			return "UNKNOWN";
		case 0:
			return "NONE";
		case 1:
			return "MSZIP";
		case 2:
			return "QUANTUM:" + Integer.toString(getCompressionWindowSize());
		case 3:
		}
		return "LZX:" + Integer.toString(getCompressionWindowSize());
	}

	public void setCompression(int i, int j) throws CabException {
		this.A5 = (j << 8 | i);
	}
}
