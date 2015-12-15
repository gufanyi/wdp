package xap.lui.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.control.ResourceFrom;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.RuntimeEnv;

public class ContextResourceUtil {

	public static String FileSeperator = File.separator;
	public static String DefalutPortalAppPath = WdpWebAppContainer.getInstace().getWebApp(WdpWebAppContainer.DefaultPortlAppName);
	public static String DefalutWFWAppPath = WdpWebAppContainer.getInstace().getWebApp(WdpWebAppContainer.DefaultWFWAppName);

	public static String getLastModified(String configPath, boolean isAbsloute) {
		try {
			String str = ContextResourceUtil.getResourceAsString(configPath, isAbsloute);
			CRC32 crc32 = new CRC32();
			crc32.update(str.getBytes());
			return String.valueOf(crc32.getValue());
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return "-1";
	}

	public static String getLuiAppPath() {
		String path = null;
		if (absolutePathMode()) {
			path = RuntimeEnv.getInstance().getNCHome() + "/" + WdpWebAppContainer.DefaultWFWAppName;
		} else {
			path = LuiRuntimeContext.getServletContext().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}

	public static boolean isAbsolutePath() {
		ResourceFrom resouceForm = LuiRuntimeContext.getResourceFrom();
		if (ContextResourceUtil.isFreeBillOrPersona(resouceForm)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isFreeBillOrPersona(ResourceFrom resoureFrom) {
		if (ResourceFrom.classjar.equals(resoureFrom)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isDesignerNode(String pageId) {
		String[] nodes = new String[] { "combocfg", "combodatacfg", "commitrule", "dscfg", "fieldmgr", "formula", "groupcolumns", "guided", "menumgr", "refcfg", "rightMenu", "toolbarmgr", "top", "treedatasetting", "uistate", "pa", };
		for (String node : nodes) {
			if (node.equalsIgnoreCase(pageId)) {
				return true;
			}
		}
		return false;
	}

	public static String getPortalAppPath() {
		String path = null;
		if (absolutePathMode()) {
			path = RuntimeEnv.getInstance().getNCHome() + "/" + WdpWebAppContainer.DefaultPortlAppName;
		} else {
			path = DefalutPortalAppPath;
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}

	public static String getCurrentAppPath() {
		String path = null;
		if (absolutePathMode()) {
			String rootPath = LuiRuntimeContext.getRootPath();
			path = RuntimeEnv.getInstance().getNCHome() + "/" + WdpWebAppContainer.DefaultWebContainerName + "/" + rootPath + "/";
		} else {
			path = LuiRuntimeContext.getServletContext().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}
	public static Set<String> getResourcePaths(String dirPath) {
		if (absolutePathMode()) {
			String absPath = getCurrentAppPath() + "/" + dirPath;
			File f = new File(absPath);
			String[] files = f.list();
			Set<String> fileSet = new HashSet<String>();
			if (files != null && files.length != 0) {
				for (int i = 0; i < files.length; i++) {
					fileSet.add(dirPath + "/" + files[i]);
				}
			}
			return fileSet;
		}
		Set<String> set1 = LuiRuntimeContext.getServletContext().getResourcePaths(dirPath);
		String absPath = DefalutPortalAppPath + dirPath;
		File f = new File(absPath);
		String[] files = f.list();
		Set<String> fileSet = new HashSet<String>();
		if (files != null && files.length != 0) {
			for (int i = 0; i < files.length; i++) {
				fileSet.add(dirPath + "/" + files[i]);
			}
		}
		if (set1 != null) {
			fileSet.addAll(set1);
		}
		return fileSet;
	}

	public static String getLuiFolderPath(String pageId) {
		ServletContext servletContext = LuiRuntimeContext.getServletContext();
		String contextPath = servletContext.getRealPath("/");
		ResourceFrom resouceForm = LuiRuntimeContext.getResourceFrom();
		String folderPath = null;
		if (ResourceFrom.nodedef.equals(resouceForm)) {
			String version = LuiRuntimeContext.getVesion();
			folderPath = contextPath + "/lui/nodes/" + pageId + "/" + version + "/";
		}
		if (ResourceFrom.persona.equals(resouceForm)) {
			String pesonaCode = LuiRuntimeContext.getPersonaCode();
			folderPath = contextPath + "/lui/nodes/" + pageId + "/" + pesonaCode + "/";

		}
		if (ResourceFrom.classjar.equals(resouceForm)) {
			folderPath = "/lui/nodes/" + pageId;
		}
		return folderPath;
	}

	public static String getLuiPropertyPath(String pageId) {
		return ContextResourceUtil.getLuiFolderPath(pageId) + "/node.properties";
	}

	public static String getLuiPageLayOutPath(String pageId) {
		return ContextResourceUtil.getLuiViewLayoutPath(pageId, null);
	}

	public static String getLuiViewLayoutPath(String pageId, String viewId) {
		String folderPath = ContextResourceUtil.getLuiFolderPath(pageId);
		String path0 = null;
		if (StringUtils.isNotBlank(viewId)) {
			path0 = folderPath + "/" + pageId + "." + viewId + ".layout.xml";
		} else {
			path0 = folderPath + "/" + pageId + ".page.layout.xml";
		}
		return path0;
	}

	public static String getLuiPagePartMetaPath(String pageId) {
		return ContextResourceUtil.getLuiViewpartMetaPath(pageId, null);
	}

	public static String getLuiViewpartMetaPath(String pageId, String viewId) {
		String folderPath = ContextResourceUtil.getLuiFolderPath(pageId);
		String path0 = null;
		if (StringUtils.isNotBlank(viewId)) {
			path0 = folderPath + "/" + pageId + "." + viewId + ".view.xml";
		} else {
			path0 = folderPath + "/" + pageId + ".page.meta.xml";
		}
		return path0;
	}

	public static InputStream getResourceAsStream(String filePath) {
		if (ContextResourceUtil.isFreeBillOrPersona(LuiRuntimeContext.getResourceFrom())) {
			return ContextResourceUtil.getResourceAsStream(filePath, true);
		} else {
			return ContextResourceUtil.getResourceAsStream(filePath, false);
		}

	}

	public static InputStream getResourceAsStream(String filePath, boolean isAbsolute) {
		InputStream input = null;
		try {
			if (isAbsolute) {
				File file = new File(filePath);
				if (file.exists()) {
					input = new FileInputStream(file);
				}
				return input;
			}

			ClassLoader loader = ContextResourceUtil.class.getClassLoader();
			Thread.currentThread().getContextClassLoader();
			if (filePath.startsWith("/")) {
				filePath = filePath.substring(1);
			}
			URL url = loader.getResource(filePath);
			if (url == null) {
				return null;
			}
			input = loader.getResourceAsStream(filePath);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return input;
	}

	public static String getResourceAsString(String filePath, boolean isAbsolute) {
		InputStream input = ContextResourceUtil.getResourceAsStream(filePath, isAbsolute);
		String str = ContextResourceUtil.inputStream2String(input);
		return str;
	}

	public static String inputStream2String(InputStream input) {
		StringBuffer out = new StringBuffer();
		InputStreamReader inread = null;
		try {
			inread = new InputStreamReader(input, "UTF-8");
			char[] b = new char[4096];
			for (int n; (n = inread.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return out.toString();
	}

	/**
	 * 打开输出流，只限于用ServletContext能找到
	 * 
	 * @param absFilePath
	 * @param fullPath
	 *            true表示从全路径获得文件
	 * @return
	 */
	public static Writer getOutputStream(String filePath) {
		return getOutputStream(filePath, "UTF-8");
	}

	/**
	 * 以特定编码打开输出流
	 * 
	 * @param filePath
	 * @param rootPath
	 * @param charset
	 * @return
	 */
	public static Writer getOutputStream(String filePath, String charset) {
		File f = null;
		String absPath = getCurrentAppPath() + filePath;
		f = new File(absPath);
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			return new OutputStreamWriter(new FileOutputStream(f), charset);
		} catch (FileNotFoundException e) {
			LuiLogger.error(e.getMessage(), e);
			return null;
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 判断资源文件是否存在,目前采用绝对路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean resourceExist(String filePath, boolean isFindPortal) {
		String absPath = getCurrentAppPath() + filePath;
		File f = new File(absPath);
		if (f.exists()) {
			return true;
		}
		if (isFindPortal) {
			absPath = getPortalAppPath() + filePath;
			f = new File(absPath);
			if (f.exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean resourceExist(String filePath) {
		return resourceExist(filePath, true);
	}

	/**
	 * 此方法用来获取Lui下的路径资源
	 * 
	 * @param filePath
	 * @return
	 */
	public static File getLuiFile(String filePath) {
		String path = getLuiAppPath() + "/" + filePath;
		return new File(path);
	}

	/**
	 * 此方法用来判断是否需要使用绝对路径方式来访问资源。一般情况下，websphere，weblogic，nc集群及设计态，
	 * 由于通过ServletContext无法取得对应资源，所以采用绝对路径方式
	 * 
	 * @return
	 */
	private static boolean absolutePathMode() {
		return false;
	}

	public static File getCntWebAppFile(String filePath) {
		String cntAppPath = System.getProperty("user.home");
		String fullFilePath = cntAppPath + FileSeperator + filePath;
		File file = new File(fullFilePath);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}
}
