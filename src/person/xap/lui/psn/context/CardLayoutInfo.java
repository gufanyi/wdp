/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;


/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class CardLayoutInfo extends LayoutInfo {
	
	private static final long serialVersionUID = 1L;

	public CardLayoutInfo(){
		super();
//		ComboPropertyInfo currentItem = new ComboPropertyInfo();
//		currentItem.setId("currentItem");
//		currentItem.setVisible(true);
//		currentItem.setEditable(true);
//		currentItem.setType(StringDataTypeConst.STRING);
//		currentItem.setDsField("combo_ext1");
//		currentItem.setLabel("当前项1");
//		currentItem.setKeys(new String[]{"0","1","2"});
//		currentItem.setValues(new String[]{"0","1","2"});
//		currentItem.setVoField("currentItem");
//		list.add(currentItem);
//		
		
		
		StringPropertyInfo currentItem = new StringPropertyInfo();
		currentItem.setId("currentItem");
		currentItem.setVisible(true);
		currentItem.setEditable(true);
		currentItem.setDsField("string_ext16");
		currentItem.setVoField("1");
		currentItem.setLabel("当前项");
		list.add(currentItem);
//		StringPropertyInfo curr = new StringPropertyInfo();
//		curr.setId("currentItem");
//		curr.setVisible(true);
//		curr.setEditable(true);
//		curr.setDsField("string_ext4");
//		curr.setLabel("当前项");
//		curr.setVoField("currentitem");
//		list.add(curr);
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
