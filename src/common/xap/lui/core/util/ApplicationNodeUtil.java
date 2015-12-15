package xap.lui.core.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xap.lui.core.cache.LUICache;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.Application;


public final class ApplicationNodeUtil {
	
	private static final String APPLICATION = "_APPLICATION";
	
	private static Map<String, String> dirPathMap = new HashMap<String, String>();
	
	public static void refresh(String nodeDir) {
		String ctxPath = LuiRuntimeContext.getRootPath();
		refresh(nodeDir, ctxPath);
	}
	
	/**
	 * 仅供开发工具调用
	 */
	public static void refresh() {
		Iterator<Entry<String, String>> eit = dirPathMap.entrySet().iterator();
		while(eit.hasNext()){
			Entry<String, String> entry = eit.next();
			refresh(entry.getKey(), entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshApplication(String ctxPath, Application appConf){
		Iterator<Entry<String, String>> eit = dirPathMap.entrySet().iterator();
		while(eit.hasNext()){
			Entry<String, String> entry = eit.next();
			if(entry.getValue() != null && entry.getValue().equals(ctxPath)){
				refresh(entry.getKey(), entry.getValue());
				break;
			}
		}
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, Application> nodeMetaMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, Application>();
			cache.put(cacheKey + APPLICATION, nodeMetaMap);
		}
		nodeIdMap.put(appConf.getId(), appConf.getRealPath());
		nodeMetaMap.put(appConf.getId(), appConf);
	}
	
	@SuppressWarnings("unchecked")
	private static void refresh(String nodeDir, String ctxPath){
		dirPathMap.put(nodeDir, ctxPath);
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, Application> nodeMetaMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, Application>();
			cache.put(cacheKey + APPLICATION, nodeMetaMap);
		}
		
		//读取服务器下当前Application的应用节点
		File f = new File(nodeDir);
		File[] fs = f.listFiles();
		nodeIdMap.clear();
		if(fs != null){
			for (int i = 0; i < fs.length; i++) {
				File file = fs[i];
				if(file.isDirectory()){
					scanDir(null, f.getAbsolutePath(), file, nodeIdMap, nodeMetaMap);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getCacheMap(String ctxPath){
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, String> pageNodeMap = (Map<String, String>) cache.get(cacheKey);
		return pageNodeMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Application> getCacheMetaMap(String ctxPath){
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_APPLICATIONNODES;
		Map<String, Application> pageNodeMap = (Map<String, Application>) cache.get(cacheKey + APPLICATION);
		return pageNodeMap;
	}
	
	public static String getApplicationNodeDir(String appId){
		String ctxPath = LuiRuntimeContext.getRootPath();
		Map<String, String> cacheMap = getCacheMap(ctxPath);
		if(cacheMap == null)
			cacheMap = 	new HashMap<String, String>();
//			throw new LuiRuntimeException(ctxPath + " is not initialized");  在应用了布局管理之后发生的异常
		return cacheMap.get(appId);
	}
	
	/**
	 * 递归扫描目录下的所有节点
	 * @param prefix
	 * @param basePath
	 * @param dir
	 * @param nodeIdDirMap
	 * @param nodeMetaMap
	 */
	private static void scanDir(String prefix, String basePath, File dir, Map<String, String> nodeIdDirMap, Map<String, Application> nodeMetaMap) {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			if(f.isFile()){
				if(f.getName().endsWith(".app")){
					try{
						String id = dir.getName();
						if(prefix != null)
							id = prefix + "." + id;
						prefix = id;
						String absPath = dir.getAbsolutePath();
						absPath = absPath.replaceAll("\\\\", "/");
						String realPath = absPath.substring(basePath.length() + 1);
						nodeIdDirMap.put(id, realPath);
					}
					catch(Throwable e){
						LuiLogger.error(e);
					}
				}
			}
			else{
				scanDir(prefix, basePath, f, nodeIdDirMap, nodeMetaMap);
			}
		}
	}
}
