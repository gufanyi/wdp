package xap.lui.core.common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiObj;
import xap.lui.core.builder.LuiSet;
@XmlRootElement(name = "Theme")
@XmlAccessorType(XmlAccessType.NONE)
public class Theme implements Serializable {
	private static final long serialVersionUID = -1684792772032253827L;
	public static final String LUI_BORDER_COLOR = "LUI_BORDER_COLOR";
	public static final String LUI_BACKGROUND_COLOR = "LUI_BACKGROUND_COLOR";
	public static final String LUI_COMP_BACKGROUND_COLOR = "LUI_COMP_BACKGROUND_COLOR";
	// 每个spliter区域的border颜色
	public static final String LUI_SPLITER_BORDER_COLOR = "LUI_SPLITER_BORDER_COLOR";
	@XmlElement(name="Element")
	private LuiSet<Element> elements = new LuiHashSet<Element>();
	public LuiSet<Element> getElements() {
		return elements;
	}
	public void setElements(LuiSet<Element> elements) {
		this.elements = elements;
	}
	
	
	@XmlAccessorType(XmlAccessType.NONE)
	public static class Element implements LuiObj {
		@XmlAttribute
		private String id;
		@XmlAttribute
		private String value;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	// 皮肤所在上下文路径
	private String ctxPath;
	// 主题文件绝对路径。
	private String absPath;
//	public void setThemeElement(String key, String value) {
//		elementMap.put(key, value);
//	}
	public String getThemeElement(String key) {
		return this.elements.find(key).getValue();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbsPath() {
		return absPath;
	}
	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}
	public String getCtxPath() {
		return ctxPath;
	}
	public void setCtxPath(String ctxPath) {
		this.ctxPath = ctxPath;
	}
}
