package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.TextAreaContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.render.PCTextAreaCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 文本域控件后台属性配置类
 */
@XmlRootElement(name = "TextArea")
@XmlAccessorType(XmlAccessType.NONE)
public class TextAreaComp extends TextComp {
	private static final long serialVersionUID = -5959445344434487325L;
	public static final String WIDGET_NAME = "textarea";
	// 行宽
	@XmlAttribute
	private String rows;
	// 列宽
	@XmlAttribute
	private String cols;

	private PCTextAreaCompRender render = null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	@Override
	public BaseContext getContext() {
		TextAreaContext ctx = new TextAreaContext();
		ctx.setId(this.getId());
		// ctx.setEnabled(this.enabled);
		// ctx.setValue(this.getValue());
		// ctx.setFocus(this.isFocus());
		// ctx.setReadOnly(this.isReadOnly());
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		TextAreaContext taCtx = (TextAreaContext) ctx;
		this.setEnabled(taCtx.isEnabled());
		this.setValue(taCtx.getValue());
		this.setReadOnly(taCtx.isReadOnly());
		this.setCtxChanged(false);
	}

	@Override
	public String getEditorType() {
		return EditorTypeConst.TEXTAREA;
	}

	@Override
	public PCTextAreaCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCTextAreaCompRender(this));
		}
		return render;
	}

}
