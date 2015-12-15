package xap.lui.psn.menumgr;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.state.ButtonStateManager;
import xap.lui.core.util.ObjSelectedUtil;
import xap.mw.coreitf.d.FBoolean;
public class MenuMgrController implements IWindowCtrl {
	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	public void onDataLoad(DatasetEvent DatasetEvent) {
		// 得到当前的数据集
		Dataset currDs = DatasetEvent.getSource();
		Row row = currDs.getEmptyRow();
		int idIndex = currDs.nameToIndex("id");
		int labelIndex = currDs.nameToIndex("label");
		int visibleIndex = currDs.nameToIndex("visible");
		String type = getTypeEdit();
		if ("menubar".equals(type)) {
			MenubarComp menu = getEditMenu();
			/**
			 * 对于menu数据的操作
			 */
			List<MenuItem> items = menu.getMenuList();
			if (items != null && items.size() > 0) {
				for (int i = 0; i < items.size(); i++) {
					row = currDs.getEmptyRow();
					MenuItem item = items.get(i);
					row.setValue(idIndex, item.getId());
					row.setValue(labelIndex, item.getText());
					row.setValue(visibleIndex, FBoolean.valueOf(item.isVisible()));
					currDs.addRow(row);
				}
			}
		}
		currDs.setEdit(true);
		ButtonStateManager.updateButtons();
	}
	public void onAddEvent(MouseEvent<MenuItem> mouseEvent) {
		Dataset ds = getCntView().getViewModels().getDataset("ds_proxy");
		Row row = ds.getEmptyRow();
		row.setBoolean(ds.nameToIndex("visible"), true);
		ds.addRow(row);
		ds.setEdit(true);
	}
	public void onDeleteEvent(MouseEvent<MenuItem> mouseEvent) {
		Dataset ds = getCntView().getViewModels().getDataset("ds_proxy");
		Row row = ds.getSelectedRow();
		ds.removeRow(row);
	}
	public void onMoveUpEvent(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset("ds_proxy");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		int currIndex = ds.getSelectedIndex().intValue();
		/**
		 * 调整所选控件的位置
		 */
		if (0 < currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, currIndex - 1);
			ds.setRowSelectIndex(currIndex - 1);
		}
	}
	public void onMoveDownEvent(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset("ds_proxy");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 向下移动，如果最后一行，则不调整
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex + 1 < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, currIndex + 1);
			ds.setRowSelectIndex(currIndex + 1);
		}
	}
	public void onMoveTopEvent(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset("ds_proxy");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 将当前选中行，调整到最顶端
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, 0);
			ds.setRowSelectIndex(0);
		}
	}
	public void onMoveBottomEvent(MouseEvent<MenuItem> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset("ds_proxy");
		Row row = ds.getSelectedRow();
		if (row == null)
			throw new LuiRuntimeException("请选中要移动的数据！");
		/**
		 * 将当前选中行，调整到最底端
		 */
		int currIndex = ds.getSelectedIndex().intValue();
		if (0 <= currIndex && currIndex < ds.getCurrentPageRowCount()) {
			ds.getCurrentPageData().moveRow(currIndex, ds.getCurrentPageRowCount() - 1);
			ds.setRowSelectIndex(ds.getCurrentPageRowCount() - 1);
		}
	}
	private ViewPartMeta getCntView() {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		return widget;
	}
	public void onOkEvent(MouseEvent<ButtonComp> mouseEvent) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset rightDs = widget.getViewModels().getDataset("ds_proxy");
		ViewPartMeta editWidget = getEditWidget();
		String type = getTypeEdit();
		if ("menubar".equals(type)) {
			reconstructEditMenu(rightDs, editWidget);
		}
		MenuMgrController.triggerUpdate();
		/**
		 * 正常执行完后，关闭当前的对话框
		 */
		getCurrentAppContext().closeWinDialog();
	}
	public void onCancelEvent(MouseEvent<ButtonComp> mouseEvent) {
		AppSession.current().getAppContext().closeWinDialog();
	}
	/**
	 * 获取要编辑的widget
	 */
	private ViewPartMeta getEditWidget() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		@SuppressWarnings("restriction")
		String sessionId = this.getSessionId();
		String viewId = session.getOriginalParameter("sourceViewId");
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		return widget;
	}
	public String getSessionId() {
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		if (sessionId == null) {
			sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		}
		return sessionId;
	}
	/**
	 * 获取编辑的控件类型
	 * 
	 * @return
	 */
	private String getTypeEdit() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String type = session.getOriginalParameter("sourceType");
		return type;
	}
	/**
	 * 重新构造Menu控件
	 * 
	 * @param rightDs
	 * @param editWidget
	 */
	private void reconstructEditMenu(Dataset rightDs, ViewPartMeta editWidget) {
		MenubarComp menu = getEditMenu();
		MenubarComp tempMenu = (MenubarComp) menu.clone();
		Row[] rtRows = rightDs.getCurrentPageData().getRows();
		/**
		 * 如果右边数据集为空，则说明无内容，清除控件内容
		 */
		if (rtRows == null || rtRows.length == 0) {
			clearMenu(menu);
		} else {
			clearMenu(menu);
			for (int i = 0; i < rtRows.length; i++) {
				Row rtRow = rtRows[i];
				if (rtRow != null) {
					LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
					// 将ajax的状态置为nullstatus
					RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
					String id = (String) rtRow.getValue(rightDs.nameToIndex("id"));
					String text = (String) rtRow.getValue(rightDs.nameToIndex("label"));
					FBoolean vis = (FBoolean) rtRow.getValue(rightDs.nameToIndex("visible"));
					MenuItem item = null;
					if (tempMenu.getMenuList() != null && tempMenu.getMenuList().size() != 0) {
						item = tempMenu.getItem(id);
						if (item == null)
							item = new MenuItem(id);
					} else
						item = new MenuItem(id);
					item.setId(id);
					item.setText(text);
					item.setVisible(vis.booleanValue());
					if(StringUtils.isBlank(item.getImgIcon())) {
						item.setImgIcon("new.png");
					}
					item.setMenu(menu);
					menu.addMenuItem(item);
					RequestLifeCycleContext.get().setPhase(phase);
				}
			}
		}
		/**
		 * 触发前台代码进行对应的调整
		 */
		UIElement ele = UIElementFinder.findElementById(getEditUIMeta(), menu.getId());
		String id = ObjSelectedUtil.toSelected(getEditUIMeta(), getEditPageMeta(), ele);
		getCurrentAppContext().addExecScript("var obj = {id : '" + id + "', type : 'menubar' , widgetid :'" + editWidget.getId() + "', eleid :'" + menu.getId() + "'}; \n");
		getCurrentAppContext().addExecScript("parent.toOperate(obj, 'repaintMenuBarComp');\n");
	}
	/**
	 * 获取要编辑的Menu
	 * 
	 * @return
	 */
	private MenubarComp getEditMenu() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String eleId = session.getOriginalParameter("sourceEleId");
		ViewPartMeta widget = getEditWidget();
		MenubarComp menu = null;
		menu = widget.getViewMenus().getMenuBar(eleId);
		if (menu == null)
			menu = (MenubarComp) widget.getViewComponents().getComponent(eleId);
		return menu;
	}
	/**
	 * 获取要编辑的pagemeta
	 */
	private PagePartMeta getEditPageMeta() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		String sessionId = this.getSessionId();
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		return pagemeta;
	}
	/**
	 * 获取当前编辑的uimeta
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	private UIPartMeta getEditUIMeta() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes =  new PaEditorServiceImpl();
		String pageId = session.getOriginalParameter("sourceWinId");
		String sessionId = this.getSessionId();
		UIPartMeta meta = pes.getOriUIMeta(pageId, sessionId);
		return meta;
	}
	/**
	 * 清除menu中的元素
	 * 
	 * @param menu
	 */
	private void clearMenu(MenubarComp menu) {
		List<MenuItem> items = menu.getMenuList();
		if (items == null || items.size() == 0)
			return;
		items.clear();
	}
	/**
	 * 触发前台事件进行相应的更新修改
	 */
	public static void triggerUpdate() {
		ModePhase modePhase =LuiRuntimeContext.getModePhase();
		//if (modePhase != null && modePhase.equals(ModePhase.eclipse))
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
	}
	/**
	 * 获取当前的Application的上下文
	 * 
	 * @return
	 */
	protected AppContext getCurrentAppContext() {
		return AppSession.current().getAppContext();
	}
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		ButtonStateManager.updateButtons();
	}
}
