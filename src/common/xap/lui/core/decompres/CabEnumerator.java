package xap.lui.core.decompres;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public final class CabEnumerator implements Enumeration {
	private int A2;
	private int A3;
	private CabDecoder A4;
	private boolean A5;
	private int AA;

	public Object nextElement() {
		if (!this.A5) {
			if (this.A2 < this.A4.A5.C7) {
				return this.A4.B5[(this.A2++)];
			}
			throw new NoSuchElementException();
		}
		if (this.A4.B5[this.A2].B5 != this.AA) {
			this.AA = this.A4.B5[this.A2].B5;
			if (this.A3 < this.A4.AA.length)
				return this.A4.AA[(this.A3++)];
		}
		if (this.A2 < this.A4.A5.C7) {
			return this.A4.B5[(this.A2++)];
		}
		throw new NoSuchElementException();
	}

	CabEnumerator(CabDecoder cabdecoder, boolean flag) {
		this.A2 = 0;
		this.A3 = 0;
		this.A4 = cabdecoder;
		this.A5 = flag;
		this.AA = -2;
	}

	public boolean hasMoreElements() {
		return this.A2 < this.A4.A5.C7;
	}
}
