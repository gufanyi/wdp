package xap.lui.core.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.util.LuiClassUtil;

public class LuiPaltformContranier {

	private static LuiPaltformContranier instance = null;
	private static String PlatformExtCompCacheKeyName = "$_PlatformExtCompCacheKeyName";
	private LUICache container = null;

	static {
		instance = new LuiPaltformContranier();
		instance.container = CacheMgr.getStrongCache(PlatformExtCompCacheKeyName, "desgin");
		LuiPaltformContranier.getInstance().init();
	}

	private LuiPaltformContranier() {

	}

	public static LuiPaltformContranier getInstance() {
		return instance;
	}

	public ILuiPaltformExtProvier[] getProvideres() {
		LUICache caches = LuiPaltformContranier.getInstance().container;
		Set<Object> keys = caches.getKeys();
		Iterator<Object> iter = keys.iterator();
		List<ILuiPaltformExtProvier> providers = new ArrayList<ILuiPaltformExtProvier>();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			providers.add((ILuiPaltformExtProvier) caches.get(key));
		}
		return providers.toArray(new ILuiPaltformExtProvier[0]);

	}

	public void init() {
		ServletContext servletCtx = LuiRuntimeContext.getLuiServletContext();
		String providers = servletCtx.getInitParameter("extCompProviders");
		String[] str = providers.split(",");
		for (String inner : str) {
			ILuiPaltformExtProvier provider = (ILuiPaltformExtProvier) LuiClassUtil.loadClass(inner);
			container.put(provider.getCompTypeName(), provider);
		}
	}

}
