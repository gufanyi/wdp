package xap.lui.core.layout;

import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCCheckBoxCompRender;
import xap.lui.core.render.PCCheckboxGroupCompRender;
import xap.lui.core.render.PCComboCompRender;
import xap.lui.core.render.PCRadioCompRender;
import xap.lui.core.render.PCRadioGroupCompRender;
import xap.lui.core.render.PCTextAreaCompRender;
import xap.lui.core.render.PCTextCompRender;
import xap.lui.core.render.notify.RenderProxy;

public class UITextField extends UIComponent {
	private static final long serialVersionUID = 1656422141563952123L;
	public static final String TYPE = "type";
	public static final String IMG_SRC = "imgsrc";

	public static final String VALGIN = "valgin";
	private String valgin;
	private String type;
	private String imgsrc;

	public UITextField() {
		this.width = "120";
		setHeight("28");
	}

	public void setValgin(String valgin) {
		this.valgin=valgin;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setAlign(valgin);
		}
	}
	
	public void setWidth(String width){
		this.width = width;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setWidth(width);
		}
	}
	
	public String getValgin() {
		return valgin;
	}

	public void setType(String type) {
		this.type=type;
	}

	public String getType() {
		return type;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc=imgsrc;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public PCTextCompRender getRender() {
		if(render == null) {
			Object comp = this.getWebComp();
			if (comp instanceof ComboBoxComp) {
				render = RenderProxy.getRender(new PCComboCompRender((ComboBoxComp)comp));
			} else if(comp instanceof CheckBoxComp) {
				render = RenderProxy.getRender(new PCCheckBoxCompRender((CheckBoxComp)comp));
			} else if(comp instanceof CheckboxGroupComp) {
				render = RenderProxy.getRender(new PCCheckboxGroupCompRender((CheckboxGroupComp)comp));
			} else if(comp instanceof RadioComp) {
				render = RenderProxy.getRender(new PCRadioCompRender((RadioComp)comp));
			} else if(comp instanceof RadioGroupComp) {
				render = RenderProxy.getRender(new PCRadioGroupCompRender((RadioGroupComp)comp));
			} else if(comp instanceof TextAreaComp) {
				render = RenderProxy.getRender(new PCTextAreaCompRender((TextAreaComp)comp));
			} else {
				render = RenderProxy.getRender(new PCTextCompRender((TextComp)comp));
			}
		}
		return (PCTextCompRender)render;
	}

}
