package xap.mw.core.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BeanHelper {
    // [start] singleton
    private static BeanHelper bhelp = new BeanHelper();

    public static BeanHelper getInstance() {
	return bhelp;
    }

    private BeanHelper() {
    }

    // [end]

    protected static final Object[] NULL_ARGUMENTS = {};

    private static Map<String, ReflectBuffer> buffer = new ConcurrentHashMap<String, ReflectBuffer>();

    public static void setProp(Object bean, String propName, Object val) {
	try {
	    Method method = getInstance().getMethod(bean, propName, true);
	    if (propName != null && method == null) {
		return;
	    } else if (method == null) {
		return;
	    }
	    method.invoke(bean, val);
	} catch (java.lang.IllegalArgumentException e) {
	    String err = "Failed to set property: " + propName + " at bean: "
		    + bean.getClass().getName() + " with value:" + val
		    + " type:"
		    + (val == null ? "null" : val.getClass().getName());
	    throw new IllegalArgumentException(err, e);
	} catch (Exception e) {
	    String err = "Failed to set property: " + propName + " at bean: "
		    + bean.getClass().getName() + " with value:" + val;
	    throw new RuntimeException(err, e);
	}
    }

    public static List<String> getPropList(Object bean) {
	return Arrays.asList(getInstance().getPropArr(bean));

    }

    public String[] getPropArr(Object bean) {
	ReflectBuffer reflectionInfo = null;

	reflectionInfo = cacheReflectBuffer(bean.getClass());
	Set<String> propertys = new HashSet<String>();
	for (String key : reflectionInfo.writes.keySet()) {
	    if (reflectionInfo.writes.get(key) != null) {
		propertys.add(key);
	    }
	}
	return propertys.toArray(new String[0]);
    }

    public static Object getProp(Object bean, String propName) {

	try {
	    Method method = getInstance().getMethod(bean, propName, false);
	    if (propName != null && method == null) {
		return null;
	    } else if (method == null) {
		return null;
	    }
	    return method.invoke(bean, NULL_ARGUMENTS);
	} catch (Exception e) {
	    String errStr = "Failed to get property: " + propName;
	    // Logger.warn(errStr, e);
	    throw new RuntimeException(errStr, e);
	}
    }

    public static Object[] getPropVals(Object bean, String[] props) {
	Object[] result = new Object[props.length];
	try {
	    Method[] methods = getInstance().getMethods(bean, props, false);
	    for (int i = 0; i < props.length; i++) {
		if (props[i] == null || methods[i] == null) {
		    result[i] = null;
		} else {
		    result[i] = methods[i].invoke(bean, NULL_ARGUMENTS);
		}
	    }
	} catch (Exception e) {
	    String errStr = "Failed to get getPropertys from "
		    + bean.getClass();
	    throw new RuntimeException(errStr, e);
	}
	return result;
    }

    public static Method getMethod(Object bean, String propertyName) {
	return getInstance().getMethod(bean, propertyName, true);
    }

    public static Method getGetMethod(Object bean, String propertyName) {
	return getInstance().getMethod(bean, propertyName, false);
    }

    public static Method getSetMethod(Object bean, String propertyName) {
	return getInstance().getMethod(bean, propertyName, true);
    }

    public static Method[] getMethods(Object bean, String[] propertys) {
	return getInstance().getMethods(bean, propertys, true);
    }

    private Method[] getMethods(Object bean, String[] propertys,
	    boolean isSetMethod) {
	Method[] methods = new Method[propertys.length];
	ReflectBuffer reflectionInfo = null;

	reflectionInfo = cacheReflectBuffer(bean.getClass());
	for (int i = 0; i < propertys.length; i++) {
	    Method method = null;
	    if (isSetMethod) {
		method = reflectionInfo.getWriteMethod(propertys[i]);
	    } else {
		method = reflectionInfo.getReadMethod(propertys[i]);
	    }
	    methods[i] = method;
	}
	return methods;
    }

    public static void invokeMethod(Object bean, Method method, Object value) {
	try {
	    if (method == null)
		return;
	    Object[] arguments = { value };
	    method.invoke(bean, arguments);
	} catch (Exception e) {
	    String err = "invokeMethod failed: " + method.getName();
	    throw new RuntimeException(err, e);
	}
    }

    public Method[] getAllGetMethod(Class<?> beanCls, String[] fieldNames) {
	Method[] methods = null;
	ReflectBuffer reflectionInfo = null;
	List<Method> al = new ArrayList<Method>();
	reflectionInfo = cacheReflectBuffer(beanCls);
	for (String str : fieldNames) {
	    al.add(reflectionInfo.getReadMethod(str));
	}
	methods = al.toArray(new Method[al.size()]);
	return methods;
    }

    private List<PropDescriptor> getPropertyDescriptors(Class<?> clazz) {
	List<PropDescriptor> descList = new ArrayList<PropDescriptor>();
	List<PropDescriptor> superDescList = new ArrayList<PropDescriptor>();
	List<String> propsList = new ArrayList<String>();
	Class<?> propType = null;
	for (Method method : clazz.getDeclaredMethods()) {
	    if (method.getName().length() < 4) {
		continue;
	    }
	    if (method.getName().charAt(3) < 'A'
		    || method.getName().charAt(3) > 'Z') {
		continue;
	    }
	    if (method.getName().startsWith("set")) {
		if (method.getParameterTypes().length != 1) {
		    continue;
		}
		if (method.getReturnType() != void.class) {
		    continue;
		}
		propType = method.getParameterTypes()[0];
	    } else if (method.getName().startsWith("get")) {
		if (method.getParameterTypes().length != 0) {
		    continue;
		}
		propType = method.getReturnType();
	    } else {
		continue;
	    }
	    String propname = method.getName().substring(3, 4).toLowerCase();
	    if (method.getName().length() > 4) {
		propname = propname + method.getName().substring(4);
	    }
	    if (propname.equals("class")) {
		continue;
	    }
	    if (propsList.contains(propname)) {
		continue;
	    } else {
		propsList.add(propname);
	    }
	    descList.add(new PropDescriptor(clazz, propType, propname));
	}

	Class<?> superClazz = clazz.getSuperclass();
	if (superClazz != null) {
	    superDescList = getPropertyDescriptors(superClazz);
	    descList.addAll(superDescList);
	    if (!isBeanCached(superClazz)) {
		cacheReflectBuffer(superClazz, superDescList);
	    }
	}
	return descList;
    }

    private boolean isBeanCached(Class<?> bean) {
	String key = bean.getName();
	ReflectBuffer cMethod = buffer.get(key);
	if (cMethod == null) {
	    cMethod = buffer.get(key);
	    if (cMethod == null) {
		return false;
	    }
	}
	return true;
    }

    private Method getMethod(Object bean, String propName, boolean isSet) {
	Method method = null;
	ReflectBuffer buf = cacheReflectBuffer(bean.getClass());
	if (isSet) {
	    method = buf.getWriteMethod(propName);
	} else {
	    method = buf.getReadMethod(propName);
	}
	return method;
    }

    private ReflectBuffer cacheReflectBuffer(Class<?> beanCls) {
	return cacheReflectBuffer(beanCls, null);
    }

    private ReflectBuffer cacheReflectBuffer(Class<?> beanCls,
	    List<PropDescriptor> propDescList) {
	String key = beanCls.getName();
	ReflectBuffer reflectionInfo = buffer.get(key);
	if (reflectionInfo == null) {
	    reflectionInfo = buffer.get(key);
	    if (reflectionInfo == null) {
		reflectionInfo = new ReflectBuffer();
		List<PropDescriptor> propDesc = new ArrayList<PropDescriptor>();
		if (propDescList != null) {
		    propDesc.addAll(propDescList);
		} else {
		    propDesc = getPropertyDescriptors(beanCls);
		}
		for (PropDescriptor pd : propDesc) {
		    Method readMethod = pd.getReadMethod(beanCls);
		    Method writeMethod = pd.getWriteMethod(beanCls);
		    if (readMethod != null)
			reflectionInfo.reads.put(pd.getName().toLowerCase(),
				readMethod);
		    if (writeMethod != null)
			reflectionInfo.writes.put(pd.getName().toLowerCase(),
				writeMethod);
		}
		buffer.put(key, reflectionInfo);
	    }
	}
	return reflectionInfo;

    }

    // [start] 内部�?
    private static class ReflectBuffer {
	private Map<String, Method> reads = new HashMap<String, Method>();
	private Map<String, Method> writes = new HashMap<String, Method>();

	public Method getReadMethod(String prop) {
	    return prop == null ? null : reads.get(prop.toLowerCase());
	}

	public Method getWriteMethod(String prop) {
	    return prop == null ? null : writes.get(prop.toLowerCase());
	}
    }
    // [end]
}
