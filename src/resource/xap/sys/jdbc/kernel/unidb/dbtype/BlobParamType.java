package xap.sys.jdbc.kernel.unidb.dbtype;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

/**
 * Blob类型
 * 
 *
 */
public class BlobParamType implements SQLParamType {

	private static final long serialVersionUID = 1L;

	Object blob = null;

	byte bytes[] = null;

	int length = -1;

	private transient InputStream input = null;

	public BlobParamType(Object blob) {
		this.blob = blob;
	}

	public BlobParamType(byte[] bytes) {
		this.bytes = bytes;
		this.length = bytes.length;
	}

	public BlobParamType(InputStream input, int length) {
		this.input = input;
		this.length = length;
	}

	public Object getBlob() {
		return blob;
	}

	public byte[] getBytes() {
		if (bytes == null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(blob);
				oos.flush();
				baos.flush();
				bytes = baos.toByteArray();
			} catch (IOException e) {
			}
		}
		return bytes;
	}

	public InputStream getInputStream() {
		if (input == null)
			input = new ByteArrayInputStream(getBytes());
		return input;
	}

	public int getLength() {
		if (length == -1)
			length = getBytes().length;
		return length;
	}
}
