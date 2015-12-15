package xap.lui.core.listener;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import xap.lui.core.builder.LuiObj;

/**
 * Dataset提交类型
 * 
 * @author guoweic
 * 
 */
@XmlRootElement(name="DatasetRule")
@XmlAccessorType(XmlAccessType.NONE)
public class DatasetRule implements Cloneable, Serializable,LuiObj {

	private static final long serialVersionUID = -4616047733374430734L;
	
	// 当前行
	public static final String TYPE_CURRENT_LINE = "ds_current_line";
	// 当前页
	public static final String TYPE_CURRENT_PAGE = "ds_current_page";
	// 当前页更新行
	public static final String TYPE_CURRENT_KEY = "ds_current_key";
	// 所有行
	public static final String TYPE_ALL_LINE = "ds_all_line";
	// 所有选中行
	public static final String TYPE_ALL_SEL_LINE = "ds_all_sel_line";
	// Dataset的ID
	@XmlAttribute
	private String id;
	// 提交类型
	
	@XmlValue
	private String type = TYPE_CURRENT_LINE;

	public Object clone(){
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(id);
		dsRule.setType(type);
		return dsRule;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
