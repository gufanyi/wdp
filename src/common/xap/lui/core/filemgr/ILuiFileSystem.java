package xap.lui.core.filemgr;

import java.io.InputStream;
import java.io.OutputStream;

import xap.lui.core.vos.LuiFileVO;

/**
 * LUI文件操作接口
 * 
 * @author licza
 * 
 */
public interface ILuiFileSystem {
	/**
	 * 下载文件
	 * 
	 * @param FileVO：文档对象信息
	 * @param out：读取的数据写入out流中
	 * @return
	 */
	public Object download(LuiFileVO fileVO, OutputStream out) throws Exception;

	/**
	 * 上载文件
	 * 
	 * @param fileVO
	 * @param input
	 * @throws Exception
	 */
	public void upload(LuiFileVO fileVO, InputStream input) throws Exception;

	/**
	 * 删除文件
	 * 
	 * @param fileVO
	 * @throws Exception
	 */
	public void deleteFile(LuiFileVO fileVO) throws Exception;
	

	/**
	 * 文件系统中是否存在此文件
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean existInFs(String fileNo) throws Exception;
	
 }
