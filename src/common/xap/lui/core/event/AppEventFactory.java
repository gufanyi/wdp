package xap.lui.core.event;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.constant.DatasetConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.ParameterSet;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.JsEventDesc;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.ClassUtil;
public final class AppEventFactory {
	private IEventSupport source;
	private String level;
	public Object getController() {
		AppSession ctx = AppSession.current();
		String ctrlClazz = ctx.getParameter("clc");
		// 没有单独指定，走当前全局Controller
		if (ctrlClazz == null || ctrlClazz.trim().equals("")) {
			ctrlClazz = getControllerClazz();
		}
		if (ctrlClazz == null) {
			return null;
		} else {
			return ClassUtil.newInstance(ctrlClazz);
		}
	}
	private String getLevel() {
		if (level == null)
			level = AppSession.current().getParameter(AppSession.EVENT_LEVEL);
		return level;
	}
	private String getControllerClazz() {
		String level = getLevel();
		if (AppSession.EVENT_LEVEL_VIEW.equals(level))
			return AppSession.current().getViewContext().getView().getController();
		else if (AppSession.EVENT_LEVEL_WIN.equals(level))
			return AppSession.current().getWindowContext().getPagePartMeta().getController();
		else if (AppSession.current().getAppContext().getApplication() != null)
			return AppSession.current().getAppContext().getApplication().getControllerClazz();
		else
			return null;
	}
	private IEventSupport getSource() {
		if (source == null) {
			AppSession ctx = AppSession.current();
			String sourceType = ctx.getParameter(LuiPageContext.SOURCE_TYPE);
			String sourceId = ctx.getParameter(LuiPageContext.SOURCE_ID);
			if (LuiPageContext.SOURCE_TYPE_DATASET.equals(sourceType)) {
				source = AppSession.current().getViewContext().getView().getViewModels().getDataset(sourceId);
			} else if (LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM.equals(sourceType)) {
				String parentSourceId = ctx.getParameter(LuiPageContext.PARENT_SOURCE_ID);
				source = getMenuItem(sourceId, parentSourceId, ctx.getViewContext().getView());
			} else if (LuiPageContext.SOURCE_TYPE_PAGEMETA.equals(sourceType)) {
				source = AppSession.current().getWindowContext().getPagePartMeta();
			} else if (LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON.equals(sourceType)) {
				String parentSourceId = ctx.getParameter(LuiPageContext.PARENT_SOURCE_ID);
				ToolBarComp toolBar = (ToolBarComp) LuiAppUtil.getCntView().getViewComponents().getComponent(parentSourceId);
				source = toolBar.getElementById(sourceId);
			} else if(LuiPageContext.SOURCE_FORMELEMENT.equals(sourceType)) {
				String parentSourceId = ctx.getParameter(LuiPageContext.PARENT_SOURCE_ID);
				FormComp form = (FormComp)LuiAppUtil.getCntView().getViewComponents().getComponent(parentSourceId);
				source = form.getElementById(sourceId);
			} else if (LuiPageContext.SOURCE_TYPE_WIDGT.equals(sourceType)) {
				source = AppSession.current().getWindowContext().getPagePartMeta().getWidget(sourceId);
			} else if (LuiPageContext.SOURCE_TYPE_CONTEXTMENU_MENUITEM.equals(sourceType)) {
				String parentSourceId = ctx.getParameter(LuiPageContext.PARENT_SOURCE_ID);
				ContextMenuComp menubar = AppSession.current().getViewContext().getView().getViewMenus().getContextMenu(parentSourceId);
				List<MenuItem> list = menubar.getItemList();
				Iterator<MenuItem> it = list.iterator();
				MenuItem mItem = null;
				while (it.hasNext()) {
					MenuItem item = it.next();
					if (item.getId().equals(sourceId)) {
						mItem = item;
						break;
					}
					if (mItem == null) {
						mItem = getMenuItem(item, sourceId);
					}
				}
				if (mItem == null)
					throw new LuiRuntimeException("没有找到对应的MenuItem," + sourceId);
				source = mItem;
			} else if ("tag".equals(sourceType) || "cardlayout".equals(sourceType)) {
				source = UIElementFinder.findElementById(AppSession.current().getViewContext().getUIMeta(), sourceId);
				if (source == null) {
					source = UIElementFinder.findElementById((UIPartMeta) AppSession.current().getWindowContext().getUIPartMeta(), sourceId);
				}
			} else {
				if (sourceId != null)
					source = AppSession.current().getViewContext().getView().getViewComponents().getComponent(sourceId);
				else
					source = AppSession.current().getWindowContext().getPagePartMeta();
			}
		}
		return source;
	}
	private MenuItem getMenuItem(String menuItemId, String sourceId, ViewPartMeta widget) {
		MenubarComp menubar = widget.getViewMenus().getMenuBar(sourceId);
		List<MenuItem> list = menubar.getMenuList();
		Iterator<MenuItem> it = list.iterator();
		MenuItem mItem = null;
		while (it.hasNext()) {
			MenuItem item = it.next();
			if (item.getId().equals(menuItemId)) {
				mItem = item;
				break;
			}
			if (mItem == null) {
				mItem = getMenuItem(item, menuItemId);
			}
		}
		if (mItem == null)
			throw new LuiRuntimeException("没有找到对应的MenuItem," + sourceId);
		return mItem;
	}
	private MenuItem getMenuItem(MenuItem item, String sourceId) {
		List<MenuItem> items = item.getChildList();
		if (items != null && items.size() > 0) {
			Iterator<MenuItem> cIt = items.iterator();
			while (cIt.hasNext()) {
				MenuItem cItem = cIt.next();
				if (cItem.getId().equals(sourceId)) {
					return cItem;
				}
				cItem = getMenuItem(cItem, sourceId);
				if (cItem != null)
					return cItem;
			}
		}
		return null;
	}
	public AbstractServerEvent<?> getServerEvent() {
		AppSession ctx = AppSession.current();
		String eventName = ctx.getParameter(LuiPageContext.EVENT_NAME);
		String sourceType = ctx.getParameter(LuiPageContext.SOURCE_TYPE);
		JsEventDesc desc = getEventDesc(eventName);
		if (desc == null) {
			desc = new JsEventDesc(null, null);
			desc.setEventClazz(xap.lui.core.event.ScriptEvent.class.getName());
		}
		String clazz = desc.getEventClazz();
		try {
			Class<?> c = ClassUtil.forName(clazz);
			IEventSupport source = getSource();
			if (LuiPageContext.SOURCE_TYPE_DATASET.equals(sourceType)) {
				Dataset ds = (Dataset) source;
				if (eventName.equals(DatasetEvent.ON_DATA_LOAD)) {
					DatasetEvent serverEvent = new DatasetEvent(ds);
					ParameterSet params = ds.getReqParameters();
//					serverEvent.setOriginalPageIndex(ds.getRowSet().getPaginationInfo().getPageIndex());  TODO
					String pageIndexStr = params.getParameterValue(DatasetConstant.QUERY_PARAM_PAGEINDEX);
					int pageIndex = 0;
					if (pageIndexStr != null)
						pageIndex = Integer.parseInt(pageIndexStr);
					ds.getPaginationInfo().setPageIndex(pageIndex);
					return serverEvent;
				} else if (eventName.equals(DatasetEvent.ON_AFTER_ROW_INSERT)) {
					String insertIndex = ctx.getParameter("row_insert_index");
					RowInsertEvent event = new RowInsertEvent(ds);
					event.setInsertedIndex(Integer.valueOf(insertIndex));
					return event;
				} else if (eventName.equals(DatasetEvent.ON_AFTER_DATA_CHANGE) || eventName.equals(DatasetEvent.ON_BEFORE_DATA_CHANGE)) {
					String colSingle = ctx.getParameter("isColSingle");
					// 如果不是列批量修改
					if (colSingle == null || colSingle.equalsIgnoreCase("false")) {
						String rowIndex = ctx.getParameter("cellRowIndex");
						String colIndex = ctx.getParameter("cellColIndex");
						String newValue = ctx.getParameter("newValue");
						String oldValue = ctx.getParameter("oldValue");
						DatasetCellEvent event = new DatasetCellEvent(ds);
						event.setRowIndex(rowIndex == null ? -1 : Integer.parseInt(rowIndex));
						event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
						event.setNewValue(newValue);
						event.setOldValue(oldValue);
						return event;
					} else {
						// 处理某一列批量修改
						String rowIndex = ctx.getParameter("cellRowIndices");
						String colIndex = ctx.getParameter("cellColIndex");
						String newValue = ctx.getParameter("newValues");
						String oldValue = ctx.getParameter("oldValues");
						DatasetCellEvent event = new DatasetColSingleEvent(ds);
						String[] strRowIndices = rowIndex.split(",");
						int[] rowIndices = new int[strRowIndices.length];
						for (int i = 0; i < strRowIndices.length; i++) {
							rowIndices[i] = Integer.parseInt(strRowIndices[i]);
						}
						((DatasetColSingleEvent) event).setRowIndices(rowIndices);
						event.setColIndex(colIndex == null ? -1 : Integer.parseInt(colIndex));
						// 处理空值
						newValue = newValue.replaceAll(",,", ", ,");
						if (newValue.startsWith(","))
							newValue = " " + newValue;
						if (newValue.endsWith(","))
							newValue = newValue + " ";
						oldValue = oldValue.replaceAll(",,", ", ,");
						if (oldValue.startsWith(","))
							oldValue = " " + oldValue;
						if (oldValue.endsWith(","))
							oldValue = oldValue + " ";
						((DatasetColSingleEvent) event).setNewValues(newValue.split(","));
						((DatasetColSingleEvent) event).setOldValues(oldValue.split(","));
						return (DatasetCellEvent) event;
					}
				}
			} else if (LuiPageContext.SOURCE_TYPE_TREE.equals(sourceType)) {
				TreeViewComp tree = (TreeViewComp) source;
				if (TreeNodeDragEvent.ON_DRAG_START.equals(eventName)) {
					TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
					String sourceNodeRowId = ctx.getParameter("sourceNodeRowId");
					((TreeNodeDragEvent) serverEvent).setSourceNodeRowId(sourceNodeRowId);
					return serverEvent;
				} else if (TreeNodeDragEvent.ON_DRAG_END.equals(eventName)) {
					TreeNodeDragEvent serverEvent = new TreeNodeDragEvent(tree);
					String sourceNodeRowId = ctx.getParameter("sourceNodeRowId");
					String targetNodeRowId = ctx.getParameter("targetNodeRowId");
					((TreeNodeDragEvent) serverEvent).setSourceNodeRowId(sourceNodeRowId);
					((TreeNodeDragEvent) serverEvent).setTargetNodeRowId(targetNodeRowId);
					return serverEvent;
				} else if (TreeNodeEvent.ON_DBCLICK.equals(eventName) || TreeNodeEvent.ON_CLICK.equals(eventName)) {
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent) serverEvent).setNodeRowId(nodeRowId);
					return serverEvent;
				} else if (TreeNodeEvent.ON_CHECKED.equals(eventName)) {
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent) serverEvent).setNodeRowId(nodeRowId);
					return serverEvent;
				} else if (TreeNodeEvent.AFTER_SEL_NODE_CHANGE.equals(eventName)) {
					TreeNodeEvent serverEvent = new TreeNodeEvent(tree);
					String nodeRowId = ctx.getParameter("nodeRowId");
					((TreeNodeEvent) serverEvent).setNodeRowId(nodeRowId);
					String currentDsId = ctx.getParameter("datasetId");
					((TreeNodeEvent) serverEvent).setCurrentdsId(currentDsId);
					return serverEvent;
				}
			} else if (LuiPageContext.SOURCE_TYPE_GRID.equals(sourceType)) {
				GridComp grid = (GridComp) source;
				if (GridCellEvent.AFTER_EDIT.equals(eventName)) {
					GridCellEvent serverEvent = new GridCellEvent(grid);
					String rowIndex = ctx.getParameter("rowIndex");
					String colIndex = ctx.getParameter("colIndex");
					String newValue = ctx.getParameter("newValue");
					String oldValue = ctx.getParameter("oldValue");
					if (rowIndex != null && !rowIndex.equals(""))
						serverEvent.setRowIndex(Integer.parseInt(rowIndex));
					if (colIndex != null && !rowIndex.equals(""))
						serverEvent.setColIndex(Integer.parseInt(colIndex));
					serverEvent.setNewValue(newValue);
					serverEvent.setOldValue(oldValue);
					return serverEvent;
				} else if (GridCellEvent.BEFORE_EDIT.equals(eventName) || GridCellEvent.CELL_EDIT.equals(eventName) || GridCellEvent.CELL_VALUE_CHANGED.equals(eventName) || GridCellEvent.ON_CELL_CLICK.equals(eventName)) {
					GridCellEvent serverEvent = new GridCellEvent(grid);
					String rowIndex = ctx.getParameter("rowIndex");
					String colIndex = ctx.getParameter("colIndex");
					if (rowIndex != null)
						serverEvent.setRowIndex(Integer.parseInt(rowIndex));
					if (colIndex != null)
						serverEvent.setColIndex(Integer.parseInt(colIndex));
					return serverEvent;
				}
			}
			Constructor<?>[] cons = c.getConstructors();
			if (cons != null) {
				for (Constructor<?> con : cons) {
					try{
						return (AbstractServerEvent<?>) con.newInstance(source);
					}catch(Throwable e){
						 
					}
				
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			LuiLogger.error(e);
			throw new LuiRuntimeException("构造事件参数出现问题, class:" + clazz);
		}
	}
	private JsEventDesc getEventDesc(String eventName) {
		IEventSupport source = getSource();
		List<JsEventDesc> list = source.getAcceptEventDescs();
		if (list == null)
			return null;
		Iterator<JsEventDesc> it = list.iterator();
		while (it.hasNext()) {
			JsEventDesc desc = it.next();
			if (desc.getName().equals(eventName))
				return desc;
		}
		return null;
	}
}
