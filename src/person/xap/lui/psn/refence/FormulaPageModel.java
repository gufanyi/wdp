package xap.lui.psn.refence;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.Window;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.GridTreeLevel;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.LuiParseException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.formula.Category;
import xap.lui.psn.formula.FormulaXml;
public abstract class FormulaPageModel extends Window {
	private static final String FILE_PATH = "/lui/nodes/formula/formulasetting.xml";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DESC = "description";
	private static final String PID = "pid";
	private static final String DEMO = "demo";
	private static final String GRID="grid";
	private static final String DS = "ds";
	protected static final String VIEWID = "editor";
	private ViewPartMeta sourceWidget = null;
	private PagePartMeta sourceWin = null;
	
	@Override
	protected void initPageMetaStruct() {
		List<Category> categories = getCategories();
		if (categories != null && categories.size() > 0) {
			ViewPartMeta formulaView = getFormulaView();
			DataModels datamodels = formulaView.getViewModels();
			ViewPartComps comps = formulaView.getViewComponents();
			for (Category category : categories) {
				Dataset ds = new Dataset();
				ds.setId(category.getId() + DS);
				ds.setCaption(category.getName());
				ds.setEdit(false);//可编辑状态不支持双击事件
				ds.setLazyLoad(true);
				datamodels.addDataset(ds);
				constructDs(ds);

				GridComp gridComp = new GridComp(category.getId() + GRID);
				// gridComp.setId(category.getId()+"grid");
				gridComp.setDataset(ds.getId());
				gridComp.setCaption(category.getName());
				constructGrid(gridComp, category.getId());
				LuiEventConf eventConf = new LuiEventConf();
				eventConf.setEventType("GridRowEvent");
				eventConf.setEventName("onRowSelected");
				eventConf.setOnserver(true);
				eventConf.setAsync(true);
				eventConf.setNmc(true);
				eventConf.setEventStatus(1);
				eventConf.setMethod("setDescription2");
//				eventConf.setScript("setDescription2(rowSelectedEvent);");
				gridComp.addEventConf(eventConf);
				
				LuiEventConf eventConf2 = new LuiEventConf();
				eventConf2.setEventType("GridRowEvent");
				eventConf2.setEventName("onRowDbClick");
				eventConf2.setOnserver(true);
				eventConf2.setAsync(true);
				eventConf2.setNmc(true);
				eventConf2.setEventStatus(1);
				eventConf2.setMethod("onFormulaDbClick2");
//				eventConf2.setScript("onFormulaDbClick2(rowEvent);");
				gridComp.addEventConf(eventConf2);
				
				comps.addComponent(gridComp);
			}
//			java.util.logging.Logger.getAnonymousLogger().info(formulaView.toXml());
		}
		
	}

	protected ViewPartMeta getFormulaView() {
		PagePartMeta formulaPartMeta = getPageMeta();
		ViewPartMeta formulaView = formulaPartMeta.getWidget("editor");
		formulaView.setController(LuiRuntimeContext.getWebContext().getParameter("ctrl"));
		return formulaView;
	}
	
	@Override
	protected void initUIMetaStruct() {
		List<Category> categories = getCategories();
		UITabComp tabLayout = (UITabComp) getUIElement("tag1984");
		int count=0;
		for (Category category : categories) {
			UITabItem tabItem = new UITabItem();
			tabItem.setId("UITabItem"+(count++));
			tabItem.setText(category.getName());
			tabItem.setViewId(VIEWID);
			tabLayout.addPanel(tabItem);

			UIGridComp uiGridComp = new UIGridComp();
			uiGridComp.setId(category.getId() + GRID);
			uiGridComp.setViewId(VIEWID);
			tabItem.setElement(uiGridComp);
		}
		addTabItem();
		
	    UIViewPart  uielement=	(UIViewPart)getUIPartMeta().findChildById("editor");
	    
//		java.util.logging.Logger.getAnonymousLogger().info(uielement.getUimeta().toXml());
	}
	
	protected abstract void addTabItem();
	
	private UIElement getUIElement(String eleid) {
		return getUIPartMeta().findChildById(eleid);
	}
	private UITabComp getTab(){//得到可扩展的tab
		UITabComp tabLayout = (UITabComp) getUIElement("tag8157");
		return tabLayout;
	}
	protected UITabItem createTabItem(String text) {
		UITabItem tabItem = new UITabItem();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		tabItem.setId("UITabItem"+uuid);
		tabItem.setText(text);
		tabItem.setViewId(VIEWID);
		UITabComp tab = getTab();
		tab.addPanel(tabItem);
		return tabItem;
	}
	
	public static FormulaXml fetchFormulaXmlRoot() {
		FormulaXml formulaXmlRoot = null;
		InputStream input = null;
		try {
			input = ContextResourceUtil.getResourceAsStream(FILE_PATH);
			if (input == null) {
				throw new LuiParseException("从路径:" + FILE_PATH + ",没有找到xml文件！");
			}
			formulaXmlRoot = FormulaXml.parse(input);
			if (formulaXmlRoot == null) {
				throw new LuiParseException("获取FormulaXmlRoot时出错!");
			}
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return formulaXmlRoot;
	}

	private List<Category> getCategories() {
		FormulaXml formulaXmlRoot = fetchFormulaXmlRoot();
		List<Category> categories = formulaXmlRoot.getCategoryList();
		return categories;
	}
	private void constructGrid(GridComp gridComp, String cateid){
		GridColumn columnId = generateGridColumn(ID);
		columnId.setFitWidth(true);
		if(StringUtils.equals(cateid, "valiformula"))
			columnId.setText("校验名");
		gridComp.addColumn(columnId);
		
		GridColumn columnName = generateGridColumn(NAME);
		if(StringUtils.equals(cateid, "valiformula"))
			columnName.setText("正则表达式");
		columnName.setFitWidth(true);
		gridComp.addColumn(columnName);
		
		GridTreeLevel gridTree = new GridTreeLevel();
		gridTree.setId("level"+gridComp.getId());
		gridTree.setRecursiveField(ID);
		gridTree.setRecursiveParentField(PID);
		gridTree.setDataset(gridComp.getDataset());
		gridTree.setLabelFields(NAME);
		gridComp.setTopLevel(gridTree);
	}
	private GridColumn generateGridColumn(String str) {
		GridColumn columnId = new GridColumn();
		columnId.setId(str);
		columnId.setText(str);
		columnId.setWidth(120);
		columnId.setDataType(StringDataTypeConst.STRING);
		columnId.setVisible(true);
		columnId.setEdit(false);
		return columnId;
	}
	private void constructDs(Dataset ds) {
		Field idField = new Field();
		idField.setId(ID);
		idField.setText(ID);
		ds.addField(idField);
		
		Field pidField = new Field();
		pidField.setId(PID);
		pidField.setText(PID);
		ds.addField(pidField);
		
		Field nameField = new Field();
		nameField.setId(NAME);
		nameField.setText(NAME);
		ds.addField(nameField);
		
		Field descField = new Field();
		descField.setId(DESC);
		descField.setText(DESC);
		ds.addField(descField);
		
		Field demoField = new Field();
		demoField.setId(DEMO);
		demoField.setText(DEMO);
		ds.addField(demoField);
	}
	private String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}
}
