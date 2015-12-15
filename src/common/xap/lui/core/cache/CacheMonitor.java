package xap.lui.core.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 缓存监控工具类，提供了缓存监控所需的api
 * 
 */
public final class CacheMonitor {

	public static Set<String> getExistCacheKeys() {
		// Set<String> set = LuiCacheManager.regionSet;
		return null;
	}

	public static Map<String, LUICache> getExistCacheMapByRegion(String key) {
		// ICache cache = CacheManager.getInstance().getCache(key);
		// return cache.toMap();
		return null;
	}

	public static Map<String, LUICache> getExitFileCacheMap() {
		Map<String, LUICache> cache = CacheMgr.fileCacheMap;
		Map<String, LUICache> ncache = new HashMap<String, LUICache>();
		Iterator<Entry<String, LUICache>> it = cache.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, LUICache> entry = it.next();
			String key = entry.getKey();
			if (key.startsWith(CacheMgr.SESSION_PRE))
				continue;
			ncache.put(key, entry.getValue());
		}
		return ncache;
	}
}
