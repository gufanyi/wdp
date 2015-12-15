package xap.lui.core.layout;

import xap.lui.core.comps.FormElement;
import xap.lui.core.render.PCFormCompRender;

public class UIFormComp extends UIComponent {
	private static final long serialVersionUID = 3977206346723075062L;	
	public static final String LABEL_POSITION = "label_position";
	private String label_position;
	
	/**
	 * @param ele
	 * 删除form中的元素
	 */
	public void removeElement(FormElement ele){
		((PCFormCompRender)this.getRender()).removeElement(ele);
	}
	
	/**
	 * @param ele
	 * 添加form中的元素
	 */
	public void addElement(FormElement ele){
		((PCFormCompRender)this.getRender()).addFormElement(ele);
	}
	

	
	public void setLabelPosition(String labelPosition){
		this.label_position=labelPosition;
	}
	
	public String getLabelPosition(){
		return label_position;
	}
	

}
