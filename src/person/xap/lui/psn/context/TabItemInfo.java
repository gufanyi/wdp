package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.layout.UIConstant;

/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 * @desc 页签项信息
 */
public class TabItemInfo extends LayoutPanelInfo {
	
	private static final long serialVersionUID = 1L;
	private static final String[] KEYS = new String[]{"是","否"};
	private static final String[] VALUES = new String[]{UIConstant.TRUE + "", UIConstant.FALSE + ""};
	
	public TabItemInfo(){
		super();
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext3");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(false);
		i18n.setEditable(true);
		i18n.setDsField("string_ext4");
		i18n.setLabel("多语资源");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		/*
		IntegerPropertyInfo state = new IntegerPropertyInfo();
		state.setId("state");
		state.setVisible(true);
		state.setEditable(true);
		state.setType(StringDataTypeConst.INTEGER);
		state.setDsField("integer_ext1");
		state.setLabel("state");
		state.setVoField("state");
		list.add(state);
		*/
		
		ComboPropertyInfo showCloseIcon = new ComboPropertyInfo();
		showCloseIcon.setId("showCloseIcon");
		showCloseIcon.setVisible(true);
		showCloseIcon.setEditable(true);
		showCloseIcon.setType(StringDataTypeConst.INTEGER);
		showCloseIcon.setKeys(KEYS);
		showCloseIcon.setValues(VALUES);
		showCloseIcon.setDsField("combo_ext1");
		showCloseIcon.setLabel("可关闭");
		showCloseIcon.setVoField("showcloseicon");
		list.add(showCloseIcon);
		
		ComboPropertyInfo active = new ComboPropertyInfo();
		active.setId("active");
		active.setVisible(true);
		active.setEditable(true);
		active.setKeys(KEYS);
		active.setValues(VALUES);
		active.setType(StringDataTypeConst.INTEGER);
		active.setDsField("combo_ext2");
		active.setLabel("预先激活");
		active.setVoField("active");
		list.add(active);
		
		/*
		ComboPropertyInfo visible = new ComboPropertyInfo();
		visible.setId("visible");
		visible.setVisible(true);
		visible.setEditable(true);
		visible.setKeys(KEYS);
		visible.setValues(VALUES);
		visible.setType(StringDataTypeConst.bOOLEAN);
		visible.setDsField("combo_ext3");
		visible.setLabel("是否可见");
		visible.setVoField("visibles");
		list.add(visible);
		
		ComboPropertyInfo disabled = new ComboPropertyInfo();
		disabled.setId("disabled");
		disabled.setVisible(true);
		disabled.setEditable(true);
		disabled.setKeys(KEYS);
		disabled.setValues(VALUES);
		disabled.setType(StringDataTypeConst.bOOLEAN);
		disabled.setDsField("combo_ext4");
		disabled.setLabel("是否禁用");
		disabled.setVoField("disabled");
		list.add(disabled);
		*/
		
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
