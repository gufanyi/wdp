package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class MenuGroupInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	public MenuGroupInfo(){
		super();
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
