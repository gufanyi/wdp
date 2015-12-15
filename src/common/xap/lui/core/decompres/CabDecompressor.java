package xap.lui.core.decompres;

abstract interface CabDecompressor extends CabConstants {
	public abstract void reset(int paramInt) throws CabException;

	public abstract int getMaxGrowth();

	public abstract void decompress(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
			throws CabException;

	public abstract void init(int paramInt) throws CabException;
}
