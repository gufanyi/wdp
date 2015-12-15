package xap.lui.core.dataset;

import xap.lui.psn.context.ControlInfo;
import xap.lui.psn.context.IBaseInfo;
import xap.lui.psn.context.StringPropertyInfo;

public class ComboDataInfo extends ControlInfo implements IBaseInfo  {
private static final long serialVersionUID = 1L;
	
	public ComboDataInfo(){
		super();
		
		StringPropertyInfo caption = new StringPropertyInfo();
		caption.setId("caption");
		caption.setVisible(true);
		caption.setEditable(true);
		caption.setDsField("string_ext5");
		caption.setLabel("标题");
		caption.setVoField("caption");
		list.add(caption);
	}
}
