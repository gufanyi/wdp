package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-9-7
 * @since 1.6
 * @desc ShutterItemInfo:百叶窗容器项信息
 */
public class ShutterItemInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	public ShutterItemInfo(){
		super();
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext3");
		text.setLabel("显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo i18nName = new StringPropertyInfo();
		i18nName.setId("i18nName");
		i18nName.setVisible(false);
		i18nName.setEditable(true);
		i18nName.setDsField("string_ext4");
		i18nName.setLabel("多语显示值");
		i18nName.setVoField("i18nname");
		list.add(i18nName);
		
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
