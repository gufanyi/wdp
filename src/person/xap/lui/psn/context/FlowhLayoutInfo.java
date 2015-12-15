/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.layout.UIConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class FlowhLayoutInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	public FlowhLayoutInfo(){
		super();
		ComboPropertyInfo autofill = new ComboPropertyInfo();
		autofill.setId("autoFill");
		autofill.setKeys(new String[]{"是","否"});
		autofill.setValues(new String[]{UIConstant.TRUE + "", UIConstant.FALSE + ""});
		autofill.setDsField("combo_ext1");
		autofill.setType(StringDataTypeConst.INTEGER);
		autofill.setLabel("自动扩展");
		autofill.setVoField("autofill");
		autofill.setVisible(true);
		autofill.setEditable(true);
		list.add(autofill);
	}

}
