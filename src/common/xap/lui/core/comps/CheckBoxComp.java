package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.CheckBoxContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCCheckBoxCompRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "CheckBox")
@XmlAccessorType(XmlAccessType.NONE)
public class CheckBoxComp extends TextComp {
	private static final long serialVersionUID = 1L;
	public static final String WIDGET_NAME = "checkbox";
	// 此checkbox的真实值
	@XmlAttribute
	private String value;
	// checkbox旁边显示的文字
	@XmlAttribute
	private String i18nName;
	// 初始是否被选中
	@XmlAttribute
	protected boolean checked = false;
	@XmlAttribute
	private String dataType = StringDataTypeConst.FBOOLEAN;

	@JSONField(serialize=false)
	private PCCheckBoxCompRender render;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		if (this.checked != checked) {
			this.checked = checked;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setChecked(checked);
			}
		}
	}

	public String getValue() {
		return value;
	}


	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	@Override
	public BaseContext getContext() {
		CheckBoxContext checkCtx = new CheckBoxContext();
		super.getContext(checkCtx);
		return checkCtx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		super.setContext(ctx);
		CheckBoxContext checkCtx = (CheckBoxContext) ctx;
		this.setChecked(checkCtx.isChecked());
		setCtxChanged(false);
	}

	@Override
	public PCCheckBoxCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCCheckBoxCompRender(this));

		}
		return render;

	}
}
