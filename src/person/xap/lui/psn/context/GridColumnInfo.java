package xap.lui.psn.context;

import xap.lui.core.comps.FormComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.psn.formula.DesignerFormulaController;
import xap.lui.psn.formula.DesignerFormulaPageModel;
import xap.lui.psn.refence.MockRefDataRefController;
import xap.lui.psn.refence.MockRefPageModel;
import xap.lui.psn.refence.MockTreeGridComboDataRefController;
import xap.lui.psn.refence.MockTreeGridComboDataRefPageModel;

/**
 * @author wupeng1
 * @version 6.0 2011-9-27
 * @since 1.6
 */
public class GridColumnInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	
	
	private static final String[] KEYS = new String[]{"是","否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	private static final String REFERENCE_REFNODE_URL = "app/mockapp/cdref?model="+MockRefPageModel.class.getName()+"&ctrl="+MockRefDataRefController.class.getName();
	private static final String REFERENCE_COMBODATA_URL = "app/mockapp/cdref?model="+MockTreeGridComboDataRefPageModel.class.getName()+"&ctrl="+MockTreeGridComboDataRefController.class.getName();
	private static final String FORMULA_REF_URL = "app/mockapp/formula?model=" + DesignerFormulaPageModel.class.getName()+"&ctrl=" + DesignerFormulaController.class.getName();
	
	public GridColumnInfo(){
		super();
		StringPropertyInfo field = new StringPropertyInfo();
		field.setId("field");
		field.setEditable(false);
		field.setVisible(true);
		field.setDsField("string_ext4");
		field.setVoField("field");
		field.setLabel("字段");
		list.add(field);
		
		StringPropertyInfo langDir = new StringPropertyInfo();
		langDir.setId("langDir");
		langDir.setEditable(true);
		langDir.setVisible(false);
		langDir.setDsField("string_ext5");
		langDir.setVoField("langdir");
		langDir.setLabel("多语目录");
		list.add(langDir);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(false);
		i18n.setEditable(true);
		i18n.setDsField("string_ext6");
		i18n.setLabel("多语资源");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext7");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		IntegerPropertyInfo width = new IntegerPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setType(StringDataTypeConst.INT);
		width.setDsField("integer_ext1");
		width.setLabel("宽度");
		width.setVoField("width");
		list.add(width);
		
		ComboPropertyInfo datatype = new ComboPropertyInfo();
		datatype.setId("dataType");
		datatype.setVisible(true);
		datatype.setEditable(true);
		datatype.setDsField("combo_ext14");
		datatype.setType(StringDataTypeConst.STRING);
		datatype.setKeys(new String[]{"String", "Integer", "Double", "FDouble", "Float", "Byte", "Boolean", "FBoolean", "Date",  "BigDecimal", "Long",  "FDateTime", "FDate", "FTime", "Decimal", "Entity", "Object", "SelfDefine", "Memo"});
		datatype.setValues(new String[]{"String", "Integer", "Double", "FDouble", "Float", "Byte", "Boolean", "FBoolean", "Date",  "BigDecimal", "Long",  "FDateTime", "FDate", "FTime", "Decimal", "Entity", "Object", "SelfDefine", "Memo"});
		datatype.setLabel("数据类型");
		datatype.setVoField("datatype");
		list.add(datatype);
		
		ComboPropertyInfo isSort = new ComboPropertyInfo();
		isSort.setId("sort");
		isSort.setVisible(true);
		isSort.setEditable(true);
		isSort.setKeys(KEYS);
		isSort.setValues(VALUES);
		isSort.setType(StringDataTypeConst.bOOLEAN);
		isSort.setDsField("combo_ext1");
		isSort.setLabel("是否可排序");
		isSort.setVoField("sort");
		list.add(isSort);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext2");
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
		isEdit.setDsField("combo_ext3");
		isEdit.setLabel("是否可编辑");
		isEdit.setVoField("edit");
		list.add(isEdit);
		
		ComboPropertyInfo autoExpand = new ComboPropertyInfo();
		autoExpand.setId("fitWidth");
		autoExpand.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		autoExpand.setEditable(true);
		autoExpand.setType(StringDataTypeConst.bOOLEAN);
		autoExpand.setKeys(KEYS);
		autoExpand.setValues(VALUES);
		autoExpand.setDsField("combo_ext4");
		autoExpand.setLabel("自动扩展");
		autoExpand.setVoField("fitWidth");
		list.add(autoExpand);
		
		ComboPropertyInfo isFixed = new ComboPropertyInfo();
		isFixed.setId("fixed");
		isFixed.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		isFixed.setEditable(true);
		isFixed.setType(StringDataTypeConst.bOOLEAN);
		isFixed.setKeys(KEYS);
		isFixed.setValues(VALUES);
		isFixed.setDsField("combo_ext5");
		isFixed.setLabel("固定表头");
		isFixed.setVoField("fixed");
		list.add(isFixed);
		
		ComboPropertyInfo imageOnly = new ComboPropertyInfo();
		imageOnly.setId("imageOnly");
		imageOnly.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		imageOnly.setEditable(true);
		imageOnly.setType(StringDataTypeConst.bOOLEAN);
		imageOnly.setKeys(KEYS);
		imageOnly.setValues(VALUES);
		imageOnly.setDsField("combo_ext6");
		imageOnly.setLabel("只显示图片");
		imageOnly.setVoField("imageonlys");
		list.add(imageOnly);
		
		ComboPropertyInfo sumCol = new ComboPropertyInfo();
		sumCol.setId("sumCol");
		sumCol.setVisible(true);
		sumCol.setEditable(true);
		sumCol.setType(StringDataTypeConst.bOOLEAN);
		sumCol.setKeys(KEYS);
		sumCol.setValues(VALUES);
		sumCol.setDsField("combo_ext7");
		sumCol.setLabel("是否合计列");
		sumCol.setVoField("sumcols");
		list.add(sumCol);
		
		ComboPropertyInfo isRequire = new ComboPropertyInfo();
		isRequire.setId("require");
		isRequire.setVisible(true);
		isRequire.setEditable(true);
		isRequire.setType(StringDataTypeConst.bOOLEAN);
		isRequire.setKeys(KEYS);
		isRequire.setValues(VALUES);
		isRequire.setDsField("combo_ext8");
		isRequire.setLabel("是否可以为空");
		isRequire.setVoField("require");
		list.add(isRequire);
		
		ComboPropertyInfo showCheckBox = new ComboPropertyInfo();
		showCheckBox.setId("showCheckBox");
		showCheckBox.setVisible(true);
		showCheckBox.setEditable(true);
		showCheckBox.setType(StringDataTypeConst.bOOLEAN);
		showCheckBox.setKeys(KEYS);
		showCheckBox.setValues(VALUES);
		showCheckBox.setDsField("combo_ext15");
		showCheckBox.setLabel("表头显示复选框");
		showCheckBox.setVoField("showCheckBox");
		list.add(showCheckBox);
		
		ComboPropertyInfo bgColor = new ComboPropertyInfo();
		bgColor.setId("columBgColor");
		bgColor.setVisible(true);
		bgColor.setEditable(true);
		bgColor.setType(StringDataTypeConst.STRING);
		bgColor.setKeys(getColorKeys());
		bgColor.setValues(getColorValues());
		bgColor.setDsField("combo_ext11");
		bgColor.setLabel("列背景色");
		bgColor.setVoField("columbgcolor");
		list.add(bgColor);
		
		
		ComboPropertyInfo align = new ComboPropertyInfo();
		align.setId("align");
		align.setVisible(true);
		align.setEditable(true);
		align.setType(StringDataTypeConst.STRING);
		align.setKeys(new String[]{
				"左", 
				"中", 
				"右"
			}
		);
		align.setValues(new String[]{"left", "center", "right"});
		align.setDsField("combo_ext10");
		align.setLabel("内容位置");
		align.setVoField("align");
		list.add(align);
		
		ComboPropertyInfo textColor = new ComboPropertyInfo();
		textColor.setId("textColor");
		textColor.setVisible(true);
		textColor.setEditable(true);
		textColor.setType(StringDataTypeConst.STRING);
		textColor.setKeys(getColorKeys());
		textColor.setValues(getColorValues());
		textColor.setDsField("combo_ext12");
		textColor.setLabel("内容颜色");
		textColor.setVoField("textcolor");
		list.add(textColor);
		
		ComboPropertyInfo renderType = new ComboPropertyInfo();
		renderType.setId("renderType");
		renderType.setVisible(true);
		renderType.setEditable(true);
		renderType.setType(StringDataTypeConst.STRING);
		renderType.setKeys(new String[]{"文本","布尔","整数","浮点","下拉框","图片","日期","时间"});
		renderType.setValues(new String[]{"DefaultRender","BooleanRender","IntegerRender","DecimalRender","ComboRender","ImageRender","DateRender","DateTimeRender"});
		renderType.setDsField("combo_ext13");
		renderType.setLabel("渲染类型");
		renderType.setVoField("renderType");
		list.add(renderType);
		
		SelfDefRefPropertyInfo refnode = new SelfDefRefPropertyInfo();
		refnode.setId("refNode");
		refnode.setVisible(true);
		refnode.setEditable(true);
		refnode.setType(StringDataTypeConst.STRING);
		refnode.setDsField("ref_ext1");
		refnode.setLabel("引用参照");
		refnode.setUrl(REFERENCE_REFNODE_URL);
		refnode.setVoField("refnode");
		list.add(refnode);
		
		SelfDefRefPropertyInfo refComboData = new SelfDefRefPropertyInfo();
		refComboData.setId("refComboData");
		refComboData.setVisible(true);
		refComboData.setEditable(true);
		refComboData.setHeight("540");
		refComboData.setWidth("700");
		refComboData.setType(StringDataTypeConst.STRING);
		refComboData.setDsField("ref_ext4");
		refComboData.setLabel("引用枚举");
		refComboData.setVoField("refComboData");
		refComboData.setUrl(REFERENCE_COMBODATA_URL);
		list.add(refComboData);
		
		ComboPropertyInfo editorType = new ComboPropertyInfo();
		editorType.setId("editorType");
		editorType.setVisible(true);
		editorType.setEditable(true);
		editorType.setType(StringDataTypeConst.STRING);
		editorType.setKeys(getValue());
		editorType.setValues(TEXTTYPE);
		editorType.setDsField("combo_ext9");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		editorType.setChangeMonitor(new IPropertyChangeMonitor(){
			@Override
			public void on(FormComp form, IPropertyInfo pi, Object val) {
				String refnodefield = "ref_ext1";
				String refcombofield = "ref_ext4";
				if(EditorTypeConst.REFERENCE.equals(val)){
					form.getElementById(refcombofield).setVisible(false);
					form.getElementById(refnodefield).setVisible(true);
				}else if(EditorTypeConst.COMBODATA.equals(val) || EditorTypeConst.RADIOCOMP.equals(val)|| EditorTypeConst.CHECKBOXGROUP.equals(val)){
					form.getElementById(refcombofield).setVisible(true);
					form.getElementById(refnodefield).setVisible(false);
				}else{
					form.getElementById(refcombofield).setVisible(false);
					form.getElementById(refnodefield).setVisible(false);
				}
			}});
		list.add(editorType);
		
		StringPropertyInfo maxValue = new StringPropertyInfo();
		maxValue.setId("maxValue");
		maxValue.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		maxValue.setEditable(true);
		maxValue.setDsField("string_ext16");
		maxValue.setLabel("最大值");
		maxValue.setVoField("maxvalue");
		list.add(maxValue);
		
		StringPropertyInfo minValue = new StringPropertyInfo();
		minValue.setId("minValue");
		minValue.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		minValue.setEditable(true);
		minValue.setDsField("string_ext17");
		minValue.setLabel("最小值");
		minValue.setVoField("minvalue");
		list.add(minValue);
		
		StringPropertyInfo precisions = new StringPropertyInfo();
		precisions.setId("precision");
		precisions.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		precisions.setEditable(true);
		precisions.setDsField("string_ext18");
		precisions.setLabel("精度");
		precisions.setVoField("precisions");
		list.add(precisions);
		
		StringPropertyInfo maxlength = new StringPropertyInfo();
		maxlength.setId("maxLength");
		maxlength.setVisible(true);
		maxlength.setEditable(true);
		maxlength.setDsField("string_ext19");
		maxlength.setLabel("最大长度");
		maxlength.setVoField("maxlength");
		list.add(maxlength);
		
		StringPropertyInfo columngroup = new StringPropertyInfo();
		columngroup.setId("colmngroup");
		columngroup.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		columngroup.setEditable(true);
		columngroup.setDsField("string_ext20");
		columngroup.setLabel("所属组");
		columngroup.setVoField("columngroup");
		list.add(columngroup);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
		
		SelfDefRefPropertyInfo vdfRef = new SelfDefRefPropertyInfo();
		vdfRef.setId("validateFormula");
		vdfRef.setVisible(true);
		vdfRef.setEditable(true);
		vdfRef.setHeight("650");
		vdfRef.setWidth("1000");
		vdfRef.setType(StringDataTypeConst.STRING);
		vdfRef.setDsField("ref_ext2");
		vdfRef.setLabel("校验公式");
		vdfRef.setUrl(FORMULA_REF_URL);
		vdfRef.setVoField("validateFormula");
		list.add(vdfRef);		
		
		SelfDefRefPropertyInfo efref = new SelfDefRefPropertyInfo();
		efref.setId("editFormular");
		efref.setHeight("650");
		efref.setWidth("1000");		
		efref.setEditable(true);
		efref.setVisible(true);
		efref.setType(StringDataTypeConst.STRING);
		efref.setDsField("ref_ext3");
		efref.setLabel("编辑公式");
		efref.setUrl(FORMULA_REF_URL);
		efref.setVoField("editFormular");
		list.add(efref);
		
		StringPropertyInfo ext1 = new StringPropertyInfo();
		ext1.setId("ext1");
		ext1.setVisible(true);
		ext1.setEditable(true);
		ext1.setDsField("string_ext31");
		ext1.setLabel("自定义" + "1");
		ext1.setVoField("ext1");
		list.add(ext1);
		
		StringPropertyInfo ext2 = new StringPropertyInfo();
		ext2.setId("ext2");
		ext2.setVisible(true);
		ext2.setEditable(true);
		ext2.setDsField("string_ext32");
		ext2.setLabel("自定义" + "2");
		ext2.setVoField("ext2");
		list.add(ext2);
		
		StringPropertyInfo ext3 = new StringPropertyInfo();
		ext3.setId("ext3");
		ext3.setVisible(true);
		ext3.setEditable(true);
		ext3.setDsField("string_ext33");
		ext3.setLabel("自定义" + "3");
		ext3.setVoField("ext3");
		list.add(ext3);
	}

}
