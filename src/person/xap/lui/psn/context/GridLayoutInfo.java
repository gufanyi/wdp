package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class GridLayoutInfo extends LayoutInfo {
	
	private static final long serialVersionUID = 1L;

	public GridLayoutInfo(){
		super();

		StringPropertyInfo border = new StringPropertyInfo();
		border.setId("border");
		border.setVisible(true);
		border.setEditable(true);
		border.setDsField("string_ext6");
		border.setLabel("边框");
		border.setVoField("border");
		list.add(border);
		 
		ComboPropertyInfo borderStyle = new ComboPropertyInfo();
		borderStyle.setId("borderStyle");
		borderStyle.setVisible(true);
		borderStyle.setEditable(true);
		borderStyle.setDsField("combo_ext3");
		borderStyle.setLabel("边框样式");
		borderStyle.setVoField("borderStyle");
		borderStyle.setKeys(getBorderStyleKeys());
		borderStyle.setValues(getBorderStyleValues());
		borderStyle.setType(StringDataTypeConst.STRING);
		list.add(borderStyle);
		
		ComboPropertyInfo borderColor = new ComboPropertyInfo();
		borderColor.setType(StringDataTypeConst.STRING);
		borderColor.setId("borderColor");
		borderColor.setVisible(true);
		borderColor.setEditable(true);
		borderColor.setDsField("combo_ext4");
		borderColor.setLabel("边框颜色");
		borderColor.setVoField("borderColor");
		borderColor.setKeys(getColorKeys());
		borderColor.setValues(getColorValues());
		list.add(borderColor);
		
		StringPropertyInfo cellBorder = new StringPropertyInfo();
		cellBorder.setId("cellBorder");
		cellBorder.setVisible(true);
		cellBorder.setEditable(true);
		cellBorder.setDsField("string_ext7");
		cellBorder.setLabel("单元格边框");
		cellBorder.setVoField("cellBorder");
		list.add(cellBorder);
		
		ComboPropertyInfo cellBorderStyle = new ComboPropertyInfo();
		cellBorderStyle.setId("cellBorderStyle");
		cellBorderStyle.setVisible(true);
		cellBorderStyle.setEditable(true);
		cellBorderStyle.setDsField("combo_ext5");
		cellBorderStyle.setLabel("单元格边框样式");
		cellBorderStyle.setVoField("cellBorderStyle");
		cellBorderStyle.setKeys(getBorderStyleKeys());
		cellBorderStyle.setValues(getBorderStyleValues());
		cellBorderStyle.setType(StringDataTypeConst.STRING);
		list.add(cellBorderStyle);
		
		ComboPropertyInfo cellBorderColor = new ComboPropertyInfo();
		cellBorderColor.setType(StringDataTypeConst.STRING);
		cellBorderColor.setId("cellBorderColor");
		cellBorderColor.setVisible(true);
		cellBorderColor.setEditable(true);
		cellBorderColor.setDsField("combo_ext6");
		cellBorderColor.setLabel("单元格边框颜色");
		cellBorderColor.setVoField("cellBorderColor");
		cellBorderColor.setKeys(getColorKeys());
		cellBorderColor.setValues(getColorValues());
		list.add(cellBorderColor);
	}
}
