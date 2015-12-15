package xap.sys.jdbc.kernel.unidb.dbtype;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Clob类型
 * 
 * ClobParamType类的说明
 */
public class ClobParamType implements SQLParamType {
    private static final long serialVersionUID = 1L;
    String s = null;

    int length = 0;

    private transient Reader reader = null;

    public ClobParamType(String s) {
	try {
	    this.s = s;
	    length = s.getBytes("iso8859-1").length;
	} catch (UnsupportedEncodingException e) {
	}
    }

    public ClobParamType(Reader read, int length) {
	this.reader = read;
	this.length = length;
    }

    public Reader getReader() {
	if (reader == null) {
	    reader = new StringReader(s);
	}
	return reader;
    }

    public int getLength() {
	return length;
    }

}
