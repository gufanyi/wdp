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
 * @version 6.0 2011-10-10
 * @since v6.1
 */
public class FormElementInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	private static final String REFERENCE_REFNODE_URL = "app/mockapp/cdref?model="+MockRefPageModel.class.getName()+"&ctrl="+MockRefDataRefController.class.getName();
	private static final String REFERENCE_COMBODATA_URL = "app/mockapp/cdref?model="+MockTreeGridComboDataRefPageModel.class.getName()+"&ctrl="+MockTreeGridComboDataRefController.class.getName();
	
	public FormElementInfo(){
		super();
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(new ModePhase[]{ModePhase.eclipse, ModePhase.persona},true);
		className.setEditable(true);
		className.setDsField("string_ext15");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
		StringPropertyInfo field = new StringPropertyInfo();
		field.setId("field");
		field.setVisible(true);
		field.setEditable(false);
		field.setDsField("string_ext4");
		field.setLabel("字段");
		field.setVoField("field");
		list.add(field);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("label");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext5");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(false);
		i18n.setEditable(true);
		i18n.setDsField("string_ext6");
		i18n.setLabel("多语资源");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		StringPropertyInfo description = new StringPropertyInfo();
		description.setId("description");
		description.setVisible(false);
		description.setEditable(true);
		description.setDsField("string_ext7");
		description.setLabel("描述");
		description.setVoField("description");
		list.add(description);
		
		StringPropertyInfo langDir = new StringPropertyInfo();
		langDir.setId("langDir");
		langDir.setVisible(false);
		langDir.setEditable(true);
		langDir.setDsField("string_ext8");
		langDir.setLabel("多语目录");
		langDir.setVoField("langdir");
		list.add(langDir);

		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("refNode");
		refInfo.setVisible(true);
		refInfo.setEditable(true);
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setWidth("800");
		refInfo.setHeight("480");
		refInfo.setLabel("引用参照");
		refInfo.setUrl(REFERENCE_REFNODE_URL);
		refInfo.setVoField("refnode");
		list.add(refInfo);
		
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
		
		ComboPropertyInfo labelPos = new ComboPropertyInfo();
		labelPos.setId("labelPos");
		labelPos.setKeys(new String[]{"左", "右"});
		labelPos.setValues(new String[]{"left", "right"});
		labelPos.setVisible(true);
		labelPos.setEditable(true);
		labelPos.setDsField("combo_ext11");
		labelPos.setType(StringDataTypeConst.STRING);
		labelPos.setDefaultValue("left");
		labelPos.setVoField("labelPos");
		labelPos.setLabel("显示值位置");
		list.add(labelPos);
		
		ComboPropertyInfo editorType = new ComboPropertyInfo();
		editorType.setId("editorType");
		editorType.setVisible(true);
		editorType.setEditable(true);
		editorType.setType(StringDataTypeConst.STRING);
		editorType.setKeys(getValue());
		editorType.setValues(TEXTTYPE);
		editorType.setDsField("combo_ext6");
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
				}else if(EditorTypeConst.COMBODATA.equals(val) || EditorTypeConst.RADIOGROUP.equals(val)|| EditorTypeConst.CHECKBOXGROUP.equals(val)){
					form.getElementById(refcombofield).setVisible(true);
					form.getElementById(refnodefield).setVisible(false);
				}else{
					form.getElementById(refcombofield).setVisible(false);
					form.getElementById(refnodefield).setVisible(false);
				}
			}});
		list.add(editorType);
		
		StringPropertyInfo dataType = new StringPropertyInfo();
		dataType.setId("dataType");
		dataType.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		dataType.setEditable(new ModePhase[]{ModePhase.eclipse},true);
		dataType.setDsField("string_ext10");
		dataType.setLabel("数据类型");
		dataType.setVoField("datatype");
		list.add(dataType);
		

		ComboPropertyInfo labelColorCombo = new ComboPropertyInfo();
		labelColorCombo.setId("labelColor");
		labelColorCombo.setKeys(getColorKeys());
		labelColorCombo.setValues(getColorValues());		
		labelColorCombo.setVisible(true);
		labelColorCombo.setEditable(true);
		labelColorCombo.setDsField("combo_ext10");
		labelColorCombo.setType(StringDataTypeConst.STRING);
		labelColorCombo.setLabel("字体颜色");
		labelColorCombo.setVoField("labelcolor");
		list.add(labelColorCombo);
		
		StringPropertyInfo defaultValue = new StringPropertyInfo();
		defaultValue.setId("defaultValue");
		defaultValue.setVisible(false);
		defaultValue.setEditable(true);
		defaultValue.setDsField("string_ext14");
		defaultValue.setLabel("默认值");
		defaultValue.setVoField("defaultvalue");
		list.add(defaultValue);

		StringPropertyInfo maxLength = new StringPropertyInfo();
		maxLength.setId("maxLength");
		maxLength.setVisible(false);
		maxLength.setEditable(true);
		maxLength.setDsField("string_ext17");
		maxLength.setLabel("最大字节长度");
		maxLength.setVoField("maxlength");
		list.add(maxLength);
		
		StringPropertyInfo input = new StringPropertyInfo();
		input.setId("inputAssistant");
		input.setVisible(false);
		input.setEditable(true);
		input.setDsField("string_ext18");
		input.setLabel("输入辅助提示");
		input.setVoField("inputassistant");
		list.add(input);
		
		StringPropertyInfo maxValue = new StringPropertyInfo();
		maxValue.setId("maxValue");
		maxValue.setVisible(false);
		maxValue.setEditable(true);
		maxValue.setDsField("string_ext19");
		maxValue.setLabel("最大值");
		maxValue.setVoField("maxvalue");
		list.add(maxValue);
		
		StringPropertyInfo bindId = new StringPropertyInfo();
		bindId.setId("bindId");
		bindId.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		bindId.setEditable(true);
		bindId.setDsField("string_ext20");
		bindId.setLabel("控件ID");
		bindId.setVoField("bindid");
		list.add(bindId);
		
		StringPropertyInfo minValue = new StringPropertyInfo();
		minValue.setId("minValue");
		minValue.setVisible(false);
		minValue.setEditable(true);
		minValue.setDsField("string_ext21");
		minValue.setLabel("最小值");
		minValue.setVoField("minvalue");
		list.add(minValue);
		
		StringPropertyInfo precision = new StringPropertyInfo();
		precision.setId("precision");
		precision.setVisible(true);
		precision.setEditable(true);
		precision.setDsField("string_ext22");
		precision.setLabel("精度");
		precision.setVoField("precisions");
		list.add(precision);
		
		StringPropertyInfo hideBarIndices = new StringPropertyInfo();
		hideBarIndices.setId("hideBarIndices");
		hideBarIndices.setVisible(false);
		hideBarIndices.setEditable(true);
		hideBarIndices.setDsField("string_ext23");
		hideBarIndices.setLabel("hideBarIndices");
		hideBarIndices.setVoField("hidebarindices");
		list.add(hideBarIndices);
		
		StringPropertyInfo hideImageIndices = new StringPropertyInfo();
		hideImageIndices.setId("hideImageIndices");
		hideImageIndices.setVisible(false);
		hideImageIndices.setEditable(true);
		hideImageIndices.setDsField("string_ext24");
		hideImageIndices.setLabel("hideImageIndices");
		hideImageIndices.setVoField("hideimageindices");
		list.add(hideImageIndices);
		
		StringPropertyInfo tip = new StringPropertyInfo();
		tip.setId("tip");
		tip.setVisible(true);
		tip.setEditable(true);
		tip.setDsField("string_ext25");
		tip.setLabel("提示");
		tip.setVoField("tip");
		list.add(tip);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext26");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext27");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		ComboPropertyInfo editable = new ComboPropertyInfo();
		editable.setId("editable");
		editable.setVisible(true);
		editable.setEditable(true);
		editable.setType(StringDataTypeConst.bOOLEAN);
		editable.setKeys(KEYS);
		editable.setValues(VALUES);
		
		editable.setDsField("combo_ext1");
		editable.setLabel("是否可编辑");
		editable.setVoField("editables");
		list.add(editable);
		
		ComboPropertyInfo enabled = new ComboPropertyInfo();
		enabled.setId("enabled");
		enabled.setVisible(true);
		enabled.setEditable(true);
		enabled.setType(StringDataTypeConst.bOOLEAN);
		enabled.setKeys(KEYS);
		enabled.setValues(VALUES);
		enabled.setDsField("combo_ext2");
		enabled.setLabel("是否可用");
		enabled.setVoField("enableds");
		list.add(enabled);
		
		ComboPropertyInfo visible = new ComboPropertyInfo();
		visible.setId("visible");
		visible.setVisible(true);
		visible.setEditable(true);
		visible.setType(StringDataTypeConst.bOOLEAN);
		visible.setKeys(KEYS);
		visible.setValues(VALUES);
		visible.setDsField("combo_ext3");
		visible.setLabel("是否可见");
		visible.setVoField("visibles");
		list.add(visible);
		
		ComboPropertyInfo nextLine = new ComboPropertyInfo();
		nextLine.setId("nextLine");
		nextLine.setVisible(false);
		nextLine.setEditable(true);
		nextLine.setType(StringDataTypeConst.bOOLEAN);
		nextLine.setKeys(KEYS);
		nextLine.setValues(VALUES);
		nextLine.setDsField("combo_ext4");
		nextLine.setLabel("nextLine");
		nextLine.setVoField("nextline");
		list.add(nextLine);
		
		ComboPropertyInfo imageOnly = new ComboPropertyInfo();
		imageOnly.setId("imageOnly");
		imageOnly.setVisible(false);
		imageOnly.setEditable(true);
		imageOnly.setType(StringDataTypeConst.bOOLEAN);
		imageOnly.setKeys(KEYS);
		imageOnly.setValues(VALUES);
		imageOnly.setDsField("combo_ext5");
		imageOnly.setLabel("只显示图片");
		imageOnly.setVoField("imageonlys");
		list.add(imageOnly);
		
		ComboPropertyInfo nullAble = new ComboPropertyInfo();
		nullAble.setId("nullAble");
		nullAble.setVisible(true);
		nullAble.setEditable(true);
		nullAble.setType(StringDataTypeConst.bOOLEAN);
		nullAble.setKeys(KEYS);
		nullAble.setValues(VALUES);
		nullAble.setDsField("combo_ext7");
		nullAble.setLabel("是否可以为空");
		nullAble.setVoField("nullables");
		list.add(nullAble);
		
		ComboPropertyInfo attachNext = new ComboPropertyInfo();
		attachNext.setId("attachNext");
		attachNext.setVisible(false);
		attachNext.setEditable(true);
		attachNext.setType(StringDataTypeConst.bOOLEAN);
		attachNext.setKeys(KEYS);
		attachNext.setValues(VALUES);
		attachNext.setDsField("combo_ext8");
		attachNext.setLabel("连通下一元素");
		attachNext.setVoField("attachnexts");
		list.add(attachNext);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setKeys(KEYS);
		focus.setValues(VALUES);
		focus.setDsField("combo_ext9");
		focus.setLabel("是否聚焦");
		focus.setVoField("focuss");
		list.add(focus);
		
		IntegerPropertyInfo rowSpan = new IntegerPropertyInfo();
		rowSpan.setId("rowSpan");
		rowSpan.setEditable(true);
		rowSpan.setVisible(true);
		rowSpan.setType(StringDataTypeConst.INTEGER);
		rowSpan.setDsField("integer_ext1");
		rowSpan.setLabel("跨行数");
		rowSpan.setVoField("rowspan");
		list.add(rowSpan);
		
		IntegerPropertyInfo colSpan = new IntegerPropertyInfo();
		colSpan.setId("colSpan");
		colSpan.setEditable(true);
		colSpan.setVisible(true);
		colSpan.setType(StringDataTypeConst.INTEGER);
		colSpan.setDsField("integer_ext2");
		colSpan.setLabel("跨列数");
		colSpan.setVoField("colspan");
		list.add(colSpan);
		
		IntegerPropertyInfo index = new IntegerPropertyInfo();
		index.setId("index");
		index.setEditable(true);
		index.setVisible(false);
		index.setType(StringDataTypeConst.INT);
		index.setDsField("integer_ext3");
		index.setLabel("索引");
		index.setVoField("indexs");
		list.add(index);
		
		StringPropertyInfo eleWidth = new StringPropertyInfo();
		eleWidth.setId("eleWidth");
		eleWidth.setEditable(true);
		eleWidth.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		eleWidth.setDsField("string_ext30");
		eleWidth.setLabel("元素宽度");
		eleWidth.setVoField("elewidth");
		list.add(eleWidth);
		
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
		vdfRef.setUrl("app/mockapp/formula?model=" + DesignerFormulaPageModel.class.getName() + "&ctrl=" + DesignerFormulaController.class.getName());
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
		efref.setUrl("app/mockapp/formula?model=" + DesignerFormulaPageModel.class.getName() + "&ctrl=" + DesignerFormulaController.class.getName());
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
