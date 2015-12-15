package xap.lui.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ChartBaseComp;
import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.ImageComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ProgressBarComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.comps.WebPartContentFetcherImpl;
import xap.lui.core.constant.CompIdGenerator;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.RefDataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.exception.ComboInputItem;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.IntInputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIBarChartComp;
import xap.lui.core.layout.UIBorder;
import xap.lui.core.layout.UIBorderTrue;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UICanvas;
import xap.lui.core.layout.UICanvasPanel;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UIChartComp;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIDivid;
import xap.lui.core.layout.UIDividCenter;
import xap.lui.core.layout.UIDividProp;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIFormComp;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UIFormGroupComp;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIIFrame;
import xap.lui.core.layout.UIImageComp;
import xap.lui.core.layout.UILabelComp;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UILineCharComp;
import xap.lui.core.layout.UILinkComp;
import xap.lui.core.layout.UIMenuGroup;
import xap.lui.core.layout.UIMenuGroupItem;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.layout.UIPanel;
import xap.lui.core.layout.UIPanelPanel;
import xap.lui.core.layout.UIPartComp;
import xap.lui.core.layout.UIPieChartComp;
import xap.lui.core.layout.UIProgressBar;
import xap.lui.core.layout.UISelfDefComp;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.layout.UIShutterItem;
import xap.lui.core.layout.UISplitter;
import xap.lui.core.layout.UISplitterOne;
import xap.lui.core.layout.UISplitterTwo;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UIToolBar;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

public class UIElementFactory {
	public UIElement createUIElement(String elementType, String widget) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try {
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			if (elementType == null) {
				throw new LuiRuntimeException("容器不支持添加");
			}
			UIElement ele = this.createComponent(elementType, widget);
			if (ele == null)
				ele = this.createLayout(elementType, widget);
			if (ele == null)
				ele = this.createPanel(elementType, widget);
			return ele;
		} finally {
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}

	public UIFormElement createFormElement(String eleId, String formId, String widgetId, String eleType) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		try {
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			UIFormElement uiFormEle = new UIFormElement();
			uiFormEle.setId(eleId);
			uiFormEle.setFormId(formId);
			uiFormEle.setViewId(widgetId);
			uiFormEle.setElementType(eleType);
			if (StringUtils.equals(EditorTypeConst.RICHEDITOR, eleType)) {
				uiFormEle.setHeight("100%");
				uiFormEle.setWidth("100%");
				uiFormEle.setEleWidth("80%");
			}
			return uiFormEle;
		} finally {
			RequestLifeCycleContext.get().setPhase(phase);
		}
	}

	public WebComp createWebComponent(UIElement ele) {
		WebComp comp = null;
		if (ele instanceof UIButton) {
			UIButton uiEle = (UIButton) ele;
			ButtonComp c = new ButtonComp(uiEle.getId());
			c.setText(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIChartComp) {
			UIChartComp uiEle = (UIChartComp) ele;
			ChartBaseComp c = new ChartBaseComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIFormComp) {
			UIFormComp uiEle = (UIFormComp) ele;
			IPaEditorService pes = new PaEditorServiceImpl();
			PagePartMeta pm = pes.getOriPageMeta(LuiRuntimeContext.getWebContext().getPageId(), LuiRuntimeContext.getWebContext().getRequest().getSession().getId());
			ViewPartMeta widget = pm.getWidget(ele.getViewId());
			Map<String, String> rs = getAllDatasetDialog(widget);
			String selectDsId = null;
			if (rs != null)
				selectDsId = rs.get("elementType");
			String dsid = (String) uiEle.getAttribute("DATASETID");
			FormComp c = new FormComp();
			Dataset ds = null;
			if (dsid != null) {
				ds = widget.getViewModels().getDataset(dsid);
			} else {
				if (selectDsId != null && !selectDsId.equals("CREATE_NEW")) {
					ds = widget.getViewModels().getDataset(selectDsId);
				} else {
					ds = createNewDs(widget, uiEle);
				}
			}
			c.setDataset(ds.getId());
			List<String> mutiLangField = new ArrayList<String>();
			if (ds.getFields() != null) {
				for (Field field : ds.getFields()) {
					String id = field.getId();
					if (mutiLangField.contains(id)) {
						continue;
					}
					if (field.getExtSource() != null && field.getExtSourceAttr() != null)
						continue;
					if ("ts".equals(id) || "dr".equals(id) || "status".equals(id))
						continue;
					if (field.isPK())
						continue;
					FormElement fe = new FormElement();
					fe.setId(id);
					fe.setText(field.getText());
					fe.setField(id);
					if (Integer.valueOf(58).equals(field.getExtendAttributeValue("Multi"))) {
						mutiLangField.add(id);
						for (int i = 2; i < 7; i++) {
							mutiLangField.add(id + i);
						}
						fe.setEditorType(EditorTypeConst.LANGUAGECOMBODATA);
					} else {
						String sourceField = field.getSourceField();
						if (sourceField != null) {
							fe.setEditorType(EditorTypeConst.REFERENCE);
							String refNode = CompIdGenerator.generateRefCompId(ds.getId(), field.getId());
							fe.setRefNode(refNode);
						} else
							fe.setEditorType(EditorTypeConst.getEditorTypeByString(field.getDataType()));
					}
					fe.setDataType(field.getDataType());
					fe.setVisible(true);
					c.addElement(fe);
				}
			}
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIFormGroupComp) {
			UIFormGroupComp uiEle = (UIFormGroupComp) ele;
			FormComp c = new FormComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIGridComp) {
			PagePartMeta pm =PaCache.getEditorPagePartMeta();
			ViewPartMeta widget = pm.getWidget(ele.getViewId());
			UIGridComp uiEle = (UIGridComp) ele;
			Map<String, String> rs = getAllDatasetDialog(widget);
			String selectDsId = null;
			if (rs != null) {
				selectDsId = rs.get("elementType");
			}
			String dsid = (String) uiEle.getAttribute("DATASETID");
			GridComp c = new GridComp();
			if (ModePhase.nodedef.equals(LuiRuntimeContext.getModePhase())){
				c.setShowToolbar(true);
			}
			Dataset ds = null;
			if (dsid != null) {
				ds = widget.getViewModels().getDataset(dsid);
			} else {
				if (selectDsId != null && !selectDsId.equals("CREATE_NEW")) {
					ds = widget.getViewModels().getDataset(selectDsId);
				} else {
					ds = createNewDs(widget, uiEle);
				}
			}
			c.setDataset(ds.getId());
			List<String> mutiLangField = new ArrayList<String>();
			for (Field field : ds.getFields()) {
				String id = field.getId();
				if (mutiLangField.contains(id)) {
					continue;
				}
				if (field.getExtSource() != null && field.getExtSourceAttr() != null)
					continue;
				if ("ts".equals(id) || "dr".equals(id) || "status".equals(id))
					continue;
				if (field.isPK())
					continue;
				GridColumn gc = new GridColumn();
				gc.setId(id);
				gc.setText(field.getText());
				gc.setField(id);
				gc.setWidth(120);
				if (Integer.valueOf(58).equals(field.getExtendAttributeValue("Multi"))) {
					mutiLangField.add(id);
					for (int i = 2; i < 7; i++) {
						mutiLangField.add(id + i);
					}
					gc.setEditorType(EditorTypeConst.LANGUAGECOMBODATA);
				} else {
					String sourceField = field.getSourceField();
					if (sourceField != null) {
						gc.setEditorType(EditorTypeConst.REFERENCE);
						String refNode = CompIdGenerator.generateRefCompId(ds.getId(), field.getId());
						gc.setRefNode(refNode);
					} else
						gc.setEditorType(EditorTypeConst.getEditorTypeByString(field.getDataType()));
				}
				gc.setDataType(field.getDataType());
				gc.setVisible(true);
				c.addColumn(gc);
			}
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIIFrame) {
			UIIFrame uiEle = (UIIFrame) ele;
			IFrameComp c = new IFrameComp();
			c.setId(uiEle.getId());
			c.setSrc("");
			comp = c;
		} else if (ele instanceof UIImageComp) {
			UIImageComp uiEle = (UIImageComp) ele;
			ImageComp c = new ImageComp();
			c.setId(uiEle.getId());
			c.setImage1("");
			comp = c;
		} else if (ele instanceof UILabelComp) {
			UILabelComp uiEle = (UILabelComp) ele;
			LabelComp c = new LabelComp();
			c.setId(uiEle.getId());
			c.setText(uiEle.getId());
			comp = c;
		} else if (ele instanceof UILinkComp) {
			UILinkComp uiEle = (UILinkComp) ele;
			LinkComp c = new LinkComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIMenubarComp) {
			UIMenubarComp uiEle = (UIMenubarComp) ele;
			MenubarComp c = new MenubarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIProgressBar) {
			UIProgressBar uiEle = (UIProgressBar) ele;
			ProgressBarComp c = new ProgressBarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UISelfDefComp) {
			UISelfDefComp uiEle = (UISelfDefComp) ele;
			SelfDefComp c = new SelfDefComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UITextField) {
			UITextField uiEle = (UITextField) ele;
			String widget = uiEle.getViewId();
			String type = uiEle.getType();
			if (type.equals(EditorTypeConst.CHECKBOX)) {
				CheckBoxComp c = new CheckBoxComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.CHECKBOXGROUP)) {
				String cdId = getComboData(widget);
				CheckboxGroupComp c = new CheckboxGroupComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				c.setDataListId(cdId);
				comp = c;
			} else if (type.equals(EditorTypeConst.COMBODATA)) {
				String cdName = getComboData(widget);
				ComboBoxComp c = new ComboBoxComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				c.setRefComboData(cdName);
				comp = c;
			} else if (type.equals(EditorTypeConst.RADIOCOMP)) {
				RadioComp c = new RadioComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.RADIOGROUP)) {
				String cdId = getComboData(widget);
				RadioGroupComp c = new RadioGroupComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				c.setDataListId(cdId);
				comp = c;
			} else if (type.equals(EditorTypeConst.TEXTAREA)) {
				TextAreaComp c = new TextAreaComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else if (type.equals(EditorTypeConst.REFERENCE)) {
				ReferenceComp c = new ReferenceComp();
				c.setId(uiEle.getId());
				c.setEditorType(type);
				comp = c;
			} else {
				TextComp c = new TextComp();
				c.setId(uiEle.getId());
				if (EditorTypeConst.DATETEXT.equals(type)) {
					ComboInputItem intInputItem = new ComboInputItem("datetype", "类型", true);
					DataList comboData = new DataList();
					DataItem item = new DataItem();
					item.setText("日期");
					item.setValue(EditorTypeConst.DATETEXT);
					comboData.addDataItem(item);
					DataItem item2 = new DataItem();
					item2.setText("日期+时间");
					item2.setValue(EditorTypeConst.DATETIMETEXT);
					comboData.addDataItem(item2);
					intInputItem.setComboData(comboData);
					intInputItem.setValue(EditorTypeConst.DATETEXT);
					InteractionUtil.showInputDialog("选择输入类型", new InputItem[] { intInputItem });
					Map<String, String> val = InteractionUtil.getInputDialogResult();
					type = val.get("datetype");
					((UITextField) ele).setType(type);
				}
				c.setEditorType(type);
				comp = c;
			}
		} else if (ele instanceof UIToolBar) {
			UIToolBar uiEle = (UIToolBar) ele;
			ToolBarComp c = new ToolBarComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UITreeComp) {
			UITreeComp uiEle = (UITreeComp) ele;
			TreeViewComp c = new TreeViewComp();
			c.setId(uiEle.getId());
			comp = c;
		} else if (ele instanceof UIPartComp) {
			UIPartComp uiHtml = (UIPartComp) ele;
			WebPartComp html = new WebPartComp();
			html.setId(uiHtml.getId());
			html.setContentFetcher(WebPartContentFetcherImpl.class.getName());
			comp = html;
		}
		return comp;
	}

	public Dataset createNewDs(ViewPartMeta widget, UIComponent uiEle) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
		Dataset ds = null;
		try {
			ds = new Dataset(uiEle.getId() + "_ds");
			ds.setLazyLoad(true);
			ds.setPageSize(-1);
			ds.setWidget(widget);
			widget.getViewModels().addDataset(ds);
			String id = "relationid";
			Field field = new Field(id);
			field.setText("关联外键");
			field.setDataType(StringDataTypeConst.STRING);
			Field pkField = new Field("pk_subtable");
			pkField.setText("主键");
			pkField.setPK(true);
			pkField.setDataType(StringDataTypeConst.STRING);
			ds.addField(field);
			ds.addField(pkField);
		} finally {
			RequestLifeCycleContext.get().setPhase(phase);
		}
		return ds;
	}

	private Map<String, String> getAllDatasetDialog(ViewPartMeta widget) {
		DataList comboData = new DataList();
		Dataset[] dss = widget.getViewModels().getDatasets();
		for (Dataset ds : dss) {
			if (!(ds instanceof RefDataset) && !(ds instanceof RefMdDataset)) {
				String label = ds.getCaption();
				if (ds.getCaption() == null)
					label = ds.getId();
				DataItem item = new DataItem(ds.getId(), label);
				comboData.addDataItem(item);
			}
		}
		DataItem item1 = new DataItem("CREATE_NEW", "创建新");
		comboData.addDataItem(item1);
		ComboInputItem editor = new ComboInputItem("elementType", "数据集：", true);
		editor.setComboData(comboData);
		InteractionUtil.showInputDialog("getDs", "确认", new InputItem[] { editor });
		Map<String, String> rs = InteractionUtil.getInputDialogResult("getDs");
		return rs;
	}

	private Map<String, String> getCreateTypeDialog() {
		ComboInputItem crtType = new ComboInputItem("crtType", "创建类型", true);
		ComboData cd = new DataList();
		DataItem it1 = new DataItem("copyAdd", "复制添加");
		DataItem it2 = new DataItem("shareAdd", "共享添加");
		cd.addDataItem(it1);
		cd.addDataItem(it2);
		crtType.setComboData(cd);
		InteractionUtil.showInputDialog("crt", "选择创建方式", new InputItem[] { crtType });
		Map<String, String> rs = InteractionUtil.getInputDialogResult("crt");
		return rs;
	}

	
	public static String getComboData(String widget) {
		ViewPartMeta wdt = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(widget);
		DataList comboData = new DataList();
		LuiRuntimeContext.getWebContext().getPageMeta();
		DataItem tipItem = new DataItem();
		tipItem.setValue("tipComboData");
		tipItem.setText("稍后创建ComboData");
		comboData.addDataItem(tipItem);
		ComboData[] cds = wdt.getViewModels().getComboDatas();
		if (cds != null && cds.length > 0) {
			for (ComboData cd : cds) {
				DataItem item = new DataItem();
				item.setValue(cd.getId());
				item.setText(cd.getCaption());
				comboData.addDataItem(item);
			}
		}
		ComboInputItem ori = new ComboInputItem("selectcd", "选择ComboData", true);
		ori.setComboData(comboData);
		ori.setValue("tipComboData");
		InteractionUtil.showInputDialog("设置ComboData", new InputItem[] { ori });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		String cdName = rs.get("selectcd");
		if ("tipComboData".equals(cdName)) {
			return null;
		} else {
			return cdName;
		}
	}

	public static int showInputDialog(String title) {
		return 3;
	}

	public static Map<String, String> showInputDialogForTable() {
		InputItem row = new IntInputItem("rowcount", "行数", true);
		row.setValue(3);
		InputItem cell = new IntInputItem("cellcount", "列数", true);
		cell.setValue(3);
		// InputItem type = new IntInputItem("type", "类别", true);
		InteractionUtil.showInputDialog("表格行列", new InputItem[] { row, cell });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}

	private Map<String, String> showInputDialogForBorderLayout() {
		DataList comboData = new DataList();
		DataItem item1 = new DataItem("true", "true");
		DataItem item2 = new DataItem("false", "false");
		comboData.addDataItem(item1);
		comboData.addDataItem(item2);
		ComboInputItem top = new ComboInputItem("top", "上", true);
		top.setComboData(comboData);
		ComboInputItem but = new ComboInputItem("buttom", "下", true);
		comboData = new DataList();
		item1 = new DataItem("true", "true");
		item2 = new DataItem("false", "false");
		comboData.addDataItem(item1);
		comboData.addDataItem(item2);
		but.setComboData(comboData);
		ComboInputItem left = new ComboInputItem("left", "左", true);
		comboData = new DataList();
		item1 = new DataItem("true", "true");
		item2 = new DataItem("false", "false");
		comboData.addDataItem(item1);
		comboData.addDataItem(item2);
		left.setComboData(comboData);
		ComboInputItem right = new ComboInputItem("right", "右", true);
		comboData = new DataList();
		item1 = new DataItem("true", "true");
		item2 = new DataItem("false", "false");
		comboData.addDataItem(item1);
		comboData.addDataItem(item2);
		right.setComboData(comboData);
		InteractionUtil.showInputDialog("border布局", new InputItem[] { top, but, left, right });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}

	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}

	public void setPhase(LifeCyclePhase phase) {
		RequestLifeCycleContext.get().setPhase(phase);
	}

	private UIElement createLayout(String elementType, String widget) {
		UILayout layout = null;
		if (elementType.equals(LuiPageContext.SOURCE_TYPE_WIDGT)) {
			UIViewPart uiWieget = new UIViewPart();
			uiWieget.setId(elementType);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FLOWVLAYOUT)) {
			layout = new UIFlowvLayout();
			String t = randomT(4);
			int count = showInputDialog("纵向流式布局");
			for (int i = 0; i < count; i++) {
				UIFlowvPanel panel = (UIFlowvPanel) this.createPanel(LuiPageContext.SOURCE_TYPE_FLOWVPANEL, widget);
				panel.setId("panelv" + i + t);
				layout.addPanel(panel);
			}
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_PANELLAYOUT)) {
			layout = new UIPanel();
			String className = "";
			layout.setClassName(className);
			String t = randomT(4);
			UILayoutPanel panel = (UILayoutPanel) this.createPanel(LuiPageContext.SOURCE_TYPE_PANELPANEL, widget);
			panel.setId(LuiPageContext.SOURCE_TYPE_PANELPANEL + t);
			layout.addPanel(panel);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FLOWHLAYOUT)) {
			layout = new UIFlowhLayout();
			int count = showInputDialog("横向流式布局");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UIFlowhPanel panel = (UIFlowhPanel) this.createPanel(LuiPageContext.SOURCE_TYPE_FLOWHPANEL, widget);
				panel.setId("panelh" + i + t);
				layout.addPanel(panel);
			}
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_CARDLAYOUT)) {
			UICardLayout card = new UICardLayout();
			String currentItem = "0";
			if (currentItem != null) {
				LifeCyclePhase ori = this.getPhase();
				this.setPhase(LifeCyclePhase.nullstatus);
				card.setCurrentItem(currentItem);
				this.setPhase(ori);
			}
			int count = 2;//showInputDialog("卡片容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UICardPanel panel = (UICardPanel) this.createPanel(LuiPageContext.SOURCE_TYPE_CARDPANEL, widget);
				panel.setId("cardpanel" + i + t);
				card.addPanel(panel);
			}
			layout = card;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT)) {
			UISplitter spliter = new UISplitter();
			spliter.setOrientation(UISplitter.ORIENTATION_H);
			spliter.setHideDirection(UISplitter.HIDEDIRECTION_R);
			UISplitterOne splite1 = (UISplitterOne) this.createPanel(LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL, widget);
			splite1.setId("s1");
			UISplitterTwo splite2 = (UISplitterTwo) this.createPanel(LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE, widget);
			splite2.setId("s2");
			spliter.addPanel(splite1);
			spliter.addPanel(splite2);
			layout = spliter;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_DIVIDLAYOUT)) {
			UIDivid divid = new UIDivid();
			divid.setOrientation(UIDivid.ORIENTATION_H);
			divid.setInverse(UIConstant.FALSE);
			UIDividCenter dividCenter = (UIDividCenter) this.createPanel(LuiPageContext.SOURCE_TYPE_DIVIDCENTERPANEL, widget);
			dividCenter.setId("s1");
			UIDividProp dividProp = (UIDividProp) this.createPanel(LuiPageContext.SOURCE_TYPE_DIVIDPROPPANEL, widget);
			dividProp.setId("s2");
			divid.addPanel(dividCenter);
			divid.addPanel(dividProp);
			layout = divid;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_MENU_GROUP)) {
			layout = new UIMenuGroup();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_BORDER)) {
			layout = new UIBorder();
			String width = "100";
			layout.setAttribute("width", width);
			String leftWidth = "1";
			layout.setAttribute("leftWidth", leftWidth);
			String rightWidth = "1";
			layout.setAttribute("rightWidth", rightWidth);
			String topWidth = "1";
			layout.setAttribute("topWidth", topWidth);
			String bottomWidth = "1";
			layout.setAttribute("bottomWidth", bottomWidth);
			String color = "";
			layout.setAttribute(UIBorder.COLOR, color);
			String leftcolor = "";
			layout.setAttribute(UIBorder.LEFTCOLOR, leftcolor);
			String rightcolor = "";
			layout.setAttribute(UIBorder.RIGHTCOLOR, rightcolor);
			String topcolor = "";
			layout.setAttribute(UIBorder.TOPCOLOR, topcolor);
			String bottomcolor = "";
			layout.setAttribute(UIBorder.BOTTOMCOLOR, bottomcolor);
			String className = "";
			layout.setAttribute(UIBorder.CLASSNAME, className);
			String t = randomT(4);
			UIBorderTrue panel = (UIBorderTrue) this.createPanel(LuiPageContext.SOURCE_TYPE_BORDERTRUE, widget);
			panel.setAttribute(UIBorderTrue.ID, LuiPageContext.SOURCE_TYPE_BORDERTRUE + t);
			layout.addPanel(panel);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TAG)) {
			UITabComp tabComp = new UITabComp();
			LifeCyclePhase ori = this.getPhase();
			this.setPhase(LifeCyclePhase.nullstatus);
			tabComp.setCurrentItem(0);
			this.setPhase(ori);
			String oneTabHide = "false";
			if (oneTabHide != null && !oneTabHide.equals("")) {
				tabComp.setOneTabHide(oneTabHide.equals("true") ? true : false);
			}
			int count = showInputDialog("页签容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UITabItem item = (UITabItem) this.createPanel(LuiPageContext.SOURCE_TYPE_TABITEM, widget);
				item.setId("UITabItem" + i + t);
				item.setText("页签" + (i + 1));
				tabComp.addPanel(item);
			}
			layout = tabComp;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR)) {
			layout = new UIShutter();
			String className = "";
			((UIShutter) layout).setClassName(className);
			((UIShutter) layout).setCurrentItem(0);
			int count = showInputDialog("百叶窗容器");
			String t = randomT(4);
			for (int i = 0; i < count; i++) {
				UIShutterItem item = (UIShutterItem) this.createPanel(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM, widget);
				item.setId("UIShutterItem" + i + t);
				item.setText("item" + (i + 1));
				layout.addPanel(item);
			}
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_CANVASLAYOUT)) {
			layout = new UICanvas();
			UICanvasPanel panel = (UICanvasPanel) this.createPanel(LuiPageContext.SOURCE_TYPE_CANVASPANEL, widget);
			panel.setId(LuiPageContext.SOURCE_TYPE_CANVASPANEL + randomT(4));
			layout.addPanel(panel);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_GRIDLAYOUT)) {
			int rowcount = 3;
			int cellcount = 3;
			int type = 0;
			Map<String, String> rs = this.showInputDialogForTable();
			if (rs != null && !rs.isEmpty()) {
				try {
					rowcount = Integer.parseInt(rs.get("rowcount"));
				} catch (Exception e) {
					throw new LuiRuntimeException(e.getMessage());
				}
				try {
					cellcount = Integer.parseInt(rs.get("cellcount"));
				} catch (Exception e) {
					throw new LuiRuntimeException(e.getMessage());
				}
				type = 0;
			}
			if (type == 0) {
				UIGridLayout layout2 = new UIGridLayout();
				layout2.setRowcount(rowcount);
				layout2.setColcount(cellcount);
				// layout2.setGridType(0);
				String t = randomT(4);
				for (int i = 0; i < rowcount; i++) {
					UIGridRowLayout row = new UIGridRowLayout();
					row.setId("row" + i + t);
					row.setViewId(widget);
					for (int j = 0; j < cellcount; j++) {
						UIGridPanel cell = new UIGridPanel();
						cell.setId("cell" + i + j + t);
						//if (i == 0)
							cell.setColWidth("120");
						//if (j == 0)
							cell.setColHeight("30");
						cell.setLayout(row);
						cell.setViewId(widget);
						cell.setRowIndex(String.valueOf(i));
						cell.setColIndex(String.valueOf(j));
						row.addPanel(cell);
					}
					layout2.addGridRow(row);
				}
				// layout2.setWidth("100");
				// layout2.setHeight("100");
				//layout2.setBorder("1");
				layout = layout2;
			} else {
				UIGridLayout layout2 = new UIGridLayout();
				layout2.setRowcount(rowcount);
				layout2.setColcount(cellcount * 3);
				// layout2.setGridType(1);
				String t = randomT(4);
				for (int i = 0; i < rowcount; i++) {
					UIGridRowLayout row = new UIGridRowLayout();
					row.setId("row" + i + t);
					row.setViewId(widget);
					for (int j = 0; j < cellcount; j++) {
						UIGridPanel cell0 = new UIGridPanel();
						cell0.setId("cell0" + i + j + t);
						cell0.setColWidth("120");
						cell0.setColHeight("30");
						cell0.setCellType("0");
						cell0.setViewId(widget);
						row.addPanel(cell0);
						UIGridPanel cell1 = new UIGridPanel();
						cell1.setId("cell1" + i + j + t);
						cell1.setColWidth("1");
						cell1.setColHeight("30");
						cell1.setCellType("1");
						cell1.setViewId(widget);
						row.addPanel(cell1);
						UIGridPanel cell2 = new UIGridPanel();
						cell2.setId("cell2" + i + j + t);
						cell2.setColWidth("120");
						cell2.setColHeight("30");
						cell2.setCellType("2");
						cell2.setViewId(widget);
						row.addPanel(cell2);
					}
					layout2.addGridRow(row);
				}
				// layout2.setWidth("100%");
				// layout2.setHeight("100%");
				layout2.setBorder("1");
				layout = layout2;
			}
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_ABSOLUTELAYOUT)) {
			UIAbsoluteLayout layout2 = new UIAbsoluteLayout();
			if (null != layout2) {
				layout2.setId(elementType);
				layout2.setViewId(widget);
			}
			return layout2;
		}
		if (null != layout) {
			layout.setId(elementType);
			layout.setViewId(widget);
		}
		return layout;
	}

	public static String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}

	/**
	 * 2011-10-11 下午02:27:17 renxh des：创建控件UIElement
	 * 
	 * @param elementType
	 */
	private UIElement createComponent(String elementType, String widget) {
		UIComponent comp = null;
		if (elementType.equals(LuiPageContext.SOURCE_TYPE_GRID)) {
			comp = new UIGridComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TREE)) {
			comp = new UITreeComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FORMCOMP)) {
			comp = new UIFormComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FORMGROUP)) {
			comp = new UIFormGroupComp();
			String forms = null;
			((UIFormGroupComp) comp).setForms(forms);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_MENUBAR_MENUITEM) || elementType.equals(LuiPageContext.SOURCE_TYPE_MENUBAR)) {
			comp = new UIMenubarComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_IFRAME)) {
			comp = new UIIFrame();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_HTMLCONTENT)) { // html内容
			UIPartComp hcomp = new UIPartComp();
			comp = hcomp;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TEXT)) {
			comp = new UITextField();
			String type = "text";
			((UITextField) comp).setType(type);
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_BUTTON)) {
			comp = new UIButton();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_CHART))
			comp = new UIChartComp();
		else if (elementType.equals(LuiPageContext.SOURCE_TYPE_LINKCOMP)) {
			comp = new UILinkComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TOOLBAR_BUTTON)) {
			comp = new UIToolBar();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_LABEL)) {
			comp = new UILabelComp();
			comp.setWidth("100%");
			comp.setHeight("28");
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_IMAGECOMP)) {
			comp = new UIImageComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_SELF_DEF_COMP)) {
			comp = new UISelfDefComp();
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_PROGRESS_BAR)) {
			comp = new UIProgressBar();
		}else if(elementType.equals("line")){
			comp = new UILineCharComp();
		} else if("bar".equals(elementType)){
			comp = new UIBarChartComp();
		}else if("pie".equals(elementType)){
			comp = new UIPieChartComp();
		}else {
			String editorType = EditorTypeConst.getEditorTypeByString(elementType);
			if (editorType != null) {
				comp = new UITextField();
				((UITextField) comp).setType(editorType);
				if (editorType.equals(EditorTypeConst.CHECKBOXGROUP) || EditorTypeConst.RADIOGROUP.equals(editorType))
					comp.setWidth("100%");
			}
		}
		if (comp != null) {
			comp.setId(elementType);
			comp.setViewId(widget);
		}
		return comp;
	}

	/**
	 * 2011-10-11 下午03:12:34 renxh des：创建容器UIElement
	 * 
	 * @param elementType
	 * @param widget
	 * @return
	 */
	private UIElement createPanel(String elementType, String widget) {
		UILayoutPanel panel = null;
		if (elementType.equals(LuiPageContext.SOURCE_TYPE_PANELPANEL)) {
			UIPanelPanel panelLayout = new UIPanelPanel();
			panelLayout.setClassName("small_panel_div");
			panel = panelLayout;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FLOWVPANEL)) {
			UIFlowvPanel flowvLayout = new UIFlowvPanel();
			// String height = null;
			// if (height != null)
			// flowvLayout.setHeight(height);
			// String anchor = null;
			// if (anchor != null)
			// flowvLayout.setAnchor(anchor);
			panel = flowvLayout;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_FLOWHPANEL)) {
			UIFlowhPanel flowhLayout = new UIFlowhPanel();
			// String width = null;
			// if (width != null)
			// flowhLayout.setWidth(width);
			panel = flowhLayout;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_CARDPANEL)) {
			UICardPanel cardPanel = new UICardPanel();
			panel = cardPanel;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_SPLITERONEPANEL)) {
			UISplitterOne one = new UISplitterOne();
			panel = one;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_SPLITERTWOPANLE)) {
			UISplitterTwo two = new UISplitterTwo();
			panel = two;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_DIVIDCENTERPANEL)) {
			UIDividCenter center = new UIDividCenter();
			panel = center;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_DIVIDPROPPANEL)) {
			UIDividProp prop = new UIDividProp();
			panel = prop;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_BORDERTRUE)) {
			UIBorderTrue border = new UIBorderTrue();
			panel = border;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_CANVASPANEL)) {
			UICanvasPanel canvasPanel = new UICanvasPanel();
			panel = canvasPanel;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TABSPACE)) {
			UITabRightPanel item = new UITabRightPanel();
			String width = "200";
			item.setWidth(width);
			panel = item;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_TABITEM)) {
			UITabItem item = new UITabItem();
			String text = elementType + randomT(4);
			item.setText(text);
			String i18nName = null;
			item.setI18nName(i18nName);
			// String state = null;
			// if (state != null && !state.equals("")) {
			// item.setState(Integer.valueOf(state));
			// }
			panel = item;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM)) {
			UIShutterItem item = new UIShutterItem();
			String text = elementType;
			item.setText(text);
			String i18nName = null;
			item.setI18nName(i18nName);
			panel = item;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_MENU_GROUP_ITEM)) {
			UIMenuGroupItem groupItem = new UIMenuGroupItem();
			// String state = null;
			// if (state != null)
			// groupItem.setState(Integer.valueOf(state));
			panel = groupItem;
		} else if (elementType.equals(LuiPageContext.SOURCE_TYPE_GRIDPANEL)) {
			UIGridPanel gridPanel = new UIGridPanel();
			String rowSpan = "1";
			if (rowSpan != null)
				gridPanel.setRowSpan(rowSpan);
			String colSpan = "1";
			if (colSpan != null)
				gridPanel.setColSpan(colSpan);
			// String colWidth = null;
			// if (colWidth != null)
			// gridPanel.setColWidth(colWidth);
			// String colHeight = null;
			// if (colHeight != null)
			// gridPanel.setColHeight(colHeight);
			panel = gridPanel;
		}
		if (panel != null) {
			panel.setId(elementType);
			panel.setViewId(widget);
		}
		return panel;
	}
}
