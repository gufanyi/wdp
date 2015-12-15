package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ButtonContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCToolbarCompRender;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.NONE)
public class ToolBarItem extends WebComp {
	private static final long serialVersionUID = 1L;
	public static final String WIDGET_NAME = "button";
	public static final String BUTTON_TYPE = "button";

	public static final String NOTIFY_UPDATE_TYPE = "notifyUpdateType";
	public static final String WIDGET_ID = "widgetId";
	public static final String TOOLBAR_ID = "toolbarId";
	public static final String TOOLBAR_ITEM_ID = "toolbarItemId";
	public static final String REFIMG_CHANGE = "refImgChange";

	private String type = BUTTON_TYPE;
	@XmlAttribute
	private String refImg = "";
	// 图片路径是否改变
	private boolean refImgChanged = true;
	// 图片真实路径，在context中使用
	private String realRefImg;
	@XmlAttribute
	private String text = "";
	@XmlAttribute
	private String tip = "";
	@XmlAttribute
	private String align = "left";
	@XmlAttribute
	private boolean withSep = false;

	private ToolBarComp toolbar = null;
	// 修饰符，默认＝CTRL
	private int modifiers = java.awt.Event.CTRL_MASK;

	private PCToolbarCompRender render = null;

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	// 热键
	private String hotKey = null;
	// 热键显示名称
	private String displayHotKey = null;

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public static final int DEFAULT_WIDTH = 120;
	public static final int DEFAULT_HEIGHT = 20;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (this.text != text) {
			this.text = text;
			this.setCtxChanged(true);
		}
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public String getRefImg() {
		return refImg;
	}

	public void setRefImg(String refImg) {
		this.refImg = refImg;
		if (this.getWidget() != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setToolBarItemRefImg(this);
			}
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isWithSep() {
		return withSep;
	}

	public void setWithSep(boolean withSep) {
		this.withSep = withSep;
	}

	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if (this.getWidget() != null) {
			this.isVisible = visible;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setToolBarItemVisible(this);
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (this.getWidget() != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setToolBarItemEnabled(this);
			}
		}
	}

	@Override
	public BaseContext getContext() {
		if (type.equals(BUTTON_TYPE)) { // 按钮子项
			ButtonContext ctx = new ButtonContext();
			ctx.setId(this.getId());
			ctx.setText(this.getText());
			ctx.setEnabled(this.enabled);
			ctx.setVisible(this.isVisible());
			return ctx;
		} else { // TODO 其他子项
		}
		return null;
	}

	@Override
	public void setContext(BaseContext ctx) {
		if (type.equals(BUTTON_TYPE)) { // 按钮子项
			ButtonContext btCtx = (ButtonContext) ctx;
			this.enabled = btCtx.isEnabled();
			this.text = btCtx.getText();
		} else { // TODO 其他子项
		}
		this.setCtxChanged(false);
	}

	@Override
	public String toString() {
		return getId() + ":" + getText();
	}

	public String getHotKey() {
		return hotKey;
	}

	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}

	public String getDisplayHotKey() {
		return displayHotKey;
	}

	public void setDisplayHotKey(String displayHotKey) {
		this.displayHotKey = displayHotKey;
	}

	public boolean isRefImgChanged() {
		return refImgChanged;
	}

	public void setRefImgChanged(boolean refImgChanged) {
		this.refImgChanged = refImgChanged;
	}

	public String getRealRefImg(String size) {
		String temImg = "icon/"+size+"/" + this.refImg;
		realRefImg = getRealImgPath(temImg, null);
		return realRefImg;
	}

	public void setRealRefImg(String realRefImg) {
		this.realRefImg = realRefImg;
	}

	public ToolBarComp getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolBarComp toolbar) {
		this.toolbar = toolbar;
	}

	public PCToolbarCompRender getRender() {
		if (render == null) {
			render = this.getToolbar().getRender();
		}
		return render;
	}


}
