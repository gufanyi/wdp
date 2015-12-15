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
public class TabCompInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final String[] KEYS = new String[]{"顶部", "底部"};
	private static final String[] VALUES = new String[]{"top", "bottom"};

	

	public TabCompInfo(){
		super();
//		StringPropertyInfo curr = new StringPropertyInfo();
//		curr.setId("currentItem");
//		curr.setVisible(true);
//		curr.setEditable(true);
//		curr.setDsField("string_ext3");
//		curr.setLabel("当前页签");
//		curr.setVoField("currentitem");
//		list.add(curr);
		ComboPropertyInfo currentItem = new ComboPropertyInfo();
		currentItem.setId("currentItem");
		currentItem.setVisible(true);
		currentItem.setEditable(true);
		currentItem.setType(StringDataTypeConst.INT);
		currentItem.setDsField("combo_ext1");
		currentItem.setLabel("当前页签");
		currentItem.setKeys(new String[]{"0","1","2","3","4","5"});
		currentItem.setValues(new String[]{"0","1","2","3","4","5"});
		currentItem.setVoField("currentItem");
		list.add(currentItem);

		ComboPropertyInfo tabType = new ComboPropertyInfo();
		tabType.setId("tabType");
		tabType.setVisible(true);
		tabType.setEditable(true);
		tabType.setType(StringDataTypeConst.STRING);
		tabType.setKeys(KEYS);
		tabType.setValues(VALUES);
		tabType.setDsField("combo_ext2");
		tabType.setLabel("标签位置");
		tabType.setDefaultValue("顶部");
		tabType.setVoField("tabtype");
		list.add(tabType);
		
		
		ComboPropertyInfo oneTabHide = new ComboPropertyInfo();
		oneTabHide.setId("oneTabHide");
		oneTabHide.setVisible(true);
		oneTabHide.setEditable(true);
		oneTabHide.setType(StringDataTypeConst.INTEGER);
		oneTabHide.setKeys(new String[]{"是","否"});
		oneTabHide.setValues(new String[]{"1","0"});
		oneTabHide.setDsField("combo_ext3");
		oneTabHide.setLabel("单页签隐藏");
		oneTabHide.setVoField("onetabhide");
		list.add(oneTabHide);
		
		IntegerPropertyInfo itemWidth = new IntegerPropertyInfo();
		itemWidth.setId("itemWidth");
		itemWidth.setVisible(true);
		itemWidth.setEditable(true);
		itemWidth.setType(StringDataTypeConst.INTEGER);
		itemWidth.setDsField("integer_ext1");
		itemWidth.setVoField("itemWidth");
		itemWidth.setLabel("单叶签宽");
		list.add(itemWidth);
		
		IntegerPropertyInfo itemHeight = new IntegerPropertyInfo();
		itemHeight.setId("itemHeight");
		itemHeight.setVisible(true);
		itemHeight.setEditable(true);
		itemHeight.setType(StringDataTypeConst.INTEGER);
		itemHeight.setDsField("integer_ext2");
		itemHeight.setVoField("itemHeight");
		itemHeight.setLabel("单叶签高");
		list.add(itemHeight);
		
		IntegerPropertyInfo width = new IntegerPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setType(StringDataTypeConst.INTEGER);
		width.setDsField("integer_ext3");
		width.setVoField("width");
		width.setLabel("叶签宽");
		list.add(width);
		
		IntegerPropertyInfo height = new IntegerPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setType(StringDataTypeConst.INTEGER);
		height.setDsField("integer_ext4");
		height.setVoField("height");
		height.setLabel("叶签高");
		list.add(height);
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
