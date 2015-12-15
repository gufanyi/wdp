/**
 * 
 */
package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class BorderLayoutInfo extends LayoutInfo {


	private static final long serialVersionUID = 1L;

	public BorderLayoutInfo(){
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
