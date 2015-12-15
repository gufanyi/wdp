package xap.lui.core.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.UIAbsoluteLayoutRender;
import xap.lui.core.render.notify.RenderProxy;


@XmlRootElement(name="AbsoluteLayout")
@XmlAccessorType(XmlAccessType.NONE)
public class UIAbsoluteLayout extends UIElement{
	
	private static final long serialVersionUID = -1830660364820705690L;
	public static final String CSSSTYLE = "cssStyle";
	
	protected List<UIComponent> componentList = new ArrayList<UIComponent>();
	private Integer isAutoFill;
	private String cssStyle;
	private String position;
	
	
	public UIAbsoluteLayout (){
		this.isAutoFill=UIConstant.FALSE;
		this.position="relative";
	}
	
	/**
	 * 产生的component要附带left top 信息
	 * 增加控件
	 * @param component
	 */
	public void addComponent(UIComponent component){
		if(this.componentList==null)
			this.componentList=new ArrayList<UIComponent>();
		component.setPosition("absolute");
		this.componentList.add(component);
		super.addElement(component);
	}
	
	public void setElement(UIElement component){
		if(component instanceof UIComponent){
			addComponent((UIComponent) component);
		}
	}
	
	/**
	 * 删除控件
	 * @param component
	 */
	public void removeComponent(UIComponent component){
		if (component != null) {
			if (componentList != null) {
				super.removeElement(component);
				for (UIComponent _component : componentList) {
					if (_component.getId().equals(component.getId())) {
						componentList.remove(_component);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public UIAbsoluteLayout doClone() {
		UIAbsoluteLayout layout = (UIAbsoluteLayout) super.doClone();
		if (this.componentList != null) {
			layout.componentList = new ArrayList<UIComponent>();
			Iterator<UIComponent> components = this.componentList.iterator();
			while (components.hasNext()) {
				UIComponent component = components.next();
				layout.componentList.add((UIComponent) component.doClone());
			}
		}
		return layout;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new UIAbsoluteLayoutRender(this));
		}
		return render;
	}
	
	public List<UIComponent> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<UIComponent> componentList) {
		this.componentList = componentList;
	}
	
	public String getCssStyle() {
		return cssStyle;
	}
	
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}

	public Integer getAutoFill() {
		return isAutoFill;
	}

	public void setAutoFill(Integer isAutoFill) {
		this.isAutoFill = isAutoFill;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}
