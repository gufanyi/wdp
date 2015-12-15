package xap.lui.core.filemgr;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.sso.SessionBean;
import xap.lui.core.util.RuntimeEnv;
import xap.lui.core.vos.LuiFileVO;
import xap.mw.coreitf.d.FDateTime;

/**
 * LUI文件系统
 * 
 * @author licza
 * 
 * @modify lisw 增加系统预置filemanager的方法
 * 
 * @modify lisw 将 FileManager 提高至系统对外公开接口
 */
public class FileManager {
	public final static String FILE_PK = "filepk";
	private final static String FILEMANAGER = "filemanager";

	/**
	 * 是否客户机
	 * 
	 * @return
	 */
	public boolean isClient() {
		return Boolean.TRUE;
	}

	/**
	 * 获得文件存储路径
	 * 
	 * @return
	 */
	public String getFileStore() {
		String fileStore = "$/resources/wfw/attachment";
		/**
		 * 当前ctx配置
		 */
		if (fileStore.startsWith("$"))
			fileStore = RuntimeEnv.getInstance().getNCHome() + fileStore.substring(1);

		return fileStore;
	};

	/**
	 * 获得上传服务器链接
	 * 
	 * @return
	 * 
	 */
	public String getFileServerURL() {
		String fileServerURL = LuiRuntimeContext.getModelServerConfig().getConfigValue("fileserver");
		if (StringUtils.isEmpty(fileServerURL)) {
			throw new LuiRuntimeException("未配置文件存储路径!");
		}
		return fileServerURL;
	};

	/**
	 * 获得LUI文件系统
	 * 
	 * @return
	 */
	public ILuiFileSystem getFileSystem() {
		return new DocumentCenterFileSystem(getFileStore());
	}

	public ILuiFileSystem getFileSystem(LuiFileVO vo) {
		return getFileSystem();
	}

	/**
	 * 上传文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param billType
	 *            业务类型
	 * @param billItem
	 *            业务标志
	 * @param in
	 *            入流
	 * @return
	 * @throws Exception
	 */
	public LuiFileVO upload(String fileName, String billType, String billItem, long size, InputStream in) throws Exception {
		return upload(fileName, billType, billItem, size, in, Boolean.FALSE.booleanValue());
	}

	/**
	 * 
	 * 上传文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param billType
	 *            业务类型
	 * @param billItem
	 *            业务标志
	 * @param in
	 *            入流
	 * @param override是否覆盖
	 * @return
	 * @throws Exception
	 */
	public LuiFileVO upload(String fileName, String billType, String billItem, long size, InputStream in, boolean override) throws Exception {
		SessionBean ses = LuiRuntimeContext.getSessionBean();
		String pk_user = null;
		if (ses != null)
			pk_user = ses.getPk_user();
		LuiFileVO vo = new LuiFileVO();
		vo.setFilename(fileName);
		vo.setPk_billitem(billItem);
		vo.setPk_billtype(billType);
		vo.setCreator(pk_user);
		vo.setCreattime(new FDateTime());
		vo.setLastmodifyer(pk_user);
		vo.setLastmodifytime(new FDateTime());
		vo.setCreatestatus("0");
		try {
			vo.setFilesize(size);
			vo.setFiletypo(getFileType(fileName));
			vo.setFilemgr(this.getClass().getName());
			return vo;
		} catch (Throwable e) {
			throw e;
		} finally {
			if (StringUtils.isNotBlank(vo.getPk_luifile()) && in != null)
				upload(vo, in);
		}
	}

	public void upload(LuiFileVO vo, InputStream in) throws Exception {
		getFileSystem(vo).upload(vo, in);
	}

	/**
	 * 文件是否存在
	 * 
	 * @param fileNo
	 * @return
	 * @throws Exception
	 */
	public boolean exist(String fileNo) throws Exception {
		return true;
	}

	/**
	 * 根据文件后缀名获得文件类型
	 * 
	 * @param fileName
	 * @return
	 */
	public String getFileType(String fileName) {
		String filetypo = "NaN";
		int beginIndex = fileName.lastIndexOf(".");
		if (beginIndex > 0) {
			filetypo = fileName.substring(beginIndex + 1);
		}
		return filetypo;
	}

}
