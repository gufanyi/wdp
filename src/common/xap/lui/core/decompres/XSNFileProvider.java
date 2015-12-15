package xap.lui.core.decompres;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;

/**
 * @author zuopf
 * @date 2012-9-12 下午01:01:07
 */
public class XSNFileProvider {

	private static final String VIEW1_XSL = "view1.xsl";

	public static String loadXSL(InputStream fin) {
		// 初始化
		DecompressionRuntime.getInstance();
		// 解压cab
		byte[] bytes = DecompressionRuntime.getInstance().readResourceData(fin);
		String xslString = null;
		ByteArrayInputStream bin = null;
		try {
			bin = new ByteArrayInputStream(bytes);
			IFormResoureProvider ff = new CabFileResourceProvider(bin);
			// 抽取xsl
			xslString = ff.loadResource(VIEW1_XSL);
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			try {
				fin.close();
			} catch (Throwable e) {
				throw new LuiRuntimeException(e.getMessage());
			}
		}
		return xslString;
	}

}
