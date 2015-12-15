package xap.lui.core.util;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
public class LuiClassUtil {
	public static Object invokeMethod(Object owner, String methodName, Object[] args) {
		Class<? extends Object> ownerClass = owner.getClass();
		Class<?>[] argsClass = null;
		if (args != null) {
			argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				if (args[i] == null) {
					return null;
				}
				argsClass[i] = args[i].getClass();
			}
		}
		Method method = null;
		try {
			method = ownerClass.getMethod(methodName, argsClass);
			return method.invoke(owner, args);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public static Class<?> forName(String className) {
		try {
			ClassLoader loader = LuiClassUtil.class.getClassLoader();
			Class<?> clazz = loader.loadClass(className);
			return clazz;
		} catch (ClassNotFoundException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException("没有加载到类:" + className);
		}
	}
	public static Object loadClass(String className) {
		try {
			ClassLoader loader = LuiClassUtil.class.getClassLoader();
			Class<?> clazz = loader.loadClass(className);
			Object object = clazz.newInstance();
			return object;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage()+"类加载发生错误");
		}
	}
	public static Class<?> forName(String className, ClassLoader loader) {
		try {
			return Class.forName(className, true, loader);
		} catch (ClassNotFoundException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException("构造类出错," + e.getMessage());
		}
	}
	public static Object newInstance(String className) {
		if (className == null)
			throw new LuiRuntimeException("classname 不能为空");
		return newInstance(forName(className));
	}
	public static Object newInstance(String className, ClassLoader loader) {
		return newInstance(forName(className, loader));
	}
	public static Object newInstance(String className, Class[] params, Object[] values) {
		return newInstance(forName(className), params, values);
	}
	public static Object newInstance(String className, Class[] params, Object[] values, ClassLoader loader) {
		return newInstance(forName(className, loader), params, values);
	}
	public static <T> T newInstance(Class<T> c, Class[] params, Object[] values) {
		try {
			Constructor<T> cs = c.getConstructor(params);
			return cs.newInstance(values);
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	public static <T> T newInstance(Class<T> c) {
		try {
			return c.newInstance();
		}
		// catch all, include nullpointer
		catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		}
	}
}
