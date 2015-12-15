package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.LinkContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCLinkCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 链接控件配置
 * 
 * @author dengjt
 */
@XmlRootElement(name = "Link")
@XmlAccessorType(XmlAccessType.NONE)
public class LinkComp extends WebComp {
	private static final long serialVersionUID = -1529867689635200981L;
	public static final String WIDGET_NAME = "link";
	@XmlAttribute
	private String href;
	@XmlAttribute
	private String i18nName;
	@XmlAttribute
	private boolean hasImg;
	@XmlAttribute
	private String image;

	// 图片路径是否改变
	@XmlAttribute
	private boolean imageChanged = true;
	// 图片真实路径，在context中使用
	@XmlAttribute
	private String realImage;
	@XmlAttribute
	private String langDir;
	@XmlAttribute
	private String target = "_blank";

	@XmlAttribute
	private String text;

	private PCLinkCompRender render = null;

	public LinkComp() {
		this(null);
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public LinkComp(String id) {
		super(id);
		// height = "20";
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public boolean isHasImg() {
		return hasImg;
	}

	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
		setCtxChanged(true);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		if (visible != this.isVisible) {
			this.isVisible = visible;
			setCtxChanged(true);
		}
	}

	public boolean isImageChanged() {
		return imageChanged;
	}

	public void setImageChanged(boolean imageChanged) {
		this.imageChanged = imageChanged;
	}

	public String getRealImage() {
		if (isImageChanged()) {
			realImage = getRealImgPath(this.image, null);
			setImageChanged(false);
		}
		return realImage;
	}

	public void setRealImage(String realImage) {
		this.realImage = realImage;
	}

	@Override
	public BaseContext getContext() {
		LinkContext ctx = new LinkContext();
		ctx.setVisible(isVisible());
		ctx.setText(getI18nName());
		ctx.setEnabled(isEnabled());
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		LinkContext linkCtx = (LinkContext) ctx;
		this.setVisible(linkCtx.isVisible());
		this.setI18nName(linkCtx.getText());
		this.setEnabled(linkCtx.isEnabled());
		this.setCtxChanged(false);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.i18nName = null;
		this.text = text;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setText(text);
		}
	}

	@Override
	public PCLinkCompRender getRender() {
		if (render == null) {
			render=RenderProxy.getRender(new PCLinkCompRender(this));
		}
		return render;
	}

}
