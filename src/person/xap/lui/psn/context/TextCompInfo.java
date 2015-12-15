
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.psn.formula.DesignerFormulaController;
import xap.lui.psn.formula.DesignerFormulaPageModel;

/**
 * @author wupeng1
 * @version 6.0 2011-9-7
 * @since 1.6
 */
public class TextCompInfo extends ControlInfo {

	private static final long serialVersionUID = 1L;
	
//	private static final String FORMULA_REF_URL = "app/mockapp/cdref?model="+ValidateFormulaPageModel.class.getName()+"&ctrl="+ValidateFormulaViewController.class.getName();
	private static final String FORMULA_REF_URL = "app/mockapp/formula?model=" + DesignerFormulaPageModel.class.getName()+"&ctrl=" + DesignerFormulaController.class.getName();
	
	public TextCompInfo(){
		super();
		
		ComboPropertyInfo visi = new ComboPropertyInfo();
		visi.setId("visible");
		visi.setKeys(new String[]{
				"是",
				"否"
			}
		);
		visi.setValues(new String[]{"Y", "N"});
		visi.setType(StringDataTypeConst.bOOLEAN);
		visi.setDsField("combo_ext1");
		visi.setLabel("是否可见");
		visi.setVoField("visibles");
		visi.setVisible(true);
		visi.setEditable(true);
		list.add(visi);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(new String[]{
				"是",
				"否"
			}
		);
		ena.setValues(new String[]{"Y", "N"});
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可用");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo readonly = new ComboPropertyInfo();
		readonly.setId("readOnly");
		readonly.setVisible(true);
		readonly.setEditable(true);
		readonly.setType(StringDataTypeConst.bOOLEAN);
		readonly.setKeys(new String[]{
				"是",
				"否"
			}
		);
		readonly.setValues(new String[]{"Y", "N"});
		readonly.setDsField("combo_ext3");
		readonly.setLabel("是否只读");
		readonly.setVoField("readonly");
		list.add(readonly);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setKeys(new String[]{
				"是",
				"否"
			}
		);
		focus.setValues(new String[]{"Y", "N"});
		focus.setDsField("combo_ext4");
		focus.setLabel("是否聚焦");
		focus.setVoField("focus");
		list.add(focus);
		
		ComboPropertyInfo showmark = new ComboPropertyInfo();
		showmark.setId("showMark");
		showmark.setVisible(false);
		showmark.setEditable(true);
		showmark.setType(StringDataTypeConst.bOOLEAN);
		showmark.setKeys(new String[]{
				"是",
				"否"
			}
		);
		showmark.setValues(new String[]{"Y", "N"});
		showmark.setDsField("combo_ext5");
		showmark.setLabel("showMark");
		showmark.setVoField("showmark");
		list.add(showmark);
		
		ComboPropertyInfo textalign = new ComboPropertyInfo();
		textalign.setId("textAlign");
		textalign.setVisible(true);
		textalign.setEditable(true);
		textalign.setType(StringDataTypeConst.STRING);
		textalign.setKeys(new String[]{
				"左", 
				"右"
			}
		);
		textalign.setValues(new String[]{"left", "right"});
		textalign.setDsField("combo_ext6");
		textalign.setLabel("标签位置");
		textalign.setVoField("textalign");
		list.add(textalign);
		
		ComboPropertyInfo valgin = new ComboPropertyInfo();
		valgin.setId("valgin");
		valgin.setVisible(true);
		valgin.setEditable(true);
		valgin.setType(StringDataTypeConst.STRING);
		valgin.setKeys(new String[]{
				"是",
				"否"
		});
		valgin.setValues(new String[]{"Y","N"});
		valgin.setDsField("combo_ext7");
		valgin.setLabel("垂直居中");
		valgin.setVoField("valgin");
		list.add(valgin);
		
		
		ComboPropertyInfo nullable = new ComboPropertyInfo();
		nullable.setId("nullable");
		nullable.setVisible(new ModePhase[]{ModePhase.nodedef},true);
		nullable.setEditable(new ModePhase[]{ModePhase.nodedef},true);
		nullable.setType(StringDataTypeConst.bOOLEAN);
		nullable.setKeys(new String[]{
				"是",
				"否"
			}
		);
		nullable.setValues(new String[]{"Y", "N"});
		nullable.setDsField("combo_ext8");
		nullable.setLabel("是否可以为空");
		nullable.setVoField("nullable");
		list.add(nullable);
		
		ComboPropertyInfo showlabel = new ComboPropertyInfo();
		showlabel.setId("showlabel");
		showlabel.setVisible(new ModePhase[]{ModePhase.nodedef},true);
		showlabel.setEditable(new ModePhase[]{ModePhase.nodedef},true);
		showlabel.setType(StringDataTypeConst.bOOLEAN);
		showlabel.setKeys(new String[]{
				"是",
				"否"
			}
		);
		showlabel.setValues(new String[]{"Y", "N"});
		showlabel.setDsField("combo_ext9");
		showlabel.setLabel("是否可见");
		showlabel.setVoField("showlabel");
		list.add(showlabel);
		
		IntegerPropertyInfo textWidth = new IntegerPropertyInfo();
		textWidth.setId("textWidth");
		textWidth.setVisible(true);
		textWidth.setEditable(true);
		textWidth.setType(StringDataTypeConst.INT);
		textWidth.setDsField("integer_ext1");
		textWidth.setLabel("标签宽度");
		textWidth.setVoField("textwidth");
		list.add(textWidth);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext4");
		height.setVoField("height");
		height.setLabel("高");
		list.add(height);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext5");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		contextmenu.setDsField("string_ext9");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setEditable(true);
		classname.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		classname.setDsField("string_ext10");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(false);
		langdir.setDsField("string_ext11");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(false);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("多语显示值");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext13");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo value = new StringPropertyInfo();
		value.setId("value");
		value.setEditable(true);
		value.setVisible(true);
		value.setDsField("string_ext14");
		value.setLabel("输入值");
		value.setVoField("value");
		list.add(value);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(false);
		editorType.setVisible(true);
		editorType.setDsField("string_ext15");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		StringPropertyInfo maxValue = new StringPropertyInfo();
		maxValue.setId("maxValue");
		maxValue.setEditable(true);
		maxValue.setVisible(true);
		maxValue.setDsField("string_ext16");
		maxValue.setLabel("最大值");
		maxValue.setVoField("maxvalue");
		list.add(maxValue);
		
		StringPropertyInfo minValue = new StringPropertyInfo();
		minValue.setId("minValue");
		minValue.setEditable(true);
		minValue.setVisible(true);
		minValue.setDsField("string_ext17");
		minValue.setLabel("最小值");
		minValue.setVoField("minvalue");
		list.add(minValue);

		StringPropertyInfo tip = new StringPropertyInfo();
		tip.setId("tip");
		tip.setEditable(true);
		tip.setVisible(true);
		tip.setDsField("string_ext19");
		tip.setLabel("提示");
		tip.setVoField("tip");
		list.add(tip);
		
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
		
		SelfDefRefPropertyInfo editFormular = new SelfDefRefPropertyInfo();
		editFormular.setId("editFormular");
		editFormular.setVisible(true);
		editFormular.setEditable(true);
		editFormular.setHeight("650");
		editFormular.setWidth("1000");
		editFormular.setType(StringDataTypeConst.STRING);
		editFormular.setDsField("ref_ext3");
		editFormular.setLabel("编辑公式");
		editFormular.setUrl(FORMULA_REF_URL);
		editFormular.setVoField("editFormular");
		list.add(editFormular);		
		
		StringPropertyInfo mask = new StringPropertyInfo();
		mask.setId("mask");
		mask.setEditable(true);
		mask.setVisible(true);
		mask.setDsField("string_ext22");
		mask.setLabel("显示样式");
		mask.setVoField("mask");
		list.add(mask);
	}
}
