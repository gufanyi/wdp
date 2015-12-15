package xap.lui.core.processor;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
/**
 * 抽象化的请求处理基类。将一般的业务处理分为三步：
 * 1.处理请求模型
 * 2.业务逻辑调用
 * 3.处理返回模型
 * @author dengjt
 *
 */
public abstract class RequestProcessorBase<T> implements IRequestProcessor{
	//模型对象
	private T modelObject;
	//业务处理返回对象,可以返回任何可序列化对象
	private Object result;
	public String[] doProcess() {
		try{
			//处理数据模型
			modelObject = processModel();
			result = process();
			String[] returnStr = generateReturnString(result);
			return returnStr;
		}
		finally{
			finallyProcess();
		}
	}
	
	protected void finallyProcess() {
	}

	protected T getModelObject() {
		return modelObject;
	}
	
	/**
	 * 根据业务返回结果，构造返回值
	 * @param result
	 * @return
	 */
	protected abstract String[] generateReturnString(Object result);

	/**
	 * 构造请求模型对象
	 * @return
	 */
	protected abstract T processModel();
	
	/**
	 * 业务处理调用
	 * @return
	 */
	protected abstract Object process();

	/**
	 * 获取当前请求信息
	 * @return
	 */
	protected LuiWebContext getWebContext() {
		return LuiRuntimeContext.getWebContext();
	}
}
