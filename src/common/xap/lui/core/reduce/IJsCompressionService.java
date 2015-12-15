package xap.lui.core.reduce;


/**
 * Js代码压缩服务类
 * @author gd 2010-4-23
 * @version NC6.0
 */
public interface IJsCompressionService {
	public String compressJs(String[] jsFiles);
	public String compressCss(String[] cssFiles);
	public String noCompressJs(String[] jsFiles);
	public String noCompressCss(String[] cssFiles);
}
