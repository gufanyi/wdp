package xap.lui.core.processor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.exception.LuiInteractionException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.serializer.IObject2XmlSerializer;
import xap.lui.core.serializer.IXml2ObjectSerializer;
public abstract class AbstractRequestProcessor<T> extends RequestProcessorBase<T> {
	private String requestXml = null;
	private String requestType = null;
	@Override
	protected Object process() {
		Object result = null;
		LuiWebContext ctx = getWebContext();
		String type = ctx.getParameter("type");
		if (type == null){
			throw new LuiRuntimeException("没有指明调用的方法");
		}
		try {
			result = doIt();
		} catch (LuiValidateException e) {
			List<String> execs = AppSession.current().getAppContext().getExecScript();
			if (execs != null && execs.size() > 0) {
				String[] errors = new String[execs.size()];
				execs.toArray(errors);
				e.setErrors(errors);
			}
			throw e;
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	protected Object doIt() {
		requestType = getWebContext().getParameter("type");
		if (requestType == null || requestType.trim().equals(""))
			return null;
		Method method = null;
		try {
			method = this.getClass().getMethod(requestType, new Class[] {});
			return (T) method.invoke(this, new Object[] {});
		} catch (NoSuchMethodException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		} catch (IllegalArgumentException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			LuiLogger.error(e.getMessage(), e);
			Throwable exp = e.getTargetException();
			if (exp instanceof LuiRuntimeException)
				throw (LuiRuntimeException) e.getTargetException();
			if (exp instanceof LuiInteractionException)// 交互异常
				throw (LuiInteractionException) e.getTargetException();
			throw new LuiRuntimeException(e.getTargetException());
		}
	}
	@Override
	protected T processModel() {
		requestXml = getWebContext().getParameter("xml");
		if (requestXml == null || requestXml.trim().equals(""))
			return null;
		return getRequestSerializer().serialize(requestXml, null);
	}
	protected abstract IXml2ObjectSerializer<T> getRequestSerializer();
	@SuppressWarnings("unchecked")
	@Override
	protected String[] generateReturnString(Object result) {
		if (result == null)
			return null;
		if (getModelObject() != null && result.getClass().equals(getModelObject().getClass())) {
			IObject2XmlSerializer<T> serializer = getResponseSerializer();
			if (serializer != null)
				return serializer.serialize((T) result);
		}
		if (result instanceof String)
			return new String[] { result.toString() };
		if (result instanceof String[])
			return (String[]) result;
		return new String[] { result.toString() };
	}
	/**
	 * 结果到xml的序列化器
	 * 
	 * @return
	 */
	protected IObject2XmlSerializer<T> getResponseSerializer() {
		return null;
	}
}
