/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class BorderPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	 
	public BorderPanelInfo(){
		
		StringPropertyInfo top = new StringPropertyInfo();
		top.setId("top");
		top.setVisible(true);
		top.setEditable(true);
		top.setDsField("string_ext3");
		top.setLabel("top");
		top.setVoField("itop");
		list.add(top);
		
		StringPropertyInfo center = new StringPropertyInfo();
		center.setId("center");
		center.setVisible(true);
		center.setEditable(true);
		center.setDsField("string_ext4");
		center.setLabel("center");
		center.setVoField("center");
		list.add(center);
		
		StringPropertyInfo bottom = new StringPropertyInfo();
		bottom.setId("bottom");
		bottom.setVisible(true);
		bottom.setEditable(true);
		bottom.setDsField("string_ext5");
		bottom.setLabel("bottom");
		bottom.setVoField("bottom");
		list.add(bottom);
		
		StringPropertyInfo left = new StringPropertyInfo();
		left.setId("left");
		left.setVisible(true);
		left.setEditable(true);
		left.setDsField("string_ext6");
		left.setLabel("left");
		left.setVoField("ileft");
		list.add(left);
		
		StringPropertyInfo right = new StringPropertyInfo();
		right.setId("right");
		right.setVisible(true);
		right.setEditable(true);
		right.setDsField("string_ext7");
		right.setLabel("right");
		right.setVoField("iright");
		list.add(right);
		
		ComboPropertyInfo position = new ComboPropertyInfo();
		position.setId("position");
		position.setKeys(new String[]{"相对的", "绝对的"});
		position.setValues(new String[]{"relative", "absolute"});
		position.setVisible(true);
		position.setEditable(true);
		position.setType(StringDataTypeConst.STRING);
		position.setDsField("combo_ext1");
		position.setLabel("定位方式");
		position.setVoField("positions");
		list.add(position);			
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext9");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext10");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
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
