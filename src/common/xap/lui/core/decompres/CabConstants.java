package xap.lui.core.decompres;

public abstract interface CabConstants {
	public static final int CAB_BLOCK_SIZE = 32768;
	public static final int COMPRESSION_NONE = 0;
	public static final int COMPRESSION_MSZIP = 1;
	public static final int COMPRESSION_QUANTUM = 2;
	public static final int COMPRESSION_LZX = 3;
	public static final int RESERVED_CFHEADER = 1;
	public static final int RESERVED_CFFOLDER = 2;
	public static final int RESERVED_CFDATA = 3;
	public static final int CAB_PROGRESS_INPUT = 1;
}
