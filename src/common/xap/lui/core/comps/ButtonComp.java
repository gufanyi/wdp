package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ButtonContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCButtonCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * Button 控件配置
 * 
 * @author dengjt
 * 
 */
@XmlRootElement(name = "Button")
@XmlAccessorType(XmlAccessType.NONE)
public class ButtonComp extends WebComp {
	private static final long serialVersionUID = -3640014425289622883L;
	public static final String WIDGET_NAME = "button";
	@XmlAttribute
	private String tip;
	private String i18nName;
	@XmlAttribute
	private String refImg;
	@XmlAttribute
	private int width;

	@JSONField(serialize=false)
	private PCButtonCompRender render = null;// (ButtonNotifyRender)RenderProxy.getNotifyRender(new
												// ButtonNotifyRender(null,null,null,null,null));

	// 图片路径是否改变
	@XmlAttribute
	private boolean refImgChanged = true;
	// 图片真实路径，在context中使用
	@XmlAttribute
	private String realRefImg;
	// private String align = "left";
	@XmlAttribute
	private String langDir;
	@XmlAttribute
	private String text;
	private String tipI18nName;
	// 热键
	@XmlAttribute
	private String hotKey = null;
	// 热键显示名称
	@XmlAttribute
	private String displayHotKey = null;

	private int modifiers = java.awt.Event.CTRL_MASK;

	public ButtonComp() {
	}

	public ButtonComp(String id) {
		super(id);
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public String getRefImg() {
		return refImg;
	}

	public void setRefImg(String refImg) {
		this.refImg = refImg;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public Object clone() {

		return super.clone();
	}

	@Override
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getText() {
		return text;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		if (width != this.width) {
			this.width = width;
			this.getRender().setWidth(width);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setEnable(enabled);
			}
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible != this.isVisible) {
			this.isVisible = visible;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setVisible(visible);
			}
		}
	}

	public void setText(String text) {
		if (!StringUtils.equals(text, this.text)) {
			this.text = text;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setText(text);
			}
		}
	}

	public String getTipI18nName() {
		return tipI18nName;
	}

	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}

	@Override
	public BaseContext getContext() {
		ButtonContext ctx = new ButtonContext();
		ctx.setId(this.getId());
		ctx.setRefImg(this.realRefImg);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ButtonContext btCtx = (ButtonContext) ctx;
		this.setEnabled(btCtx.isEnabled());
		this.setVisible(btCtx.isVisible());
		this.setCtxChanged(false);
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

	public String getRealRefImg() {
		if (isRefImgChanged()) {
			realRefImg = getRealImgPath(this.refImg, null);
			setRefImgChanged(false);
		}
		return realRefImg;
	}

	public void setRealRefImg(String realRefImg) {
		this.realRefImg = realRefImg;
	}

	@Override
	public PCButtonCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCButtonCompRender(this));
		}
		return render;

	}

}
