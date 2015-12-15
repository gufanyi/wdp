package xap.lui.core.filemgr;



/**
 * 
 * @author lisw
 * 上传文件扩展操作
 * 如果先上传文件再执行其他操作，会形成垃圾文件文件；为解决这个问题,增加了扩展机制
 */
public interface IFileUploadExtender {
	/**
	 * 扩展执行
	 * @param req 请求对象
	 * @param filevo 文件对象
	 */
	//public void extend(MultipartHttpServletRequest req,String filepk) throws LuiRuntimeException;
	
	public String[] getRetValues();
}
