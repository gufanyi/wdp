/**
 * 
 */
package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class ComboPropertyInfo extends BasePropertyInfo {
	private static final long serialVersionUID = -3535592580972657633L;
	private String[] keys;
	private String[] values;
	/* (non-Javadoc)
	 */
	public String[] getKeys() {
		return keys;
	}
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public boolean editable() {
		return false;
	}
	 

}
