package xap.lui.core.util;

import xap.lui.core.comps.WebComp;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.render.ILuiRender;

public class ObjSelectedUtil {
	
	public static String toSelected(UIPartMeta uimeta,PagePartMeta pageMeta,UIElement ele){
		//IUIRenderGroup group = RenderKit.getUIRenderGroup(RenderKit.RENDER_PC);
		String renderDiv = "";
		if(ele instanceof UILayoutPanel){
			if(ele instanceof UIGridPanel){
				UIGridPanel cell = (UIGridPanel)ele;
				UIGridRowLayout row = cell.getParent();
				UIGridLayout table = row.getParent();
				ILuiRender t =table.getRender();
				ILuiRender r =row.getRender();
				ILuiRender c =cell.getRender();
				renderDiv = c.getDivId();
			}
			else if(ele instanceof UIPanelPanel){
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				renderDiv = parent.getId() + "_content";
			}else{
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				ILuiRender parentRender =parent.getRender();
				ILuiRender render =ele.getRender();
				renderDiv = render.getDivId();
			}
			
		}
		else{
			if(ele instanceof UIGridRowLayout){
				UIElement parent = UIElementFinder.findParent(uimeta, ele);
				ILuiRender parentRender= parent.getRender();
				ILuiRender render =ele.getRender();
				renderDiv = render.getDivId();
			}
			else{
				if(ele instanceof UIComponent) {
					WebComp webEle = pageMeta.getWidget(ele.getViewId()).getViewComponents().getComponent(ele.getId());
					if(webEle == null){
						webEle = pageMeta.getWidget(ele.getViewId()).getViewMenus().getMenuBar(ele.getId());
						if(webEle == null)
							webEle = pageMeta.getWidget(ele.getViewId()).getViewMenus().getContextMenu(ele.getId());
					}
					ILuiRender render = webEle.getRender();
					if(render!=null){
						renderDiv = render.getDivId();
					}
				} else{
					ILuiRender render =ele.getRender();
					if(render!=null){
						renderDiv = render.getDivId();
					}
				}
			}
		}
		return renderDiv;
	}

}
