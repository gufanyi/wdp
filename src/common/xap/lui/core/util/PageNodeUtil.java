package xap.lui.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;


public final class PageNodeUtil {
	private static final String PAGEMETA = "_PAGEMETA";
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
	private static void refresh(String nodeDir, String ctxPath){
		dirPathMap.put(nodeDir, ctxPath);
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, "lui");
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, String> nodeIdMap = (Map<String, String>) cache.get(cacheKey);
		Map<String, PagePartMeta> nodeMetaMap = (Map<String, PagePartMeta>) cache.get(cacheKey + PAGEMETA);
		if(nodeIdMap == null){
			nodeIdMap = new HashMap<String, String>();
			cache.put(cacheKey, nodeIdMap);
			
			nodeMetaMap = new HashMap<String, PagePartMeta>();
			cache.put(cacheKey + PAGEMETA, nodeMetaMap);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getCacheMap(String ctxPath){
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, String> pageNodeMap = (Map<String, String>) cache.get(cacheKey);
		return pageNodeMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, PagePartMeta> getCacheMetaMap(String ctxPath){
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = ctxPath + WebConstant.CACHE_PAGENODES;
		Map<String, PagePartMeta> pageNodeMap = (Map<String, PagePartMeta>) cache.get(cacheKey + PAGEMETA);
		return pageNodeMap;
	}
	
	public static String getPageNodeDir(String pageId){
		String ctxPath = LuiRuntimeContext.getRootPath();
		if("login".equals(pageId)){
			while(getCacheMap(ctxPath) == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
		}
		return getCacheMap(ctxPath).get(pageId);
	}
	
	
}
