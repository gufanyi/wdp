package xap.lui.core.decompres;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface CabDecoderInterface extends CabProgressInterface {
	public abstract InputStream openCabinet(String paramString1, String paramString2);

	public abstract boolean closeOutputStream(OutputStream paramOutputStream, CabFileEntry paramCabFileEntry, boolean paramBoolean);

	public abstract OutputStream openOutputStream(CabFileEntry paramCabFileEntry);

	public abstract boolean reservedAreaData(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);
}
