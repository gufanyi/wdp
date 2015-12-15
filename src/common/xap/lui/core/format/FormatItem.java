
package xap.lui.core.format;

import java.io.Serializable;


public class FormatItem implements Serializable {
	
	
	private static final long serialVersionUID = 7609775844376411524L;
	
	private String code;
	private String name;
	private String value;
	
	public FormatItem(){};
	
	public FormatItem(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof FormatItem)){
			return false;
		}
		return this.getCode().equals(((FormatItem)obj).getCode());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode()+Integer.valueOf(getCode());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
