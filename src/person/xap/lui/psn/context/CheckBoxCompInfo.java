package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-10-10
 * @since 1.6
 */
public class CheckBoxCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	public CheckBoxCompInfo(){
		super();
		ComboPropertyInfo visible = new ComboPropertyInfo();
		visible.setId("visible");
		visible.setKeys(KEYS);
		visible.setValues(VALUES);
		visible.setType(StringDataTypeConst.bOOLEAN);
		visible.setDsField("combo_ext1");
		visible.setLabel("是否可见");
		visible.setVoField("visibles");
		visible.setVisible(true);
		visible.setEditable(true);
		list.add(visible);
		
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
		
		ComboPropertyInfo checked = new ComboPropertyInfo();
		checked.setId("checked");
		checked.setVisible(true);
		checked.setEditable(true);
		checked.setKeys(KEYS);
		checked.setValues(VALUES);
		checked.setType(StringDataTypeConst.bOOLEAN);
		checked.setDsField("combo_ext3");
		checked.setLabel("是否选中");
		checked.setVoField("checkeds");
		list.add(checked);
		
		ComboPropertyInfo focus = new ComboPropertyInfo();
		focus.setId("focus");
		focus.setVisible(true);
		focus.setEditable(true);
		focus.setKeys(KEYS);
		focus.setValues(VALUES);
		focus.setType(StringDataTypeConst.bOOLEAN);
		focus.setDsField("combo_ext4");
		focus.setLabel("是否聚焦");
		focus.setVoField("focuss");
		list.add(focus);
		
		ComboPropertyInfo textalign = new ComboPropertyInfo();
		textalign.setId("textAlign");
		textalign.setType(StringDataTypeConst.STRING);
		textalign.setVisible(true);
		textalign.setEditable(true);
		textalign.setKeys(new String[]{"左", "中", "右"});
		textalign.setValues(new String[]{"left", "center", "right"});
		textalign.setDsField("combo_ext6");
		textalign.setLabel("标签位置");
		textalign.setVoField("textalign");
		list.add(textalign);
		
		IntegerPropertyInfo textWidth = new IntegerPropertyInfo();
		textWidth.setId("textWidth");
		textWidth.setVisible(true);
		textWidth.setEditable(true);
		textWidth.setType(StringDataTypeConst.INT);
		textWidth.setDsField("integer_ext1");
		textWidth.setLabel("标签宽度");
		textWidth.setVoField("textwidth");
		list.add(textWidth);
		
		StringPropertyInfo dataType = new StringPropertyInfo();
		dataType.setId("dataType");
		dataType.setEditable(true);
		dataType.setVisible(true);
		dataType.setDsField("string_ext4");
		dataType.setVoField("datatype");
		dataType.setLabel("标签类型");
		list.add(dataType);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext5");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setEditable(true);
		left.setVisible(false);
		left.setDsField("string_ext7");
		left.setLabel("左边距");
		left.setVoField("ileft");
		list.add(left);
		
		ComboPropertyInfo position = new ComboPropertyInfo();
		position.setId("position");
		position.setKeys(new String[]{"相对的", "绝对的"});
		position.setValues(new String[]{"relative", "absolute"});
		position.setVisible(true);
		position.setEditable(true);
		position.setType(StringDataTypeConst.STRING);
		position.setDsField("combo_ext7");
		position.setLabel("定位方式");
		position.setVoField("positions");
		list.add(position);			
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(true);
		contextmenu.setDsField("string_ext9");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setEditable(true);
		classname.setVisible(true);
		classname.setDsField("string_ext10");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(true);
		langdir.setDsField("string_ext11");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("多语显示值");
		i18nname.setEditable(true);
		i18nname.setVisible(true);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("i18nName");
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
		editorType.setEditable(true);
		editorType.setVisible(true);
		editorType.setDsField("string_ext15");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		StringPropertyInfo parentId = new StringPropertyInfo();
		parentId.setId("");
		parentId.setEditable(true);
		parentId.setVisible(false);
		parentId.setDsField("parentid");
		parentId.setLabel("父ID");
		parentId.setVoField("parentid");
		list.add(parentId);

		StringPropertyInfo imgSrc = new StringPropertyInfo();
		imgSrc.setId("imgsrc");
		imgSrc.setEditable(true);
		imgSrc.setVisible(true);
		imgSrc.setDsField("string_ext16");
		imgSrc.setLabel("显示图片路径");
		imgSrc.setVoField("imgsrc");
		list.add(imgSrc);
		
	}
}
