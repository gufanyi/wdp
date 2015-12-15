package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public final class CabDecoder {
	private cab22 A3;
	private CabDecoderInterface A4;
	cab7 A5;
	CabFolderEntry[] AA;
	CabFileEntry[] B5;

	public Enumeration entries() {
		return new CabEnumerator(this, false);
	}

	public Enumeration entries(boolean flag) {
		return new CabEnumerator(this, flag);
	}

	public CabDecoder(InputStream inputstream,
			CabDecoderInterface cabdecoderinterface) throws CabException,
			IOException {
		this.A4 = cabdecoderinterface;
		this.A3 = new cab22(inputstream);
		A2();
	}

	private void A2() throws CabException, IOException {
		this.A5 = new cab7(this.A4);
		this.A5.A3(this.A3);
		if (this.A5.C9 <= 2147483647L)
			this.A3.mark((int) this.A5.C9);
		this.AA = new CabFolderEntry[this.A5.C6];
		for (int i = 0; i < this.A5.C6; i++) {
			this.AA[i] = new CabFolderEntry();
			this.AA[i].A2(this.A3);
		}

		// CabFileEntry设置
		this.B5 = new CabFileEntry[this.A5.C7];
		this.A3.seek(this.A5.C5);
		for (int j = 0; j < this.A5.C7; j++) {
			this.B5[j] = new CabFileEntry();
			this.B5[j].A3(this.A3);
		}

		if (this.A5.C9 <= 2147483647L)
			this.A3.mark((int) this.A5.C9);
	}

	public void extract() throws CabException, IOException {
		int i = -1;
		int j = 0;
		int k = 0;
		boolean flag = false;
		cab1 cab1_1 = new cab1(this.A3, this.A5.C8);
		// this.A5.C7是XSN解压后的文件数量（xml、xsf、xsd、xsl、js）
		for (int l = 0; l < this.A5.C7; l++) {
			if (this.B5[l].B5 != i) {
				if (i + 1 >= this.A5.C6)
					throw new CabCorruptException();
				i++;
				flag = false;
				j = 0;
				k = 0;
			}
			// 没有设值成功?
			OutputStream outputstream = this.A4.openOutputStream(this.B5[l]);
			if (outputstream != null) {
				if (!flag) {
					this.A3.seek(this.AA[i].A3);
					cab1_1.A3(this.AA[i].A5);
				}
				flag = true;
				if (j != k) {
					cab21 cab21_1 = new cab21();
					cab1_1.A2(k - j, cab21_1);
					j = k;
				}
				cab1_1.A2(this.B5[l].A5, outputstream);
				this.A4.closeOutputStream(outputstream, this.B5[l], true);
				j = (int) (j + this.B5[l].A5);
			}
			k = (int) (k + this.B5[l].A5);
		}
	}
}
