package xap.lui.core.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringSerializer {


	public static String serializeObjectByBase64(Object parameter) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(parameter);
			oos.close();
			byte[] data = baos.toByteArray();
			BASE64Encoder encoder = new BASE64Encoder();
			String base64 = encoder.encode(data);
			return base64.replace('\n', '%').replace('\r', '?');
		} catch (IOException e) {
		}
		return null;
	}

	public static Object deserializeObjectByBase64(String base64) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] data = decoder.decodeBuffer(base64.replace('%', '\n').replace('?', '\r'));
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Throwable e) {
		}
		return null;
	}

}
