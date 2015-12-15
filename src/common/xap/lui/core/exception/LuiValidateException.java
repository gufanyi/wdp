package xap.lui.core.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuiValidateException extends LuiRuntimeException {
	private static final long serialVersionUID = 5643508282282916234L;
	
	private String[] errors = null;
	
	private String viewId = null;
	
	private List<String> componentIds = null;
	// formElement 错误信息 key-formElementId value-错误信息
	private Map<String, String> elementMap = null;

	public LuiValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public LuiValidateException(String s) {
		super(s);
	}

	public LuiValidateException(Throwable cause) {
		super(cause);
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public Map<String, String> getElementMap() {
		if(elementMap == null){
			elementMap = new HashMap<String, String>();
		}
		return elementMap;
	}

	public void setElementMap(Map<String, String> elementMap) {
		this.elementMap = elementMap;
	}
	
	/**
	 * 存放formElement错误提示信息
	 * @param map
	 * @param elementId
	 * @param errorMsg
	 * @return
	 */
	public static Map<String, String> putFormElementToMap(Map<String, String> map, String elementId, String errorMsg){
		if(map == null){
			map = new HashMap<String, String>();
		}
		if(errorMsg != null && errorMsg.trim().length() > 0){
			if(map.get(elementId) == null){
				map.put(elementId, errorMsg.trim());
			}else{
				map.put(elementId, map.get(elementId) + ";" + errorMsg.trim());
			}
		}
		return map;
	}

	public List<String> getComponentIds() {
		return componentIds;
	}

	public void setComponentIds(List<String> componentIds) {
		this.componentIds = componentIds;
	}
	
	public void addComponentId(String componentId){
		List<String> list = getComponentIds();
		if(list == null){
			list = new ArrayList<String>();
			setComponentIds(list);
		}
		list.add(componentId);
	}
	
}
