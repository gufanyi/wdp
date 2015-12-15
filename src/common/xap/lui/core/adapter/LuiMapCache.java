package xap.lui.core.adapter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import xap.lui.core.cache.LUICache;
import xap.lui.core.exception.CacheException;


/**
 * LUI框架NC缓存适配
 * 
 * @author dengjt
 * 
 */
public class LuiMapCache implements LUICache,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6089918094219545026L;
	private Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
	private String name;

	public LuiMapCache(String name) {
		this.name = name;
	}

	public Object get(Object key) throws CacheException {
		return map.get(key);
	}

	public Object put(Object key, Object value) throws CacheException {
		if (key instanceof String) {
			String str = (String) key;
			if (str.indexOf("_pagemeta_ori") > -1) {
				//java.util.logging.Logger.getLogger(LuiMapCache.class.getName()).info((String) key);
			}
			if(str.indexOf("_uimeta_ori")>-1){
				//java.util.logging.Logger.getLogger(LuiMapCache.class.getName()).info((String) key);
			}
		}
		return map.put(key, value);
	}

	public Object remove(Object key) throws CacheException {
		String keyStr=(String)key;
		if (keyStr.indexOf("_pagemeta_ori") > -1) {
			java.util.logging.Logger.getLogger(LuiMapCache.class.getName()).info((String) key);
		}
		if(keyStr.indexOf("_uimeta_ori")>-1){
			java.util.logging.Logger.getLogger(LuiMapCache.class.getName()).info((String) key);
		}
		return map.remove(key);
	}

	public Set<Object> getKeys() {
		return map.keySet();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		map.clear();
	}
}
