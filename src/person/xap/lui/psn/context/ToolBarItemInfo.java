package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

public class ToolBarItemInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	public ToolBarItemInfo(){
		super();
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(new String[]{"是", "否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visible");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(new String[]{"是","否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可用");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo withSep = new ComboPropertyInfo();
		withSep.setId("withSep");
		withSep.setVisible(true);
		withSep.setEditable(true);
		withSep.setType(StringDataTypeConst.bOOLEAN);
		withSep.setKeys(new String[]{"是", "否"});
		withSep.setValues(new String[]{"Y", "N"});
		withSep.setDsField("combo_ext3");
		withSep.setLabel("是否加分割线");
		withSep.setVoField("withSep");
		list.add(withSep);
		
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext4");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext5");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext6");
		text.setLabel("显示文本");
		text.setVoField("text");
		list.add(text);
		
		StringPropertyInfo refImg = new StringPropertyInfo();
		refImg.setId("refImg");
		refImg.setEditable(true);
		refImg.setVisible(true);
		refImg.setDsField("string_ext7");
		refImg.setLabel("图片路径");
		refImg.setVoField("refImg");
		list.add(refImg);
		
		StringPropertyInfo align = new StringPropertyInfo();
		align.setId("align");
		align.setEditable(true);
		align.setVisible(true);
		align.setDsField("string_ext8");
		align.setLabel("文字对齐方式");
		align.setVoField("align");
		list.add(align);
		
		StringPropertyInfo modifiers = new StringPropertyInfo();
		modifiers.setId("modifiers");
		modifiers.setEditable(true);
		modifiers.setVisible(true);
		modifiers.setDsField("string_ext9");
		modifiers.setLabel("修饰符");
		modifiers.setVoField("modifiers");
		list.add(modifiers);
	}
}
