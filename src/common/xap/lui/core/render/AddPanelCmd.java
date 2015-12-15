package xap.lui.core.render;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.layout.UIShutterItem;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.util.UIElementFactory;

/**
 * 新增Panel命令
 * 
 * @author licza
 * 
 */
public class AddPanelCmd extends AbstractRaCommand {
	UIElement uiEle;
	public AddPanelCmd(RaParameter rp,UIElement uiEle) {
		super(rp);
		this.uiEle = uiEle;
	}

	@Override
	public void execute() {
		String currentDropObjType2 = rp.getCurrentDropObjType2();
		if(currentDropObjType2 == null )
			return ;
		String type = rp.getType();
		if("isLayout".equals(currentDropObjType2)){
			if(type == null){
				throw new LuiRuntimeException("添加类型为空");
			}
			if(type.equals(LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT))
				type = LuiPageContext.SOURCE_TYPE_FLOWHPANEL;
			else if(type.equals(LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT))
				type = LuiPageContext.SOURCE_TYPE_FLOWVPANEL;
			else if(type.equals(LuiPageContext.SOURCE_TYPE_CARDLAYOUT))
				type = LuiPageContext.SOURCE_TYPE_CARDPANEL;
			else if(type.equals(LuiPageContext.SOURCE_TYPE_BORDER))
				type = LuiPageContext.SOURCE_TYPE_BORDERTRUE;
			else if(type.equals(LuiPageContext.SOURCE_TYPE_TAG))
				type = LuiPageContext.SOURCE_TYPE_TABITEM;
			else if(type.equals(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR))
				type = LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM;
		
		}else 
			type = null;
		
		if(type != null){
			int count = UIElementFactory.showInputDialog("确认");
			UIElementFactory uf = new UIElementFactory();
			for(int i=0;i<count;i++){
				UIElement child = uf.createUIElement(type, rp.getWidgetId());
				String t = randomT(4);
				if(child instanceof UITabItem){
					int lenth = ((UITabComp)uiEle).getPanelList().size();
					int index = lenth + 1;
					((UITabItem) child).setText("页签" + index);
				}
				if(child instanceof UIShutterItem){
					int lenth = ((UIShutter)uiEle).getPanelList().size();
					int index = lenth + 1;
					((UIShutterItem) child).setText("item" + index);
				}
				child.setId(type+i+t);
				if(child != null){
					this.addUIElement(uiEle, child);
				}else{
					throw new LuiRuntimeException("创建panel失败");
				}
					
			}
		}

		
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = rp.getWidgetId();
		paramObj.uiId = uiEle.getId();
//		paramObj.subuiId = child.getId();s
//		if(child instanceof UIComponent)
//			paramObj.eleId = child.getId();
		paramObj.type = type;
		callServer(paramObj, RaParameter.ADD);
	}
}
