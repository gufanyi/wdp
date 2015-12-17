package xap.lui.core.dao;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.util.ClassUtil;


public class CRUDHelper {
	private static Map<String, LuiCRUDService> serviceMap = new HashMap<String, LuiCRUDService>();
	private static LuiCRUDService getCRUDService(String clazz) {
		if(serviceMap.get(clazz) == null){
			LuiCRUDService crud = (LuiCRUDService) ClassUtil.newInstance(clazz);
			serviceMap.put(clazz, crud);
		}
		return serviceMap.get(clazz);
	}
	
	public static LuiCRUDService getCRUDService() {
		return (LuiCRUDService) getCRUDService("");
	}
}
