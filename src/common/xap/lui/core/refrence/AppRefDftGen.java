package xap.lui.core.refrence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.SimpleTreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.dataset.ParameterSet;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.GridRowEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.ViewPartMeta;

/**
 * 根据参照model生成dataset和控件的工具类
 */
public class AppRefDftGen {
	// private static final String PARENT_WIDGET = "main";
	private String WIDGET_ID = "main";
	private IRefModel model;
	private GenericRefNode refnode;

	public AppRefDftGen(IRefModel model, GenericRefNode refnode) {
		this.model = model;
		this.refnode = refnode;
	}

	/**
	 * 根据refNodeName生成dataset
	 * 
	 * @param dsId
	 * @return
	 */
	public Dataset[] getDataset() {
		ArrayList<Dataset> datasets = new ArrayList<Dataset>();
		int refType = RefSelfUtil.getRefType(model);
		Dataset ds = null;
		switch (refType) {
		case IRefConst.GRID:
			IRefGridModel gmodel = (IRefGridModel) model;
			ds = generateGridDataset(gmodel);
			datasets.add(ds);
			break;
		case IRefConst.TREE:
			// IRefTreeModel treeAbsModel = (IRefTreeModel) model;
			ds = generateTreeDataset();
			datasets.add(ds);
			break;
		case IRefConst.GRIDTREE:
			IRefTreeGridModel treeGridAbsModel = (IRefTreeGridModel) model;
			ds = new Dataset(IRefConst.MASTER_DS_ID + "_tree");
			String[] codes = treeGridAbsModel.getClassFieldCode();
			if (codes != null) {
				for (int j = 0; j < codes.length; j++) {
					if (codes[j] == null) {
						continue;
					}
					Field field = new Field();
					field.setDataType(StringDataTypeConst.STRING);
					if (codes[j].indexOf(".") != -1) {
						field.setField(codes[j].split("\\.")[1].replaceAll("\\.|\\|\\||'|-", "_"));
						field.setId(codes[j].split("\\.")[1].replaceAll("\\.|\\|\\||'|-", "_"));
					} else {
						field.setField(codes[j].replaceAll("\\.|\\|\\||'|-", "_"));
						field.setId(codes[j].replaceAll("\\.|\\|\\||'|-", "_"));
					}
					ds.addField(field);
				}
			}
			ds.setLazyLoad(false);
			datasets.add(ds);
			geneDatasetLoadEvent(ds);
			geneDatasetRowSelEvent(ds);
			Dataset gridDs = new Dataset(IRefConst.MASTER_DS_ID);
			// gridDs.setPageSize(11);
			// 显示字段列表
			String[] showColumns = treeGridAbsModel.getShowFieldCode();
			// 不显示字段列表
			String[] hiddenColums = treeGridAbsModel.getHiddenFieldCode();
			ArrayList<String> columnIds = new ArrayList<String>();
			if (showColumns != null) {
				String[] showNames = treeGridAbsModel.getShowFieldName();
				for (int i = 0; i < showColumns.length; i++) {
					Field field = new Field();
					String showColumn = showColumns[i];
					String text = null;
					if (i >= showNames.length) {
						text = showColumn;
					} else {
						text = showNames[i];
					}
					field.setText(text);
					field.setDataType(StringDataTypeConst.STRING);
					field.setField(showColumn.replaceAll("\\.|\\|\\||'|-", "_"));
					field.setId(showColumn.replaceAll("\\.|\\|\\||'|-", "_"));
					columnIds.add(field.getId());
					gridDs.addField(field);
				}
			}
			if (hiddenColums != null) {
				String[] hiddenNames = treeGridAbsModel.getHiddenFieldCode();
				for (int i = 0; i < hiddenColums.length; i++) {
					Field field = new Field();
					field.setField(hiddenColums[i]);
					field.setText(hiddenNames[i]);
					field.setDataType(StringDataTypeConst.STRING);
					if (hiddenColums[i].indexOf(".") != -1) {
						field.setId(hiddenColums[i].split("\\.")[1].replaceAll("\\.", "_"));
					} else
						field.setId(hiddenColums[i].replaceAll("\\.", "_"));
					gridDs.addField(field);
				}
			}
			gridDs.setLazyLoad(true);
			geneDatasetLoadEvent(gridDs);
			datasets.add(gridDs);
		}
		// 树表型参照
		return datasets.toArray(new Dataset[0]);
	}

	private void geneDatasetRowSelEvent(Dataset ds) {
		EventSubmitRule sr = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		sr.addWidgetRule(wr);
		sr.setParentSubmitRule(generateParentSubmitRule());
		LuiEventConf datasetSelevent = new LuiEventConf();
		datasetSelevent.setEventType(DatasetEvent.class.getSimpleName());
		datasetSelevent.setOnserver(true);
		datasetSelevent.setSubmitRule(sr);
		datasetSelevent.setEventName("onAfterRowSelect");
		datasetSelevent.setMethod("onAfterRowSelect");
		if (refnode.getController() != null)
			datasetSelevent.setControllerClazz(refnode.getController());
		ds.addEventConf(datasetSelevent);
	}

	private void geneDatasetLoadEvent(Dataset ds) {
		EventSubmitRule sr = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		sr.addWidgetRule(wr);
		sr.setParentSubmitRule(generateParentSubmitRule());
		LuiEventConf event = new LuiEventConf();
		event.setEventType(DatasetEvent.class.getSimpleName());
		event.setOnserver(true);
		event.setSubmitRule(sr);
		event.setEventName("onDataLoad");
		if (refnode.getController() != null)
			event.setControllerClazz(refnode.getController());
		event.setMethod("onDataLoad");
		ds.addEventConf(event);
	}

	/**
	 * 属性参照生成数据集ds
	 * 
	 * @return
	 */
	private Dataset generateTreeDataset() {
		Dataset ds = new Dataset(IRefConst.MASTER_DS_ID);
		ds.setEdit(true);
		// 显示字段列表
		String[] showColumns = model.getShowFieldCode();
		// 不显示字段列表
		String[] hiddenColums = model.getHiddenFieldCode();
		if (showColumns != null) {
			for (int i = 0; i < showColumns.length; i++) {
				Field field = new Field();
				String showColumn = showColumns[i];
				field.setId(showColumn.replaceAll("\\.", "_"));
				field.setField(showColumn);
				field.setDataType(StringDataTypeConst.STRING);
				ds.addField(field);
			}
		}
		if (hiddenColums != null) {
			for (int i = 0; i < hiddenColums.length; i++) {
				Field field = new Field();
				String hiddenColumn = hiddenColums[i];
				if (hiddenColumn.indexOf(".") != -1) {
					field.setId(hiddenColumn.split("\\.")[1].replaceAll("\\.", "_"));
					field.setField(hiddenColumn.split("\\.")[1]);
				} else {
					field.setId(hiddenColumn.replaceAll("\\.", "_"));
					field.setField(hiddenColumn);
				}
				field.setText(hiddenColums[i]);
				field.setDataType(StringDataTypeConst.STRING);
				ds.addField(field);
			}
		}
		ds.setLazyLoad(false);
		geneDatasetLoadEvent(ds);
		return ds;
	}

	/**
	 * 表型参照生成Dataset
	 * 
	 * @param gmodel
	 * @return
	 */
	private Dataset generateGridDataset(IRefGridModel gmodel) {
		if (gmodel == null)
			LuiLogger.error("参照model为空！");
		Dataset ds = new Dataset(IRefConst.MASTER_DS_ID);
		ds.setPageSize(IRefConst.GRID_PAGESIZE);
		String filterSql = this.refnode.getFilterSql();
		if (!StringUtils.isEmpty(filterSql)) {
			ParameterSet ps = new ParameterSet();
			ps.addParameter(new Parameter("relationWhereSql", filterSql));
			ds.setReqParameters(ps);
		}
		// 显示字段列表
		String[] showColumns = gmodel.getShowFieldCode();
		// 不显示字段列表
		String[] hiddenColums = gmodel.getHiddenFieldCode();
		if (showColumns != null) {
			String[] showNames = gmodel.getShowFieldName();
			for (int i = 0; i < showColumns.length; i++) {
				String showColumn = showColumns[i];
				Field field = new Field();
				String text = null;
				if (i >= showNames.length) {
					text = showColumn;
				} else
					text = showNames[i];
				field.setText(text);
				field.setDataType(StringDataTypeConst.STRING);
				LuiLogger.error("参照的字段Id" + field.getId() + "; 显示值 text=" + text);
				field.setId(showColumn.replaceAll("\\.", "_"));
				field.setField(showColumn.replaceAll("\\.", "_"));
				ds.addField(field);
			}
		}
		if (hiddenColums != null) {
			for (int i = 0; i < hiddenColums.length; i++) {
				Field field = new Field();
				field.setText(hiddenColums[i]);
				field.setDataType(StringDataTypeConst.STRING);
				// int asIndex = hiddenColums[i].indexOf(" as ");
				// if (asIndex != -1) {
				// String fid = hiddenColums[i].substring(asIndex + 4);
				// field.setField(fid);
				// field.setId(fid);
				// } else if (hiddenColums[i].indexOf(".") != -1) {
				// field.setId(hiddenColums[i].split("\\.")[1].replaceAll("\\.",
				// "_"));
				// field.setField(hiddenColums[i].split("\\.")[1]);
				// } else {
				//
				// }
				field.setField(hiddenColums[i]);
				field.setId(hiddenColums[i].replaceAll("\\.", "_"));
				ds.addField(field);
			}
		}
		ds.setLazyLoad(false);
		geneDatasetLoadEvent(ds);
		return ds;
	}

	/**
	 * 生成参照控件对象
	 * 
	 * @return
	 */
	public WebComp[] getComponent() {
		int refType = RefSelfUtil.getRefType(model);
		if (refType == IRefConst.GRID) {
			return new GridComp[] { generateGrid(IRefConst.MASTER_DS_ID) };
		} else if (refType == IRefConst.TREE) {
			// 生成树
			TreeViewComp treeView = (TreeViewComp) generateTree(IRefConst.MASTER_DS_ID)[0];
			treeView.setOpenLevel(1);
			return new WebComp[] { treeView };
		} else if (refType == IRefConst.GRIDTREE) {
			WebComp[] webComponents = generateGridTree(IRefConst.MASTER_DS_ID);
			return webComponents;
		}
		return null;
	}

	/**
	 * 生成带组织参照选择框的树表型参照
	 * 
	 * @param dsId
	 * @return
	 */
	public WebComp[] getnerateTreeGridText(String dsId) {
		TreeViewComp tree = generateTgTree(dsId + "_tree");
		GridComp grid = generateGrid(dsId);
		ReferenceComp refComp = genOrgRefComp();
		return new WebComp[] { tree, grid, refComp };
	}

	/*
	 * 生成带组织的参照表格参照
	 */
	public WebComp[] getnerateGridText(String dsId) {
		GridComp grid = generateGrid(dsId);
		ReferenceComp refComp = genOrgRefComp();
		return new WebComp[] { grid, refComp };
	}

	/**
	 * 生成组织参照文本框
	 * 
	 * @return
	 */
	protected ReferenceComp genOrgRefComp() {
		ReferenceComp refComp = new ReferenceComp();
		refComp.setText("业务单元");
		// refComp.setWidth("250");
		refComp.setRefcode("refnode_org");
		refComp.setEditorType("Reference");
		refComp.setEnabled(true);
		refComp.setId("refcomp_org");
		// 添加textListener，值改变时执行此Listener
		LuiEventConf event = geneTextEvent(refComp);
		refComp.addEventConf(event);
		return refComp;
	}

	/**
	 * 
	 * @return
	 */
	protected ReferenceComp genFilterRefComp(String filterName) {
		ReferenceComp refComp = new ReferenceComp();
		String showTitle = filterName;
		refComp.setText(showTitle);
		// refComp.setWidth("250");
		refComp.setRefcode("refnode_org");
		refComp.setEditorType("Reference");
		refComp.setEnabled(true);
		refComp.setId("refcomp_org");
		// 添加textListener，值改变时执行此Listener
		LuiEventConf event = geneTextEvent(refComp);
		event.setMethod("filterValueChanged");
		refComp.addEventConf(event);
		return refComp;
	}

	private LuiEventConf geneTextEvent(ReferenceComp refComp) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(IRefConst.MASTER_DS_ID + "_tree");
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		wr.addDsRule(dsRule);
		LuiEventConf textEvent = new LuiEventConf();
		textEvent.setEventType(TextEvent.class.getSimpleName());
		textEvent.setOnserver(true);
		textEvent.setSubmitRule(submitRule);
		textEvent.setEventName("valueChanged");
		textEvent.setMethod("orgValueChanged");
		return textEvent;
	}

	/**
	 * 得到参照的3大要素,pk,code,name
	 * 
	 * @return
	 */
	public String[] getRefElements() {
		String refPk = model.getPkFieldCode();
		if (refPk != null) {
			if (refPk.indexOf(".") != -1)
				refPk = refPk.split("\\.")[1];
		}
		String code = model.getRefCodeField();
		if (code != null) {
			if (code.indexOf(".") != -1) {
				code = code.split("\\.")[1];
			}
		}
		String name = model.getRefNameField();
		return new String[] { refPk, code, name };
	}

	public String[] getRefElementsNoTrans() {
		return new String[] { model.getPkFieldCode(), model.getRefCodeField(), model.getRefNameField() };
	}

	/**
	 * 获取客户端用户设置column宽度
	 * 
	 * @return
	 */
	private int getColumnsWidthFromWebContext(String field) {
		Map<String, Integer> map = null;
		String param = LuiRuntimeContext.getWebContext().getParameter("param");
		if (param != null && param.trim().length() > 0) {
			String[] params = param.split(",");
			if (params != null && params.length > 0) {
				map = new HashMap<String, Integer>();
				for (String p : params) {
					if (p.indexOf(":") != -1 && p.indexOf(":") < p.length() - 1) {
						try {
							map.put(p.substring(0, p.indexOf(":")), Integer.parseInt(p.substring(p.indexOf(":") + 1)));
						} catch (NumberFormatException e) {
							map.put(p.substring(0, p.indexOf(":")), 120);
						}
					}
				}
			}
		}
		int width = 120;
		if (map != null) {
			try {
				width = map.get(field);
			} catch (Exception e) {
			}
		}
		return width;
	}

	/**
	 * 根据model生成grid控件对象
	 * 
	 * @param refModel
	 * @return
	 */
	private GridComp generateGrid(String dsId) {
		// 显示字段列表
		String[] showColumns = model.getShowFieldCode();
		String[] nameColumns = ((IRefGridModel) model).getShowFieldName();
		ArrayList<IGridColumn> columns = new ArrayList<IGridColumn>();
		for (int i = 0; i < showColumns.length && i < nameColumns.length; i++) {
			GridColumn column = new GridColumn();
			column.setFitWidth(true);
			column.setText(nameColumns[i]);
			column.setField(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
			column.setId(showColumns[i].replaceAll("\\.|\\|\\||'|-", "_"));
			column.setWidth(getColumnsWidthFromWebContext(column.getField()));
			column.setDataType(StringDataTypeConst.STRING);
			column.setVisible(true);
			column.setEdit(false);
			columns.add(column);
		}
		GridComp grid = null;
		grid = new GridComp();
		grid.setSimplePagination(false);
		grid.setDataset(dsId);
		grid.setColumnList(columns);
		grid.setEdit(false);
		grid.setId("refgrid");
		if (refnode.isMultiple()) {
			grid.setMultiple(true);
		} else {
			grid.setMultiple(false);
			geneGridDbRowEvent(grid);
		}
		return grid;
	}

	private void geneGridDbRowEvent(GridComp grid) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(IRefConst.MASTER_DS_ID);
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		wr.addDsRule(dsRule);
		// 父页面Widget提交规则
		EventSubmitRule parentSubmitRule = generateParentSubmitRule();
		submitRule.setParentSubmitRule(parentSubmitRule);
		LuiEventConf rowSelEvent = new LuiEventConf();
		rowSelEvent.setEventType(GridRowEvent.class.getSimpleName());
		rowSelEvent.setOnserver(true);
		rowSelEvent.setSubmitRule(submitRule);
		rowSelEvent.setEventName("onRowDbClick");
		rowSelEvent.setMethod("onRowDbClick");
		if (StringUtils.isNotBlank(refnode.getController())) {
			rowSelEvent.setControllerClazz(refnode.getController());
		}
		grid.addEventConf(rowSelEvent);
	}

	private TreeViewComp generateTgTree(String dsId) {
		IRefTreeGridModel treeModel = (IRefTreeGridModel) model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		// tree.setHeight("100%");
		tree.setShowRoot(false);
		tree.setExpand(true);
		tree.setText(treeModel.getRootName());
		int refType = RefSelfUtil.getRefType(model);
		String classChildField = treeModel.getChildField();
		// if(classChildField == null)
		// classChildField = treeModel.getClassChildField();
		if (classChildField == null) {
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if (refType == IRefConst.TREE) {
				level.setMasterKeyField(treeModel.getPkFieldCode());
				level.setLabelFields(treeModel.getRefCodeField().replaceAll("\\.", "_") + "," + treeModel.getRefNameField().replaceAll("\\.", "_"));
			} else if (refType == IRefConst.GRIDTREE) {
				String classRefPkField = treeModel.getClassPkFieldCode();
				if (classRefPkField.indexOf(".") != -1)
					level.setMasterKeyField(classRefPkField.split("\\.")[1].replaceAll("\\.", "_"));
				else
					level.setMasterKeyField(classRefPkField.replaceAll("\\.", "_"));
				String classRefCode = treeModel.getClassRefCodeField();
				String classRefNameField = treeModel.getClassRefNameField();
				if (classRefCode.indexOf(".") != -1 && classRefNameField.indexOf(".") != -1) {
					level.setLabelFields(classRefCode.split("\\.")[1].replaceAll("\\.", "_") + "," + classRefNameField.split("\\.")[1].replaceAll("\\.", "_"));
				} else
					level.setLabelFields(classRefCode.replaceAll("\\.", "_") + "," + classRefNameField.replaceAll("\\.", "_"));
			}
			tree.setTopLevel(level);
		}
		// 递归树
		else {
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if (refType == IRefConst.TREE) {
				level.setMasterKeyField(treeModel.getPkFieldCode());
				String code = treeModel.getRefCodeField();
				String name = treeModel.getRefNameField();
				String labelFields = "";
				if (code != null && !"".equals(code))
					labelFields = code.replaceAll("\\.", "_") + "," + name.replaceAll("\\.", "_");
				else
					labelFields = name.replaceAll("\\.", "_");
				level.setLabelFields(labelFields);
				level.setLabelFields(labelFields);
			} else if (refType == IRefConst.GRIDTREE) {
				level.setMasterKeyField(treeModel.getClassPkFieldCode());
				// level.setMasterKeyField(treeModel.getClassChildField());
				String code = treeModel.getClassRefCodeField();
				String name = treeModel.getClassRefNameField();
				String labelFields = "";
				if (code != null && !"".equals(code))
					labelFields = code.replaceAll("\\.", "_") + "," + name.replaceAll("\\.", "_");
				else
					labelFields = name.replaceAll("\\.", "_");
				level.setLabelFields(labelFields);
			}
			// level.setRecursiveKeyField(treeModel.getClassJoinField());
			level.setRecursiveField(treeModel.getChildField());
			level.setRecursiveParentField(treeModel.getFatherField());
			tree.setTopLevel(level);
		}
		return tree;
	}

	private void geneTreeDbEvent(TreeViewComp tree) {
		EventSubmitRule submitRule = new EventSubmitRule();
		WidgetRule wr = new WidgetRule();
		wr.setId(WIDGET_ID);
		submitRule.addWidgetRule(wr);
		if (refnode.isOnlyLeaf()) {
			TreeRule treeRule = new TreeRule();
			treeRule.setId("reftree");
			treeRule.setType(TreeRule.TREE_CURRENT_PARENT_CHILDREN);
			wr.addTreeRule(treeRule);
		}
		DatasetRule dsRule = new DatasetRule();
		dsRule.setId(IRefConst.MASTER_DS_ID);
		dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		submitRule.getWidgetRules().find(WIDGET_ID).addDsRule(dsRule);
		// 父页面Widget提交规则
		EventSubmitRule parentSubmitRule = generateParentSubmitRule();
		submitRule.setParentSubmitRule(parentSubmitRule);
		LuiEventConf rowSelEvent = new LuiEventConf();
		rowSelEvent.setEventType(TreeNodeEvent.class.getSimpleName());
		rowSelEvent.setOnserver(true);
		rowSelEvent.setSubmitRule(submitRule);
		rowSelEvent.setEventName("ondbclick");
		rowSelEvent.setMethod("onTreeNodedbclick");
		LuiParameter param = new LuiParameter();
		param.setName("treeNodeEvent");
		rowSelEvent.addParam(param);
		if (refnode.getController() != null && !"".equals(refnode.getController()))
			rowSelEvent.setControllerClazz(refnode.getController());
		tree.addEventConf(rowSelEvent);
	}

	/**
	 * 根据model生成tree控件
	 * 
	 * @param dsId
	 * @param refModel
	 * @return
	 */
	private WebComp[] generateTree(String dsId) {
		IRefTreeModel treeModel = (IRefTreeModel) model;
		TreeViewComp tree = new TreeViewComp();
		tree.setId("reftree");
		tree.setShowRoot(false);
		tree.setExpand(true);
		tree.setText(treeModel.getRootName());
		if (refnode.isMultiple()) {
			tree.setShowCheckBox(true);
		}
		// 简单树
		if (treeModel.getChildField() == null && treeModel.getFatherField() == null) {
			SimpleTreeLevel level = new SimpleTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			String pkField = treeModel.getPkFieldCode();
			if (pkField.indexOf(".") != -1) {
				level.setMasterKeyField(pkField.split("\\.")[1].replaceAll("\\.", "_"));
			} else {
				level.setMasterKeyField(pkField.replaceAll("\\.", "_"));
			}
			String code = treeModel.getRefCodeField().indexOf(".") != -1 ? treeModel.getRefCodeField().split("\\.")[1].replaceAll("\\.", "_") : treeModel.getRefCodeField().replaceAll("\\.", "_");
			String name = treeModel.getRefNameField().indexOf(".") != -1 ? treeModel.getRefNameField().split("\\.")[1].replaceAll("\\.", "_") : treeModel.getRefNameField().replaceAll("\\.", "_");
			level.setLabelFields(code + "," + name);
			tree.setTopLevel(level);
		}
		boolean flag1 = treeModel.getFatherField() != null;
		boolean flag2 = treeModel.getChildField() != null;
		if (flag1 || flag2) {
			RecursiveTreeLevel level = new RecursiveTreeLevel();
			level.setId("level1");
			level.setDataset(dsId);
			if (flag1) {
				if (treeModel.getFatherField().indexOf(".") != -1) {
					level.setRecursiveParentField(treeModel.getFatherField().split("\\.")[1].replaceAll("\\.", "_"));
				} else {
					level.setRecursiveParentField(treeModel.getFatherField().replaceAll("\\.", "_"));
				}
			}
			if (flag2) {
				if (treeModel.getChildField().indexOf(".") != -1) {
					level.setRecursiveField(treeModel.getChildField().split("\\.")[1].replaceAll("\\.", "_"));
				} else {
					level.setRecursiveField(treeModel.getChildField().replaceAll("\\.", "_"));
				}
			}else{
				level.setRecursiveField(treeModel.getPkFieldCode());
			}

			level.setMasterKeyField(level.getRecursiveField());
			String refCodeField = treeModel.getRefCodeField();
			String refNameField = treeModel.getRefNameField();
			if (refCodeField != null) {
				level.setLabelFields(refCodeField + "," + refNameField);
			} else {
				level.setLabelFields(refNameField);
			}
			tree.setTopLevel(level);
		}
		// 如果是树型参照,并且此树可以树表切换,创建grid控件
		// TODO 树节点的双击事件支持
		geneTreeDbEvent(tree);
		return new WebComp[] { tree };
		// return null;
	}

	/**
	 * 生成父页面提交规则
	 * 
	 * @return
	 */
	private EventSubmitRule generateParentSubmitRule() {
		EventSubmitRule submitRule = new EventSubmitRule();
		String pwidgetId = refnode.getWidget().getId();
		WidgetRule wr = new WidgetRule();
		wr.setId(pwidgetId);
		submitRule.addWidgetRule(wr);
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getParentPageMeta().getWidget(pwidgetId);
		if (widget != null) {
			Dataset[] dss = widget.getViewModels().getDatasets();
			for (int i = 0; i < dss.length; i++) {
				DatasetRule parentDsRule = new DatasetRule();
				parentDsRule.setId(dss[i].getId());
				parentDsRule.setType(DatasetRule.TYPE_CURRENT_PAGE);
				wr.addDsRule(parentDsRule);
			}
		}
		return submitRule;
	}

	/**
	 * 根据model生成tree,grid控件
	 * 
	 * @param dsId
	 * @param refModel
	 * @return
	 */
	private WebComp[] generateGridTree(String dsId) {
		TreeViewComp tree = generateTgTree(dsId + "_tree");
		GridComp grid = generateGrid(dsId);
		return new WebComp[] { tree, grid };
	}

	/**
	 * 过滤模型字段
	 * 
	 * @param showColumn
	 * @return
	 */
	public static String getRefFieldCodeReplace(String showColumn) {
		if (showColumn == null)
			return null;
		int asIndex = showColumn.indexOf(" as ");
		String fid = showColumn;
		if (asIndex != -1) {
			fid = showColumn.substring(asIndex + 4);
		} else if (showColumn.lastIndexOf(".") != -1) {
			int lastPointIndex = showColumn.lastIndexOf(".");
			int lastBlankIndex1 = showColumn.lastIndexOf(" ", lastPointIndex);
			int lastBlankIndex2 = showColumn.indexOf(" ", lastPointIndex);
			if (lastBlankIndex1 != -1 && lastBlankIndex2 != -1) {
				fid = showColumn.substring(lastBlankIndex1, lastBlankIndex2).trim();
			} else if (lastBlankIndex1 != -1) {
				fid = showColumn.substring(lastBlankIndex1).trim();
			} else {
				fid = showColumn;
			}
		}
		return fid;
	}

	/**
	 * 过滤模型字段
	 * 
	 * @param showColumns
	 * @return
	 */
	public static String[] getRefFieldCodeReplace(String[] showColumns) {
		List<String> list = new ArrayList<String>();
		if (showColumns != null && showColumns.length > 0) {
			for (String showColumn : showColumns) {
				list.add(getRefFieldCodeReplace(showColumn));
			}
		}
		return list.toArray(new String[0]);
	}

	public DatasetRelation[] getRelation() {
		// 获取参照类型
		int refType = RefSelfUtil.getRefType(model);
		// "表型参照"
		List<DatasetRelation> relation = new ArrayList<DatasetRelation>();
		if (refType == IRefConst.GRIDTREE) {
			IRefTreeGridModel treeGridAbsModel = (IRefTreeGridModel) model;
			DatasetRelation rel = new DatasetRelation();
			rel.setId("master_slave_rel");
			rel.setDetailDataset(IRefConst.MASTER_DS_ID);
			rel.setMasterDataset(IRefConst.MASTER_DS_ID + "_tree");
			if (treeGridAbsModel.getClassJoinField().indexOf(".") != -1)
				rel.setMasterKeyField(treeGridAbsModel.getClassJoinField().split("\\.")[1]);
			else
				rel.setMasterKeyField(treeGridAbsModel.getClassJoinField());
			if (treeGridAbsModel.getDocJoinField().indexOf(".") != -1)
				rel.setDetailForeignKey(treeGridAbsModel.getDocJoinField().split("\\.")[1].replaceAll("\\.", "_"));
			else
				rel.setDetailForeignKey(treeGridAbsModel.getDocJoinField().replaceAll("\\.", "_"));
			relation.add(rel);
		}
		return (DatasetRelation[]) relation.toArray(new DatasetRelation[0]);
	}

	public GenericRefNode[] getRefNodes() {
		// if (refnode instanceof NCRefNode) {
		// NCRefNode ncRefNode = (NCRefNode) refnode;
		// if (ncRefNode.isOrgs()) {
		// RefNode refNode = this.genOrgRefRefNode(ncRefNode.isGroup());
		// return new RefNode[] { refNode };
		// } else if (model.getFilterRefNodeNames() != null &&
		// ncRefNode.isFilterRefNodeNames()) {
		// NCRefNode refNode = this.genOrgRefRefNode(ncRefNode.isGroup());
		// IRefModel ncmodel =
		// RefUtil.getRefModel(model.getFilterRefNodeNames()[0]);
		// if (ncmodel != null) {
		// AppRefGenUtil gen = new AppRefGenUtil(ncmodel, null);
		// String[] refEles = gen.getRefElements();
		// String readFields = refEles[0] + "," + refEles[2];
		// refNode.setReadFields(readFields);
		// refNode.setRefcode(model.getFilterRefNodeNames()[0]);
		// return new RefNode[] { refNode };
		// }
		// }
		// }
		return null;
	}
}
