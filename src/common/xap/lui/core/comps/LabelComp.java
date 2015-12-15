package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.LabelContext;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCLabelCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 
 * 
 *
 */
@XmlRootElement(name = "Label")
@XmlAccessorType(XmlAccessType.NONE)
public class LabelComp extends WebComp {
	private static final long serialVersionUID = 518406987071519211L;
	public static final String WIDGET_NAME = "label";
	@XmlAttribute
	private String i18nName;// 多语显示值
	@XmlAttribute
	private String langDir;// 多语目录
	@XmlAttribute
	private String text;
	@XmlAttribute
	private String innerHTML;// html内容，如<p style=””><p/>等
	@XmlAttribute
	private String color;
	@XmlAttribute
	private String decoration;

	private PCLabelCompRender render = null;

	public LabelComp() {
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != this.text) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.i18nName = null;
			}
			this.text = text;
			setCtxChanged(true);
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		if (i18nName != this.i18nName) {
			this.i18nName = i18nName;
			setCtxChanged(true);
		}
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setdDecoration(decoration);
		}
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	@Override
	public BaseContext getContext() {
		LabelContext labelCtx = new LabelContext();
		labelCtx.setEnabled(isEnabled());
		labelCtx.setText(text);
		labelCtx.setVisible(isVisible());
		labelCtx.setInnerHTML(getInnerHTML());
		labelCtx.setColor(getColor());
		return labelCtx;
	}

	public void setContext(BaseContext ctx) {
		LabelContext labelCtx = (LabelContext) ctx;
		setVisible(labelCtx.isVisible());
		setText(labelCtx.getText());
		setEnabled(labelCtx.isEnabled());
		setCtxChanged(false);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (color != this.color) {
			this.color = color;
			setCtxChanged(true);
		}
	}


	public String getInnerHTML() {
		return innerHTML;
	}

	public void setInnerHTML(String innerHTML) {
		this.innerHTML = innerHTML;
		setCtxChanged(true);
	}

	@Override
	public PCLabelCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCLabelCompRender(this));
		}
		return render;
	}

}
