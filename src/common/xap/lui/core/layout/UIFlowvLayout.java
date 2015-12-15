package xap.lui.core.layout;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCFlowvLayoutRender;
import xap.lui.core.render.notify.RenderProxy;


@XmlRootElement(name="FlowVLayout")
@XmlAccessorType(XmlAccessType.NONE)
public class UIFlowvLayout extends UILayout {
	private static final long serialVersionUID = -4255303814886822777L;
	private static final String GEN_PANEL_PRE = "g_p_";
	private int currentPanelId = 1;
	
	/**
	 * jaxb 
	 */
	@XmlElementRefs({
		@XmlElementRef(name="FlowHPanel",type=UIFlowhLayout.class),
		@XmlElementRef(name="FlowVPanel",type=UIFlowvPanel.class)
	})
	
	private UIElement element;
	
	
	/**
	 * 向当前布局中增加元素，此API将首先添加一个Panel，并将元素添加到对应元素中
	 * @param ele 被添加元素
	 */
	public UIFlowvPanel addElementToPanel(UIElement ele) {
		UIFlowvPanel panel = new UIFlowvPanel();
		panel.setId(GEN_PANEL_PRE + (currentPanelId ++));
		this.addPanel(panel);
		if(ele != null)
			panel.setElement(ele);
		return panel;
	}
	
	/**
	 * 获取当前布局中的元素，此API将遍历所有Panel，并返回ID相等的元素
	 * @param id
	 * @return
	 */
	public UIElement getElementFromPanel(String id) {
		Iterator<UILayoutPanel> it = getPanelList().iterator();
		while(it.hasNext()){
			UIFlowvPanel panel = (UIFlowvPanel) it.next();
			UIElement ele = panel.getElement();
			if(ele == null)
				continue;
			if(id.equals(ele.getId()))
				return ele;
		}
		return null;
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCFlowvLayoutRender(this));
		}
		return render;
	}
	
	
}
