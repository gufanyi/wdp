package xap.lui.psn.context;

import xap.lui.core.comps.FormComp;

/**
 * <p>
 * 监听属性发生改变.并做出相应的处理.
 * </p>
 * 此处禁止抛出任何异常.包括Runtime异常
 * 
 * @author licza
 * 
 */
public interface IPropertyChangeMonitor {
	/**
	 * 处理修改事件
	 * @param form
	 */
	void on(FormComp form, IPropertyInfo pi, Object val);
}
