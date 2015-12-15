package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.GridContext;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.render.PCGridCompRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Grid控件后台对应的配置类
 *
 */
@XmlRootElement(name = "Grid")
@XmlAccessorType(XmlAccessType.NONE)
public class GridComp extends WebComp implements IDataBinding, IContainerComp<GridColumn> {
	private static final long serialVersionUID = -525473184313225199L;

	public static final String WIDGET_NAME = "grid";
	@XmlAttribute
	private String dataset;

	// gird的headers列表

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	@XmlElementRefs({ @XmlElementRef(name = "Column", type = GridColumn.class), @XmlElementRef(name = "ColumnGroup", type = GridColumnGroup.class),
						@XmlElementRef(name = "Property", type = Property.class), @XmlElementRef(name = "PropertyGroup", type = PropertyGroup.class)})
	protected List<IGridColumn> columnList = null;
	// 是否为可编辑grid
	@XmlAttribute(name = "isEdit")
	private boolean isEdit = true;
	@XmlAttribute(name = "isAllowMouseoverChange")
	private boolean isAllowMouseoverChange = true; // 是否允许鼠标悬浮改变行颜色
	// 是否显示固定选择列
	@XmlAttribute(name = "isMultiple")
	private boolean isMultiple = false;
	// grid单行高度
	@XmlAttribute
	private String rowHeight = null;
	// grid表头单行高度
	@XmlAttribute
	private String headerRowHeight = null;
	// 是否显示数字列
	@XmlAttribute(name = "isShowNum")
	private boolean isShowNum = false;
	// 是否显示合计行
	@XmlAttribute(name = "isShowTotalRow")
	private boolean isShowTotalRow = false;
	// 服务器端grid分页每页数目
	@XmlAttribute
	private String pageSize = null;
	// 分组的headerIds按照给定的顺序分组,各个header间逗号分隔
	@XmlAttribute
	private String groupColumns = null;//用于标记表格分组列是否改变

	// 整体是否可以排序
	@XmlAttribute
	private boolean sortable = true;
	// 是否显示表头
	@XmlAttribute(name = "isShowHeader")
	private boolean isShowHeader = true;
	// 是否显示功能按钮
	@XmlAttribute(name = "isShowToolbar")
	private boolean isShowToolbar = false;
	// 自定义功能按钮Render
	@XmlAttribute(name = "toolbarRender")
	private String toolbarRender = null;

	// 当前选中的列Id
	@XmlAttribute
	private String currentColID;

	// 是否展开树表
	@XmlAttribute(name = "isExpandTree")
	private boolean isExpandTree = false;
	@XmlAttribute(name = "isCopy")
	private boolean isCopy = true;

	// 是否显示提示
	@XmlAttribute(name = "isShowTip")
	private boolean isShowTip = true;
	
	@XmlAttribute
	private String headerPosition;

	@JSONField(serialize = false)
	private PCGridCompRender render;

	public String getCurrentColID() {
		return currentColID;
	}

	public void setCurrentColID(String currentColID) {
		this.currentColID = currentColID;
	}

	/**
	 * 是否参照的grid
	 */
	private boolean simplePagination = false;

	public boolean isSimplePagination() {
		return simplePagination;
	}

	public void setSimplePagination(boolean simple) {
		this.simplePagination = simple;
	}

	// 显示名称
	@XmlAttribute
	private String caption;

	// 显示列ID字符串，用“,”分割，中间不能有空格，用于在后台中向前台传递context
	@XmlAttribute
	private String showColumns;
	// 树表Level
	// @XmlElementRef(name = "RecursiveGridLevel", type = GridTreeLevel.class)
	@XmlElement(name = "RecursiveGrid")
	private GridTreeLevel topLevel = null;
	// row render类 行渲染
	@XmlAttribute
	private String rowRender;
	// 行高自适应
	@XmlAttribute(name = "isFitRowHeight")
	private Boolean isFitRowHeight = false;
	// 自定义 cellEditor方法 cellEditor
	@XmlAttribute
	private String extendCellEditor;
	// 显示描述信息
	private String[] gridDescContents = null;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 判断单双行类型，“0”为单双行各一行交错排列；“1”为单行1行，双行2行交错排列
	 */
	private String oddType = "1";

	public String getOddType() {
		return oddType;
	}

	public void setOddType(String oddType) {
		this.oddType = oddType;
	}

	public boolean isShowHeader() {
		return isShowHeader;
	}

	public void setShowHeader(boolean isShowHeader) {
		this.isShowHeader = isShowHeader;
	}

	// 分页工具条是否在顶端
	@XmlAttribute(name = "isPageTop")
	private boolean isPageTop = false;
	// 是否可显示“显示列”和“锁定列”菜单
	@XmlAttribute(name = "isShowColMenu")
	private boolean isShowColMenu = false;
	// Grid右肩菜单
	private MenubarComp menuBar = new MenubarComp();

	// 初始化Grid右肩菜单
	private static void initGridMenubar(GridComp gc) {
		String[] itemIds = new String[] { "gridHeaderBtn_Add", "gridHeaderBtn_Edit", "gridHeaderBtn_Delete" };
		// String[] itemCaptions = new String[]{"Add","Edit","Delete"};
		// String[] itemCaptions = new String[]{"新建","编辑","删除"};
		String[] itemTip = new String[] { "新建", "编辑", "删除" };
		// String[] itemI18nNames = new String[]{"GridComp-000005",
		// "GridComp-000006", "GridComp-000007"};
		String[] itemRealImgIcons = new String[] { "platform/theme/${theme}/global/images/icon/16/new.png", "platform/theme/${theme}/global/images/icon/16/edit.png",
				"platform/theme/${theme}/global/images/icon/16/delete.png" };
		String[] eventMethodNames = new String[] { "onGridAddClick", "onGridEditClick", "onGridDeleteClick" };
		for (int i = 0; i < itemIds.length; i++) {
			MenuItem item = new MenuItem(itemIds[i]);
			// item.setText(itemCaptions[i]);
			// item.setI18nName(itemI18nNames[i]);
			item.setTip(itemTip[i]);
			item.setLangDir("bc");
			item.setImgIcon(itemRealImgIcons[i]);
			LuiEventConf event = new LuiEventConf();
			event.setMethod(eventMethodNames[i]);
			// event.setControllerClazz(BillViewController.class.getName());
			event.setEventType(MouseEvent.class.getSimpleName());
			event.setOnserver(true);
			event.setEventName("onclick");
			item.addEventConf(event);
			{
				EventSubmitRule sr = new EventSubmitRule();
				WidgetRule  widgetRule=new WidgetRule();
				widgetRule.setId(gc.getWidgetName());
				sr.addWidgetRule(widgetRule);
				DatasetRule datasetRule=new DatasetRule();
				String dsId=gc.getDataset();
				datasetRule.setId(dsId);
				datasetRule.setType(DatasetRule.TYPE_ALL_LINE);
				widgetRule.addDsRule(datasetRule);
			}
			LifeCyclePhase pa = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			gc.getMenuBar().addMenuItem(item);
			RequestLifeCycleContext.get().setPhase(pa);
		}
	}

	public GridComp() {
		super();
		// zww 2015/9/28 如果展示grid的工具栏再进行实例化 否则不触发
		// if(isShowToolbar){
		initGridMenubar(this);
		// }
	}

	public GridComp(String id) {
		super(id);
		initGridMenubar(this);
	}

	public List<IGridColumn> getColumnList() {
		return columnList;
	}
	
	public IGridColumn getColumnById(String id) {
		Iterator<IGridColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IGridColumn inner = it.next();
			if (inner instanceof GridColumn) {
				GridColumn column = (GridColumn) inner;
				if (column.getId().equals(id))
					return column;
			} else if (inner instanceof GridColumnGroup) {
				GridColumnGroup gridColGroup = (GridColumnGroup) inner;
				if (gridColGroup.getId().equals(id)) {
					return gridColGroup;
				}else{
					if(gridColGroup.getColumnById(id) == null)
						continue;
					return gridColGroup.getColumnById(id);
				}
			}
		}
		return null;
	}

	public IGridColumn getColumn(int index) {
		return columnList.get(index);
	}

	public IGridColumn getColumnByField(String field) {
		Iterator<IGridColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IGridColumn column = it.next();
			if (column instanceof GridColumnGroup) {
				GridColumnGroup gridColGroup = (GridColumnGroup) column;
				return gridColGroup.getColumnByField(field);
//				List<IGridColumn> childList = gridColGroup.getChildColumnList();
//				for (int i = 0; i < childList.size(); i++) {
//					IGridColumn col = (IGridColumn) childList.get(i);
//					if (((GridColumn) col).getField().equals(field))
//						return col;
//				}
			} else {
				GridColumn col = (GridColumn) column;
				String fd = col.getField();
				if (fd != null && fd.equals(field))
					return column;
			}
		}
		return null;
	}

	/**
	 * 设置表头列表
	 * 
	 * @param columnList
	 */
	public void setColumnList(List<IGridColumn> columnList) {
		this.columnList = columnList;
		if (columnList != null && columnList.size() > 0) {
			Iterator<IGridColumn> it = columnList.iterator();
			while (it.hasNext()) {
				IGridColumn col = it.next();
				col.setGridComp(this);
			}
		}
	}

	/**
	 * 增加表头
	 * 
	 * @param col
	 *            gridCoulumn
	 */
	public void addColumn(IGridColumn col) {
		if (this.columnList == null) {
			this.columnList = new ArrayList<IGridColumn>();
		}
		this.columnList.add(col);
		col.setGridComp(this);
		if (col instanceof GridColumnGroup) {
			GridColumnGroup gridgroup = (GridColumnGroup) col;
			List<IGridColumn> children = gridgroup.getChildColumnList();
			if (children != null && children.size() > 0) {
				for (int j = 0; j < children.size(); j++) {
					IGridColumn colu = children.get(j);
					colu.setGridComp(this);
				}
			}
		}
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addColumn(col);
		}
	}

	public void addColumns(List<IGridColumn> columns) {
		for (IGridColumn col : columns)
			addColumn(col);
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			this.getRender().addColumns(columns);
//		}
	}

	public void insertColumn(int index, IGridColumn col) {
		if (index < 0 || index > getColumnCount())
			index = getColumnCount();
		if (this.columnList == null) {
			this.columnList = new ArrayList<IGridColumn>();
		}
		col.setGridComp(this);
		this.columnList.add(index, col);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addColumn(col);
		}
	}

	public int getColumnCount() {
		return this.columnList.size();
	}

	/**
	 * 移除表头列(只移除和ds field关联的列,即GridColumn类型的列)
	 * 
	 * @param fieldId
	 *            该列所引用的ds的fieldId
	 */
	public void removeColumnByField(String fieldId) {
		if (fieldId == null)
			return;
		if (this.columnList != null) {
			for (int i = 0; i < this.columnList.size(); i++) {
				if (this.columnList.get(i) instanceof GridColumn) {
					GridColumn colum = (GridColumn) this.columnList.get(i);
					if (colum.getField() != null && colum.getField().equals(fieldId)) {
						this.columnList.remove(i);
						if (LifeCyclePhase.ajax.equals(getPhase())) {
							this.getRender().deleteColumn(colum);
						}
					}
				}
			}

		}
	}

	public void removeColumn(IGridColumn col) {
		columnList.remove(col);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().deleteColumn(col);
		}
	}

	public void removeColumns(List<IGridColumn> columns) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().deleteColumns(columns);
		}
		for (int i = columns.size() - 1; i >= 0; i--) {
			IGridColumn col = columns.get(i);
			columnList.remove(col);
		}
	}

	public void setGridTipContent(String gridTipContent) {
		this.getRender().setGridTipContent(gridTipContent);
	}

	public void setDataset(String datasetId) {
		this.dataset = datasetId;
	}

	public String getDataset() {
		return dataset;
	}

	public Object clone() {
		GridComp grid = (GridComp) super.clone();
		if (this.columnList != null) {
			grid.columnList = new ArrayList<IGridColumn>();
			Iterator<IGridColumn> it = this.columnList.iterator();
			while (it.hasNext())
				grid.addColumn((IGridColumn) it.next().clone());
		}
		if (this.topLevel != null) {
			grid.setTopLevel((GridTreeLevel) this.topLevel.clone());
		}
		return grid;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean editable) {
		if (editable != this.isEdit) {
			this.isEdit = editable;
			setCtxChanged(true);
		}
	}

	public boolean isShowTip() {
		return isShowTip;
	}

	public void setShowTip(boolean isShowTip) {
		this.isShowTip = isShowTip;
	}

	public boolean isAllowMouseoverChange() {
		return isAllowMouseoverChange;
	}

	public void setAllowMouseoverChange(boolean isAllowMouseoverChange) {
		if (isAllowMouseoverChange != this.isAllowMouseoverChange) {
			this.isAllowMouseoverChange = isAllowMouseoverChange;
			setCtxChanged(true);
		}
	}

	public String getHeaderPosition() {
		return headerPosition;
	}

	public void setHeaderPosition(String headerPosition) {
		if(!StringUtils.equals(this.headerPosition, headerPosition)) {
			this.headerPosition = headerPosition;
			if(LifeCyclePhase.ajax.equals(this.getPhase())) {
				//重绘grid组件
				UIGridComp uiGrid=this.getRender().getUiElement();
				UIPartMeta uiMeta=	LuiRenderContext.current().getUiPartMeta();
				UILayoutPanel  layoutPanel=(UILayoutPanel)	UIElementFinder.findParent(uiMeta, uiGrid);
				this.getRender().destroy_layout();
				layoutPanel.setElement(uiGrid);
			}
		}
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public void setMultiple(boolean multiSelect) {
		if (multiSelect != this.isMultiple) {
			this.isMultiple = multiSelect;
			setCtxChanged(true);
		}
	}

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getHeaderRowHeight() {
		return headerRowHeight;
	}

	public void setHeaderRowHeight(String headerRowHeight) {
		this.headerRowHeight = headerRowHeight;
	}

	public boolean isShowNum() {
		return isShowNum;
	}

	public void setShowNum(boolean isShowNum) {
		this.isShowNum = isShowNum;
	}

	public boolean isShowTotalRow() {
		return isShowTotalRow;
	}

	public void setShowTotalRow(boolean showSumRow) {
		this.isShowTotalRow = showSumRow;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getShowColumns() {
		return showColumns;
	}

	public void setShowColumns(String showColumns) {
		this.showColumns = showColumns;
		setCtxChanged(true);
		addCtxChangedProperty("showColumns");
	}

	public List<GridColumn> filterColumns(String name) {
		List<GridColumn> eleList = new ArrayList<GridColumn>();
		Iterator<IGridColumn> it = columnList.iterator();
		while (it.hasNext()) {
			IGridColumn ele = it.next();
			if (ele instanceof GridColumn) {
				GridColumn col = (GridColumn) ele;
				if (col.getId().startsWith(name))
					eleList.add(col);
			}
		}
		return eleList;
	}

	/**
	 * 合并属性。注意此合并算法仅仅适用于从无分级的单据模板到有分级的增量配置文件。 如果今后支持其它方式，此算法应该做适当改进。
	 */
	public void mergeProperties(LuiElement ele) {
		super.mergeProperties(ele);
		// 合并基本属性
		GridComp grid = (GridComp) ele;

		String dataset = grid.getDataset();
		if (dataset != null)
			this.setDataset(dataset);

		String rowHeight = grid.getHeaderRowHeight();
		if (rowHeight != null)
			this.setRowHeight(rowHeight);

		// String pageClientSize = grid.getPageClientSize();
		// if(pageClientSize != null)
		// this.setPageClientSize(pageClientSize);

		String pageSize = grid.getPageSize();
		if (pageSize != null)
			this.setPageSize(pageSize);

		String groupColumns = grid.getGroupColumns();
		if (groupColumns != null)
			this.setGroupColumns(groupColumns);

		boolean sortable = grid.isSortable();
		if (sortable == false)
			this.setSortable(sortable);

		// 合并列。
		List<IGridColumn> tmpColumnList = grid.getColumnList();
		if (tmpColumnList != null) {
			Iterator<IGridColumn> it = tmpColumnList.iterator();
			while (it.hasNext()) {
				IGridColumn column = it.next();
				// 如果待和并列是组
				if (column instanceof GridColumnGroup) {
					GridColumnGroup group = (GridColumnGroup) column;
					// 是增加的组，则添加，并递归处理此组下的子列
					if (group.getConfType().equals(LuiElement.CONF_ADD)) {
						// 深度克隆此组
						GridColumnGroup clonedGroup = (GridColumnGroup) group.clone();
						// 递归处理组
						mergeGroup(clonedGroup);
						// 将处理过的组添加到列表中
						if (clonedGroup.getConfPos() == -1)
							this.columnList.add(clonedGroup);
						else
							this.columnList.add(clonedGroup.getConfPos(), clonedGroup);
					}
				} else {
					GridColumn tmpColumn = (GridColumn) column;
					if (tmpColumn.getConfType().equals(LuiElement.CONF_ADD)) {
						this.columnList.add((IGridColumn) tmpColumn.clone());
					} else if (tmpColumn.getConfType().equals(LuiElement.CONF_DEL)) {
						IGridColumn currColumn = this.getColumnById(tmpColumn.getId());
						if (currColumn == null) {
							// logger.warn("没有从列表中找到待删除的列:" +
							// tmpColumn.getId());
							return;
						}

						this.columnList.remove(currColumn);
					} else if (tmpColumn.getConfType().equals(LuiElement.CONF_REF)) {
						GridColumn currColumn = (GridColumn) this.getColumnById(tmpColumn.getId());
						if (currColumn == null) {
							// logger.warn("没有从列表中找到待修改的列:" +
							// tmpColumn.getId());
							return;
						}
						currColumn.mergeProperties(tmpColumn);
						if (tmpColumn.getConfPos() != -1) {
							this.columnList.remove(currColumn);
							this.columnList.add(tmpColumn.getConfPos(), currColumn);
						}
					}
				}
			}
		}
	}

	/**
	 * 处理组的递归方法
	 * 
	 * @param group
	 */
	private void mergeGroup(GridColumnGroup group) {
		// 获取当前组的子列表
		List<IGridColumn> childColumnList = group.getChildColumnList();
		if (childColumnList != null) {
			Iterator<IGridColumn> it = childColumnList.iterator();
			// 暂存提取出的列
			List<GridColumn> tempList = new ArrayList<GridColumn>();
			while (it.hasNext()) {
				IGridColumn column = it.next();
				// 如果是组
				if (column instanceof GridColumnGroup) {
					GridColumnGroup childGroup = (GridColumnGroup) column;
					// 是新加组
					if (childGroup.getConfType().equals(LuiElement.CONF_ADD)) {
						mergeGroup(childGroup);
					}
				} else {
					GridColumn childColumn = (GridColumn) column;
					// TODO 删除此group下对应的column
					if (childColumn.getConfType().equals(LuiElement.CONF_DEL)) {
						// 从当前组下删除column
						it.remove();
						// 从原有配置中删除column
						IGridColumn currColumn = this.getColumnById(childColumn.getId());
						if (currColumn != null)
							this.columnList.remove(currColumn);
					} else if (childColumn.getConfType().equals(LuiElement.CONF_REF)) {
						// 从当前group中删除列
						it.remove();
						// 获取原列信息
						GridColumn sourceColumn = (GridColumn) this.getColumnById(childColumn.getId());
						// 从原列中删除此列
						this.columnList.remove(sourceColumn);
						// 补充信息
						sourceColumn.mergeProperties(childColumn);
						// childColumn.mergeProperties(ele)
						tempList.add(sourceColumn);
					}
					// 增加不做事
				}
			}
			Iterator<GridColumn> colIt = tempList.iterator();

			while (colIt.hasNext()) {
				GridColumn column = colIt.next();
				if (column.getConfPos() != -1) {
					if (column.getConfPos() >= group.getChildColumnList().size())
						group.getChildColumnList().add(column);
					else
						group.getChildColumnList().add(column.getConfPos(), column);
				} else
					group.getChildColumnList().add(column);
			}
		}
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public String getGroupColumns() {
		return groupColumns;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if(!visible){
			if(this.columnList != null){
				for(IGridColumn item : columnList){
					item.setVisible(visible);
				}
			}
		}
	}

	public void setGroupColumns(String groupColumns) {
		if(this.groupColumns != groupColumns){//如果发生改变，则重新画grid
			this.groupColumns = groupColumns;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				UIGridComp   uiGrid=this.getRender().getUiElement();
				UIPartMeta uiMeta=	LuiRenderContext.current().getUiPartMeta();
				UILayoutPanel  layoutPanel=(UILayoutPanel)	UIElementFinder.findParent(uiMeta, uiGrid);
				this.getRender().destroy_layout();
				layoutPanel.setElement(uiGrid);
				///this.getRender().create();
				
//				List<IGridColumn> cols = new ArrayList<IGridColumn>();
//				cols.addAll(columnList);
//				this.removeColumns(columnList); 
//				this.addColumns(cols);
			}
		}
	}

	public boolean isPageTop() {
		return isPageTop;
	}

	public void setPageTop(boolean pagenationTop) {
		this.isPageTop = pagenationTop;
	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "GridComp-000003")/*表格的ID不能为空!\r\n*/);
			buffer.append("表格的ID不能为空!\r\n");
		}
		if (this.getDataset() == null || this.getDataset().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "GridComp-000004")/*表格引用的数据集不能为空!\r\n*/);
			buffer.append("表格引用的数据集不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());
	}

	@Override
	public BaseContext getContext() {
		GridContext ctx = new GridContext();
		ctx.setEnabled(this.enabled);
		ctx.setEditable(this.isEdit);
		ctx.setAllowMouseoverChange(this.isAllowMouseoverChange);
		ctx.setCurrentColID(this.getCurrentColID());
		ctx.setMultiSelect(this.isMultiple);
		if (checkCtxPropertyChanged("showColumns"))
			ctx.setShowColumns(this.showColumns);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		GridContext gridctx = (GridContext) ctx;
		if (gridctx.getId() != null && !"".equals(gridctx.getId()))
			this.setId(gridctx.getId());
		this.setEdit(gridctx.isEditable());
		this.setShowTip(gridctx.isShowTip());
		this.setEnabled(gridctx.isEnabled());
		this.setShowColumns(gridctx.getShowColumns());
		this.setCurrentColID(gridctx.getCurrentColID());
		this.setCtxChanged(false);
	}

	public boolean isShowColMenu() {
		return isShowColMenu;
	}

	public void setShowColMenu(boolean isShowColMenu) {
		this.isShowColMenu = isShowColMenu;
	}

	public GridTreeLevel getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(GridTreeLevel topLevel) {
		this.topLevel = topLevel;
	}

	public String getRowRender() {
		return rowRender;
	}

	public void setRowRender(String rowRender) {
		this.rowRender = rowRender;
	}

	public String getExtendCellEditor() {
		return extendCellEditor;
	}

	public void setExtendCellEditor(String extendCellEditor) {
		this.extendCellEditor = extendCellEditor;
	}

	@Override
	public GridColumn getElementById(String id) {
		IGridColumn gc = getColumnById(id);
		if (gc instanceof GridColumn) {
			return (GridColumn) gc;
		}
		return null;
	}

	public boolean isShowToolbar() {
		return isShowToolbar;
	}

	public void setShowToolbar(boolean isShowToolbar) {
		this.isShowToolbar = isShowToolbar;
		if (LifeCyclePhase.ajax.equals(getPhase()))
			this.getRender().setShowImageBtn(this.isShowToolbar);
	}

	public Boolean isFitRowHeight() {
		return isFitRowHeight;
	}

	public void setFitRowHeight(Boolean isFitRowHeight) {
		this.isFitRowHeight = isFitRowHeight;
	}

	public boolean isExpandTree() {
		return isExpandTree;
	}

	public void setExpandTree(boolean isExpandTree) {
		this.isExpandTree = isExpandTree;
	}

	public String getToolbarRender() {
		return toolbarRender;
	}

	public void setToolbarRender(String toolbarRender) {
		this.toolbarRender = toolbarRender;
	}

	public String[] getGridDescContents() {
		return gridDescContents;
	}

	public void setGridDescContents(String[] gridDescContents) {
		this.gridDescContents = gridDescContents;
		this.getRender().setGridDescContent(gridDescContents);
	}

	public MenubarComp getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(MenubarComp menuBar) {
		this.menuBar = menuBar;
	}

	public boolean isCopy() {
		return isCopy;
	}

	public void setCopy(boolean isCopy) {
		this.isCopy = isCopy;
	}

	// public void setGridColun(String ele){
	// this.getRender().set
	// this.notifyChange(UIElement.UPDATE,"REPAINTGRID");
	// }

	public int idToIndex(String id) {
		if (columnList == null) {
			return -1;
		}
		int size = columnList.size();
		for (int i = 0; i < size; i++) {
			if (columnList.get(i) instanceof GridColumn) {
				if (((GridColumn) columnList.get(i)).getId().trim().equals(id))
					return i;
			} else if (columnList.get(i) instanceof GridColumnGroup) {
				if (((GridColumnGroup) columnList.get(i)).getId().trim().equals(id))
					return i;
			}
		}
		return -1;
	}

	public List<IGridColumn> getAllColumnList() {
		List<IGridColumn> columnList = null;
		if (this.getColumnCount() > 0) {
			columnList = new ArrayList<IGridColumn>();
			List<IGridColumn> columns = this.getColumnList();
			for (IGridColumn column : columns) {
				if (column instanceof GridColumn) {
					columnList.add(column);
				} else if (column instanceof GridColumnGroup) {
					columnList = getChildColumns((GridColumnGroup) column, columnList);
				}
			}
		}
		return columnList;
	}

	private List<IGridColumn> getChildColumns(GridColumnGroup gcc, List<IGridColumn> list) {
		if (gcc != null) {
			if (list == null) {
				list = new ArrayList<IGridColumn>();
			}
			List<IGridColumn> children = gcc.getChildColumnList();
			if (children != null && children.size() > 0) {
				for (IGridColumn child : children) {
					if (child instanceof GridColumn) {
						list.add(child);
					} else if (child instanceof GridColumnGroup) {
						list = getChildColumns(gcc, list);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 动态隐藏整体错误提示框
	 */
	public void hideErrorMsg() {
		this.getRender().hideErrorMsg();
	}

	public PCGridCompRender getRender() {
		if (this.render == null) {
			render = RenderProxy.getRender(new PCGridCompRender(this));
		}
		return this.render;
	}

}
