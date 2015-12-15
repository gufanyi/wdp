package xap.lui.psn.services;

import xap.lui.psn.pamgr.PaBusinessException;


/**
 * @author zuopf
 * @date 2012-9-20 上午09:09:57
 */
public interface IInfoPathDecompressionService {

	/**
	 * 解压infopath表单模板文件XSN,获得xsl文件
	 * @param file
	 * @throws Exception
	 */
	public String getInfopathXSL(String filePath) throws PaBusinessException;
}
