package xap.lui.core.dataset;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.context.ComboPropertyInfo;
import xap.lui.psn.context.ControlInfo;
import xap.lui.psn.context.IBaseInfo;
import xap.lui.psn.context.IntegerPropertyInfo;
import xap.lui.psn.context.StringPropertyInfo;

public class DatasetInfo extends ControlInfo implements IBaseInfo  {
private static final long serialVersionUID = 1L;
	
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	
	public DatasetInfo(){
		super();
		
		StringPropertyInfo caption = new StringPropertyInfo();
		caption.setType(StringDataTypeConst.STRING);
		caption.setId("caption");
		caption.setVisible(true);
		caption.setEditable(true);
		caption.setDsField("string_ext5");
		caption.setLabel("标题");
		caption.setVoField("caption");
		list.add(caption);
		
		ComboPropertyInfo isEdit = new ComboPropertyInfo();
		isEdit.setId("edit");
		isEdit.setVisible(true);
		isEdit.setEditable(true);
		isEdit.setType(StringDataTypeConst.bOOLEAN);
		isEdit.setKeys(KEYS);
		isEdit.setValues(VALUES);
		isEdit.setDsField("combo_ext1");
		isEdit.setLabel("是否可编辑");
		isEdit.setVoField("edit");
		list.add(isEdit);
		
		IntegerPropertyInfo pageSize = new IntegerPropertyInfo();
		pageSize.setId("pageSize");
		pageSize.setType(StringDataTypeConst.INTEGER);
		pageSize.setVisible(true);
		pageSize.setEditable(true);
		pageSize.setDsField("integer_ext2");
		pageSize.setLabel("每页记录条数");
		pageSize.setVoField("pageSize");
		list.add(pageSize);
		
		ComboPropertyInfo isLazyLoad = new ComboPropertyInfo();
		isLazyLoad.setId("lazyLoad");
		isLazyLoad.setVisible(true);
		isLazyLoad.setEditable(true);
		isLazyLoad.setType(StringDataTypeConst.bOOLEAN);
		isLazyLoad.setKeys(KEYS);
		isLazyLoad.setValues(VALUES);
		isLazyLoad.setDsField("combo_ext3");
		isLazyLoad.setLabel("是否懒加载");
		isLazyLoad.setVoField("lazyLoad");
		list.add(isLazyLoad);
		
	}
}
