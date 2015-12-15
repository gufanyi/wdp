package xap.lui.core.render;

import java.util.List;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.util.UIElementFactory;


/**
 * @author licza
 */
public class AddPanelWithDirection extends AbstractRaCommand {
	private String direction;
	public AddPanelWithDirection(RaParameter rp, String direction) {
		super(rp);
		this.direction = direction;
	}

	@Override
	public void execute() {
		String type = rp.getType();
		UILayout layout = (UILayout) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
		UILayoutPanel target = (UILayoutPanel)UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId(), rp.getSubuiId());
		
		UIElementFactory uf = new UIElementFactory();
		UIElement child = uf.createUIElement(type, rp.getWidgetId());

		copyAttribute(target,child);
		
		String t = randomT(4);
		child.setId(type+1+t);
		boolean after = true;
		if("Left".equals(this.direction) || "Up".equals(this.direction) ){
			after = false;
		}
		List<UILayoutPanel> panelList = layout.getPanelList(); 
		boolean isLastOne = false;
		if(panelList!=null){
			int idx = panelList.indexOf(target);
			if((idx+1)==panelList.size()){
				isLastOne = true;
			}
		}
		
		if(after && isLastOne){
			layout.addPanel((UILayoutPanel)child);
		}else{
			layout.addPanel((UILayoutPanel)child, target, after);
		}
		
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = rp.getWidgetId();
		paramObj.uiId = rp.getUiId();
		paramObj.subuiId = child.getId();
		paramObj.direction = this.direction;
		paramObj.type = type;
		callServer(paramObj, RaParameter.ADD);
		
		if(layout instanceof UICardLayout && child instanceof UICardPanel){
			int idx = panelList.indexOf(target);
			if(after){
				((UICardLayout)layout).setCurrentItem(String.valueOf(idx+1));
			}else{
				if(idx==1 && !after){
					((UICardLayout)layout).setCurrentItem("0");
				}else{
					((UICardLayout)layout).setCurrentItem(String.valueOf(idx-1));
				}
			}
		}
	}
	
	private void copyAttribute(UILayoutPanel target,UIElement child){
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		if(child instanceof UILayoutPanel){
			UILayoutPanel newPanel = (UILayoutPanel)child;
			if(target.getClassName()!=null)
				newPanel.setClassName(target.getClassName());
			if(target.getCssStyle()!=null)
				newPanel.setCssStyle(target.getCssStyle());
			if(target.getBorder()!=null)
				newPanel.setBorder(target.getBorder());
			if(target.getTopBorder()!=null)
				newPanel.setTopBorder(target.getTopBorder());
			if(target.getTopPadding()!=null)
				newPanel.setTopPadding(target.getTopPadding());
			if(target.getBottomBorder()!=null)
				newPanel.setBottomBorder(target.getBottomBorder());
			if(target.getBottomPadding()!=null)
				newPanel.setBottomPadding(target.getBottomPadding());
			if(target.getLeftBorder()!=null)
				newPanel.setLeftBorder(target.getLeftBorder());
			if(target.getLeftPadding()!=null)
				newPanel.setLeftPadding(target.getLeftPadding());
			if(target.getRightBorder()!=null)
				newPanel.setRightBorder(target.getRightBorder());
			if(target.getRightPadding()!=null)
				newPanel.setRightPadding(target.getRightPadding());
		}
		if(target instanceof UIFlowvPanel && child instanceof UIFlowvPanel){
			UIFlowvPanel tPanel = (UIFlowvPanel)target;
			UIFlowvPanel nPanel = (UIFlowvPanel)child;
			if(tPanel.getHeight()!=null)
				nPanel.setHeight(tPanel.getHeight());
		}
		if(target instanceof UIFlowhPanel && child instanceof UIFlowhPanel){
			UIFlowhPanel tPanel = (UIFlowhPanel)target;
			UIFlowhPanel nPanel = (UIFlowhPanel)child;
			if(tPanel.getWidth()!=null)
				nPanel.setWidth(tPanel.getWidth());
		}		
		RequestLifeCycleContext.get().setPhase(phase);
	}

}
