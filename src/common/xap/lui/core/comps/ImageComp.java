package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ImageContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCIFrameRender;
import xap.lui.core.render.PCImageCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * Image控件配置
 * 
 * @author dengjt
 *
 */
@XmlRootElement(name = "ImageComp")
@XmlAccessorType(XmlAccessType.NONE)
public class ImageComp extends WebComp {

	private static final long serialVersionUID = 5777383343036006996L;
	public static final String WIDGET_NAME = "image";
	@XmlAttribute
	private String alt = "";
	@XmlAttribute
	private String image1;
	@XmlAttribute
	private String image2;
	// 图片路径是否改变
	@XmlAttribute
	private boolean image1Changed = true;
	// 图片真实路径，在context中使用
	@XmlAttribute
	private String realImage1;
	// 图片路径是否改变
	@XmlAttribute
	private boolean image2Changed = true;
	// 图片真实路径，在context中使用
	@XmlAttribute
	private String realImage2;
	@XmlAttribute
	private String imageInact;
	@XmlAttribute
	private boolean floatRight = false;
	@XmlAttribute
	private boolean floatLeft = false;
	@XmlAttribute
	private boolean maxShow = false;
	@JSONField(serialize=false)
	public PCImageCompRender render = null;
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public boolean isMaxShow() {
		return maxShow;
	}

	public void setMaxShow(boolean maxShow) {
		this.maxShow = maxShow;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public boolean isFloatLeft() {
		return floatLeft;
	}

	public void setFloatLeft(boolean floatLeft) {
		this.floatLeft = floatLeft;
	}

	public boolean isFloatRight() {
		return floatRight;
	}

	public void setFloatRight(boolean floatRight) {
		this.floatRight = floatRight;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		if (!image1.equals(this.image1)) {
			this.image1 = image1;
			setImage1Changed(true);
			addCtxChangedProperty("image1");
			setCtxChanged(true);
		}
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		if (!image2.equals(this.image2)) {
			this.image2 = image2;
			setImage2Changed(true);
			addCtxChangedProperty("image2");
			setCtxChanged(true);
		}
	}

	public String getImageInact() {
		return imageInact;
	}

	public void setImageInact(String imageInact) {
		this.imageInact = imageInact;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public boolean isImage1Changed() {
		return image1Changed;
	}

	public void setImage1Changed(boolean image1Changed) {
		this.image1Changed = image1Changed;
	}

	public String getRealImage1() {
		if (isImage1Changed()) {
			realImage1 = getRealImgPath(this.image1, null);
			setImage1Changed(false);
		}
		return realImage1;
	}

	public void setRealImage1(String realImage1) {
		this.realImage1 = realImage1;
	}

	public boolean isImage2Changed() {
		return image2Changed;
	}

	public void setImage2Changed(boolean image2Changed) {
		this.image2Changed = image2Changed;
	}

	public String getRealImage2() {
		if (isImage2Changed()) {
			realImage2 = getRealImgPath(this.image2, null);
			setImage2Changed(false);
		}
		return realImage2;
	}

	public void setRealImage2(String realImage2) {
		this.realImage2 = realImage2;
	}

	@Override
	public BaseContext getContext() {
		ImageContext ctx = new ImageContext();
		ctx.setId(this.getId());
		ctx.setEnabled(this.enabled);
		ctx.setVisible(this.isVisible());
		if (checkCtxPropertyChanged("image1"))
			ctx.setImage1(this.getRealImage1());
		if (checkCtxPropertyChanged("image2"))
			ctx.setImage2(this.getRealImage2());
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ImageContext imgCtx = (ImageContext) ctx;
		this.setEnabled(imgCtx.isEnabled());
		this.setVisible(imgCtx.isVisible());
		this.setCtxChanged(false);
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render=RenderProxy.getRender(new PCImageCompRender(this));
		}
		return render;
	}

}
