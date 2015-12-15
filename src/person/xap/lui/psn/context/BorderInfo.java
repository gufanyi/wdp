package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class BorderInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	public BorderInfo(){
		super();
		StringPropertyInfo color = new StringPropertyInfo();
		color.setId("color");
		color.setVisible(true);
		color.setEditable(true);
		color.setDsField("string_ext3");
		color.setLabel("边框颜色");
		color.setVoField("color");
		list.add(color);
		
		StringPropertyInfo lcolor = new StringPropertyInfo();
		lcolor.setId("leftColor");
		lcolor.setVisible(false);
		lcolor.setEditable(true);
		lcolor.setDsField("string_ext4");
		lcolor.setLabel("左边颜色");
		lcolor.setVoField("leftcolor");
		list.add(lcolor);
		
		StringPropertyInfo rcolor = new StringPropertyInfo();
		rcolor.setId("rightColor");
		rcolor.setVisible(false);
		rcolor.setEditable(true);
		rcolor.setDsField("string_ext5");
		rcolor.setLabel("右边颜色");
		rcolor.setVoField("rightcolor");
		list.add(rcolor);
		
		StringPropertyInfo tcolor = new StringPropertyInfo();
		tcolor.setId("topColor");
		tcolor.setVisible(false);
		tcolor.setEditable(true);
		tcolor.setDsField("string_ext6");
		tcolor.setLabel("上边颜色");
		tcolor.setVoField("topcolor");
		list.add(tcolor);
		
		StringPropertyInfo bcolor = new StringPropertyInfo();
		bcolor.setId("bottomColor");
		bcolor.setVisible(false);
		bcolor.setEditable(true);
		bcolor.setDsField("string_ext7");
		bcolor.setLabel("底边颜色");
		bcolor.setVoField("bottomcolor");
		list.add(bcolor);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext8");
		width.setLabel("宽");
		width.setVoField("width");
		width.setValue("1");
		list.add(width);
		
		StringPropertyInfo lwidth = new StringPropertyInfo();
		lwidth.setId("leftWidth");
		lwidth.setVisible(false);
		lwidth.setEditable(true);
		lwidth.setDsField("string_ext9");
		lwidth.setLabel("左边框宽");
		lwidth.setValue("-1");
		lwidth.setVoField("leftwidth");
		list.add(lwidth);
		
		StringPropertyInfo rwidth = new StringPropertyInfo();
		rwidth.setId("rightWidth");
		rwidth.setVisible(false);
		rwidth.setEditable(true);
		rwidth.setDsField("string_ext10");
		rwidth.setLabel("右边框宽");
		rwidth.setValue("-1");
		rwidth.setVoField("rightwidth");
		list.add(rwidth);
		
		StringPropertyInfo twidth = new StringPropertyInfo();
		twidth.setId("topWidth");
		twidth.setVisible(false);
		twidth.setEditable(true);
		twidth.setDsField("string_ext11");
		twidth.setLabel("上边框宽");
		twidth.setValue("-1");
		twidth.setVoField("topwidth");
		list.add(twidth);
		
		StringPropertyInfo bwidth = new StringPropertyInfo();
		bwidth.setId("bottomWidth");
		bwidth.setVisible(false);
		bwidth.setEditable(true);
		bwidth.setDsField("string_ext12");
		bwidth.setLabel("下边框宽");
		bwidth.setValue("-1");
		bwidth.setVoField("bottomwidth");
		list.add(bwidth);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(true);
		classname.setEditable(true);
		classname.setDsField("string_ext13");
		classname.setLabel("样式类名");
		classname.setVoField("classname");
		list.add(classname);
		
//		StringPropertyInfo roundBorder = new StringPropertyInfo();
//		roundBorder.setId("roundBorder");
//		roundBorder.setVisible(true);
//		roundBorder.setEditable(true);
//		roundBorder.setDsField("string_ext14");
//		roundBorder.setLabel("roundBorder");
//		roundBorder.setVoField("roundborder");
//		list.add(roundBorder);
	

		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
		
	//		IntegerPropertyInfo pageSize = new IntegerPropertyInfo();
//		pageSize.setId("pageSize");
//		pageSize.setVisible(true);
//		pageSize.setEditable(true);
//		pageSize.setDsField("integer_ext2");
//		pageSize.setLabel("每页记录条数");
//		pageSize.setVoField("pageSize");
//		list.add(pageSize);	
		
		ComboPropertyInfo showLeft = new ComboPropertyInfo();
		showLeft.setId("showLeft");
		showLeft.setVisible(true);
		showLeft.setEditable(true);
		showLeft.setType(StringDataTypeConst.INT);
		showLeft.setKeys(new String[]{"是", "否"});
		showLeft.setValues(new String[]{"0", "1"});
		showLeft.setDsField("combo_ext1");
		showLeft.setLabel("显示左边框");
		showLeft.setVoField("showLeft");
		showLeft.setValue("1");
		list.add(showLeft);
		
		ComboPropertyInfo showRight = new ComboPropertyInfo();
		showRight.setId("showRight");
		showRight.setVisible(true);
		showRight.setEditable(true);
		showRight.setType(StringDataTypeConst.INT);
		showRight.setKeys(new String[]{"是", "否"});
		showRight.setValues(new String[]{"0", "1"});
		showRight.setDsField("combo_ext2");
		showRight.setLabel("显示右边框");
		showRight.setVoField("showRight");
		showRight.setValue("1");
		list.add(showRight);
		
		ComboPropertyInfo showTop = new ComboPropertyInfo();
		showTop.setId("showTop");
		showTop.setVisible(true);
		showTop.setEditable(true);
		showTop.setType(StringDataTypeConst.INT);
		showTop.setKeys(new String[]{"是", "否"});
		showTop.setValues(new String[]{"0", "1"});
		showTop.setDsField("combo_ext3");
		showTop.setLabel("显示上边框");
		showTop.setVoField("showTop");
		showTop.setValue("1");
		list.add(showTop);

		ComboPropertyInfo showBottom = new ComboPropertyInfo();
		showBottom.setId("showBottom");
		showBottom.setVisible(true);
		showBottom.setEditable(true);
		showBottom.setType(StringDataTypeConst.INT);
		showBottom.setKeys(new String[]{"是", "否"});
		showBottom.setValues(new String[]{"0", "1"});
		showBottom.setDsField("combo_ext4");
		showBottom.setLabel("显示下边框");
		showBottom.setVoField("showBottom");
		showBottom.setValue("1");
		list.add(showBottom);
	
	}
}
