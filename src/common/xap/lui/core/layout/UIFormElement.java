package xap.lui.core.layout;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCFormElementRender;


public class UIFormElement extends UIComponent {

	private static final long serialVersionUID = 1L;

	public static final String FORM_ID = "form_id";

	public static final String ELEWIDTH = "eleWidth";
	private String eleWidth;
//	private String width;
	private String form_id;
	private String type;

	public UIFormElement() {
		setWidth("160");
		setHeight("24");
		this.eleWidth = "120";
	}

	public void setEleWidth(String width) {
		this.eleWidth = width;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			((PCFormElementRender)this.getRender()).setEleWidth(width);
		}
	}

	public String getEleWidth() {
		return (String) eleWidth;
	}
	
//	public String getWidth() {
//		return width;
//	}
//
//	public void setWidth(String width) {
//		this.width = width;
//		if(LifeCyclePhase.ajax.equals(getPhase())) {
//			((PCFormElementRender)this.getRender()).setWidth(width);
//		}
//	}

	public void setFormId(String formId) {
		this.form_id = formId;
	}

	public String getFormId() {
		return form_id;
	}

	public void setElementType(String elementType) {
		this.type = elementType;
	}

	public String getElementType() {
		return type;
	}

}
