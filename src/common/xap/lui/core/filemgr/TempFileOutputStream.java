package xap.lui.core.filemgr;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TempFileOutputStream extends OutputStream {
	private FileChannel channel;
	public TempFileOutputStream(FileChannel _channel){
		channel = _channel;
	}
	@Override
	public void write(int b) throws IOException {
		channel.write(ByteBuffer.wrap(new byte[]{(byte) b}));

	}
	@Override
	public void write(byte b[]) throws IOException{
		channel.write(ByteBuffer.wrap(b));
	}
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if (b == null) {
		    throw new NullPointerException();
		} else if ((off < 0) || (off > b.length) || (len < 0) ||
			   ((off + len) > b.length) || ((off + len) < 0)) {
		    throw new IndexOutOfBoundsException();
		} else if (len == 0) {
		    return;
		}
		byte[] newb = new byte[len];
		for (int i = 0 ; i < len ; i++) {
			newb[i] = b[off+i];
		}
		write(newb);
	}

}
