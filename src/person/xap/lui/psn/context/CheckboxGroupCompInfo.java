package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.psn.refence.MockComboDataRefController;
import xap.lui.psn.refence.MockTreeRefPageModel;

/**
 * @author wupeng1
 * @version 6.0 2011-10-10
 * @since 1.6
 */
public class CheckboxGroupCompInfo extends TextCompInfo {
	
	private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	
	public CheckboxGroupCompInfo(){
		super();
		 
		//移除最大最小值
		removePropertyInfoById("maxValue");
		removePropertyInfoById("minValue");
		
		ComboPropertyInfo changeLine = new ComboPropertyInfo();
		changeLine.setId("changeLine");
		changeLine.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		changeLine.setEditable(true);
		changeLine.setType(StringDataTypeConst.bOOLEAN);
		changeLine.setKeys(KEYS);
		changeLine.setValues(VALUES);
		changeLine.setDsField("combo_ext8");
		changeLine.setLabel("changeLine");
		changeLine.setVoField("changelines");
		list.add(changeLine);
		
		IntegerPropertyInfo sepWidth = new IntegerPropertyInfo();
		sepWidth.setId("sepWidth");
		sepWidth.setVisible(true);
		sepWidth.setEditable(true);
		sepWidth.setType(StringDataTypeConst.INT);
		sepWidth.setDsField("integer_ext2");
		sepWidth.setLabel("间距");
		sepWidth.setVoField("sepwidth");
		list.add(sepWidth);
		
		IntegerPropertyInfo tabIndex = new IntegerPropertyInfo();
		tabIndex.setId("tabIndex");
		tabIndex.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		tabIndex.setEditable(true);
		tabIndex.setType(StringDataTypeConst.INT);
		tabIndex.setDsField("integer_ext3");
		tabIndex.setLabel("tabIndex");
		tabIndex.setVoField("tabindex");
		list.add(tabIndex);
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(true);
		editorType.setVisible(false);
		editorType.setDsField("string_ext15");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		list.add(editorType);
		 
		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("comboDataId");
		refInfo.setVisible(true);
		refInfo.setEditable(true);
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setLabel("引用枚举");
		refInfo.setUrl("app/mockapp/cdref?model="+MockTreeRefPageModel.class.getName()+"&ctrl="+MockComboDataRefController.class.getName());
		refInfo.setVoField("comboDataId");
		list.add(refInfo);
	}
	
}
