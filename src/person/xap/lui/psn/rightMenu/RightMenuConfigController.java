package xap.lui.psn.rightMenu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

public class RightMenuConfigController {

	public static final String RIGHTMENU_ROOT_ID = "tree_rightmenu_root_id";

	// form数据集加载时 给一个空行 并且设为可编辑，选中这一行，否则 Form 不能编辑
	public void rightMenuFormOnLoad(DatasetEvent e) {

		Dataset ds = e.getSource();
		Row row = ds.getEmptyRow();
		ds.addRow(row);
		ds.setEdit(true);
		ds.setSelectedIndex(0);
	}

	// 给 树 一个跟节点
	public void rightMenuTreeOnLoad(DatasetEvent e) {
		Dataset ds = e.getSource();
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("pid"), null);
		row.setValue(ds.nameToIndex("menuItemId"), RIGHTMENU_ROOT_ID);
		row.setValue(ds.nameToIndex("menuItemName"), "右键菜单树跟节点");
		ds.addRow(row);
		ds.setSelectedIndex(0);
	}

	/**
	 * 将菜单项加入到右边的数控件
	 */
	public void addRightMenuItemToTree(MouseEvent e) {
		Dataset dsRightMenuItem = getDataSetByid("ds_RightMenu");// 当前正在编辑的菜单项																	// Form的DataSet
		Row row = dsRightMenuItem.getCurrentPageData().getRow(0);
		Dataset dsTree = getDataSetByid("ds_TreeRightMenu");// 右边 tree的DataSet
		Row empRow = dsTree.getEmptyRow();
		Row selRow = dsTree.getSelectedRow();
		String pid = selRow == null ? null : (String) selRow.getValue(dsTree.nameToIndex("menuItemId"));

		String id = (String) row.getValue(dsRightMenuItem.nameToIndex("id"));
		if (StringUtils.isBlank(id))
			throw new LuiRuntimeException("菜单项ID不能为空！");
		Row[] rows = dsTree.getCurrentPageData().getRows();
		for (Row oneRow : rows) {
			if (StringUtils.equals(id,
					(String) oneRow.getValue(dsTree.nameToIndex("menuItemId")))) {
				throw new LuiRuntimeException("菜单树中已经存在Id为" + id + "的菜单项");
			}
		}
		String name = (String) row.getValue(dsRightMenuItem.nameToIndex("name"));
		if (StringUtils.isBlank(name))
			throw new LuiRuntimeException("菜单项名称不能为空！");
		String imgPath = (String) row.getValue(dsRightMenuItem.nameToIndex("imgPath"));

		empRow.setValue(dsTree.nameToIndex("pid"), pid);
		empRow.setValue(dsTree.nameToIndex("menuItemId"), id);
		empRow.setValue(dsTree.nameToIndex("menuItemName"), name);
		empRow.setValue(dsTree.nameToIndex("menuItemImg"), imgPath);
		dsTree.addRow(empRow);
		dsTree.setSelectedIndex(0);

	}

	// 保存菜单树
	public void saveMenuTree(MouseEvent e) {
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		IPaEditorService ipaService = new PaEditorServiceImpl();
		PagePartMeta pagemeta = ipaService.getOriPageMeta(pageId, sessionId);
		ViewPartMeta widget = pagemeta.getWidget(viewId);

		StringTextComp strTxtId = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("strTxt_MenuCtrlId");
		StringTextComp strTxtName = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("strTxt_MenuCtrlName");
		String strtxtIdValue = strTxtId.getValue();
		String strtxtNameValue = strTxtName.getValue();
		if (StringUtils.isBlank(strtxtIdValue))
			throw new LuiRuntimeException("菜单树Id不能为空哦！");
		if (StringUtils.isBlank(strtxtNameValue))
			throw new LuiRuntimeException("菜单数名称不能为空哦！");
		Dataset dsTree = getDataSetByid("ds_TreeRightMenu");// 右边 tree的DataSet
		ContextMenuComp menu = new ContextMenuComp();
		menu.setId(strtxtIdValue);
		List<MenuItem> childrenList = getChildMenuItem(dsTree,RIGHTMENU_ROOT_ID);
		if (childrenList!=null&&childrenList.size()!=0)
			menu.setItemList(childrenList);
		widget.getViewMenus().addContextMenu(menu);// 保存到view文件完毕
		saveRightMenuTreeToCtrlTree(dsTree,strtxtIdValue); //保存到 控件树 完毕
		AppSession.current().getAppContext().closeWinDialog();// 关闭当前窗口
	}

	// 取消按钮 点击事件
	public void cancelBtnEvent(MouseEvent e) {
		AppSession.current().getAppContext().closeWinDialog();//关闭新建菜单树对话框
	}

	/**
	 * 获取指定数据集id的DataSet
	 */
	private Dataset getDataSetByid(String dsid) {
		ViewPartMeta widget = AppSession.current().getAppContext().getCurrentWindowContext().getPagePartMeta().getWidget("main");
		return widget.getViewModels().getDataset(dsid);
	}
	/**
	 * 遍历ContextMenuItem 加入到MenuItem里
	 */
	private List<MenuItem> getChildMenuItem(Dataset dsTree, String pid) {
		Row[] rows = dsTree.getCurrentPageData().getRows();
		List<MenuItem> listMenuItem = new ArrayList<MenuItem>();
		for (Row row : rows) {
			String _pid = (String) row.getValue(dsTree.nameToIndex("pid"));
			if (StringUtils.equals(pid, _pid)) {
				MenuItem menuItem = new MenuItem();
				String _id = (String) row.getValue(dsTree.nameToIndex("menuItemId"));
				menuItem.setId(_id);
				menuItem.setText((String) row.getValue(dsTree.nameToIndex("menuItemName")));
				menuItem.setImgIcon((String) row.getValue(dsTree.nameToIndex("menuItemImg")));
				listMenuItem.add(menuItem);
				List<MenuItem> childrenList = getChildMenuItem(dsTree, _id);
				if (childrenList!=null&&childrenList.size()!=0) {
					menuItem.setChildList(childrenList);
				}
			}
		}
		return listMenuItem;
	}
	/**
	 * 新建的 菜单树 保存到 已定义控件树上
	 */
	private void saveRightMenuTreeToCtrlTree(Dataset dsTree,String ctrlTreeId){
		Dataset ds = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("ctrlds");
		Row[] rows = dsTree.getCurrentPageData().getRows();
		for (Row row : rows) {
			String pid = (String) row.getValue(dsTree.nameToIndex("pid"));
			if (pid==null) {
				Row eptrow = ds.getEmptyRow();
				eptrow.setValue(ds.nameToIndex("id"), ctrlTreeId);
				eptrow.setValue(ds.nameToIndex("pid"), " ");
				eptrow.setValue(ds.nameToIndex("name"), "右键菜单");
				eptrow.setValue(ds.nameToIndex("type"),LuiPageContext.SOURCE_TYPE_CONTEXTMENU);
				eptrow.setValue(ds.nameToIndex("type2"),LuiPageContext.SOURCE_TYPE_CONTEXTMENU);
				eptrow.setValue(ds.nameToIndex("imgtype"),LuiPageContext.SOURCE_TYPE_MENUBAR);
				ds.addRow(eptrow);
			} else {
				Row eptrow = ds.getEmptyRow();
				eptrow.setValue(ds.nameToIndex("id"),row.getValue(dsTree.nameToIndex("menuItemId")));
				String thisRowPid=(String)row.getValue(dsTree.nameToIndex("pid"));
				eptrow.setValue(ds.nameToIndex("pid"),StringUtils.equals(thisRowPid, RightMenuConfigController.RIGHTMENU_ROOT_ID)?ctrlTreeId:thisRowPid);
				eptrow.setValue(ds.nameToIndex("name"),row.getValue(dsTree.nameToIndex("menuItemName")));
				eptrow.setValue(ds.nameToIndex("type"),LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
				eptrow.setValue(ds.nameToIndex("type2"),LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
				eptrow.setValue(ds.nameToIndex("imgtype"),LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM);
				ds.addRow(eptrow);
			}
		}
	}
	

}
