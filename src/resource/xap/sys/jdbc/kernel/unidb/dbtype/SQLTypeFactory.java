/**
 * 
 * 
 *
 *
 * ${NAME}类的说明
 */
package xap.sys.jdbc.kernel.unidb.dbtype;

import java.io.InputStream;
import java.io.Reader;

abstract public class SQLTypeFactory {
    public static SQLParamType getNullType(int type) {
	return new NullParamType(type);
    }

    public static SQLParamType getBlobType(Object obj) {
	return new BlobParamType(obj);
    }

    public static SQLParamType getBlobType(byte[] bytes) {
	return new BlobParamType(bytes);
    }

    public static SQLParamType getBlobType(InputStream input, int length) {
	return new BlobParamType(input, length);
    }

    public static SQLParamType getClobType(String s) {
	return new ClobParamType(s);
    }

    public static SQLParamType getClobType(Reader reader, int length) {
	return new ClobParamType(reader, length);
    }
}
