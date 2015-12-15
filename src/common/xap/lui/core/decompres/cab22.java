package xap.lui.core.decompres;

import java.io.IOException;
import java.io.InputStream;

final class cab22 extends InputStream {
	private InputStream A2;
	private long A3;
	private long A4;
	private boolean A5;
	private boolean AA;

	public void close() throws IOException {
		this.A2.close();
	}

	public void reset() throws IOException {
		if (this.AA) {
			this.A2.reset();
			this.A3 = this.A4;
			return;
		}

		throw new IOException();
	}

	cab22(InputStream inputstream) {
		this.A2 = inputstream;
		this.A3 = 0L;
		this.AA = this.A2.markSupported();
		this.A5 = false;
	}

	public int read() throws IOException {
		int i = this.A2.read();
		if (i >= 0)
			this.A3 += 1L;
		return i;
	}

	public int read(byte[] abyte0) throws IOException {
		int i = this.A2.read(abyte0);
		if (i > 0)
			this.A3 += i;
		return i;
	}

	public int read(byte[] abyte0, int i, int j) throws IOException {
		int k = this.A2.read(abyte0, i, j);
		if (k > 0)
			this.A3 += k;
		return k;
	}

	public int available() throws IOException {
		throw new IOException();
	}

	public void mark(int i) {
		if (this.AA) {
			this.A4 = this.A3;
			this.A2.mark(i);
		}
	}

	public long getCurrentPosition() {
		return this.A3;
	}

	public boolean markSupported() {
		return this.AA;
	}

	public void seek(long l) throws IOException {
		if (l < this.A3) {
			if ((this.AA) && (this.A5) && (this.A4 < l)) {
				reset();
				skip(l - this.A3);
				return;
			}

			throw new IOException("Cannot seek backwards");
		}
		if (l > this.A3)
			skip(l - this.A3);
	}

	public long skip(long l) throws IOException {
		long l1 = this.A2.skip(l);
		this.A3 += (int) l1;
		return l1;
	}
}
