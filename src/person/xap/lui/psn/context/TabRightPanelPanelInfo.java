/**
 * 
 */
package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-9-6
 * @since 1.6
 */
public class TabRightPanelPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	public TabRightPanelPanelInfo(){
		super();
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext3");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
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
