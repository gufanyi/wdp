package xap.lui.core.decompres;

abstract interface LZXConstants {
	public static final int PRETREE_NUM_ELEMENTS = 20;
	public static final int NUM_CHARS = 256;
	public static final int SECONDARY_NUM_ELEMENTS = 249;
	public static final int ALIGNED_NUM_ELEMENTS = 8;
	public static final int NUM_PRIMARY_LENGTHS = 7;
	public static final int MIN_MATCH = 2;
	public static final int MAX_MATCH = 257;
	public static final int NUM_REPEATED_OFFSETS = 3;
	public static final int MAX_GROWTH = 6144;
	public static final int E8_DISABLE_THRESHOLD = 32768;
	public static final int BLOCKTYPE_VERBATIM = 1;
	public static final int BLOCKTYPE_ALIGNED = 2;
	public static final int BLOCKTYPE_UNCOMPRESSED = 3;
	public static final int BLOCKTYPE_INVALID = 4;
}
