package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;


/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class LabelCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	public LabelCompInfo(){
		super();
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setKeys(new String[]{"是","否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		vis.setVisible(true);
		vis.setEditable(true);
		list.add(vis);
		
		ComboPropertyInfo decoration = new ComboPropertyInfo();
		decoration.setId("decoration");
		decoration.setKeys(new String[]{"无","闪烁","下划线","贯穿线","上划线"});
		decoration.setValues(new String[]{"none","blink","underline","line-through","overline"});
		decoration.setType(StringDataTypeConst.STRING);
		decoration.setDsField("combo_ext9");
		decoration.setLabel("外观");
		decoration.setVoField("decoration");
		decoration.setVisible(true);
		decoration.setEditable(true);
		list.add(decoration);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是", "否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setVisible(false);
		ena.setEditable(true);
		ena.setDsField("combo_ext4");
		ena.setLabel("是否可编辑");
		ena.setVoField("enableds");
		list.add(ena);
		
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
		
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setEditable(true);
//		top.setVisible(false);
//		top.setDsField("string_ext6");
//		width.setLabel("顶层距");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setEditable(true);
//		left.setVisible(false);
//		left.setDsField("string_ext7");
//		left.setLabel("左边距");
//		left.setVoField("ileft");
//		list.add(left);
		
//		ComboPropertyInfo position = new ComboPropertyInfo();
//		position.setId("position");
//		position.setKeys(new String[]{NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "ButtonInfo-000008")/*相对的*/, NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "ButtonInfo-000009")/*绝对的*/});
//		position.setValues(new String[]{"relative", "absolute"});
//		position.setVisible(true);
//		position.setEditable(true);
//		position.setType(StringDataTypeConst.STRING);
//		position.setDsField("combo_ext6");
//		position.setLabel(NCLangRes4VoTransl.getNCLangRes().getStrByID("pa", "ControlInfo-000005")/*定位方式*/);
//		position.setVoField("positions");
//		list.add(position);	
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setEditable(true);
		contextmenu.setVisible(false);
		contextmenu.setDsField("string_ext9");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
//		StringPropertyInfo classname = new StringPropertyInfo();
//		classname.setId("className");
//		classname.setEditable(true);
//		classname.setVisible(true);
//		classname.setDsField("string_ext10");
//		classname.setLabel("自定义主题");
//		classname.setVoField("classname");
//		list.add(classname);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setEditable(true);
		i18nname.setVisible(false);
		i18nname.setDsField("string_ext11");
		i18nname.setLabel("多语显示值");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setEditable(true);
		langdir.setVisible(false);
		langdir.setDsField("string_ext12");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setEditable(true);
		text.setVisible(true);
		text.setDsField("string_ext13");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo innerhtml = new StringPropertyInfo();
		innerhtml.setId("innerHTML");
		innerhtml.setEditable(true);
		innerhtml.setVisible(true);
		innerhtml.setDsField("string_ext14");
		innerhtml.setLabel("HTML内容");
		innerhtml.setVoField("innerhtml");
		list.add(innerhtml);
		
		ComboPropertyInfo color = new ComboPropertyInfo();
		color.setKeys(getColorKeys());
		color.setValues(getColorValues());	
		color.setId("color");
		color.setEditable(true);
		color.setVisible(true);
		color.setDsField("combo_ext2");
		color.setType(StringDataTypeConst.STRING);
		color.setLabel("字体颜色");
		color.setVoField("color");
  		list.add(color);
		
//		ComboPropertyInfo style = new ComboPropertyInfo();
//		style.setId("style");
//		style.setEditable(true);
//		style.setVisible(true);
//		style.setDsField("combo_ext6");
//		//normal
//		style.setKeys(new String[]{"正常", "斜体"} );
//		style.setValues(new String[]{"normal", "italic"});
//		style.setType(StringDataTypeConst.STRING);
//		style.setLabel("字体样式");
//		style.setVoField("style");
//		list.add(style);
//		
//		StringPropertyInfo weight = new StringPropertyInfo();
//		weight.setId("weight");
//		weight.setEditable(true);
//		weight.setVisible(true);
//		weight.setDsField("string_ext17");
//		weight.setLabel("外观");
//		weight.setVoField("weight");
//		//list.add(weight);
//		
//		ComboPropertyInfo size = new ComboPropertyInfo();
//		size.setId("size");
//		size.setEditable(true);
//		size.setVisible(true);
//		size.setDsField("combo_ext3");
//		size.setLabel("字体大小");
//		size.setVoField("size");
//		List<String> fontSizeList = new ArrayList<String>();
//		for(int i = 8; i < 73; i++ ){
//			fontSizeList.add(i + "pt");
//		}
//		List<String> fontSizeValueList = new ArrayList<String>();
//		for(int j = 8; j < 73; j++ ){
//			fontSizeValueList.add(j + "");
//		}
//		size.setKeys(fontSizeList.toArray(new String[]{}));
//		size.setValues(fontSizeValueList.toArray(new String[]{}));
//		size.setType(StringDataTypeConst.STRING);
//		list.add(size);
//		String[] fontNames = new String[]{"宋体","新宋体","仿宋","微软雅黑","楷体","黑体","华文细黑","华文楷体","华文宋体","华文中宋","华文仿宋","华文彩云","华文琥珀","华文隶书","华文行楷","Arial Black","方正舒体"};
//		String[] fontValues =new String[]{"SimSun","NSimSun","FangSong","Microsoft YaHei","KaiTi","SimHei","STXihei","STKaiti","STSong","STZhongsong","STFangsong","STCaiyun","STHupo","STLiti","STXingkai","Arial Black","FZShuTi"};
//		
//		
//		ComboPropertyInfo family = new ComboPropertyInfo();
//		family.setId("family");
//		family.setEditable(true);
//		family.setVisible(true);
//		family.setDsField("combo_ext7");
//		family.setKeys(fontNames);
//		family.setValues(fontValues);
//		family.setType(StringDataTypeConst.STRING);
//		family.setLabel("字体");
//		family.setVoField("family");
//		list.add(family);
  		
  		
  		
		
		
//		ComboPropertyInfo textAlign = new ComboPropertyInfo();
//		textAlign.setId("textAlign");
//		textAlign.setKeys(new String[]{"左","中", "右"});
//		textAlign.setValues(new String[]{"left", "center", "right"});
//		textAlign.setType(StringDataTypeConst.STRING);
//		textAlign.setVisible(true);
//		textAlign.setEditable(true);
//		textAlign.setDsField("combo_ext5");
//		textAlign.setLabel("文字位置");
//		textAlign.setVoField("textalign");
//		list.add(textAlign);
//		
//		SelfDefRefPropertyInfo cssStyleRef = new SelfDefRefPropertyInfo();
//		cssStyleRef.setId("cssStyle");
//		cssStyleRef.setVisible(false);
//		cssStyleRef.setEditable(true);
//		cssStyleRef.setType(StringDataTypeConst.STRING);
//		cssStyleRef.setDsField("ref_ext2");
//		cssStyleRef.setLabel("自定义CSS样式");
//		cssStyleRef.setUrl("app/mockapp/cdref?model="+CssStylePageModel.class.getName()+"&ctrl="+CssStyleViewController.class.getName()+"/");
//		cssStyleRef.setVoField("cssstyle");
//		cssStyleRef.setWidth("600");
//		cssStyleRef.setHeight("450");
//		list.add(cssStyleRef);		
//		
//		StringPropertyInfo parentid = new StringPropertyInfo();
//		parentid.setId("");
//		parentid.setVisible(false);
//		parentid.setEditable(true);
//		parentid.setDsField("parentid");
//		parentid.setLabel("父ID");
//		parentid.setVoField("parentid");
//		list.add(parentid);
	}
	
 


}
