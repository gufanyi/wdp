
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;

/**
 * @author wupeng1
 * @version 6.0 2011-8-24
 * @since 1.6
 */
public class GridCompInfo extends ControlInfo implements IBaseInfo {

	private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	public GridCompInfo(){
		super();
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext5");
		width.setLabel("宽度");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext6");
		height.setLabel("高度");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo ds = new StringPropertyInfo();
		ds.setId("dataset");
		ds.setVisible(true);
		ds.setEditable(new ModePhase[]{ModePhase.eclipse},true);
		ds.setDsField("string_ext7");
		ds.setLabel("数据集");
		ds.setVoField("dataset");
		list.add(ds);
		
		StringPropertyInfo cap = new StringPropertyInfo();
		cap.setId("caption");
		cap.setVisible(true);
		cap.setEditable(true);
		cap.setDsField("string_ext8");
		cap.setLabel("标题");
		cap.setVoField("caption");
		list.add(cap);
		
		StringPropertyInfo rowh = new StringPropertyInfo();
		rowh.setId("rowHeight");
		rowh.setVisible(true);
		rowh.setEditable(true);
		rowh.setDsField("string_ext9");
		rowh.setLabel("单行高度");
		rowh.setVoField("rowheight");
		list.add(rowh);
		
		StringPropertyInfo hrh = new StringPropertyInfo();
		hrh.setId("headerRowHeight");
		hrh.setVisible(true);
		hrh.setEditable(true);
		hrh.setDsField("string_ext10");
		hrh.setLabel("表头行高度");
		hrh.setVoField("headerrowheight");
		list.add(hrh);
		
//		StringPropertyInfo pageSize = new StringPropertyInfo();
//		pageSize.setId("pageSize");
//		pageSize.setVisible(true);
//		pageSize.setEditable(true);
//		pageSize.setDsField("string_ext11");
//		pageSize.setLabel("分页大小");
//		pageSize.setVoField("pageSize");
//		list.add(pageSize);
		

		StringPropertyInfo toolbarRender = new StringPropertyInfo();
		toolbarRender.setId("toolbarRender");
		toolbarRender.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		toolbarRender.setEditable(true);
		toolbarRender.setDsField("string_ext12");
		toolbarRender.setLabel("自定义按钮Render");
		toolbarRender.setVoField("toolbarRender");
		list.add(toolbarRender);
		
		SelfDefRefPropertyInfo groupColumns = new SelfDefRefPropertyInfo();
		groupColumns.setId("groupColumns");
		groupColumns.setVisible(true);
		groupColumns.setEditable(true);
		groupColumns.setType(StringDataTypeConst.STRING);
		groupColumns.setDsField("ref_ext3");
		groupColumns.setLabel("分组列");
		groupColumns.setUrl("app/mockapp/groupcolumns?writeDs=ds_middle&writeField=ref_ext3");
		groupColumns.setWidth("600");
		groupColumns.setHeight("500");
		groupColumns.setVoField("groupColumns");
		list.add(groupColumns);

//		StringPropertyInfo oddtype = new StringPropertyInfo();
//		oddtype.setId("oddType");
//		oddtype.setVisible(false);
//		oddtype.setEditable(true);
//		oddtype.setDsField("string_ext11");
//		oddtype.setLabel("单双行类型");
//		oddtype.setVoField("oddtype");
//		list.add(oddtype);

//		StringPropertyInfo className = new StringPropertyInfo();
//		className.setId("className");
//		className.setVisible(true);
//		className.setEditable(true);
//		className.setDsField("string_ext15");
//		className.setLabel("自定义主题");
//		className.setVoField("classname");
//		list.add(className);		
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(KEYS);
		ena.setValues(VALUES);
		ena.setDsField("combo_ext1");
		ena.setLabel("是否可用");
		ena.setVoField("enableds");
		list.add(ena);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext3");
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		list.add(vis);
		
		ComboPropertyInfo isEdit = new ComboPropertyInfo();
		isEdit.setId("edit");
		isEdit.setVisible(true);
		isEdit.setEditable(true);
		isEdit.setType(StringDataTypeConst.bOOLEAN);
		isEdit.setKeys(KEYS);
		isEdit.setValues(VALUES);
		isEdit.setDsField("combo_ext4");
		isEdit.setLabel("是否可编辑");
		isEdit.setVoField("edit");
		list.add(isEdit);
		
		ComboPropertyInfo isFitRowHeight = new ComboPropertyInfo();
		isFitRowHeight.setId("fitRowHeight");
		isFitRowHeight.setVisible(true);
		isFitRowHeight.setEditable(true);
		isFitRowHeight.setType(StringDataTypeConst.bOOLEAN);
		isFitRowHeight.setKeys(KEYS);
		isFitRowHeight.setValues(VALUES);
		isFitRowHeight.setDsField("combo_ext5");
		isFitRowHeight.setLabel("行高自适应");
		isFitRowHeight.setVoField("fitRowHeight");
		list.add(isFitRowHeight);
		
		ComboPropertyInfo isExpandTree = new ComboPropertyInfo();
		isExpandTree.setId("expandTree");
		isExpandTree.setVisible(true);
		isExpandTree.setEditable(true);
		isExpandTree.setType(StringDataTypeConst.bOOLEAN);
		isExpandTree.setKeys(KEYS);
		isExpandTree.setValues(VALUES);
		isExpandTree.setDsField("combo_ext6");
		isExpandTree.setLabel("是否展开树表");
		isExpandTree.setVoField("expandTree");
		list.add(isExpandTree);
		
		ComboPropertyInfo isCopy = new ComboPropertyInfo();
		isCopy.setId("copy");
		isCopy.setVisible(true);
		isCopy.setEditable(true);
		isCopy.setType(StringDataTypeConst.bOOLEAN);
		isCopy.setKeys(KEYS);
		isCopy.setValues(VALUES);
		isCopy.setDsField("combo_ext7");
		isCopy.setLabel("是否可复制");
		isCopy.setVoField("copy");
		list.add(isCopy);
		
		
		
		ComboPropertyInfo isAllowMouseoverChange = new ComboPropertyInfo();
		isAllowMouseoverChange.setId("allowMouseoverChange");
		isAllowMouseoverChange.setVisible(true);
		isAllowMouseoverChange.setEditable(true);
		isAllowMouseoverChange.setType(StringDataTypeConst.bOOLEAN);
		isAllowMouseoverChange.setKeys(KEYS);
		isAllowMouseoverChange.setValues(VALUES);
		isAllowMouseoverChange.setDsField("combo_ext8");
		isAllowMouseoverChange.setLabel("悬浮改变行颜色");
		isAllowMouseoverChange.setVoField("allowMouseoverChange");
		list.add(isAllowMouseoverChange);
		
		ComboPropertyInfo isPageTop = new ComboPropertyInfo();
		isPageTop.setId("pageTop");
		isPageTop.setVisible(true);
		isPageTop.setEditable(true);
		isPageTop.setType(StringDataTypeConst.bOOLEAN);
		isPageTop.setKeys(KEYS);
		isPageTop.setValues(VALUES);
		isPageTop.setDsField("combo_ext9");
		isPageTop.setLabel("分页工具在顶端");
		isPageTop.setVoField("pageTop");
		list.add(isPageTop);
		
		ComboPropertyInfo isMultiple = new ComboPropertyInfo();
		isMultiple.setId("multiple");
		isMultiple.setVisible(true);
		isMultiple.setEditable(true);
		isMultiple.setType(StringDataTypeConst.bOOLEAN);
		isMultiple.setKeys(KEYS);
		isMultiple.setValues(VALUES);
		isMultiple.setDsField("combo_ext10");
		isMultiple.setLabel("是否可多选");
		isMultiple.setVoField("multiple");
		list.add(isMultiple);
		
		ComboPropertyInfo isShowTip = new ComboPropertyInfo();
		isShowTip.setId("showTip");
		isShowTip.setVisible(true);
		isShowTip.setEditable(true);
		isShowTip.setType(StringDataTypeConst.bOOLEAN);
		isShowTip.setKeys(KEYS);
		isShowTip.setValues(VALUES);
		isShowTip.setDsField("combo_ext11");
		isShowTip.setLabel("显示提示");
		isShowTip.setVoField("showTip");
		list.add(isShowTip);

		
		
		ComboPropertyInfo isShowNum = new ComboPropertyInfo();
		isShowNum.setId("showNum");
		isShowNum.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		isShowNum.setEditable(true);
		isShowNum.setType(StringDataTypeConst.bOOLEAN);
		isShowNum.setKeys(KEYS);
		isShowNum.setValues(VALUES);
		isShowNum.setDsField("combo_ext12");
		isShowNum.setLabel("显示数字列");
		isShowNum.setVoField("showNum");
		list.add(isShowNum);
		
		ComboPropertyInfo isShowTotalRow = new ComboPropertyInfo();
		isShowTotalRow.setId("showTotalRow");
		isShowTotalRow.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		isShowTotalRow.setEditable(true);
		isShowTotalRow.setType(StringDataTypeConst.bOOLEAN);
		isShowTotalRow.setKeys(KEYS);
		isShowTotalRow.setValues(VALUES);
		isShowTotalRow.setDsField("combo_ext13");
		isShowTotalRow.setLabel("显示合计行");
		isShowTotalRow.setVoField("showTotalRow");
		list.add(isShowTotalRow);
		
		ComboPropertyInfo isShowHeader = new ComboPropertyInfo();
		isShowHeader.setId("showHeader");
		isShowHeader.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		isShowHeader.setEditable(true);
		isShowHeader.setType(StringDataTypeConst.bOOLEAN);
		isShowHeader.setKeys(KEYS);
		isShowHeader.setValues(VALUES);
		isShowHeader.setDsField("combo_ext14");
		isShowHeader.setLabel("显示表头");
		isShowHeader.setVoField("showHeader");
		list.add(isShowHeader);
		
		ComboPropertyInfo isShowToolbar = new ComboPropertyInfo();
		isShowToolbar.setId("showToolbar");
		isShowToolbar.setVisible(true);
		isShowToolbar.setEditable(true);
		isShowToolbar.setType(StringDataTypeConst.bOOLEAN);
		isShowToolbar.setKeys(KEYS);
		isShowToolbar.setValues(VALUES);
		isShowToolbar.setDsField("combo_ext2");
		isShowToolbar.setLabel("显示功能按钮");
		isShowToolbar.setVoField("showToolbar");
		list.add(isShowToolbar);
		
		ComboPropertyInfo isShowColMenu = new ComboPropertyInfo();
		isShowColMenu.setId("showColMenu");
		isShowColMenu.setVisible(true);
		isShowColMenu.setEditable(true);
		isShowColMenu.setType(StringDataTypeConst.bOOLEAN);
		isShowColMenu.setKeys(KEYS);
		isShowColMenu.setValues(VALUES);
		isShowColMenu.setDsField("combo_ext16");
		isShowColMenu.setLabel("显示列菜单");
		isShowColMenu.setVoField("showColMenu");
		list.add(isShowColMenu);

		
		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("GridColun");
		refInfo.setVisible(true);
		refInfo.setEditable(true);
		refInfo.setWidth("680");
		refInfo.setHeight("480");
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setLabel("表单项");
		refInfo.setVoField("sourcePackage");
		refInfo.setUrl("app/mockapp/fieldmgr");
		list.add(refInfo);
		
	}
}
