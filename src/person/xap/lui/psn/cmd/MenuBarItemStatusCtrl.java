package xap.lui.psn.cmd;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.psn.guided.CommBaseCreateComp;
import xap.lui.psn.guided.GuidedCfgController;

public class MenuBarItemStatusCtrl {
	/**
	 * 菜单栏 各项状态控制
	 * @param menuBarCompId 菜单id
	 * @param type 各项类型，是 新建 还是 编辑 。。。
	 * @param editType 表格编辑类型
	 */
	public MenuBarItemStatusCtrl(String menuBarCompId,String type,String editType){
		MenubarComp menuBarComp=(MenubarComp)LuiAppUtil.getCntView().getViewMenus().getMenuBar(menuBarCompId);
		 List<MenuItem> listMenuItem= menuBarComp.getMenuList();
//		 ToolBarItemStatusCtrl.getParTabStatus()
		
		 for(MenuItem meunItem:listMenuItem){
				String menuItemId=meunItem.getId();
				 if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_new)||menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_edit)){
					 if(StringUtils.equals(editType, "card"))
						 setItemsStatus(meunItem,type,false);
				 }else if(menuItemId.endsWith(GuidedCfgController.ctrlItem_MenuBar_delete)){
					if(StringUtils.equals(editType, "card")){
						 if(StringUtils.equals(type, "new")||StringUtils.equals(type, "edit")){
							 setItemsStatus(meunItem,type,false);
						 }else{
							 if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_List))//主表处于编辑状态
								 setItemsStatus(meunItem,type,false);
							 else
								 setItemsStatus(meunItem,type,true);
						 }
					}
				 }else if(StringUtils.equals(menuItemId, CommBaseCreateComp.ctrlItem_MenuBar_Remove)){
					 if(StringUtils.equals(editType, "card")){
						 if(StringUtils.equals(type, "new")||StringUtils.equals(type, "edit")){
							 setItemsStatus(meunItem,type,false);
						 }else{
							 if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_Edit))//主表处于编辑状态
								 setItemsStatus(meunItem,type,false);
							 else
								 setItemsStatus(meunItem,type,true);
						 }
					}
				 }else if(StringUtils.equals(menuItemId, CommBaseCreateComp.ctrlItem_MenuBar_Save)){
					 if(StringUtils.equals(editType, "card")){
						 if(StringUtils.equals(type, "new")||StringUtils.equals(type, "edit")){
							 if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_List))//主表处于列表（非编辑）状态
								 setItemsStatus(meunItem,type,true);
							 else
								 setItemsStatus(meunItem,type,false);
						 }else{
								 setItemsStatus(meunItem,type,true);
						 }
					 }else if(StringUtils.equals(editType, "full")){
						 if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_List))//主表处于列表（非编辑）状态
							 setItemsStatus(meunItem,type,true);
					 }
				 }else if(StringUtils.equals(menuItemId, CommBaseCreateComp.ctrlItem_MenuBar_Ok)){
					if(StringUtils.equals(type, "new")||StringUtils.equals(type, "edit")){
						if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_Edit))//主表处于编辑状态
							setItemsStatus(meunItem,type,true);
						 else
							 setItemsStatus(meunItem,type,false);
					}else{
							 setItemsStatus(meunItem,type,true);
					}
				 }else if(StringUtils.equals(menuItemId, CommBaseCreateComp.ctrlItem_MenuBar_Cancle)){
					 setItemsStatus(meunItem,type,true);
				 }
				 
			 }
		
		
	}
	
	private void setItemsStatus(MenuItem menuBarItem,String type,boolean flag){
		 if(StringUtils.equals("new", type)||StringUtils.equals("edit", type))
			 menuBarItem.setVisible(flag);
		 else
			 menuBarItem.setVisible(!flag);
	}
}
