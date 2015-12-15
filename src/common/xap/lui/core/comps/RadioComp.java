package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.RadioContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCRadioCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 单选按钮后台配置类
 * 
 * @author gd 2007-11-26
 */
@XmlRootElement(name = "Radio")
@XmlAccessorType(XmlAccessType.NONE)
public class RadioComp extends TextComp {
	private static final long serialVersionUID = 1531534959905591230L;
	public static final String WIDGET_NAME = "radio";
	// 初始是否被选中
	@XmlAttribute
	private boolean checked = false;
	// radio所属的组
	@XmlAttribute
	private String group;

	@JSONField(serialize=false)
	private PCRadioCompRender render = null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	@Override
	public BaseContext getContext() {
		RadioContext ctx = new RadioContext();
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		RadioContext radioctx = (RadioContext) ctx;
		this.setEnabled(radioctx.isEnabled());
		this.setChecked(radioctx.isChecked());
		setCtxChanged(false);
	}

	public PCRadioCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCRadioCompRender(this));
		}
		return render;
	}

}
