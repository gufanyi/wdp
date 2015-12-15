package xap.lui.core.layout;

import java.io.Serializable;

/**
 * 更新值对儿
 *
 */
public class UpdatePair implements Serializable {
	private static final long serialVersionUID = -3547244154699055457L;
	private String key;
	private Object value;
	private Object oldValue;
	public UpdatePair(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public UpdatePair(String key, Object value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getOldValue() {
		return oldValue;
	}
	
	public void setOldValue(Object value){
		this.oldValue = value;
	}
}
