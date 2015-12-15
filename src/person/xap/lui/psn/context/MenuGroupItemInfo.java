package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class MenuGroupItemInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	public MenuGroupItemInfo(){
		super();
		IntegerPropertyInfo state = new IntegerPropertyInfo();
		state.setId("state");
		state.setVisible(true);
		state.setEditable(true);
		state.setType(StringDataTypeConst.INTEGER);
		state.setDsField("integer_ext1");
		state.setLabel("state");
		state.setVoField("state");
		list.add(state);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
	}
}
