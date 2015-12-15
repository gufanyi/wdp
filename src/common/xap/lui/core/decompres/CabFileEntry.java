package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public final class CabFileEntry {
	long A5;
	long AA;
	int B5;
	int BA;
	Date C0;
	String C1;
	private Object A4;

	public boolean isHidden() {
		return (this.BA & 0x2) != 0;
	}

	public CabFileEntry() {
		this.C0 = new Date();
	}

	public Date getDate() {
		return this.C0;
	}

	public boolean isReadOnly() {
		return (this.BA & 0x1) != 0;
	}

	public void setDate(Date date) {
		this.C0 = date;
	}

	public void setSystem(boolean flag) {
		if (flag) {
			this.BA |= 4;
			return;
		}

		this.BA &= -5;
	}

	void A3(InputStream inputstream) throws IOException, CabException {
		byte[] abyte0 = new byte[256];
		this.A5 = cab8.A5(cab8.A4(inputstream));
		this.AA = cab8.A4(inputstream);
		this.B5 = cab8.A2(inputstream);
		int j = cab8.A2(inputstream);
		int k = cab8.A2(inputstream);
		this.BA = cab8.A2(inputstream);
		this.C0 = A2(j, k);
		int i = 0;
		for (i = 0; i < abyte0.length; i++) {
			int l = inputstream.read();
			if (l == -1)
				throw new CabCorruptException("EOF reading cffile");
			abyte0[i] = (byte) l;
			if (l == 0) {
				break;
			}

			// int l = inputstream.read();
			//
			// if(0 == l){
			// break;
			// }
			// if(-1 != l){
			// abyte0[i] = (byte) l;
			// }
		}
		if (i >= abyte0.length)
			throw new CabCorruptException("cffile filename not null terminated");
		if ((this.BA & 0x80) != 0) {
			// C1为空？
			this.C1 = cab8.AA(abyte0);
			if (this.C1 == null)
				throw new CabCorruptException("invalid utf8 code");
		} else {
			this.C1 = new String(abyte0, 0, 0, i);
		}
	}

	public void setArchive(boolean flag) {
		if (flag) {
			this.BA |= 32;
			return;
		}

		this.BA &= -33;
	}

	private Date A2(int i, int j) {
		int k = i & 0x1F;
		int l = (i >>> 5) - 1 & 0xF;
		int i1 = (i >>> 9) + 80;
		int j1 = (j & 0x1F) << 1;
		int k1 = j >>> 5 & 0x3F;
		int l1 = j >>> 11 & 0x1F;
		return new Date(i1, l, k, l1, k1, j1);
	}

	public Object getApplicationData() {
		return this.A4;
	}

	public void setApplicationData(Object obj) {
		this.A4 = obj;
	}

	public boolean isSystem() {
		return (this.BA & 0x4) != 0;
	}

	public boolean isArchive() {
		return (this.BA & 0x20) != 0;
	}

	public String getName() {
		return this.C1;
	}

	public long getSize() {
		return this.A5;
	}

	public void setName(String s) {
		this.C1 = s;
	}

	public void setSize(long l) {
		this.A5 = (int) l;
	}

	public void setHidden(boolean flag) {
		if (flag) {
			this.BA |= 2;
			return;
		}

		this.BA &= -3;
	}

	public void setReadOnly(boolean flag) {
		if (flag) {
			this.BA |= 1;
			return;
		}

		this.BA &= -2;
	}
}
