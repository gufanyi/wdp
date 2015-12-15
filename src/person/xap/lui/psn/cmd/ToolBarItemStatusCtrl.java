package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.psn.guided.CommBaseCreateComp;
import xap.lui.psn.guided.GuidedCfgController;

public class ToolBarItemStatusCtrl {
	public static final String toolBarStatus="toolBarStatus";
	public static final String parTabStatus_Edit="editStatus";
	public static final String parTabStatus_List="listStatus";
	/**
	 * 控制工具栏各项隐藏或显示
	 * @param toolBarComp
	 * @param type Item项的哪一项
	 */
//	public ToolBarItemStatusCtrl(ToolBarComp toolBarComp,String type){
//		ToolBarItem[] toolBarItems=toolBarComp.getElements();
//		for(ToolBarItem toolBarItem:toolBarItems){
//			String toolBarItemId=toolBarItem.getId();
//			 if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_new))
//			 	 setItemsStatus(toolBarItem,type,true);
//			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_edit))
//				 setItemsStatus(toolBarItem,type,true);
//			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete))
//				 setItemsStatus(toolBarItem,type,true);
//			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save))
//				 setItemsStatus(toolBarItem,type,false);
//			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_cancel))
//				 setItemsStatus(toolBarItem,type,false);
//		}	
//	}
	
	public ToolBarItemStatusCtrl(){}
	public ToolBarItemStatusCtrl(ToolBarComp toolBarComp,String type,String editType){
		ToolBarItem[] toolBarItems=toolBarComp.getElements();
		for(ToolBarItem toolBarItem:toolBarItems){
			String toolBarItemId=toolBarItem.getId();
			 if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_new)||toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_edit))
			 {
				 setItemsStatus(toolBarItem,type,false);
			 }
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_delete))
			 {
				 if(!StringUtils.equals(editType, "full"))
					 setItemsStatus(toolBarItem,type,false);
			 }
			 else if(toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_save)||toolBarItemId.endsWith(GuidedCfgController.ctrlItem_ToolBar_cancel))
			 {
				 setItemsStatus(toolBarItem,type,true);
				
			 }
		}
		if(StringUtils.equals("new", type)||StringUtils.equals("edit", type)){
			 setMenuBarDeleteRemove(false);
		}else{
			 setMenuBarDeleteRemove(true);
		}
	}
	private void setItemsStatus(ToolBarItem toolBarItem,String type,boolean flag){
		
		 if(StringUtils.equals("new", type)||StringUtils.equals("edit", type)){
			 toolBarItem.setVisible(flag);
			 LuiAppUtil.addAppAttr(toolBarStatus, parTabStatus_Edit);
		 }else{
			 toolBarItem.setVisible(!flag);
			 LuiAppUtil.addAppAttr(toolBarStatus, parTabStatus_List);
		 }
			 
	}
	
	public void setMenuBarDeleteRemove(boolean DeleteTrue){
		MenubarComp[] arrayMenuBar= LuiAppUtil.getCntView().getViewMenus().getMenuBars();
		if(arrayMenuBar!=null&arrayMenuBar.length>0){
			MenubarComp menuBarComp=arrayMenuBar[0];
			for(MenuItem menuItem:menuBarComp.getMenuList()){
				String	menuItemId=	menuItem.getId();
				if(StringUtils.equals(menuItemId, CommBaseCreateComp.ctrlItem_MenuBar_Remove)){
					menuItem.setVisible(!DeleteTrue);
				} else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
					menuItem.setVisible(DeleteTrue);
				}
				
			}
		}

	}


}
