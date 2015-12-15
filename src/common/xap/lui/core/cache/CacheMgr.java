package xap.lui.core.cache;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import xap.lui.core.adapter.LuiMapCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;


/**
 * LUI Cache管理，封装这一层主要解决2方面问题： 1. XAP是因为XAP提供的Cache默认不区分数据源
 */
public class CacheMgr {
	/**
	 * 文件缓存Map
	 */
	static Map<String, LUICache> fileCacheMap = new HashMap<String, LUICache>();
	public static CacheManager manager = null;
	/**
	 * 普通Cache（会话，文件缓存排除）的区域key缓存
	 */
	private static final String DEFAULT_DS_NAME = "$NULL$";
	private static final String CACHE_NAME = "LUI_CACHE";
	public static final String SESSION_PRE = "$S_";

	static {
		ClassLoader loader=Thread.currentThread().getContextClassLoader();
		URL  fileUrl=	loader.getResource("xap/lui/core/cache/ehcache-lui.xml");
		manager = CacheManager.create(fileUrl);
		manager.addCache(WebConstant.LUI_CORE_CACHE);
		manager.getCache(WebConstant.LUI_CORE_CACHE).bootstrap();
	}

	/**
	 * 获得软引用缓存
	 * 
	 * @param name
	 * @param dsName
	 * @return
	 */
	public static LUICache getSoftCache(String name, String dsName) {
		return getCache(name, dsName, true, true);
	}

	/**
	 * 获得缓存
	 * @param name
	 * @param dsName
	 * @return
	 */
	public static LUICache getStrongCache(String name, String dsName) {
		return getCache(name, dsName, false, true);
	}

	/**
	 * 按照会话分配缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * 
	 * @return
	 */
	public static LUICache getSessionCache() {
		String sesId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		return getSessionCache(sesId);
	}

	/**
	 * 按照会话分配缓存，通过此方法获得的缓存，将确保随会话一起销毁
	 * 
	 * @return
	 */
	public static LUICache getSessionCache(String sesId) {
		LUICache cache = getCache(SESSION_PRE + sesId, null, false, true);
		return cache;
	}

	/**
	 * 删除会话缓存
	 * 
	 * @param sesId
	 */
	public static void removeSessionCache(String sesId) {
		removeCache(SESSION_PRE + sesId, null, false);
	}

	/**
	 * 清除缓存
	 * 
	 * @param name
	 * @param dsName
	 * @param soft
	 */
	public static void removeCache(String name, String dsName, boolean soft) {
		String key = getCacheKeyName(name,dsName, soft);
		manager.removeCache(key);
	}

	/**
	 * 返回已有或者创建新缓存对象
	 * 
	 * @param name
	 * @param dsName
	 * @param soft
	 * @param create
	 * @return
	 */
	private static LUICache getCache(String name, String dsName, boolean soft, boolean create) {
		String key = getCacheKeyName(name,dsName, soft);
		Cache cache = manager.getCache(WebConstant.LUI_CORE_CACHE);
		Element element = cache.get(key);

		if (element == null) {
			LUICache lui = new LuiMapCache(dsName);
			element = new Element(key, lui);
			cache.put(element);
		}
		return (LUICache) element.getValue();
	}

	private static String getCacheKeyName(String name,String dsName, boolean soft) {
		if (dsName == null)
			dsName = DEFAULT_DS_NAME;
		String key = name+"_"+dsName + (soft ? "_soft" : "_strong") + CACHE_NAME;
		return key;
	}
}
