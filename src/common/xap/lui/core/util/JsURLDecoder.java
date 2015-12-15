package xap.lui.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;


public class JsURLDecoder {
	public static String decode(String source, String enc){
		try {
			return URLDecoder.decode(source, enc);
		} catch (UnsupportedEncodingException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		}
	}
}
