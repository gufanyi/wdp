package xap.lui.psn.refence;

import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;

public class MockTreeGridDsConfigRefPageModel extends MockRefPageModel {
	
	private static final String FIELD_CODE = "code";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_PID = "pid";
	private static final String FIELD_MDDATA = "mddata";
	//编码、名称
	private static final String GRID_HEADER_CODE ="编码";
	private static final String GRID_HEADER_NAME ="名称";
	
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		UIPartMeta uimeta = new UIPartMeta();
		UIViewPart widget = new UIViewPart();
		widget.setId(WIDGET_ID);
		UIPartMeta wuimeta = new UIPartMeta();
		wuimeta.setId(WIDGET_ID + "_meta");
		widget.setUimeta(wuimeta);
		constructViewUI(wuimeta);
		uimeta.setIsReference(1);
		uimeta.setElement(widget);
		return uimeta;
	}	
	
	/**
	 * 搜索
	 * @param wuimeta
	 */
	private void constructViewUI(UIPartMeta wuimeta) {
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("flowvLayout");
		wuimeta.setElement(flowvLayout);
	
		//center
		UIFlowvPanel centerFlowvPanel = new UIFlowvPanel();
		flowvLayout.addPanel(centerFlowvPanel);
		
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId("flowhlayout");
		
		centerFlowvPanel.setElement(flowhLayout);
		
		UIFlowhPanel uiSplitterOne = new UIFlowhPanel();
		uiSplitterOne.setId("panel1");
		uiSplitterOne.setWidth("200");
		uiSplitterOne.setRightBorder("#");
		flowhLayout.addPanel(uiSplitterOne);
		
		UIFlowhPanel uiSplitterTwo = new UIFlowhPanel();
		uiSplitterTwo.setId("panel2");
		flowhLayout.addPanel(uiSplitterTwo);
		
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId(TREE);
		treeComp.setViewId("main");
		
	     //定位
		UITextField search = new UITextField();
		search.setId("search");
		search.setLeft(10);
		search.setWidth("150");
		search.setViewId("main");
			
		UIFlowvLayout vlayout = new UIFlowvLayout();
		vlayout.setId("vlayout1");
		
		UIFlowvPanel vpanel0 = new UIFlowvPanel();
		vpanel0.setId("vpanel0");
		vpanel0.setHeight("9");
		vlayout.addPanel(vpanel0);
		
		UIFlowvPanel vpanel1 = new UIFlowvPanel();
		vpanel1.setId("vpanel1");
		vpanel1.setHeight("30");
		vpanel1.setElement(search);
		vlayout.addPanel(vpanel1);
	
		UIFlowvPanel vpanel2 = new UIFlowvPanel();
		vpanel2.setId("vpanel2");
		vpanel2.setElement(treeComp);
		vlayout.addPanel(vpanel2);
		
		uiSplitterOne.setElement(vlayout);	
			
		UIGridComp uigridComp = new UIGridComp();
		uigridComp.setId(GRID);
		uigridComp.setViewId("main");
		uigridComp.setHeight("345");
		uiSplitterTwo.setElement(uigridComp);
		
		//bottom
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomvp1");
		borderPanelBottom.setHeight("45");
		flowvLayout.addPanel(borderPanelBottom);

		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
	
		UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
		flowhPanel2.setAttribute("id", "flowhPanel2");
		uiflowhLayout.addPanel(flowhPanel2);
		flowhPanel2.setWidth("84");
		flowhPanel2.setTopPadding("12");
		
		UIButton buttonOk = new UIButton();
		buttonOk.setId("okbt");
		buttonOk.setViewId("main");
		buttonOk.setWidth("74");
		buttonOk.setClassName("blue_button_div");
		flowhPanel2.setElement(buttonOk); 
		
		UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
		uiflowhLayout.addPanel(flowhPanel3);
		flowhPanel3.setAttribute("id", "flowhPanel3");
		flowhPanel3.setTopPadding("12");
		flowhPanel3.setWidth("94");
		
		UIButton buttonCanCel = new UIButton();
		buttonCanCel.setId("cancelbt");
		buttonCanCel.setViewId("main");
		buttonCanCel.setWidth("74");
		flowhPanel3.setElement(buttonCanCel);
	}
	
	protected void constructGrid(GridComp grid) {
		GridColumn codeColumn = generateGridColumn(FIELD_CODE,GRID_HEADER_CODE);
		codeColumn.setWidth(300);
		grid.addColumn(codeColumn);
		GridColumn nameColumn = generateGridColumn(FIELD_NAME,GRID_HEADER_NAME);
		grid.addColumn(nameColumn);
	}
	
	private GridColumn generateGridColumn(String id, String text) {
		GridColumn column = new GridColumn();
		column.setId(id);
		column.setText(text);
		column.setWidth(160);
		column.setField(id);
		column.setDataType(StringDataTypeConst.STRING);
		column.setVisible(true);
		column.setEdit(false);
		return column;
	}	
	
	/**
	 * 构建表格对应的Dataset
	 */
	protected void constructGridDataset(Dataset ds) {
		Field idField = new Field();
		idField.setId(FIELD_CODE);
		idField.setText(GRID_HEADER_CODE);
		ds.addField(idField);
		Field pidField = new Field();
		pidField.setId(FIELD_PID);
		pidField.setText(FIELD_PID);
		ds.addField(pidField);
		Field nameField = new Field();
		nameField.setId(FIELD_NAME);
		nameField.setText(GRID_HEADER_NAME);
		ds.addField(nameField);	
		Field metadataField = new Field();
		metadataField.setId(FIELD_MDDATA);
		metadataField.setText(FIELD_MDDATA);
		ds.addField(metadataField);			
	}	
	
	protected void constructTreeDataset(Dataset ds) {
		super.constructTreeDataset(ds);
	}
}
