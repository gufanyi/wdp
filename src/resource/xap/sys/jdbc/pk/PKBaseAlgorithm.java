package xap.sys.jdbc.pk;

public class PKBaseAlgorithm {
	private final static byte MIN_CODE = 48;

	private final static byte MAX_CODE = 90;

	private final int CODE_LENGTH = 14;

	private byte[] pkBaseCodes = new byte[CODE_LENGTH];

	private PKBaseAlgorithm() {
	}

	synchronized public static PKBaseAlgorithm getInstance() {
		PKBaseAlgorithm oidBase = new PKBaseAlgorithm();
		return oidBase;
	}

	public static PKBaseAlgorithm getInstance(String pkBaseStr) {
		PKBaseAlgorithm pkBase = new PKBaseAlgorithm();
		pkBase.setPKBaseCodes(pkBaseStr.getBytes());
		return pkBase;
	}

	public String nextPKBase() {
		for (int i = pkBaseCodes.length - 1; i >= 0; --i) {
			byte code = (byte) (pkBaseCodes[i] + 1);
			boolean carryUp = false;
			byte newCode = code;
			if (code > MAX_CODE) {
				newCode = MIN_CODE;
				carryUp = true;
			}
			if (newCode == 58) {
				newCode = 65;
			}
			pkBaseCodes[i] = newCode;

			if (!carryUp)
				break;
		}

		return new String(pkBaseCodes);
	}

	/**
	 * 设置 pkBaseCodes
	 * 
	 * @return
	 */
	private void setPKBaseCodes(byte[] pkBaseCodes) {
		if (pkBaseCodes.length != CODE_LENGTH)
			return;
		System.arraycopy(pkBaseCodes, 0, this.pkBaseCodes, 0, CODE_LENGTH);
	}

}