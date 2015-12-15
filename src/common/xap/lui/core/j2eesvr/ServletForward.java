package xap.lui.core.j2eesvr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.logger.LuiLogger;

public class ServletForward {
	/**
	 * 转发Servlet请求
	 * @param method Action方法名
	 * @param request HttpRequest
	 * @param response HttpResponse
	 * @throws Throwable 
	 */ 
	@SuppressWarnings("unchecked")
	public static  void forward(Method method, HttpServletRequest request,
			HttpServletResponse response) throws Throwable {
		try {
			BaseActionInterface ba =getBaseAction(method);
			Class[] parameterTypes=method.getParameterTypes();
			Object[] param = DataBinder.bind(request,parameterTypes, method.getParameterAnnotations());
			ba.fill(request, response);
			initMutiLang(ba);
			doBeforeMethod(ba);
			MethodUtils.invokeExactMethod(ba, method.getName(), param, parameterTypes);
			doAfterMethod( ba);
			ba.fush();
		}catch (ClassNotFoundException e) {
			response.sendError(404);
			return;
		}catch (NoSuchMethodException e) {
			response.sendError(404);
			return;
		}catch (IllegalAccessException e) {
			LuiLogger.error(e.getMessage(), e);
			response.sendError(404);
			return;
		} catch (InvocationTargetException e) {
			//TODO zww
			LuiLogger.error(e.getMessage(), e);
			response.setCharacterEncoding("utf-8");
			response.getWriter().println("{\"msg\":\""+e.getTargetException().getMessage()+"\"}");
			response.getWriter().close();
			response.encodeURL("/html/portal/portlet_error.jsp");
			throw	e.getTargetException();
		}
	}
	/**
	 * 初始化多语目录
	 * @param ba
	 */
	public static void initMutiLang(BaseActionInterface ba){
		Servlet servletConfig = ba.getClass().getAnnotation(Servlet.class);
		if(servletConfig != null){
			String langDir = servletConfig.langdir();
			if(StringUtils.isNotEmpty(langDir))
				LuiRuntimeContext.setLangDir(langDir);
		}
	}
	/**
	 * 执行ServletAction执行前要执行的方法
	 * @param ba Servlet实例
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void doBeforeMethod(Object ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String beforeMethodName = getBeforeMethodName(ba);
		if (beforeMethodName != null) {
				MethodUtils.invokeMethod(ba, beforeMethodName, null);
		}
	}
	/**
	 * 执行ServletAction执行后要执行的方法
	 * @param ba Servlet实例
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void doAfterMethod(Object ba) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String  AfterMethodName = getAfterMethodName(ba);
		if (AfterMethodName != null) {
				MethodUtils.invokeMethod(ba, AfterMethodName, null);
		}
	}
	/**
	 * 获得ServletAction执行后要执行的方法
	 * @param ba Servlet实例
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String getBeforeMethodName(Object obj) {
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean isBefore = method.isAnnotationPresent(Before.class);
			if (isBefore) {
				return method.getName();
			}
		}
		return null;
	}
	/**
	 * 获得ServletAction执行前要执行的方法
	 * @param ba Servlet实例
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String getAfterMethodName(Object obj) {
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (Method method : methods) {
			boolean isAfter = method.isAnnotationPresent(After.class);
			if (isAfter) {
				return method.getName();
			}
		}
		return null;
	}
	/**
	 * 获得方法类型的实例
	 * @param method 方法实例
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static BaseAction getBaseAction(Method method) throws ClassNotFoundException{
		BaseAction ba ;
			//开发环境下动态加载类
			try {
				if(LuiRuntimeContext.getMode().equals(WebConstant.MODE_DEBUG)){
					ba = (BaseAction)ClassScan.dynamicClass(method.getDeclaringClass()).newInstance();
				}
				else{
					ba = (BaseAction) method.getDeclaringClass().newInstance();
				}
			} 
			catch (Exception e) {
				throw new ClassNotFoundException(method.getDeclaringClass().getName()+" not Found!");
			}  
		
		return ba;
	}
}
