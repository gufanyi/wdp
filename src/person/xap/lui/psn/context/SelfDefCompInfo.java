package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * 自定义控件属性信息类
 * 
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class SelfDefCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	public SelfDefCompInfo(){
		super();
	
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setKeys(new String[]{"是","否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		list.add(vis);
		
		
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
		
		ComboPropertyInfo position = new ComboPropertyInfo();
		position.setId("position");
		position.setKeys(new String[]{"相对的", "绝对的"});
		position.setValues(new String[]{"relative", "absolute"});		
		position.setVisible(true);
		position.setEditable(true);
		position.setType(StringDataTypeConst.STRING);
		position.setDsField("combo_ext2");
		position.setLabel("定位方式");
		position.setVoField("positions");
		list.add(position);
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(false);
		top.setEditable(true);
		top.setDsField("string_ext7");
		top.setLabel("顶层距");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(false);
		left.setEditable(true);
		left.setDsField("string_ext8");
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
