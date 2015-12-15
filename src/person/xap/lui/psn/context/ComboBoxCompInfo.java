package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.refence.MockTreeGridComboDataRefPageModel;

/**
 * @author wupeng1
 * @version 6.0 2011-10-10
 * @since 1.6
 */
public class ComboBoxCompInfo extends TextCompInfo {
	
	private static final long serialVersionUID = 1L;
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	public ComboBoxCompInfo(){
		super();
		
		//移除从父类继承来的某一项
		removePropertyInfoById("minValue");
		
		ComboPropertyInfo showMark = new ComboPropertyInfo();
		showMark.setId("showMark");
		showMark.setVisible(false);
		showMark.setEditable(true);
		showMark.setType(StringDataTypeConst.bOOLEAN);
		showMark.setKeys(KEYS);
		showMark.setValues(VALUES);
		showMark.setDsField("combo_ext10");
		showMark.setLabel("showMark");
		showMark.setVoField("showmarks");
		list.add(showMark);
		
		ComboPropertyInfo imageOnly = new ComboPropertyInfo();
		imageOnly.setId("imageOnly");
		imageOnly.setVisible(true);
		imageOnly.setEditable(true);
		imageOnly.setType(StringDataTypeConst.bOOLEAN);
		imageOnly.setKeys(KEYS);
		imageOnly.setValues(VALUES);
		imageOnly.setDsField("combo_ext11");
		imageOnly.setLabel("只显示图片");
		imageOnly.setVoField("imageonlys");
		list.add(imageOnly);
		
		ComboPropertyInfo selectOnly = new ComboPropertyInfo();
		selectOnly.setId("selectOnly");
		selectOnly.setVisible(true);
		selectOnly.setEditable(true);
		selectOnly.setType(StringDataTypeConst.bOOLEAN);
		selectOnly.setKeys(KEYS);
		selectOnly.setValues(VALUES);
		selectOnly.setDsField("combo_ext12");
		selectOnly.setLabel("只允许选择");
		selectOnly.setVoField("selectonlys");
		list.add(selectOnly);
		
		ComboPropertyInfo allowextendvalues = new ComboPropertyInfo();
		allowextendvalues.setId("allowExtendValue");
		allowextendvalues.setVisible(false);
		allowextendvalues.setEditable(true);
		allowextendvalues.setType(StringDataTypeConst.bOOLEAN);
		allowextendvalues.setKeys(KEYS);
		allowextendvalues.setValues(VALUES);
		allowextendvalues.setDsField("combo_ext13");
		allowextendvalues.setLabel("allowExtendValue");
		allowextendvalues.setVoField("allowextendvalues");
		list.add(allowextendvalues);
		
		StringPropertyInfo dataDivHeight = new StringPropertyInfo();
		dataDivHeight.setId("dataDivHeight");
		dataDivHeight.setEditable(true);
		dataDivHeight.setVisible(true);
		dataDivHeight.setDsField("string_ext16");
		dataDivHeight.setLabel("数据高度");
		dataDivHeight.setVoField("datadivheight");
		list.add(dataDivHeight);
		
		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("refComboData");
		refInfo.setVisible(true);
		refInfo.setEditable(true);
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setHeight("540");
		refInfo.setWidth("700");
		refInfo.setLabel("引用枚举");
		refInfo.setVoField("refComboData");
		refInfo.setUrl("app/mockapp/cdref?model="+MockTreeGridComboDataRefPageModel.class.getName()+"&ctrl="+MockTreeGridComboDataRefPageModel.class.getName());
		list.add(refInfo);
	}
}
