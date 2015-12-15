package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * 连接控件属性信息类
 * 
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class LinkCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	public LinkCompInfo(){
		super();
		
		ComboPropertyInfo hasimg = new ComboPropertyInfo();
		hasimg.setId("hasImg");
		hasimg.setKeys(new String[]{"是","否"});
		hasimg.setValues(new String[]{"Y", "N"});
		hasimg.setType(StringDataTypeConst.bOOLEAN);
		hasimg.setDsField("combo_ext1");
		hasimg.setLabel("hasImg");
		hasimg.setVoField("hasimgs");
		hasimg.setVisible(false);
		hasimg.setEditable(true);
		list.add(hasimg);
		
		ComboPropertyInfo imagechanged = new ComboPropertyInfo();
		imagechanged.setId("imageChanged");
		imagechanged.setKeys(new String[]{"是","否"});
		imagechanged.setValues(new String[]{"Y", "N"});
		imagechanged.setType(StringDataTypeConst.bOOLEAN);
		imagechanged.setDsField("combo_ext2");
		imagechanged.setLabel("imageChanged");
		imagechanged.setVoField("imagechangeds");
		imagechanged.setVisible(false);
		imagechanged.setEditable(true);
		list.add(imagechanged);
		
		ComboPropertyInfo visi = new ComboPropertyInfo();
		visi.setId("visible");
		visi.setKeys(new String[]{"是","否"});
		visi.setValues(new String[]{"Y", "N"});
		visi.setType(StringDataTypeConst.bOOLEAN);
		visi.setDsField("combo_ext3");
		visi.setLabel("是否可见");
		visi.setVoField("visibles");
		visi.setVisible(true);
		visi.setEditable(true);
		list.add(visi);
		
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
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setEditable(true);
		top.setVisible(false);
		top.setDsField("string_ext6");
		top.setLabel("顶层距");
		top.setVoField("itop");
		list.add(top);
		
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
		position.setDsField("combo_ext4");
		position.setLabel("定位方式");
		position.setVoField("positions");
		list.add(position);		
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(false);
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
		
		StringPropertyInfo href = new StringPropertyInfo();
		href.setId("href");
		href.setEditable(true);
		href.setVisible(true);
		href.setDsField("string_ext11");
		href.setLabel("链接地址");
		href.setVoField("href");
		list.add(href);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(false);
		i18nname.setDsField("string_ext12");
		i18nname.setLabel("多语显示值");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo image = new StringPropertyInfo();
		image.setId("image");
		image.setEditable(true);
		image.setVisible(true);
		image.setDsField("string_ext13");
		image.setLabel("图形");
		image.setVoField("image");
		list.add(image);
		
		StringPropertyInfo realimage = new StringPropertyInfo();
		realimage.setId("realImage");
		realimage.setEditable(true);
		realimage.setVisible(false);
		realimage.setDsField("string_ext14");
		realimage.setLabel("realImage");
		realimage.setVoField("realimage");
		list.add(realimage);
		
		StringPropertyInfo target = new StringPropertyInfo();
		target.setId("target");
		target.setEditable(true);
		target.setVisible(true);
		target.setDsField("string_ext15");
		target.setLabel("目标地址");
		target.setVoField("target");
		list.add(target);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext16");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
	}
}
