package xap.lui.core.comps;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.Theme;

import com.alibaba.fastjson.annotation.JSONField;
@XmlAccessorType(XmlAccessType.NONE)
public abstract class WebComp extends ViewElement {
	private static final long serialVersionUID = -3847364243868791054L;
	public static final String VISIBLE = "visible";
	public static final String ENABLED = "enabled";
	public static final String CHANGETEXT = "changetext";
	@XmlAttribute(name="isVisible")
	protected boolean isVisible = true;
	@XmlAttribute
	protected boolean enabled = true;
	@XmlAttribute
	private String contextMenu;//右键菜单
	public WebComp() {
		super();
	}
	public WebComp(String id) {
		super(id);
	}
	@JSONField(serialize = false)
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			setCtxChanged(true);
			addCtxChangedProperty("enabled");
		}
	}
	@JSONField(serialize = false)
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean visible) {
		if (this.isVisible != visible) {
			this.isVisible = visible;
			setCtxChanged(true);
		}
	}
	@Override
	public Object clone() {
		return super.clone();
	}
	public String getContextMenu() {
		return contextMenu;
	}
	public void setContextMenu(String contextMenu) {
		this.contextMenu = contextMenu;
	}
	@Override
	public void mergeProperties(LuiElement ele) {
		super.mergeProperties(ele);
		WebComp comp = (WebComp) ele;
		String contextMenu = comp.getContextMenu();
		if (contextMenu != null)
			this.setContextMenu(contextMenu);
		String confType = comp.getConfType();
		if (confType != null)
			this.setConfType(confType);
	}
	/**
	 * 获取图片全路径
	 * 
	 * @param refImg
	 * @param nodeImagePath
	 * @return
	 */
	public String getRealImgPath(String refImg, String nodeImagePath) {
		if (refImg == null)
			return null;
		if (refImg.indexOf("http:") != -1)
			return refImg;
		if (nodeImagePath == null) {
			nodeImagePath = LuiRuntimeContext.getWebContext().getPageMeta().getPlatformImagePath();
		}
		if (refImg.contains("${theme}")) {
			Theme theme = LuiRuntimeContext.getTheme();
			refImg = theme.getCtxPath() + "/" + refImg.replace("${theme}", theme.getId());
		} else {
			if ((!refImg.startsWith("/")) && (!"".equals(nodeImagePath))) {
				refImg = nodeImagePath + "/" + refImg;
			} else {
				refImg = LuiRuntimeContext.getRootPath() + refImg;
			}
		}
		return refImg;
	}
	
	@Override
	public String getWidgetName() {
		return null;
	}
}
