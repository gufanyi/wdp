package xap.lui.psn.designer;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Row;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.mw.core.utils.StringUtil;

/**
 * 定义 ProjViewTree 上的右键菜单项 <br/>
 * 不同节点类型对应不同的右键菜单项
 * 
 * @author Yongsheng.Qi
 */
public class PaProjViewTreeDsCtxMenu {
	
	private final String Evt_Controller = PaProjViewTreeController.class.getName();
	
	private StringBuilder str_buf = null;
	
	protected PaProjViewTreeDsCtxMenu() {
		this.str_buf = new StringBuilder(32);
	}
	
	protected void clear() {
		this.str_buf.setLength(0);
		this.str_buf.trimToSize();
	}
	
	/**
	 * 根据当前选中的树节点类型，向树控件右键菜单填充不同的项
	 * 
	 * @param dataSet
	 * @param ctxMenu
	 */
	protected void refillCtxMenu(Dataset dataSet, ContextMenuComp ctxMenu) {
		ctxMenu.removeChildrenItem();
		
		Row row_sel = dataSet.getSelectedRow();
		if(null == row_sel) return;
		String type_row_sel = (String)row_sel.getValue(dataSet.nameToIndex("type"));
		
		switch(type_row_sel) {
		case DesignerMainViewController.NODE_TYPE_APPS:
			this._fill_apps_ctxmenu(ctxMenu, dataSet);
			break;
			
		case DesignerMainViewController.NODE_TYPE_WINS:
			this._fill_wins_ctxmenu(ctxMenu, dataSet);
			break;
		case DesignerMainViewController.NODE_TYPE_PUBVIEWS:
			this._fill2_comm_ctxmenu(0x01, ctxMenu, dataSet, "PubView", "公共View", null);
			break;
			
		case DesignerMainViewController.NODE_TYPE_PAGE:
			this._fill_page_ctxmenu(ctxMenu, dataSet);
			break;
		case DesignerMainViewController.NODE_TYPE_VIEW:
			this._fill2_comm_ctxmenu(0x06, ctxMenu, dataSet, "View", "View", null);
			break;
			
		case DesignerMainViewController.NODE_TYPE_APP:
			this._fill_app_ctxmenu(ctxMenu, dataSet);
			break;
/*			
		case DesignerMainViewController.NODE_TYPE_DS_LIST:
			this._fill_dslist_ctxmenu(ctxMenu, dataSet);
			break;
		case DesignerMainViewController.NODE_TYPE_DS:
			this._fill2_comm_ctxmenu(0x12, ctxMenu, dataSet, "Ds", "数据集", null);
			break;
			
		case DesignerMainViewController.NODE_TYPE_REF_LIST:
			this._fill_reflist_ctxmenu(ctxMenu, dataSet);
			break;
		case DesignerMainViewController.NODE_TYPE_REF:
			this._fill2_comm_ctxmenu(0x12, ctxMenu, dataSet, "Ref", "参照", null);
			break;
			
		case DesignerMainViewController.NODE_TYPE_COMBO_LIST:
			this._fill2_comm_ctxmenu(0x01, ctxMenu, dataSet, "ComboList", "下拉数据集", null);
			break;
		case DesignerMainViewController.NODE_TYPE_COMBO:
			this._fill2_comm_ctxmenu(0x12, ctxMenu, dataSet, "Combo", "下拉数据集", null);
			break;
			
		case DesignerMainViewController.NODE_TYPE_VIEW_CTRLIST:
			this._fill_ctrllist_ctxmenu(ctxMenu, dataSet);
			break;
		case DesignerMainViewController.NODE_TYPE_VIEW_CTRL:
			this._fill2_comm_ctxmenu(0x12, ctxMenu, dataSet, "Ctrl", "控件", null);
			break;
*/
		}
		
		return;
	}
	
	private void _fill_apps_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;
		
		menuItem = this._create_menu("new", "新建", "App", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "App"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("refresh", "刷新", null, null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Refresh", "App"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_wins_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;
		
		menuItem = this._create_menu("new", "新建", "Win", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Win"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("refresh", "刷新", null, null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Refresh", "Win"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_page_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;

		menuItem = this._create_menu("edit", "编辑", "Page", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Edit", "Page"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("save", "保存", "Page", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Save", "Page"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("delete", "删除", "Page", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Delete", "Page"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("new_view", "新建", "View", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "View"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_app_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;

		menuItem = this._create_menu("edit", "编辑", "App", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Edit", "App"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("save", "保存", "App", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Save", "App"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("delete", "删除", "App", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Delete", "App"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_dslist_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;
		
		menuItem = this._create_menu("new_ds", "新建", "数据集", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Ds"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("new_refds", "新建", "引用数据集", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "RefDs"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("edit_ds", "编辑", "数据集", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Edit", "Ds"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_reflist_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;
		
		menuItem = this._create_menu("new_ref", "新建", "参照", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Ref"));
		ctxMenu.addMenuItem(menuItem);

		menuItem = this._create_menu("new_refrel", "新建", "参照关系", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "RefRel"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	private void _fill_ctrllist_ctxmenu(ContextMenuComp ctxMenu, Dataset dsTree) {
		MenuItem menuItem = null;
		
		menuItem = this._create_menu("new_grid", "新建", "表格", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Grid"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("new_tree", "新建", "树", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Tree"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("new_billform", "新建", "表单", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "BillForm"));
		ctxMenu.addMenuItem(menuItem);
		
		menuItem = this._create_menu("new_menu", "新建", "菜单", null);
		menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", "Menu"));
		ctxMenu.addMenuItem(menuItem);
	}
	
	/**
	 * 常规增删改查菜单项
	 * @param crudFlag	从低到高的5个bit依次表示new(0x01), delete(0x02), edit(0x04), refresh(0x08), copy(0x10)
	 * @param ctxMenu	菜单项集合
	 * @param opObjInfo	操作对象信息
	 * @param tag
	 */
	private void _fill2_comm_ctxmenu(
			int crudFlag, ContextMenuComp ctxMenu, Dataset dsTree,
			String opObjInfoEn, String opObjInfoCn, String tag) {
		MenuItem menuItem = null;
		
		if(0x01 == (crudFlag & 0x01)) {
			menuItem = this._create_menu("new", "新建", opObjInfoCn, tag);
			menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "New", opObjInfoEn));
			ctxMenu.addMenuItem(menuItem);
		}
		
		if(0x02 == (crudFlag & 0x02)) {
			menuItem = this._create_menu("delete", "删除", opObjInfoCn, tag);
			menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Delete", opObjInfoEn));
			ctxMenu.addMenuItem(menuItem);
		}
		
		if(0x04 == (crudFlag & 0x04)) {
			menuItem = this._create_menu("edit", "编辑", opObjInfoCn, tag);
			menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Edit", opObjInfoEn));
			ctxMenu.addMenuItem(menuItem);
		}
		
		if(0x08 == (crudFlag & 0x08)) {
			menuItem = this._create_menu("refresh", "刷新", opObjInfoCn, tag);
			menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Refresh", opObjInfoEn));
			ctxMenu.addMenuItem(menuItem);
		}
		
		if(0x10 == (crudFlag & 0x10)) {
			menuItem = this._create_menu("copy", "拷贝", opObjInfoCn, tag);
			menuItem.addEventConf(this._crt_ctxmenu_evt(dsTree, "Copy", opObjInfoEn));
			ctxMenu.addMenuItem(menuItem);
		}
		
		return;
	}
	
	/**
	 * 生成菜单项 MenuItem
	 * 
	 * @param opNameEn
	 * @param opNameCn
	 * @param opObjInfo
	 * @param tag
	 * @return
	 */
	private MenuItem _create_menu(String opNameEn, String opNameCn, String opObjInfo, String tag) {
		MenuItem menuItem = null;
		
		menuItem = new MenuItem();
		menuItem.setId(opNameEn);
		
		this.str_buf.setLength(0);
		this.str_buf.append(opNameCn);
		if(!StringUtil.isEmpty(opObjInfo)) {
			this.str_buf.append(' ');
			this.str_buf.append(opObjInfo);		
		}
		menuItem.setText(this.str_buf.toString());
		menuItem.setTag(tag);
		
		return menuItem;
	}
	
	/**
	 * 为菜单项生成 Event子节点 <br/>
	 * ID命名规则：on + 操作名 + 操作对象名
	 * 
	 * @param dsTree
	 * @param opNameEn	操作名称，一般首字母大写
	 * @param opObjInfo	操作对象名，一般首字母大写
	 * @return
	 */
	private LuiEventConf _crt_ctxmenu_evt(Dataset dsTree, String opNameEn, String opObjInfo) {
		Row sel_ds_row = dsTree.getSelectedRow();
		
		LuiEventConf evt = new LuiEventConf();
		
		evt.setAsync(true);
		evt.setEventType("MouseEvent");
		evt.setMethod("onClickCtxMenu");
		evt.setEventName("onclick");
		evt.setOnserver(true);
		evt.setControllerClazz(this.Evt_Controller);
		
		// 
		DatasetRule ds_rule = new DatasetRule();
		ds_rule.setId(dsTree.getId());
		ds_rule.setType("ds_current_line");
		WidgetRule view_rule = new WidgetRule();
		view_rule.setId("data");
		view_rule.addDsRule(ds_rule);
		EventSubmitRule evt_rule = new EventSubmitRule();
		evt_rule.setCardSubmit(false);
		evt_rule.addWidgetRule(view_rule);
		evt.setSubmitRule(evt_rule);
		
		// 
		LuiParameter param = new LuiParameter();
		param.setName("nodeName");
		int tree_nodename_inx = dsTree.nameToIndex("name");
		param.setValue((String) sel_ds_row.getValue(tree_nodename_inx));
		evt.addParam(param);
		
		param = new LuiParameter();
		param.setName("opNameEn");
		param.setValue(opNameEn);
		evt.addParam(param);
		
		param = new LuiParameter();
		param.setName("opObjInfo");
		param.setValue(opObjInfo);
		evt.addParam(param);
		
		return evt;
	}
}
