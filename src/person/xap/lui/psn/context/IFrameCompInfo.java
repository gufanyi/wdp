package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class IFrameCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	
	public IFrameCompInfo(){
		super();
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setKeys(new String[]{"是","否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setDefaultValue("Y");
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		vis.setVisible(true);
		vis.setEditable(true);
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是","否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setDefaultValue("Y");
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可用");
		ena.setVoField("enableds");
		ena.setVisible(true);
		ena.setEditable(true);
		list.add(ena);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext4");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext5");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo contextmenu = new StringPropertyInfo();
		contextmenu.setId("contextMenu");
		contextmenu.setVisible(true);
		contextmenu.setEditable(true);
		contextmenu.setDsField("string_ext6");
		contextmenu.setLabel("弹出菜单");
		contextmenu.setVoField("contextmenu");
		list.add(contextmenu);
		
		ComboPropertyInfo position = new ComboPropertyInfo();
		position.setId("position");
		position.setKeys(new String[]{"相对的","绝对的"});
		position.setValues(new String[]{"relative", "absolute"});
		position.setVisible(true);
		position.setEditable(true);
		position.setType(StringDataTypeConst.STRING);
		position.setDsField("combo_ext3");
		position.setLabel("定位方式");
		position.setVoField("positions");
		list.add(position);			
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(false);
		top.setEditable(true);
		top.setDsField("string_ext8");
		top.setLabel("顶层距");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(false);
		left.setEditable(true);
		left.setDsField("string_ext9");
		left.setLabel("左边距");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext10");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		StringPropertyInfo src = new StringPropertyInfo();
		src.setId("src");
		src.setVisible(true);
		src.setEditable(true);
		src.setDsField("string_ext11");
		src.setLabel("链接地址");
		src.setVoField("src");
		list.add(src);
		
		StringPropertyInfo name = new StringPropertyInfo();
		name.setId("name");
		name.setVisible(true);
		name.setEditable(true);
		name.setDsField("string_ext12");
		name.setLabel("名称");
		name.setVoField("name");
		list.add(name);
		
		StringPropertyInfo border = new StringPropertyInfo();
		border.setId("border");
		border.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		border.setEditable(true);
		border.setDsField("string_ext13");
		border.setLabel("border");
		border.setVoField("border");
		list.add(border);
		
		StringPropertyInfo frameborder = new StringPropertyInfo();
		frameborder.setId("frameBorder");
		frameborder.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		frameborder.setEditable(true);
		frameborder.setDsField("string_ext14");
		frameborder.setLabel("frameBorder");
		frameborder.setVoField("frameborder");
		list.add(frameborder);
		
		StringPropertyInfo scrolling = new StringPropertyInfo();
		scrolling.setId("scrolling");
		scrolling.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		scrolling.setEditable(true);
		scrolling.setDsField("string_ext15");
		scrolling.setLabel("scrolling");
		scrolling.setVoField("scrolling");
		list.add(scrolling);
			
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
	}


}
