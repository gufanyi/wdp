package xap.lui.core.refrence;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.context.ComboPropertyInfo;
import xap.lui.psn.context.ControlInfo;
import xap.lui.psn.context.IBaseInfo;
import xap.lui.psn.context.StringPropertyInfo;

public class GenericRefNodeInfo extends ControlInfo implements IBaseInfo  {
private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	public GenericRefNodeInfo(){
		super();
	
		ComboPropertyInfo isQuickInput = new ComboPropertyInfo();
		isQuickInput.setId("quickInput");
		isQuickInput.setVisible(true);
		isQuickInput.setEditable(true);
		isQuickInput.setType(StringDataTypeConst.bOOLEAN);
		isQuickInput.setKeys(KEYS);
		isQuickInput.setValues(VALUES);
		isQuickInput.setDsField("combo_ext1");
		isQuickInput.setLabel("允许快速输入");
		isQuickInput.setVoField("quickInput");
		list.add(isQuickInput);
		
		StringPropertyInfo controller = new StringPropertyInfo();
		controller.setId("controller");
		controller.setVisible(true);
		controller.setEditable(true);
		controller.setDsField("string_ext4");
		controller.setLabel("节点控制类");
		controller.setVoField("controller");
		list.add(controller);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext5");
		height.setLabel("对话框高度");
		height.setVoField("dialogHeight");
		list.add(height);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext6");
		width.setLabel("对话框宽度");
		width.setVoField("dialogWidth");
		list.add(width);
		
		ComboPropertyInfo isMultiple = new ComboPropertyInfo();
		isMultiple.setId("multiple");
		isMultiple.setVisible(true);
		isMultiple.setEditable(true);
		isMultiple.setType(StringDataTypeConst.bOOLEAN);
		isMultiple.setKeys(KEYS);
		isMultiple.setValues(VALUES);
		isMultiple.setDsField("combo_ext2");
		isMultiple.setLabel("允许多选");
		isMultiple.setVoField("multiple");
		list.add(isMultiple);
		
		StringPropertyInfo winModel = new StringPropertyInfo();
		winModel.setId("winModel");
		winModel.setVisible(true);
		winModel.setEditable(true);
		winModel.setDsField("string_ext7");
		winModel.setLabel("窗体模型");
		winModel.setVoField("winModel");
		list.add(winModel);
		
		StringPropertyInfo readDataset = new StringPropertyInfo();
		readDataset.setId("readDataset");
		readDataset.setVisible(true);
		readDataset.setEditable(true);
		readDataset.setDsField("string_ext8");
		readDataset.setLabel("读取的数据集");
		readDataset.setVoField("readDataset");
		list.add(readDataset);
		
		StringPropertyInfo readFields = new StringPropertyInfo();
		readFields.setId("readFields");
		readFields.setVisible(true);
		readFields.setEditable(true);
		readFields.setDsField("string_ext9");
		readFields.setLabel("读数据集字段");
		readFields.setVoField("readFields");
		list.add(readFields);
		
		StringPropertyInfo refCode = new StringPropertyInfo();
		refCode.setId("refcode");
		refCode.setVisible(true);
		refCode.setEditable(true);
		refCode.setDsField("string_ext10");
		refCode.setLabel("参照代码");
		refCode.setVoField("refCode");
		list.add(refCode);
		
		ComboPropertyInfo isOnlyLeaf = new ComboPropertyInfo();
		isOnlyLeaf.setId("onlyLeaf");
		isOnlyLeaf.setVisible(true);
		isOnlyLeaf.setEditable(true);
		isOnlyLeaf.setType(StringDataTypeConst.bOOLEAN);
		isOnlyLeaf.setKeys(KEYS);
		isOnlyLeaf.setValues(VALUES);
		isOnlyLeaf.setDsField("combo_ext3");
		isOnlyLeaf.setLabel("仅叶节点可选中");
		isOnlyLeaf.setVoField("onlyLeaf");
		list.add(isOnlyLeaf);
		
		StringPropertyInfo title = new StringPropertyInfo();
		title.setId("title");
		title.setVisible(true);
		title.setEditable(true);
		title.setDsField("string_ext11");
		title.setLabel("标题");
		title.setVoField("title");
		list.add(title);
		
		StringPropertyInfo writeDataset = new StringPropertyInfo();
		writeDataset.setId("writeDataset");
		writeDataset.setVisible(true);
		writeDataset.setEditable(true);
		writeDataset.setDsField("string_ext12");
		writeDataset.setLabel("回写的数据集");
		writeDataset.setVoField("writeDataset");
		list.add(writeDataset);
		
		StringPropertyInfo writeFields = new StringPropertyInfo();
		writeFields.setId("writeFields");
		writeFields.setVisible(true);
		writeFields.setEditable(true);
		writeFields.setDsField("string_ext12");
		writeFields.setLabel("回写数据集字段");
		writeFields.setVoField("writeFields");
		list.add(writeFields);
	}
}